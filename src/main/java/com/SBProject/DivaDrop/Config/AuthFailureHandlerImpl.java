package com.SBProject.DivaDrop.Config;

import com.SBProject.DivaDrop.CommonServices.AppConstant;
import com.SBProject.DivaDrop.Modal.User;
import com.SBProject.DivaDrop.Repository.UserRepository;
import com.SBProject.DivaDrop.Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private UserRepository ur;
    @Autowired
    private UserService us;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email=request.getParameter("username");
        User user=ur.findByEmail(email);
        if(user!=null){
        if(user.getIsEnable()){
            if(user.getAccountNonLocked()){
                if(user.getFailedAttempts()< AppConstant.ATTEMPT){
                    us.increaseFailAttempts(user);
                }else{
                    us.userAccountLock(user);
                    exception=new LockedException("Your Account has been Locked !! Failed Attempts 3");
                }
            }
            else{
                if(us.unlockAccountTimeExpire(user)){
                    exception=new LockedException("Your Account has been unLocked !! Please try to Login");
                }
            exception=new LockedException("Your Account is Locked !! Please try after some times");
            }
        }else{
            exception=new LockedException("Your Account is Disabled !!");
        }}
        else{
            exception=new LockedException("Bad Credentials !! Retry ");
        }
        super.setDefaultFailureUrl("/DivaDrop/signin?error");
        super.onAuthenticationFailure(request,response,exception);
    }
}
