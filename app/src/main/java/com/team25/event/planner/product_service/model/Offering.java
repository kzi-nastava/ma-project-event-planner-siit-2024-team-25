package com.team25.event.planner.product_service.model;

import com.team25.event.planner.product_service.enums.ProductServiceType;


public abstract class Offering {

    protected long id;
    protected String name;
    protected String description;
    protected double price;
    protected double discount;
    protected int imageURL;
    protected boolean isVisible;
    protected boolean isAvailable;
    protected ProductServiceType status;

    public Offering(){}

    public Offering(long id, String name, String description, double price, double discount, int imageURL,
                    boolean isVisible, boolean isAvailable, ProductServiceType status){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.imageURL = imageURL;
        this.isAvailable = isAvailable;
        this.isVisible = isVisible;
        this.status = status;
    }

}
