package com.SBProject.DivaDrop.Service;

import com.SBProject.DivaDrop.Modal.Product;
import com.SBProject.DivaDrop.Modal.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User addUser(User user);

    User addAdmin(User user);

    Boolean existUser(String userName);

    User getUserByEmail(String email);

    List<User> getAllUsersByRole(String role);

    Boolean updateUserStatus(Integer id, Boolean status);

    void increaseFailAttempts(User user);
    void userAccountLock(User user);
    Boolean unlockAccountTimeExpire(User user);
    void resetAttempt(int userid);

    void updateUserResetToken(String email, String reset_token);

    User getUserByToken(String token);

    User UpdateUserPassword(User user);
    User UpdateUserProfile(User user, MultipartFile img);

    Page<User> getAllUsersByRolePagging(Integer pageNo, Integer pageSize, String role);
}
