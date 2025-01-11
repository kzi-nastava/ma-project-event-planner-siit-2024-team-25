package com.team25.event.planner.service.model;


import com.team25.event.planner.service.enums.ProductServiceType;

import com.team25.event.planner.user.model.User;

import java.util.List;


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
    protected User owner;
    protected List<String> images;

    public Offering(){}



}
