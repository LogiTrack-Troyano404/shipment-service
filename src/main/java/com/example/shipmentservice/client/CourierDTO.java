package com.example.shipmentservice.client;

import lombok.Data;

@Data
public class CourierDTO {
    private Long id;
    private String name;
    private String vehiclePlate;
    private String status;
}
