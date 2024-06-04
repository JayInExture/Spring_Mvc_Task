package Task.SpringMvc.DAO;

import Task.SpringMvc.model.Address;
import Task.SpringMvc.model.UserData;

import java.util.List;

public interface UserDao {
    void saveUser(UserData user);
    UserData findUserByEmail(String email);
    List<UserData> findAllUsers();
    UserData findUserById(int userId);
    Address findAddressById(int addressId);
   void updateUser(UserData user);
   void deleteAddressById(int addressId);


   void deleteUser(UserData user);
   void deleteImageById(int RemovedImgid);
void ForgotPass(String email, String newPassword);

}

