package com.dispenser.beertapdipenser.repository;

import com.dispenser.beertapdipenser.model.BeerDispenserUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerDispenserUsageRepository extends JpaRepository<BeerDispenserUsage, Long> {
}
