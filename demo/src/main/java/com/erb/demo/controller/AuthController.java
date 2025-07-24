package com.erb.demo.controller;
import com.erb.demo.jwt.JwtTokenProvider;
import com.erb.demo.dto.AuthRequest;
import com.erb.demo.dto.AuthResponse;
import com.erb.demo.model.Customer;
import com.erb.demo.model.Employee;
import com.erb.demo.security.GeneralUserDetailsService;
import com.erb.demo.service.CustomerService;
import com.erb.demo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final GeneralUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final  EmployeeService employeeService;
    public AuthController(GeneralUserDetailsService userDetailsService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          CustomerService customerService,
                          EmployeeService employeeService,
                          PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            String token = tokenProvider.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Email or password is incorrect");
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(404).body("User not found");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PostMapping("/signup/customer")
    public ResponseEntity<?> signupCustomer(@Valid @RequestBody AuthRequest request) {
        try {
            try {
                userDetailsService.loadUserByUsername(request.getEmail());
                return ResponseEntity.status(409).body("Email already in use");
            } catch (UsernameNotFoundException e) {

            }
            Customer newCustomer = new Customer();
            newCustomer.setEmail(request.getEmail());
            newCustomer.setPassword(passwordEncoder.encode(request.getPassword()));
            newCustomer.setName(request.getName());
            newCustomer.setAddress(request.getAddress());
            customerService.save(newCustomer);
            String token = tokenProvider.generateToken(request.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PostMapping("/signup/employee")
    public ResponseEntity<?> signupEmployee(@Valid @RequestBody AuthRequest request) {
        try {
            try {
                userDetailsService.loadUserByUsername(request.getEmail());
                return ResponseEntity.status(409).body("Email already in use");
            } catch (UsernameNotFoundException e) {

            }
            Employee newEmployee = new Employee();
            newEmployee.setEmail(request.getEmail());
            newEmployee.setPassword(passwordEncoder.encode(request.getPassword()));
            newEmployee.setName(request.getName());
            String requestedRank = request.getRank();
            if (requestedRank == null) {
                return ResponseEntity.badRequest().body("Rank must be provided");
            }
            if (requestedRank.equalsIgnoreCase("SUPER_ADMIN")) {
                return ResponseEntity.status(403).body("Cannot assign SUPER_ADMIN rank");
            }
            try {
                Employee.Rank rankEnum = Employee.Rank.valueOf(requestedRank.toUpperCase());
                newEmployee.setRank(rankEnum);
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body("Invalid rank value");
            }
            employeeService.save(newEmployee);
            String token = tokenProvider.generateToken(request.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

}
