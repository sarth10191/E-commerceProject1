package com.example.demo.Service;

import com.example.demo.Exceptions.AddressNotFoundException;
import com.example.demo.Exceptions.UserNotFoundException;
import com.example.demo.Exceptions.WishListNotFoundException;
import com.example.demo.Model.*;
import com.example.demo.Repository.AddressRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.WishListRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    @Setter
    @Getter
    private Long userId;

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return Long.parseLong(userDetails.getUsername());
        }
        throw new RuntimeException("User not authenticated");
    }

    /**
     * Method to get UserId from security context.*/
    public Long getUserIdFromContext(){
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String username = user.getUsername();
        User userModel = getUserByUsername(username);
        return userModel.getId();
    }

    public User getCurrentUserProfile() {
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    public UserService(UserRepository userRepository, WishListRepository wishListRepository, AddressRepository addressRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.wishListRepository = wishListRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
    }

    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User with id "+ userId+" not found."));
        return user;
    }

    public User updateUserById(Long userId, User user) {
        User userExisting = userRepository.findById(user.getId()).orElseThrow(()-> new UserNotFoundException("User with id "+ user.getId()+" not found."));
        BeanUtils.copyProperties(user, userExisting, "id", "role");
        return userRepository.save(userExisting);
    }


    public User deleteUserById(Long userId) {
        User userExisting = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User with id "+ userId+" not found."));
        userRepository.deleteById(userId);
        return userExisting;

    }

    public List<Address> getAddressByUserId(Long userId) {
        User userExisting =  userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));
        return userExisting.getAddresses();
    }

    public Address updateAddressByUserId( Address updatedAddress){
         User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found."));

         Address addressExisting = addressRepository.findById(updatedAddress.getId()).orElseThrow(()-> new AddressNotFoundException("Address not found"));
         BeanUtils.copyProperties(updatedAddress, addressExisting, "id");
         addressRepository.save(addressExisting);
         return addressExisting;
    }


    public Address deleteAddressById(Long addressId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found."));

        Address addressToBeDeleted = addressRepository.findById(addressId).orElseThrow(()-> new AddressNotFoundException("Address not found"));
        addressRepository.delete(addressToBeDeleted);
        return  addressToBeDeleted;
    }

    public Address addAddress(Address address, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found."));
        address.setUser(user);
        user.getAddresses().add(address);
        Address save = addressRepository.save(address);
        userRepository.save(user);
        return save;
    }

    public User getUserByUsername(String username) {
        User user =  userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("NotFound"));
        this.userId = user.getId();
        return user;
    }

//    public List<Order> getOrdersForUser(Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User with id "+userId+" not found."));
//        return user.getOrders();
//    }
//
//    public CartItem addProductToCart(Long productId, Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User with id "+userId+" not found."));
//        Product product = productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Product not found."));
//        CartItem item = new CartItem(1, user, product);
//        user.getCartItems().add(item);
//        return item;
//    }

    public List<WishList> getAllWishListsForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User with id "+userId+" not found."));
        return user.getWishLists();
    }

    public WishList getWishListById(Long wishListId) {
//        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User with id "+userId+" not found."));
            WishList wishList = wishListRepository.findById(wishListId).orElseThrow(()->new WishListNotFoundException("Wishlist with id "+wishListId+" not found."));
            return wishList;
    }

    public WishList updadteWishList(Long wishListId, WishList newWishList){
        WishList wishList = wishListRepository.findById(wishListId).orElseThrow(()->new WishListNotFoundException("Wishlist with id "+wishListId+" not found."));
        BeanUtils.copyProperties(newWishList, wishList, "id");
        return wishListRepository.save(wishList);
    }

    public void deleteWishListById(Long wishListId) {
        WishList wishList = wishListRepository.findById(wishListId).orElseThrow(()->new WishListNotFoundException("Wishlist with id "+wishListId+" not found."));
        wishListRepository.deleteById(wishListId);
        return;
    }

    public WishList createNewWishList(WishList wishList) {
        Long userId = getUserIdFromContext();
        User user = userRepository.findById(this.userId).orElseThrow(()->new UserNotFoundException("User with id "+userId+" not found."));
        user.getWishLists().add(wishList);
        wishList.setUser(user);
        return wishListRepository.save(wishList);
    }

    public List<CartItem> getCart() {
        User user
    }
}
