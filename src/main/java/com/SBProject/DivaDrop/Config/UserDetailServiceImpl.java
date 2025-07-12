package com.SBProject.DivaDrop.Config;

import com.SBProject.DivaDrop.Modal.User;
import com.SBProject.DivaDrop.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private UserRepository ur;
    @Autowired
    public UserDetailServiceImpl(UserRepository ur){
        this.ur=ur;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=ur.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User Not Found");
        }

        return new CustomUser(user);
    }
}
