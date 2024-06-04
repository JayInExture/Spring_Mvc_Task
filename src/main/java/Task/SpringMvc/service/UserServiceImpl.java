package Task.SpringMvc.service;

import Task.SpringMvc.DAO.UserDao;
import Task.SpringMvc.DAO.UserDaoImpl;
import Task.SpringMvc.model.Address;
import Task.SpringMvc.model.UserData;
import Task.SpringMvc.model.UserImage;
import Task.SpringMvc.util.UserDataValidator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger Log = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;
	   @Autowired
	    private UserDataValidator userDataValidator;
	@Override
	public void saveUser(Map<String, String> userParams,List<String> interests) {
	    UserData user = new UserData();

	   
	    user.setFirstName(userParams.get("firstName"));
	    user.setLastName(userParams.get("lastName"));
	    user.setEmail(userParams.get("email"));
	    user.setPassword(userParams.get("Password")); 
	    user.setCountry(userParams.get("country"));
	    user.setUserType(userParams.get("userType"));
	    user.setDob(userParams.get("dob"));


	   user.setInterests(interests);


	    List<Address> addresses = new ArrayList<>();
	    int addressIndex = 0;
	    while (true) {
	        String street = userParams.get("addresses[" + addressIndex + "].street");
	        String city = userParams.get("addresses[" + addressIndex + "].city");
	        String zip = userParams.get("addresses[" + addressIndex + "].zip");
	        String state = userParams.get("addresses[" + addressIndex + "].state");
	        
	        if (street == null || city == null || zip == null || state == null) {
	            break; 
	        }

	        Address address = new Address(user, street, city, zip, state);
	        addresses.add(address);
	        addressIndex++;
	    }
	    user.setAddresses(addresses);
	    List<UserImage> userImages = new ArrayList<>();
	    int imageIndex = 0;
	    while (true) {
	        String imageBase64 = userParams.get("images[" + imageIndex + "]");
	        if (imageBase64 == null) {
	            break; 
	        }

	        try {
	            if (imageBase64.startsWith("data:image")) {
	                imageBase64 = imageBase64.substring(imageBase64.indexOf(",") + 1);
	            }
	            byte[] imageData = java.util.Base64.getDecoder().decode(imageBase64);
	            UserImage userImage = new UserImage();
	            userImage.setData(imageData);
	            userImage.setUser(user);
	            userImages.add(userImage);
	        }  catch (IllegalArgumentException e) {
	            Log.error("Invalid Base64 image data at index " + imageIndex, e);
	        }

	        imageIndex++;
	    }
	    user.setImages(userImages);
	    userDao.saveUser(user);
	}
	@Override
	public UserData findUserByEmail(String email) {
		return userDao.findUserByEmail(email);
	}

	@Override
	public List<UserData> findAllUsers() {
		return userDao.findAllUsers();
	}

	@Override
	public UserData findUserById(int userId) {
		return userDao.findUserById(userId);
	}

	@Override
	public Address findAddressById(int addressId) {
		return userDao.findAddressById(addressId);
	}

	private boolean isValidAddress(Address address) {
		return address != null && address.getStreet() != null && !address.getStreet().isEmpty()
				&& address.getCity() != null && !address.getCity().isEmpty() && address.getZip() != null
				&& !address.getZip().isEmpty() && address.getState() != null && !address.getState().isEmpty();
	}

	@Override
	public void deleteAddressById(int addressId) {
		userDao.deleteAddressById(addressId);
	}

	@Transactional
	public void deleteUserAndRelatedData(int userId) {
		// Retrieve the user by ID
		UserData user = userDao.findUserById(userId);
		if (user != null) {
			// Delete the user
			userDao.deleteUser(user);
		}
	}


	@Override
	@Transactional
	public void updateUser(List<String> img, UserData updatedUser,List<Integer> RmImg) {
		Log.info("service img :::-  " + img);
		Log.info("service :-  " + updatedUser);
		
		
		 String validationError = userDataValidator.validateUserData(updatedUser);
		    if (validationError != null) {
		        Log.error("Validation error: " + validationError);
		        return;
		    }else {
		UserData existingUser = userDao.findUserById(updatedUser.getId());
		if (existingUser != null) {
			existingUser.setFirstName(updatedUser.getFirstName());
			existingUser.setLastName(updatedUser.getLastName());
		existingUser.setCountry(updatedUser.getCountry());
		existingUser.setInterests(Collections.singletonList(updatedUser.getInterests()));

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dobString = dateFormat.format(updatedUser.getDob());
			existingUser.setDob(dobString);
			
	        List<Address> currentAddresses = existingUser.getAddresses();

	        List<Address> addressesToRemove = new ArrayList<>();
	        for (Address currentAddress : currentAddresses) {
	            boolean found = false;
	            for (Address updatedAddress : updatedUser.getAddresses()) {
	                if (currentAddress.getId() == updatedAddress.getId()) {
	                    found = true;
	                    // Update the existing address with the new data
	                    currentAddress.setStreet(updatedAddress.getStreet());
	                    currentAddress.setCity(updatedAddress.getCity());
	                    currentAddress.setZip(updatedAddress.getZip());
	                    currentAddress.setState(updatedAddress.getState());
	                    break;
	                }
	            }
	            if (!found) {
	                addressesToRemove.add(currentAddress);
	            }
	        }
	        for (Address addressToRemove : addressesToRemove) {
	            userDao.deleteAddressById(addressToRemove.getId());
	            currentAddresses.remove(addressToRemove);
	        }
	        for (Address updatedAddress : updatedUser.getAddresses()) {
	            if (updatedAddress.getId() == 0) {
	                if (isValidAddress(updatedAddress)) {
	                    updatedAddress.setUser(existingUser);
	                    currentAddresses.add(updatedAddress);
	                }
	            }
	        }
	        existingUser.setAddresses(currentAddresses);
	        List<UserImage> existingImages = existingUser.getImages();

	        for (String imageData : img) {
	            try {
	                if (imageData.startsWith("data:image")) {
	                    imageData = imageData.substring(imageData.indexOf(",") + 1);
	                }
	                byte[] decodedData = java.util.Base64.getDecoder().decode(imageData);
	                UserImage userImage = new UserImage();
	                userImage.setData(decodedData);
	                userImage.setUser(existingUser);
	                existingImages.add(userImage);
	            } catch (IllegalArgumentException e) {
	                Log.error("Error decoding Base64 data for image: " + imageData, e);
	            }
	        }
	        Iterator<UserImage> iterator = existingImages.iterator();
	        while (iterator.hasNext()) {
	            UserImage existingImage = iterator.next();
	            if (RmImg.contains(existingImage.getId())) {
	                iterator.remove();
	                userDao.deleteImageById(existingImage.getId());
	            }
	        }
	        existingUser.setImages(existingImages);
	        
	        userDao.updateUser(existingUser);
		
	}
		
		    }	
	}


	@Override
	public void ForgotPass(String email, String newPassword) {
		userDao.ForgotPass(email, newPassword);
		
	}
	
}



