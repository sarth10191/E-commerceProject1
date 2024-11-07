package com.example.demo.Controller;


import com.example.demo.Model.CartItem;
import com.example.demo.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;


/**
 * GET     /api/users/cart          -> Get all cart items for the user
 * POST    /api/users/cart/{productId} -> Add product to cart
 * PUT     /api/users/cart/{productId} -> Update quantity of product in cart
 * DELETE  /api/users/cart/{productId} -> Remove product from cart  */
@RestController
@RequestMapping("/api/users/cart")
public class CartController {
    private final UserService userService;

    public CartController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartForUser(){
        List<CartItem> cart = userService.getCart();
    }


}
