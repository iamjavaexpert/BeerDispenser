package com.dispenser.beertapdipenser.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "beer_dispenser")
@Data
public class BeerDispenser extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double totalVolume;
    private double leftVolume;
    private double flowVolume;
    private double totalSpend = 0;
    private float pricePerML;
    private int totalDispensedUsed = 0;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DispenserStatus status;
    public enum DispenserStatus {
        OPEN,CLOSE;
    }


}
