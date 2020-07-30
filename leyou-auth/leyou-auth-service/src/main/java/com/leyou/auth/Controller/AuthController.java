package com.leyou.auth.Controller;

import com.leyou.auth.Service.AuthService;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.properties.JwtProperties;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private AuthService authService;
    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(
            @RequestParam("username")String username,
            @RequestParam("password")String password,
            HttpServletRequest request,
            HttpServletResponse response
    )
    {
        String token=authService.accredit(username,password);
        if(!StringUtils.isNotBlank(token))
        {
            return new ResponseEntity<> (HttpStatus.UNAUTHORIZED);
        }
        CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getCookieMaxAge());
        return ResponseEntity.ok().build();
    }
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("leyou_login")String token,
                                           HttpServletRequest request,
                                           HttpServletResponse response)
    {
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            if(userInfo==null)
            {
                return null;
            }
            String newtoken = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire()*60);
            CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),newtoken,jwtProperties.getCookieMaxAge());
            System.out.println(("userinfo="+userInfo));
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
