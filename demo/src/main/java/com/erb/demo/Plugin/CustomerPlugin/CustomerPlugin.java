package com.erb.demo.Plugin.CustomerPlugin;

import com.erb.demo.model.Customer;

public interface CustomerPlugin {
    boolean supports(Customer customer);
    void process(Customer customer);
}
