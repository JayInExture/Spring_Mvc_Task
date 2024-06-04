package Task.SpringMvc.DAO;

import Task.SpringMvc.model.Address;
import Task.SpringMvc.model.UserData;
import Task.SpringMvc.model.UserImage;
import jakarta.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    private static final Logger log = LogManager.getLogger(UserDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void saveUser(UserData user) {
        log.info("Saving user: " + user);
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.save(user);
        } catch (Exception e) {
            log.error("Failed to save user", e);
            throw e; 
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserData findUserByEmail(String email) {
        try {
            Session session = sessionFactory.getCurrentSession();
            UserData user = session.createQuery("FROM UserData WHERE email = :email", UserData.class)
                    .setParameter("email", email)
                    .uniqueResult();
            if (user != null) {
                
                Hibernate.initialize(user.getAddresses());
                Hibernate.initialize(user.getImages());
                for (UserImage image : user.getImages()) {
                  
                    if (image.getData() != null && image.getData().length > 0) {
                    	String base64Image = Base64.getEncoder().encodeToString(image.getData());
                        image.setBase64Data(base64Image);
                        
                    }
                }
            }
            return user;
        } catch (Exception e) {
            log.error("Error while finding user by email", e);
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserData> findAllUsers() {
        Session session = sessionFactory.getCurrentSession();
        List<UserData> users = session.createQuery("FROM UserData", UserData.class).list();
        for (UserData user : users) {
            Hibernate.initialize(user.getAddresses()); 
        }
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public UserData findUserById(int userId) {
        try {
            Session session = sessionFactory.getCurrentSession();
            UserData user = session.get(UserData.class, userId);
            if (user != null) {
                Hibernate.initialize(user.getAddresses()); 
                Hibernate.initialize(user.getImages()); 
                for (UserImage image : user.getImages()) {
                    if (image.getData() != null && image.getData().length > 0) {
                        String base64Image = Base64.getEncoder().encodeToString(image.getData());
                        image.setBase64Data(base64Image); 
                    }
                }
            }
            return user;
        } catch (Exception e) {
            log.error("Error while finding user by ID", e);
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Address findAddressById(int addressId) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.get(Address.class, addressId);
        } catch (Exception e) {
            log.error("Error while finding address by ID", e);
            return null;
        }
    }


    @Override
    @Transactional
    public void updateUser(UserData user) {
        log.info("Updating user: " + user);
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.saveOrUpdate(user);
        } catch (Exception e) {
            log.error("Failed to update user", e);
            throw e;
        }
    }


    @Override
    @Transactional
    public void deleteAddressById(int addressId) {
        log.info("Deleting address with ID: " + addressId);
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            Address address = session.get(Address.class, addressId);
            if (address != null) {
                session.delete(address);
            }
        } catch (Exception e) {
            log.error("Failed to delete address", e);
            throw e; 
        }
    }

    @Override
    @Transactional
    public void deleteUser(UserData user) {
        log.info("Deleting user: " + user);
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.delete(user);
        } catch (Exception e) {
            log.error("Failed to delete user", e);
            throw e; 
        }
    }

	@Override
	public void deleteImageById(int RemovedImgid) {
		log.info("Deleting image with ID: " + RemovedImgid);
	    Session session = null;
	    try {
	        session = sessionFactory.getCurrentSession();
	        UserImage image = session.get(UserImage.class, RemovedImgid);
	        if (image != null) {
	            session.delete(image);
	            log.info("Image with ID " + RemovedImgid + " deleted successfully.");
	        } else {
	            log.warn("Image with ID " + RemovedImgid + " not found.");
	        }
	    } catch (Exception e) {
	        log.error("Failed to delete image with ID: " + RemovedImgid, e);
	        throw e;
	    }
		
	}

	@Override
	@Transactional
	public void ForgotPass(String email, String newPassword) {
		log.info("User email: " + email+"User newPassword:"+newPassword);
		 Session session = null;
	        try {
	            session = sessionFactory.getCurrentSession();
	            UserData user = session.createQuery("FROM UserData WHERE email = :email", UserData.class)
	                    .setParameter("email", email)
	                    .uniqueResult();
	            if (user != null) {
	                user.setPassword(newPassword);
	                session.update(user);
	                log.info("Password updated successfully for user with email: " + email);
	            } else {
	                log.warn("User with email " + email + " not found.");
	            }
	        } catch (Exception e) {
	            log.error("Failed to Update user", e);
	            throw e;
	        }
		
	}
        


}