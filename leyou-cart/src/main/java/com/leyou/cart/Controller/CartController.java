package com.leyou.cart.Controller;

import com.leyou.cart.Service.CartService;
import com.leyou.cart.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart)
    {
        cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }
    @GetMapping("list")
    private ResponseEntity<List<Cart>> queryCartList()
    {
        return ResponseEntity.ok(cartService.queryCartList());
    }
}
