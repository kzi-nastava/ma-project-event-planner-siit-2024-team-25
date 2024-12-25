package com.team25.event.planner.product_service.model;

import com.team25.event.planner.product_service.enums.ProductServiceType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public abstract class Offering {

    protected long id;
    protected String name;
    protected String description;
    protected double price;
    protected double discount;
    protected int imageURL;
    protected boolean visible;
    protected boolean available;
    protected ProductServiceType status;

    public Offering(){}



}
