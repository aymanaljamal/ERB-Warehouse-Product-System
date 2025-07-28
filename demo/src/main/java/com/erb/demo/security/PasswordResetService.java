package com.erb.demo.security;

import com.erb.demo.EmailService.EmailService;
import com.erb.demo.model.Customer;
import com.erb.demo.model.PasswordResetToken;
import com.erb.demo.repository.CustomerRepository;
import com.erb.demo.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final CustomerRepository customerRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService mailService;

    public void createResetToken(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new RuntimeException("Customer not found");
        }

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setCustomer(customer);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:8080/api/customers/reset-password?token=" + token;
        String subject = "Password Reset Request";
        String message = "Hi " + customer.getName() + ",\n\n" +
                "Click the link below to reset your password:\n" +
                resetLink + "\n\n" +
                "If you didn't request this, please ignore this email.";

        mailService.sendEmail(customer.getEmail(), subject, message);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        Customer customer = resetToken.getCustomer();
        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);

        tokenRepository.delete(resetToken);
    }
}
