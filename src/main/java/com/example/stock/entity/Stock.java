package com.example.stock.entity;

import jakarta.persistence.*;
import org.springframework.util.Assert;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    @Version
    private Long version;

    private Long quantity;

    public Stock() {
    }

    public Stock(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void decrease(Long quantity) {
        Assert.isTrue(this.quantity >= quantity, "stock quantity must be greater than the quantity to be reduced");
        this.quantity -= quantity;
    }
}
