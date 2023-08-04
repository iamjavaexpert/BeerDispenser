package com.dispenser.beertapdipenser.repository;


import com.dispenser.beertapdipenser.model.BeerDispenser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerDispenserRepository extends JpaRepository<BeerDispenser, Long> {
}
