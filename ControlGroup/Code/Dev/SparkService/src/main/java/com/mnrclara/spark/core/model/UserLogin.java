package com.mnrclara.spark.core.model;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class UserLogin {

    private String userName;
    private String password;
    private Collection<GrantedAuthority> grantedAutoriyList = new ArrayList<>();
}