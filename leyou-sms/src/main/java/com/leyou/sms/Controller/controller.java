//package com.leyou.sms.Controller;
//
//import com.leyou.sms.utils.SmsUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class controller {
//    @Autowired
//    private SmsUtil smsUtil;
//    @GetMapping("/user/test/{phone}")
//    public void test(@PathVariable("phone")String phone)
//    {
//        smsUtil.sendMessage(phone);
//    }
//}
