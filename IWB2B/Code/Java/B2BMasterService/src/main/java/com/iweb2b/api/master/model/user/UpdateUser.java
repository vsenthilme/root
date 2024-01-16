package com.iweb2b.api.master.model.user;

import lombok.Data;

@Data
public class UpdateUser {

    public enum Role {USER, ADMIN, USER_MANAGER}

    private String username;

    private String password;

    private String email;

    private Role role;
    
    private String firstname;
    private String lastname;
    private String company;
    
    private String city;    
    private String state;    
    private String country;
}
