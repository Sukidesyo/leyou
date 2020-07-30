package com.leyou.user.service;

import com.leyou.common.utils.CodecUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private AmqpTemplate amqpTemplate;

    public Boolean checkData(String data, Integer type) {
        User user = new User();
        switch (type)
        {
            case 1:
            {
                user.setUsername(data);
                break;
            }
            case 2:
            {
                user.setPhone(data);
                break;
            }
            default:
            {
                return null;
            }
        }
        int count = userMapper.selectCount(user);
        return count==0;
    }
    public void sendVerifyCode(String phone)
    {
        Map<String,String> msg=new HashMap<>();
        String key=phone;
        msg.put("phone",phone);
        Double code = Math.random() * 9000 + 1000;
        String stringCode=String.valueOf(code.intValue());
        msg.put("code",stringCode);
        System.out.println("code1=="+stringCode);
        amqpTemplate.convertAndSend("leyou.sms.send.exchange", "sms.code", msg);
    }
    public void register(User user, String code) {
        user.setId(null);
        user.setCreated(new Date());
        String key=user.getPhone();
        String value = template.opsForValue().get(key);
        if(value.equals(code))
        {
            String salt = CodecUtils.generateSalt();
            user.setSalt(salt);
            //生成密码
            String md5Pwd = CodecUtils.md5Hex(user.getPassword(), user.getSalt());
            user.setPassword(md5Pwd);
            //保存到数据库
            int count = userMapper.insert(user);

            if (count != 1) {
                return;
            }
            //把验证码从Redis中删除
            template.delete(key);
        }
    }

    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        System.out.println(user);
        if(user==null) {
            return null;
        }
        String salt = user.getSalt();
        password= CodecUtils.md5Hex(password, salt);
        System.out.println("pass="+password);
        if(StringUtils.equals(password, user.getPassword())) {
            System.out.println("yespass!");
            return user;
        }
        return null;
    }
}