package com.dispenser.beertapdipenser.dtos;

import lombok.Data;

@Data
public class DispenserRequest {
    private  Double totalVolume;
    private Double flowVolume;
    private Float pricePerML;
}
