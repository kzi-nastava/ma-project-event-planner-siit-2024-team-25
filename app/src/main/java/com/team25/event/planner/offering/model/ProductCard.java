package com.team25.event.planner.offering.model;

public class ProductCard extends OfferingCard {

    public ProductCard(int id, String name, double price, String owner, double rating) {
        super(id, name, price, owner, rating, false);
    }
}
