//package com.mnrclara.wrapper.core.config.security;
//
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
//import com.azure.spring.aad.webapp.AADWebSecurityConfigurerAdapter;
//
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class AADOAuth2LoginSecurityConfig extends AADWebSecurityConfigurerAdapter {
//
//    /**
//     * Add configuration logic as needed.
//    */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        http.authorizeRequests()
//            .anyRequest().authenticated();
//        // Do some custom configuration.
//    }
//}
//
