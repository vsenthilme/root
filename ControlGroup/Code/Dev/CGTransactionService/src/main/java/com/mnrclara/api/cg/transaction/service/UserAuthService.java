package com.mnrclara.api.cg.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mnrclara.api.cg.transaction.model.auth.UserLogin;
import com.mnrclara.api.cg.transaction.model.auth.UsersHelper;
import com.mnrclara.api.cg.transaction.repository.UserAuthRepository;

@Service
public class UserAuthService implements UserDetailsService {

    @Autowired
    UserAuthRepository userLoginRepository;

    @Override
    public UsersHelper loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogin userLogin = null;
        try {
            userLogin = userLoginRepository.getUserLoginDetails(username);
            UsersHelper usersHelper = new UsersHelper(userLogin);
            return usersHelper;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException(username + " not found..");
        }
    }
}
