package it.drau.tutorial.order_service.controller;


import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import it.drau.tutorial.order_service.model.Order;
import it.drau.tutorial.order_service.model.Product;
import it.drau.tutorial.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final EurekaClient eurekaClient;

    private final RestClient restClient = RestClient.builder().build();

    private final OrderRepository orderRepository;

    @PostMapping("/order")
    public Order addOrder(@RequestBody Order order){
        InstanceInfo productCatalogInstance = eurekaClient.getNextServerFromEureka("product-catalog",false);

        Product serviceAResponse = restClient.get()
                .uri(productCatalogInstance.getHomePageUrl() + "/product/{id}", order.productId())
                .retrieve()
                .body(Product.class);

        Order newOrder = new Order(order.id(), order.productId(), serviceAResponse.title());
        return orderRepository.insert(newOrder);
    }

    @PutMapping("/order")
    public Order updateOrder(@RequestBody Order order){
        return orderRepository.save(order);
    }

    @GetMapping("/order/{id}")
    public Order getOrderDetails(@PathVariable String id){
        return orderRepository.findById(id).orElseThrow();
    }

    @DeleteMapping("/order/{id}")
    public String deleteOrder(@PathVariable String id) {
        orderRepository.deleteById(id);
        return "Order Deleted-"+id;
    }

    @GetMapping("/order")
    public List<Order> getOrderList(){
        return orderRepository.findAll();
    }
}
