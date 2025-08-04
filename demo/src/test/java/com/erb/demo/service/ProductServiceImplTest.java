package com.erb.demo.service;

import com.erb.demo.model.Product;
import com.erb.demo.repository.ProductRepository;
import com.erb.demo.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    @BeforeEach
    public  void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public  void testGetAll() {
        Product p1 = Product.builder()
                .id(1L)
                .name("Product1")
                .description("Description1")
                .price(10.0)
                .quantity(5)
                .stockReceipts(Collections.emptyList())
                .build();

        Product p2 = Product.builder()
                .id(2L)
                .name("Product2")
                .description("Description2")
                .price(20.0)
                .quantity(3)
                .stockReceipts(Collections.emptyList())
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Product> products = service.getAll();

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(2);
        verify(repository, times(1)).findAll();
    }

    @Test
    public  void testGetById_Found() {
        Product p = Product.builder()
                .id(1L)
                .name("Product1")
                .description("Description1")
                .price(10.0)
                .quantity(5)
                .stockReceipts(Collections.emptyList())
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(p));

        Product result = service.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Product1");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    public void testGetById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Product result = service.getById(1L);

        assertThat(result).isNull();
        verify(repository, times(1)).findById(1L);
    }

    @Test
    public  void testSave() {
        Product p = Product.builder()
                .name("New Product")
                .description("New Description")
                .price(15.0)
                .quantity(7)
                .stockReceipts(Collections.emptyList())
                .build();

        Product saved = Product.builder()
                .id(1L)
                .name("New Product")
                .description("New Description")
                .price(15.0)
                .quantity(7)
                .stockReceipts(Collections.emptyList())
                .build();

        when(repository.save(p)).thenReturn(saved);

        Product result = service.save(p);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(repository, times(1)).save(p);
    }

    @Test
    public  void testDelete() {
        Long id = 1L;

        doNothing().when(repository).deleteById(id);

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }
}
