package com.books.bkb.Config.Security.UserAuth;

import com.books.bkb.DAO.inter.UserAuthDAO;
import com.books.bkb.DAO.inter.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthDetailService implements UserDetailsService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserAuthDAO userAuthDAO;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        var auth = userAuthDAO.findByUsername(username);
        System.out.println(username);
        if(auth == null)
            throw new UsernameNotFoundException(String.format("Cannot find user : '%s'", username));
        else
        {
            var user = userDAO.getById(auth.getId());
            if(user == null)
                throw new UsernameNotFoundException(String.format("Cannot find user : '%s'", username));
            else
            {
                List<GrantedAuthority> authorities = new ArrayList<>();
                if (user.isAdmin()) {
                    // 如果用户是管理员，添加 ADMIN 权限
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                } else {
                    // 如果用户不是管理员，添加 USER 权限
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }
                return new AuthDetail(user.getId(), user.getName(), auth.getUsername(), auth.getPassword(), user.getEmail(),
                        authorities, user.isEnabled());
            }
        }
    }
}
