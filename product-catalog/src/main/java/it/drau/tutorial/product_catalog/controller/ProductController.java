package it.drau.tutorial.product_catalog.controller;

import it.drau.tutorial.product_catalog.model.Product;
import it.drau.tutorial.product_catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @PostMapping("/product")
    public Product addProduct(@RequestBody Product product){
        return productRepository.insert(product);
    }

    @PutMapping("/product")
    public Product updateProduct(@RequestBody Product product){
        return productRepository.save(product);
    }

    @GetMapping("/product/{id}")
    public Product getProductDetails(@PathVariable String id){
        return productRepository.findById(id).orElseThrow();
    }

    @DeleteMapping("/product/{id}")
    public String deleteProduct(@PathVariable String id) {
        productRepository.deleteById(id);
        return "Product Deleted";
    }

    @GetMapping("/product")
    public List<Product> getProductList(){
        return productRepository.findAll();
    }
}
