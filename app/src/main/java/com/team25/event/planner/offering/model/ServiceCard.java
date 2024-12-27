package com.team25.event.planner.offering.model;

public class ServiceCard extends OfferingCard {

    public ServiceCard(int id, String name, double price, String owner, double rating) {
        super(id, name, price, owner, rating, true);
    }
}
