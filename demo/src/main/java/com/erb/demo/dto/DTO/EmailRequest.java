package com.erb.demo.dto.DTO;

import lombok.Data;

@Data
public class EmailRequest {
    private String to;
    private String subject;
    private String body;
}
