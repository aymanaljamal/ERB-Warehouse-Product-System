package com.erb.demo;

import com.erb.demo.controller.CustomerControllerTest;
import com.erb.demo.repository.DeliveryRepositoryTest;
import com.erb.demo.service.ProductServiceImplTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DemoApplicationTests {

	@Test
	void testCustomerController() throws Exception {
		CustomerControllerTest customerTest = new CustomerControllerTest();
		customerTest.testGetAllCustomers();

	}

	@Test
	void testDeliveryRepository() {
		DeliveryRepositoryTest deliveryTest = new DeliveryRepositoryTest();
		deliveryTest.testGetEmployeeSummary();
	}

	@Test
	void testProductService() {
		ProductServiceImplTest productTest = new ProductServiceImplTest();
		productTest.setup();
		productTest.testGetAll();

	}
}
