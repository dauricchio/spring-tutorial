package it.drau.tutorial.product_catalog.model;

import org.springframework.data.annotation.Id;

public record Product(@Id String id, String title) {
}
