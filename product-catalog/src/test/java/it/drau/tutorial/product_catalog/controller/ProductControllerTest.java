package it.drau.tutorial.product_catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.drau.tutorial.product_catalog.model.Product;
import it.drau.tutorial.product_catalog.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Product shoe = new Product("my_id", "my_title");
    private final List<Product> products = Collections.singletonList(this.shoe);

    @Test
    @DisplayName("Test getProductList()")
    void testGetProductList() throws Exception {
        when(this.productRepository.findAll()).thenReturn(this.products);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/product"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].title")
                                .value(this.shoe.title())
                );
    }

    @Test
    @DisplayName("Test getProductDetails(String)")
    void testGetProductDetails() throws Exception {
        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(shoe));

        // Act and Assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", shoe.id()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(shoe)))
        ;
    }

    @Test
    @DisplayName("Test updateProduct(Product)")
    void testUpdateProduct() throws Exception {
        when(productRepository.save(Mockito.any())).thenReturn(shoe);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/product")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoe)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(shoe)));
    }

    @Test
    @DisplayName("Test deleteProduct(String)")
    void testDeleteProduct() throws Exception {
        doNothing().when(productRepository).deleteById(Mockito.any());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", shoe.id()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Product Deleted"));
    }

    @Test
    @DisplayName("Test addProduct(Product)")
    void testAddProduct() throws Exception {
        when(productRepository.insert(Mockito.<Product>any())).thenReturn(shoe);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoe)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(shoe)));
    }

}
