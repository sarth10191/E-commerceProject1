package com.example.demo.Repository;

import com.example.demo.Model.Role;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
