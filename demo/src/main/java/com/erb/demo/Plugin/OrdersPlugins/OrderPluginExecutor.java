package com.erb.demo.Plugin.OrdersPlugins;
import com.erb.demo.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderPluginExecutor {

    private final List<com.erb.demo.Plugin.OrdersPlugins.OrderPlugin> plugins;
    public void execute(Order order) {
        for (com.erb.demo.Plugin.OrdersPlugins.OrderPlugin plugin : plugins) {
            if (plugin.supports(order)) {
                plugin.process(order);
            }
        }
    }
}