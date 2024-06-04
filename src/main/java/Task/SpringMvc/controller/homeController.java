package Task.SpringMvc.controller;

import Task.SpringMvc.model.Address;
import Task.SpringMvc.model.UserData;
import Task.SpringMvc.model.UserImage;
import Task.SpringMvc.service.UserService;
import Task.SpringMvc.util.UserDataValidator;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller

public class homeController {
    private static final Logger Log = LogManager.getLogger(homeController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserDataValidator userDataValidator;

    @RequestMapping("/")
    public String index() {
        return "index";
    }
    
    @RequestMapping("index")
    public String index1() {
        return "index";
    }
    @RequestMapping("error")
    public String Error() {
        return "error";
    }


    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String registerUser(@RequestParam Map<String, String> allParams,@RequestParam("interests") List<String> interests,RedirectAttributes redirectAttributes) {   	
    	String validationResult = userDataValidator.validateUserData(allParams, interests);
        
        if (validationResult != null) {
            redirectAttributes.addFlashAttribute("error", validationResult);

    		return "redirect:/";
          
        }else {

      userService.saveUser(allParams,interests);
    	return "redirect:/";
    	}
    	
    }
    @RequestMapping("/Login.html")
    public String showLoginPage() {
        return "Login";
    }
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
        
        UserData user = userService.findUserByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            Log.info(user + " User login");
            request.getSession().setAttribute("LoginUserId", user.getId());
            request.getSession().setAttribute("userType", user.getUserType());
            request.getSession().setAttribute("LoginUser", user.getEmail());
            redirectAttributes.addAttribute("email", email);
            return "redirect:/Dashboard";
        } else {
        	redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/Login.html";
        }
    }
    
    
    @RequestMapping(value = "Dashboard", method = RequestMethod.GET)
    public String showDashboardPage(@RequestParam("email") String email, Model model,HttpServletRequest request) {
        // Retrieve user by email
        UserData user = userService.findUserByEmail(email);
        String userType = (String) request.getSession().getAttribute("userType");
        Log.info("User: " + user);
        if (user != null) {
            // Add the user to the model
            model.addAttribute("user", user);
            // If the user is an admin, add all users to the model
            Log.info("UserType......"+user.getUserType());
            if ("admin".equals(user.getUserType())) {
                List<UserData> allUsers = userService.findAllUsers();
                Log.info("all user"+allUsers);
                model.addAttribute("users", allUsers);
            }
            return "Dashboard";
        } else {
            // If user not found, redirect to login page with error message
            model.addAttribute("error", "User not found");
            return "login";
        }
    }


    @RequestMapping(value = "editUser", method = RequestMethod.GET)
    public String showEditUserPage(@RequestParam("id") int userId, @RequestParam(value = "addressId", required = false) Integer addressId, Model model) {
        // Retrieve user by ID
        UserData user = userService.findUserById(userId);
        Log.info("user......"+user);
        if (user != null) {
            model.addAttribute("user", user);
            if (addressId != null) {
                Address address = userService.findAddressById(addressId);
                model.addAttribute("address", address);
            }
            return "EditUser"; 
        } else {
            
            return "error"; 
        }
    }
    @RequestMapping(value = "Update", method = RequestMethod.POST)
    public String updateUser(@RequestParam(value = "images_new[]",required = false,defaultValue = "") List<String> img, @RequestParam("email") String email ,@ModelAttribute UserData ad,@RequestParam(value = "removedImageIds",required = false,defaultValue = "")List<Integer> RmImg,RedirectAttributes redirectAttributes,HttpSession session) {

        Log.info("User img: " + img);
        Log.info("ad ad: " + ad);
        
        
        String validationError = userDataValidator.validateUserData(ad);
        if (validationError != null) {
        	redirectAttributes.addFlashAttribute("error", validationError);
        	 if ("admin".equals(session.getAttribute("userType"))) {
        		 int LoginUserId = (int) session.getAttribute("LoginUserId");
     	        String LOgi_email = (String) session.getAttribute("LoginUser");
                return "redirect:/editUser?id=" + ad.getId() + "&userType=admin&UId=" + session.getAttribute("LoginUser");
         }else {
        	return "redirect:/editUser?id=" + ad.getId();
         }
        }
        else {
        	userService.updateUser(img, ad,RmImg);
        	
        	
        	Log.info("session.getAttribute(\"userType\")"+session.getAttribute("userType"));
        	
        	 if ("admin".equals(session.getAttribute("userType"))) {
        	        String LOgi_email = (String) session.getAttribute("LoginUser");
        	        return "redirect:/Dashboard?email=" + LOgi_email;
            } else {
                return "redirect:/Dashboard?email=" + email; 
            }
        }
        
    }

    private boolean isValidAddress(Address address) {
        return address != null &&
                address.getStreet() != null && !address.getStreet().isEmpty() &&
                address.getCity() != null && !address.getCity().isEmpty() &&
                address.getZip() != null && !address.getZip().isEmpty() &&
                address.getState() != null && !address.getState().isEmpty();
    }
    
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        request.getSession().invalidate(); 
        return "redirect:/Login.html";
    }

@RequestMapping(value = "deleteUser", method = RequestMethod.POST)
public String deleteUser(@RequestParam("id") int userId, HttpSession session) {
    // Delete user and associated data
    userService.deleteUserAndRelatedData(userId);
    if ("admin".equals(session.getAttribute("userType"))) {
        // Retrieve the email from the session
        String LOgi_email = (String) session.getAttribute("LoginUser");
    return "redirect:/Dashboard?email=" + LOgi_email;
    }
    
    return "redirect:/";
}



@RequestMapping("ForgotPassword")
public String ForgotPassword() {
    return "ForgotPassword";
}

@RequestMapping(value = "NEWPassword", method = RequestMethod.POST)
public String NEWPassword(@RequestParam("email") String email,@RequestParam("newPassword") String newPassword) {
    // Delete user and associated data
	Log.info("email::- "+email+"newPassword::- "+newPassword);
    userService.ForgotPass(email, newPassword );

    return "redirect:/"; 
}
}
