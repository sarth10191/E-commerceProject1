package com.example.demo.Service;

import com.example.demo.Exceptions.CategoryNotFoundException;
import com.example.demo.Exceptions.ProductNotFoundException;
import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Model.Review;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.ReviewRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, UserService userService, ReviewRepository reviewRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.reviewRepository = reviewRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product updateProduct(Long productID, Product product) {
        Product productExisting = productRepository.findById(productID).orElseThrow(()->new ProductNotFoundException("Product with id "+productID+" not found."));
        BeanUtils.copyProperties(product, productExisting);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }


    public Product addProduct(Product product) {
        Product product1 = productRepository.save(product);
        return product1;
    }

    public Product findProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException("Product with id "+productId+" not found."));
        return product;
    }

    public String deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException("Product with id "+productId+" not found."));
        productRepository.deleteById(productId);
        return "deleted";
    }

    public List<Review> getReviewsById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException("Product with id "+productId+" not found."));
        return product.getReviews();
    }

    public Review addProductReview(Long productId, Review review) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException("Product with id "+productId+" not found."));
        List<Review> reviews = product.getReviews();
        reviews.add(review);
        product.setReviews(reviews);
        productRepository.save(product);
        review.setUser(userService.getUserById(userService.getUserId()));
        review.setProduct(product);
        reviewRepository.save(review);
        return review;
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new CategoryNotFoundException("Category not found"));
        return category.getProducts();
    }

    public Category deleteCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new CategoryNotFoundException("Category not found"));
        categoryRepository.deleteById(categoryId);
        return category;
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Product> getProductsFromMultipleCategories(List<Category> categories) {
        List<Product> products = new ArrayList<>();
        for(Category category: categories){
            products.addAll(getProductsByCategory(category.getId()));
        }
        return products;
    }
}
