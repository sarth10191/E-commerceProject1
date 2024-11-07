package com.example.demo.Controller;

import com.example.demo.Exceptions.UsernameAlreadyExistsException;
import com.example.demo.Model.Role;
import com.example.demo.Model.User;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Security.JwtUtils;
import com.example.demo.Security.LoginRequest;
import com.example.demo.Security.LoginResponse;
import com.example.demo.Security.RegisterRequest;
import com.example.demo.Service.UserService;
import org.hibernate.mapping.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public LoginController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * @param loginRequest
     * @return responseEntity*/
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        }catch (AuthenticationException exception){
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad Credentials");
            map.put("status",false);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails =(UserDetails)authentication.getPrincipal();
        String jwtToken =jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles =userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        LoginResponse loginResponse =new LoginResponse(userDetails.getUsername(), jwtToken, roles);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    /**
     * @param  registerRequest
     * @return String*/
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
        if(userRepository.findUserByUsername(registerRequest.getUsername()).isPresent()){
            throw new UsernameAlreadyExistsException("Username Already Exists");
        }
        String encryptedPassword = passwordEncoder.encode(registerRequest.getPassword());
        Set<Role> roles = new HashSet<>();
        /*
        Check if role is present in table already if not create new else don't create.
        * */
        for (Role role : registerRequest.getRoles()) {
            Role existingRole = roleRepository.findById(role.getRoleName())
                    .orElseGet(() -> roleRepository.save(role));  // Save if not present
            roles.add(existingRole);
        }
        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                encryptedPassword,
                roles
        );
        User saved = userRepository.save(user);
        saved.setPassword(registerRequest.getPassword());
        return new ResponseEntity<>("User"+saved+" Registered Successfully", HttpStatus.OK);
    }
}
