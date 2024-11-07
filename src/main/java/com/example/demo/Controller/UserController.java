package com.example.demo.Controller;

import com.example.demo.Model.Address;
import com.example.demo.Model.User;
import com.example.demo.Model.WishList;
import com.example.demo.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;




/**
 * GET     /api/users/{userId}               -> Get user profile by ID
 * PUT     /api/users/{userId}               -> Update user profile
 * DELETE  /api/users/{userId}               -> Delete user account
 *
 * POST    /api/users/{userId}/addresses     -> Add a new address
 * GET     /api/users/{userId}/addresses     -> Get all addresses of the user
 * DELETE  /api/users/{userId}/addresses/{addressId} -> Delete an address
 *
 * GET     /api/users/{userId}/wishlist      -> Get all wishlist items
 * POST    /api/users/{userId}/wishlist/{productId} -> Add product to wishlist
 * DELETE  /api/users/{userId}/wishlist/{productId} -> Remove product from wishlist*/


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * All the methods below use userid received from security context to perform actions accordingly.*/
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/")
    public ResponseEntity<User> getUserById() {
        Long userId = userService.getUserId();
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<User> updateUserById(@RequestBody User user) {
        Long userId = userService.getUserId();
        User user1 = userService.updateUserById(userId,user);
        return new ResponseEntity<>(user1, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<User> deleteUserById() {
        Long userId = userService.getCurrentUserId();
        return new ResponseEntity<>(userService.deleteUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<Address>> getAddressByUserId(){
        Long userId = userService.getUserId();
        List<Address> addresses = userService.getAddressByUserId(userId);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PutMapping("/addresses")
    public ResponseEntity<Address> updateAddressByUserId(@RequestBody Address address){
//        Long userId = userService.getUserIdFromContext();
        Address addressUpdated = userService.updateAddressByUserId( address);
        return new ResponseEntity<>(addressUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<Address> deleteAddressById(@PathVariable Long addressId){
//        Long userId = userService.getUserIdFromContext();
        Address address = userService.deleteAddressById(addressId);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PostMapping("/address")
    public  ResponseEntity<Address> addAddressToUser(@RequestBody Address address){
        Long userId = userService.getUserId();
        Address addressAdded = userService.addAddress(address, userId);
        return new ResponseEntity<>(addressAdded, HttpStatus.OK);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<List<WishList>> getAllWithListsForUser(){
        Long userId = userService.getUserId();
        List<WishList> wishLists = userService.getAllWishListsForUser(userId);
        return new ResponseEntity<>(wishLists, HttpStatus.OK);
    }

    @GetMapping("/wishList/{wishlistId}")
    public ResponseEntity<WishList> getWishListById(@PathVariable Long wishListId){
//        long userId = userService.getUserIdFromContext();
        WishList wishList = userService.getWishListById( wishListId);
        return new ResponseEntity<>(wishList, HttpStatus.OK);
    }

    @PutMapping("/wishList/{wishListId}")
    public ResponseEntity<WishList> updateWishList(@PathVariable Long wishListId, @RequestBody WishList wishList){
        WishList wishList1 = userService.updadteWishList(wishListId, wishList);
        return new ResponseEntity<>(wishList1, HttpStatus.OK);
    }

    @DeleteMapping("/wishList/{wishlistId}")
    public ResponseEntity<?> deleteWishListById(@PathVariable Long wishListId){
//        long userId = userService.getUserIdFromContext();
        userService.deleteWishListById( wishListId);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
    @PostMapping("/wishList/")
    public ResponseEntity<?> createWishListForUser(@RequestBody WishList wishList){
//        long userId = userService.getUserIdFromContext();
        WishList wishList1 = userService.createNewWishList(wishList);
        return new ResponseEntity<>(wishList1, HttpStatus.OK);
    }





}
