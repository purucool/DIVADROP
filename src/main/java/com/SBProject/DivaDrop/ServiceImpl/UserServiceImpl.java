package com.SBProject.DivaDrop.ServiceImpl;


import com.SBProject.DivaDrop.CommonServices.AppConstant;
import com.SBProject.DivaDrop.CommonServices.CommonUtil;
import com.SBProject.DivaDrop.Modal.User;
import com.SBProject.DivaDrop.Repository.UserRepository;
import com.SBProject.DivaDrop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    private UserRepository ur;

    @Autowired
    UserServiceImpl(UserRepository ur){
        this.ur=ur;
    }

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User addUser(User user) {
        user.setRole("ROLE_USER");
        user.setIsEnable(true);
        user.setFailedAttempts(0);
        user.setAccountNonLocked(true);
        user.setLockTime(null);
        String encodePassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
//        String encodePassword2=passwordEncoder.encode(user.getConfirmPassword());
//        user.setConfirmPassword(encodePassword2);
        return ur.save(user);
    }

    @Override
    public User addAdmin(User user) {
        user.setRole("ROLE_ADMIN");
        user.setIsEnable(true);
        user.setFailedAttempts(0);
        user.setAccountNonLocked(true);
        user.setLockTime(null);
        String encodePassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
//        String encodePassword2=passwordEncoder.encode(user.getConfirmPassword());
//        user.setConfirmPassword(encodePassword2);
        return ur.save(user);
    }

    @Override
    public Boolean existUser(String email) {
        return ur.findAllByUserName(email);
    }

    @Override
    public User getUserByEmail(String email) {
        return ur.findByEmail(email);
    }

    @Override
    public List<User> getAllUsersByRole(String role) {
        return ur.findByRole(role);
    }

    @Override
    public Boolean updateUserStatus(Integer id, Boolean status) {
        Optional<User> optionaluser=ur.findById(id);
        if(optionaluser.isPresent()){
            User user=optionaluser.get();
            user.setIsEnable(status);
            ur.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void increaseFailAttempts(User user) {
        int attempt=user.getFailedAttempts();
        attempt+=1;
        user.setFailedAttempts(attempt);
        ur.save(user);

    }

    @Override
    public void userAccountLock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        ur.save(user);
    }

    @Override
    public Boolean unlockAccountTimeExpire(User user) {
        long lockTime=user.getLockTime().getTime();
        long unlockTime=lockTime+ AppConstant.UNLOCK_DURATION;
        long curr_time=System.currentTimeMillis();
        if(unlockTime<curr_time){
            user.setAccountNonLocked(true);
            user.setFailedAttempts(0);
            user.setLockTime(null);
            ur.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void resetAttempt(int userid) {

    }

    @Override
    public void updateUserResetToken(String email, String reset_token) {
        User user=ur.findByEmail(email);
        user.setReset_token(reset_token);
        ur.save(user);
    }

    @Override
    public User getUserByToken(String token) {
        User user=ur.findByToken(token);
        return user;
    }

    @Override
    public User UpdateUserPassword(User user) {
        return ur.save(user);

    }

    @Override
    public User UpdateUserProfile(User user, MultipartFile img) {

        User existUser=ur.findById(user.getId()).orElse(null);
        if (existUser == null) {
            return null; // or throw an exception
        }
        if(!img.isEmpty()){
            String imgName =  img.getOriginalFilename();
            existUser.setImgName(imgName);
        }

            existUser.setUserName(user.getUserName());
            existUser.setMobileNo(user.getMobileNo());
            existUser.setAddress(user.getAddress());
            existUser.setState(user.getState());
            existUser.setCity(user.getCity());
            existUser.setPincode(user.getPincode());

            existUser=ur.save(existUser);

        try {
            if (!img.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "" + "user_img" + File.separator + img.getOriginalFilename());
                Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                fileService.updloadFileS3(img,BucketType.Profile.getId());

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return existUser;
    }

    @Override
    public Page<User> getAllUsersByRolePagging(Integer pageNo, Integer pageSize, String role) {
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        return ur.findByRole(role,pageable);

    }

}
