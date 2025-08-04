package com.erb.demo.EmailService;

import com.erb.demo.dto.DTO.EmailRequest;
import com.erb.demo.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailServiceClient {

    private final RestTemplate restTemplate;

    public EmailServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendOrderStatusEmail(Order order) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(order.getCustomer().getEmail());
        emailRequest.setSubject("Order Status Update #" + order.getId());
        emailRequest.setBody("Hello " + order.getCustomer().getName() + ", your order status is now: " + order.getStatus());

        String emailServiceUrl = "http://localhost:8081/api/email/send";
        ResponseEntity<String> response = restTemplate.postForEntity(emailServiceUrl, emailRequest, String.class);
        System.out.println("Email service response: " + response.getBody());
    }
}
