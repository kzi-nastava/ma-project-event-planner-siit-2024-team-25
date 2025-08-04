package com.team25.event.planner.offering.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductCard extends OfferingCard {

    public ProductCard(Long id, String name, double price, String owner, double rating) {
        super(id, name, price, owner, rating, false);
    }
}
