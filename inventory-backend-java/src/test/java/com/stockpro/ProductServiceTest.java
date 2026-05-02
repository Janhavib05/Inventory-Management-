package com.stockpro;

import com.stockpro.dto.ProductDTO;
import com.stockpro.exception.ResourceNotFoundException;
import com.stockpro.model.Product;
import com.stockpro.repository.ProductRepository;
import com.stockpro.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock ProductRepository repo;
    @InjectMocks ProductService service;

    Product sample;

    @BeforeEach
    void setup() {
        sample = Product.builder()
                .id(1L).name("Mouse").desc("Wireless")
                .price(new BigDecimal("29.99")).qty(50)
                .build();
    }

    @Test
    void getAll_returnsAllProducts() {
        when(repo.findAll()).thenReturn(List.of(sample));
        List<ProductDTO.Response> result = service.getAll(null);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Mouse");
    }

    @Test
    void getAll_withSearch_usesKeyword() {
        when(repo.searchByKeyword("mouse")).thenReturn(List.of(sample));
        List<ProductDTO.Response> result = service.getAll("mouse");
        assertThat(result).hasSize(1);
        verify(repo).searchByKeyword("mouse");
    }

    @Test
    void getById_found_returnsProduct() {
        when(repo.findById(1L)).thenReturn(Optional.of(sample));
        ProductDTO.Response res = service.getById(1L);
        assertThat(res.getId()).isEqualTo(1L);
    }

    @Test
    void getById_notFound_throws() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_savesAndReturnsProduct() {
        ProductDTO.Request req = new ProductDTO.Request("Mouse", "Wireless", new BigDecimal("29.99"), 50);
        when(repo.save(any())).thenReturn(sample);
        ProductDTO.Response res = service.create(req);
        assertThat(res.getName()).isEqualTo("Mouse");
        verify(repo).save(any(Product.class));
    }

    @Test
    void delete_existingProduct_deletesSuccessfully() {
        when(repo.existsById(1L)).thenReturn(true);
        service.delete(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    void delete_nonExistingProduct_throws() {
        when(repo.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
