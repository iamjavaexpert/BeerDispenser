package com.dispenser.beertapdipenser.dtos;

import lombok.Data;

@Data
public class DispenserUsageResponse {
    private double amount;
    private double volume;
}
