package Task.SpringMvc.util;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import Task.SpringMvc.DAO.UserDao;
import Task.SpringMvc.model.Address;
import Task.SpringMvc.model.UserData;

@Component
public class UserDataValidator {
    private static final Logger Log = LogManager.getLogger(UserDataValidator.class);

    @Autowired
    private UserDao userDao;

    public String validateUserData(Map<String, String> userData, List<String> interests) {
        if (!isValidName(userData.get("firstName"))) {
            return "Invalid first name";
        }
        if (!isValidName(userData.get("lastName"))) {
            return "Invalid last name";
        }

        String email = userData.get("email");
        if (!isValidEmail(email)) {
            return "Invalid email format";
        }

        try {
            if (userDao.findUserByEmail(email) != null) {
                return "Email already exists";
            }
        } catch (Exception e) {
            Log.error("Error checking email existence", e);
            return "An error occurred while checking email existence";
        }

        String password = userData.get("Password");
        String confirmPassword = userData.get("Confirm_Password");
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }

        if (!isValidPassword(password)) {
            return "Invalid password";
        }

        if (!isValidDateOfBirth(userData.get("dob"))) {
            return "Invalid date of birth";
        }

        if (!isValidCountry(userData.get("country"))) {
            return "Invalid country";
        }

        if (!isValidInterests(interests)) {
            return "Invalid interests";
        }

        String[] streets = userData.get("addresses[0].street").split(",");
        String[] cities = userData.get("addresses[0].city").split(",");
        String[] zips = userData.get("addresses[0].zip").split(",");
        String[] states = userData.get("addresses[0].state").split(",");
        if (!isValidAddress(streets, cities, zips, states)) {
            return "Invalid addresses";
        }

        return null;
    }

    private boolean isValidName(String name) {
        String nameRegex = "^[a-zA-Z ]*$";
        return name != null && name.matches(nameRegex);
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    private static boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    private static boolean isValidDateOfBirth(String dob) {
        return dob != null && !dob.isEmpty();
    }

    private static boolean isValidCountry(String country) {
        return country != null && !country.isEmpty();
    }

    private static boolean isValidInterests(List<String> interests) {
        return interests != null && !interests.isEmpty();
    }

    private static boolean isValidAddress(String[] streets, String[] cities, String[] zips, String[] states) {
        if (streets == null || cities == null || zips == null || states == null) {
            return false;
        }
        if (streets.length != cities.length || streets.length != zips.length || streets.length != states.length) {
            return false;
        }
        for (String street : streets) {
            if (street == null || street.isEmpty()) {
                return false;
            }
        }
        for (String city : cities) {
            if (city == null || city.isEmpty()) {
                return false;
            }
        }
        for (String zip : zips) {
            if (zip == null || zip.isEmpty() || !zip.matches("\\d{6}")) {
                return false;
            }
        }
        for (String state : states) {
            if (state == null || state.isEmpty() || state.matches(".*\\d+.*")) {
                return false;
            }
        }

        return true;
    }
    
    
    
    
    
    public String validateUserData(UserData userData) {
        if (!isValidName(userData.getFirstName())) {
            return "Invalid first name";
        }
        if (!isValidName(userData.getLastName())) {
            return "Invalid last name";
        }

      

        if (!isValidCountry(userData.getCountry())) {
            return "Invalid country";
        }

        if (!isValidInterests(userData.getInterests())) {
            return "Invalid Interests";
        }
        if (!isValidDob(userData.getDob())) {
            return "Invalid DOB";
        }
        if (!isValidAddressNew(userData.getAddresses())) {
            return "Invalid addresses";
        }
        
        return null;
    }
    
    private boolean isValidAddressNew(List<Address> addresses) {
        // Check if the addresses list is null or empty
        if (addresses == null || addresses.isEmpty()) {
            return false;
        }

        // Iterate over each address and validate its components
        for (Address address : addresses) {
            // Check if any of the components of the address is null or empty
            if (address == null || 
                address.getStreet() == null || address.getStreet().isEmpty() ||
                address.getCity() == null || address.getCity().isEmpty() ||
                address.getZip() == null || address.getZip().isEmpty() ||
                address.getState() == null || address.getState().isEmpty()) {
                return false;
            }

            // Validate zip format
            if (!address.getZip().matches("\\d{6}")) {
                return false;
            }

            // Validate state (assuming it should not contain digits)
            if (address.getState().matches(".*\\d+.*")) {
                return false;
            }
        }
        return true;
       }  

    
    private boolean isValidDob(Date dob) {
        // Implement your logic to validate the date of birth
        // For example, check if the date is in the past or if it's in a valid format
        return dob != null; // Placeholder logic
    }

    private boolean isValidInterests(String interests) {
        // Implement your logic to validate interests
        // For example, check if interests are not empty or if they contain valid values
        return interests != null && !interests.isEmpty(); // Placeholder logic
    }

}
