package com.dispenser.beertapdipenser.model;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "beer_dispenser_usage")
@Data
public class BeerDispenserUsage extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dispenser_id")
    private BeerDispenser dispenser;

    private double totalLitersDispensed;

    private double totalAmountCharged;
}
