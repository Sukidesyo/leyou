package com.leyou.sms.Listener;

import com.leyou.sms.utils.SmsUtil;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class SmsListener {
    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private StringRedisTemplate template;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "leyou.sms.queue",durable = "true"),
            exchange = @Exchange(name = "leyou.sms.send.exchange",type = ExchangeTypes.TOPIC),
            key = {"sms.code"}
    ))
    public void listenSms(Map<String,String> msgMap)
    {
        System.out.println("yes i am come in");
        System.out.println(msgMap);
        if(msgMap.isEmpty())
            return;
        String phone = msgMap.remove("phone");
        if(phone==null)
            return;
        String code = msgMap.get("code");
        System.out.println("code2=="+code);
        template.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
        System.out.println("get="+template.opsForValue().get(phone));
       smsUtil.sendMessage(phone,code);

    }

}
