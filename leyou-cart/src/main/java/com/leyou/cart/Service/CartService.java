package com.leyou.cart.Service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.print.attribute.HashAttributeSet;
import java.util.List;

@Service
public class CartService {
    private static final String PRE_FIX="cart:";
    @Autowired
    private StringRedisTemplate redisTemplate;
    public void addCart(Cart cart)
    {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        int num=cart.getNum();
        String mapKey=PRE_FIX+userInfo.getId();
        String skuKey=cart.getSkuId().toString();
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(PRE_FIX + userInfo.getId());
        if(hashOps.hasKey(skuKey))
        {
            String json = hashOps.get(skuKey).toString();
            cart = JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum()+num);

        }
            hashOps.put(mapKey,JsonUtils.serialize(cart));
    }

    public List<Cart> queryCartList() {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String mapKey=PRE_FIX+userInfo.getId();
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(PRE_FIX + userInfo.getId());
        hashOps.values()
    }
}
