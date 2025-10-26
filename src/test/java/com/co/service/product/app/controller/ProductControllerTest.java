package com.co.service.product.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.co.service.product.app.exception.RestExceptionHandler;
import com.co.service.product.app.model.Product;
import com.co.service.product.app.productservice.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService svc;

    @InjectMocks
    private ProductController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Product sample;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        sample = new Product();
        sample.setId(1L);
        sample.setName("Sample");
        sample.setDescription("Desc");
        sample.setPrice(new BigDecimal("10.00"));
    }

    @Test
    void createShouldReturn201() throws Exception {
        Map<String,Object> payload = Map.of("attributes", Map.of("name","X","description","Y","price",12.34));
        when(svc.create(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.type").value("products"))
            .andExpect(jsonPath("$.data.id").value("1"))
            .andExpect(jsonPath("$.data.attributes.name").value("X"));
    }

    @Test
    void getShouldReturn200() throws Exception {
        when(svc.findById(1L)).thenReturn(Optional.of(sample));

        mockMvc.perform(get("/api/products/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value("1"))
            .andExpect(jsonPath("$.data.attributes.name").value("Sample"));
    }

    @Test
    void listShouldReturn200() throws Exception {
        Page<Product> page = new PageImpl<>(List.of(sample));
        when(svc.list(any())).thenReturn(page);

        mockMvc.perform(get("/api/products?page=0&size=10").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].id").value("1"))
            .andExpect(jsonPath("$.meta.totalElements").value(1));
    }

    @Test
    void updateShouldReturn200() throws Exception {
        Map<String,Object> payload = Map.of("attributes", Map.of("name","Updated","description","NewDesc","price",99.99));
        when(svc.update(eq(1L), any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(1);
            p.setId(1L);
            return p;
        });

        mockMvc.perform(patch("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value("1"))
            .andExpect(jsonPath("$.data.attributes.name").value("Updated"));
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        doNothing().when(svc).delete(1L);

        mockMvc.perform(delete("/api/products/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    void getNotFoundShouldReturn404() throws Exception {
        when(svc.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/99").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errors[0].status").value("404"));
    }
}
