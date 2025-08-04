package com.erb.demo.controller;

import com.erb.demo.model.Customer;
import com.erb.demo.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllCustomers() throws Exception {
        System.out.println("Starting testGetAllCustomers...");

        Customer c1 = new Customer(1L, "Ali", "ali@example.com", "password123", "0599111111", "Ramallah");
        Customer c2 = new Customer(2L, "Lina", "lina@example.com", "password123", "0599222222", "Nablus");

        when(customerService.getAll()).thenAnswer(invocation -> {
            System.out.println("Mock customerService.getAll() called");
            return Arrays.asList(c1, c2);
        });

        mockMvc.perform(get("/api/customers"))
                .andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        System.out.println("Starting testGetCustomerById...");

        Customer customer = new Customer(1L, "Ali", "ali@example.com", "password123", "0599111111", "Ramallah");

        when(customerService.getById(1L)).thenAnswer(invocation -> {
            System.out.println("Mock customerService.getById(1L) called");
            return customer;
        });

        mockMvc.perform(get("/api/customers/1"))
                .andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ali@example.com"));
    }

    @Test
    public void testCreateCustomer() throws Exception {
        System.out.println("Starting testCreateCustomer...");

        Customer customer = new Customer(null, "New User", "new@example.com", "password123", "0599333333", "Jericho");

        Customer saved = new Customer(1L, "New User", "new@example.com", "password123", "0599333333", "Jericho");

        when(customerService.save(any(Customer.class))).thenAnswer(invocation -> {
            System.out.println("Mock customerService.save() called");
            return saved;
        });

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        System.out.println("Starting testUpdateCustomer...");

        Customer existing = new Customer(1L, "Old Name", "old@example.com", "password123", "0599000000", "Hebron");
        Customer updated = new Customer(1L, "New Name", "new@example.com", "newpass456", "0599444444", "Bethlehem");

        when(customerService.getById(1L)).thenAnswer(invocation -> {
            System.out.println("Mock customerService.getById(1L) called");
            return existing;
        });
        when(customerService.save(any(Customer.class))).thenAnswer(invocation -> {
            System.out.println("Mock customerService.save() called");
            return updated;
        });

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        System.out.println("Starting testDeleteCustomer...");

        mockMvc.perform(delete("/api/customers/1"))
                .andDo(result -> System.out.println("Delete response status: " + result.getResponse().getStatus()))
                .andExpect(status().isOk());
    }
}
