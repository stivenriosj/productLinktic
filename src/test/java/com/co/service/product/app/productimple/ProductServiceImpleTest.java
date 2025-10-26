package com.co.service.product.app.productimple;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.co.service.product.app.helpers.ProductRepository;
import com.co.service.product.app.model.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceImpleTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductServiceImple svc;

    private Product sample;

    @BeforeEach
    void setup() {
        sample = new Product();
        sample.setId(1L);
        sample.setName("Sample");
        sample.setDescription("Desc");
        sample.setPrice(new BigDecimal("10.00"));
    }

    @Test
    void createShouldSaveAndReturn() {
        Product toSave = new Product();
        toSave.setName("New");
        toSave.setDescription("D");
        toSave.setPrice(new BigDecimal("5.5"));

        Product saved = new Product();
        saved.setId(2L);
        saved.setName("New");
        saved.setDescription("D");
        saved.setPrice(new BigDecimal("5.5"));

        when(repo.save(toSave)).thenReturn(saved);

        Product result = svc.create(toSave);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        verify(repo).save(toSave);
    }

    @Test
    void findByIdShouldReturnOptional() {
        when(repo.findById(1L)).thenReturn(Optional.of(sample));

        Optional<Product> res = svc.findById(1L);
        assertTrue(res.isPresent());
        assertEquals("Sample", res.get().getName());
    }

    @Test
    void listShouldReturnPage() {
        PageRequest pr = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(sample), pr, 1);
        when(repo.findAll(pr)).thenReturn(page);

        Page<Product> res = svc.list(pr);
        assertEquals(1, res.getTotalElements());
        assertEquals(sample.getName(), res.getContent().get(0).getName());
    }

    @Test
    void updateShouldModifyAndSave() {
        Product updated = new Product();
        updated.setName("Updated");
        updated.setDescription("NewDesc");
        updated.setPrice(new BigDecimal("99.99"));

        Product existing = new Product();
        existing.setId(5L);
        existing.setName("Old");
        existing.setDescription("OldDesc");
        existing.setPrice(new BigDecimal("1.00"));

        when(repo.findById(5L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product res = svc.update(5L, updated);
        assertEquals("Updated", res.getName());
        assertEquals(new BigDecimal("99.99"), res.getPrice());
        verify(repo).save(existing);
    }

    @Test
    void deleteShouldRemoveWhenExists() {
        when(repo.existsById(10L)).thenReturn(true);
        // no exception should be thrown
        assertDoesNotThrow(() -> svc.delete(10L));
        verify(repo).deleteById(10L);
    }

    @Test
    void deleteShouldThrowWhenNotExists() {
        when(repo.existsById(11L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> svc.delete(11L));
        verify(repo, never()).deleteById(11L);
    }
}
