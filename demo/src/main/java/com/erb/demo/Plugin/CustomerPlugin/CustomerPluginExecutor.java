package com.erb.demo.Plugin.CustomerPlugin;
import com.erb.demo.model.Customer;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CustomerPluginExecutor {

    private final List<CustomerPlugin> plugins;

    public CustomerPluginExecutor(List<CustomerPlugin> plugins) {
        this.plugins = plugins;
    }
    public void execute(Customer customer) {
        for (CustomerPlugin plugin : plugins) {
            if (plugin.supports(customer)) {
                plugin.process(customer);
            }
        }
    }



}
