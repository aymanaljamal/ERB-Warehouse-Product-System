package com.erb.demo.EmailService;

import com.erb.demo.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderStatusEmail(Order order) {
        if (order.getCustomer() == null || order.getCustomer().getEmail() == null) {

            return;
        }

        String to = order.getCustomer().getEmail();
        String subject = "Order #" + order.getId() + " status updated";
        String body = "Dear " + order.getCustomer().getName() + ",\n\n" +
                "Your order status has changed to: " + order.getStatus() + ".\nThank you.";

        sendSimpleMail(to, subject, body);
    }

    private void sendSimpleMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
