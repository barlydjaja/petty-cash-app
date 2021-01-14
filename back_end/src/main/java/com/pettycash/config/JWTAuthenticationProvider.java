package com.pettycash.config;

import com.pettycash.dto.JWTAuthenticationToken;
import com.pettycash.dto.JwtUserDetails;
import com.pettycash.dto.LoginDTO;
import com.pettycash.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    JWTValidator validator;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        JWTAuthenticationToken jwtAuthenticationToken =(JWTAuthenticationToken) usernamePasswordAuthenticationToken;

        String token=jwtAuthenticationToken.getToken();

        LoginDTO userDomain=validator.validate(token);
        if (userDomain == null) {
            throw new RuntimeException("SALAH TOKEN");
        }
        //in case role
//        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
//                .commaSeparatedStringToAuthorityList(jwtUser.getRole)
        User user = new User();
        user.setPassword(userDomain.getPassword());
        user.setUserId(userDomain.getUserId());

        return new JwtUserDetails(user);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JWTAuthenticationToken.class.isAssignableFrom(aClass));
    }
}