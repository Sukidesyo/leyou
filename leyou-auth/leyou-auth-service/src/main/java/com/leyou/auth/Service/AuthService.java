package com.leyou.auth.Service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.properties.JwtProperties;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class AuthService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;
    public String accredit(String username, String password) {

        User user = userClient.queryUser(username, password);
        System.out.println("user="+user);
        if(user!=null) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setName(user.getUsername());
            String token = JwtUtils.generateToken(userInfo,jwtProperties.getPrivateKey(),jwtProperties.getExpire());
            return token;
        }
        return null;
    }
}
