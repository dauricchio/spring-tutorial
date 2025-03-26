package it.drau.tutorial.order_service.model;

import org.springframework.data.annotation.Id;

public record Product(@Id String id, String title) {
}
