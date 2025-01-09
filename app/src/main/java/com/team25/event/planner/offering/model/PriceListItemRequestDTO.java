package com.team25.event.planner.offering.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceListItemRequestDTO {
    private double price;
    private double discount;
}
