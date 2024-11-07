package com.example.demo.Repository;

import com.example.demo.Model.Address;
import com.example.demo.Model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
