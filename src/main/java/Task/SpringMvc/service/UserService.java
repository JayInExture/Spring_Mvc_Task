package Task.SpringMvc.service;

import Task.SpringMvc.model.Address;
import Task.SpringMvc.model.UserData;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserService {
    void saveUser(Map<String, String> userParams,List<String> interests);
    public UserData findUserByEmail(String email);

    public List<UserData> findAllUsers();

    UserData findUserById(int userId);
    Address findAddressById(int addressId);

//    void updateUser(Map<String, String> userParams, UserData ad);
void updateUser(@RequestParam("images_new[]") List<String> img,@ModelAttribute UserData ad,List<Integer> RmImg);
    void deleteAddressById(int addressId);

    void deleteUserAndRelatedData(int userId);
	void ForgotPass(String email, String newPassword);

}
