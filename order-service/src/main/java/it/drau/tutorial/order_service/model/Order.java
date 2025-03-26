package it.drau.tutorial.order_service.model;

import org.springframework.data.annotation.Id;

public record Order(@Id String id, String productId, String productTitle) {
}
