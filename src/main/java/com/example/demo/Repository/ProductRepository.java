package com.example.demo.Repository;

import com.example.demo.Model.Product;
import com.example.demo.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}