package com.example.demo.Controller;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Model.Review;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GET     /api/products                     -> Get all products
 * POST    /api/products                     -> Add a new product
 * GET     /api/products/{productId}         -> Get product by ID
 * PUT     /api/products/{productId}         -> Update product details
 * DELETE  /api/products/{productId}         -> Delete a product
 *
 * GET     /api/products/{productId}/reviews -> Get all reviews for a product
 * POST    /api/products/{productId}/reviews -> Add a review for a product
 *
// * GET     /api/products/tags/{tagId}        -> Get products with a specific tag
 * GET     /api/products/categories          -> Get all categories for all products
 * POST    /api/products/categories          -> Create a product category.
 * DELETE  /api/products/categories/{categoryId} -> Delete a product category
 * GET     /api/products/categories/{categoryId}/getAllProducts-> Get all products for category with id category id
 * GET     /api/products/categories/selectMultiple -> Get a set of categories as request body and return a inclusive list of all products in those categories
 *
 * */

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products =  productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product product1 = productService.addProduct(product);
        return new ResponseEntity<>(product1, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId){
        return new ResponseEntity<>(productService.findProductById(productId), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product product){
        return new ResponseEntity<>(productService.updateProduct(productId, product), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId){
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable Long productId){
        List<Review> reviews = productService.getReviewsById(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<?> postProductReviews(@PathVariable Long productId, @RequestBody Review review){
        return new ResponseEntity<>(productService.addProductReview(productId, review), HttpStatus.OK);
    }

    
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        return new ResponseEntity<>(productService.getCategories(), HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}/getAllProducts")
    public ResponseEntity<List<Product>> getProductsFromCategoty(@PathVariable Long categoryId){
        return new ResponseEntity<>(productService.getProductsByCategory(categoryId), HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Category> deleteCategoryById(@PathVariable Long categoryId){
        return new ResponseEntity<>(productService.deleteCategoryById(categoryId), HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category category){
        return new ResponseEntity<>(productService.addCategory(category), HttpStatus.OK);
    }

    @GetMapping("/categories/product/selectMultiple")
    public ResponseEntity<List<Product>> getProductsFromMultipleCategories(@RequestBody List<Category> categories){
        return new ResponseEntity<>(productService.getProductsFromMultipleCategories(categories), HttpStatus.OK);
    }



}
