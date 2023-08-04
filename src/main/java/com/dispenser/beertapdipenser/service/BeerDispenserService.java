package com.dispenser.beertapdipenser.service;

import com.dispenser.beertapdipenser.dtos.DispenserRequest;
import com.dispenser.beertapdipenser.dtos.DispenserUsageResponse;
import com.dispenser.beertapdipenser.model.BeerDispenser;
import com.dispenser.beertapdipenser.model.BeerDispenserUsage;
import com.dispenser.beertapdipenser.repository.BeerDispenserRepository;
import com.dispenser.beertapdipenser.repository.BeerDispenserUsageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
public class BeerDispenserService {
    private @Autowired BeerDispenserRepository beerDispenserRepository;

    private @Autowired BeerDispenserUsageRepository beerDispenserUsageRepository;


    public long createDispenser(DispenserRequest dispenserRequest) {
        BeerDispenser beerDispenser = new BeerDispenser();
        beerDispenser.setTotalVolume(dispenserRequest.getTotalVolume());
        beerDispenser.setFlowVolume(dispenserRequest.getFlowVolume());
        beerDispenser.setPricePerML(dispenserRequest.getPricePerML());
        beerDispenser.setCreatedDate(new Date());
        beerDispenser.setUpdatedDate(new Date());
        beerDispenser.setStatus(BeerDispenser.DispenserStatus.CLOSE);
        beerDispenserRepository.save(beerDispenser);
        log.info("created dispenser : ", beerDispenser.getId());
        return beerDispenser.getId();
    }

    public Long updateDispenser(long dispenserId,DispenserRequest dispenserRequest) {
        BeerDispenser beerDispenser = beerDispenserRepository.findById(dispenserId).orElse(null);
        if(beerDispenser != null) {
            if(dispenserRequest.getTotalVolume() != null) {
                beerDispenser.setTotalVolume(dispenserRequest.getTotalVolume());
            }
            if (dispenserRequest.getFlowVolume() != null) {
                beerDispenser.setFlowVolume(dispenserRequest.getFlowVolume());
            }
            if(dispenserRequest.getPricePerML() != null) {
                beerDispenser.setPricePerML(dispenserRequest.getPricePerML());
            }
            beerDispenser.setUpdatedDate(new Date());
            beerDispenser.setStatus(BeerDispenser.DispenserStatus.CLOSE);
            beerDispenserRepository.save(beerDispenser);
            log.info("updated dispenser : ", beerDispenser.getId());
            return beerDispenser.getId();
        }
        return null;
    }

    public BeerDispenser getDispenser(long dispenserId) {
        return  beerDispenserRepository.findById(dispenserId).orElse(null);
    }

    public DispenserUsageResponse updateDispenserStatus(Long dispenserId, BeerDispenser.DispenserStatus status) {
        BeerDispenser dispenser = beerDispenserRepository.findById(dispenserId).orElse(null);
        if (dispenser != null) {
            if (status == BeerDispenser.DispenserStatus.CLOSE && dispenser.getStatus() == BeerDispenser.DispenserStatus.OPEN) {
                log.info("beer dispenser status close");
                dispenser.setStatus(BeerDispenser.DispenserStatus.CLOSE);

                LocalDateTime endTime = LocalDateTime.now();
                dispenser.setEndTime(endTime);

                double flowVolume = dispenser.getFlowVolume();
                double timeInSeconds = Duration.between(dispenser.getStartTime(), endTime).toSeconds();
                double totalLitersDispensed = flowVolume * timeInSeconds;
                double totalAmountCharged = totalLitersDispensed * dispenser.getPricePerML();

                totalAmountCharged = BigDecimal.valueOf(totalAmountCharged).setScale(3, RoundingMode.HALF_UP).doubleValue();

                dispenser.setTotalSpend(dispenser.getTotalSpend() + totalAmountCharged);

                BeerDispenserUsage beerUsage = new BeerDispenserUsage();
                beerUsage.setDispenser(dispenser);
                beerUsage.setTotalLitersDispensed(totalLitersDispensed);
                beerUsage.setTotalAmountCharged(totalAmountCharged);
                beerUsage.setCreatedDate(new Date());
                beerUsage.setUpdatedDate(new Date());
                beerDispenserUsageRepository.save(beerUsage);
                log.info("created beer usage dispenser : ");
                if(dispenser.getTotalDispensedUsed()>0) {
                    dispenser.setLeftVolume(dispenser.getLeftVolume() - totalLitersDispensed);
                }else {
                    dispenser.setLeftVolume(dispenser.getTotalVolume() - totalLitersDispensed);
                }
                dispenser.setTotalDispensedUsed(dispenser.getTotalDispensedUsed() + 1);
                dispenser.setUpdatedDate(new Date());
                beerDispenserRepository.save(dispenser);
                DispenserUsageResponse dispenserUsageResponse = new DispenserUsageResponse();
                dispenserUsageResponse.setAmount(totalAmountCharged);
                dispenserUsageResponse.setVolume(totalLitersDispensed);
                return dispenserUsageResponse;
            } else if(status == BeerDispenser.DispenserStatus.OPEN && dispenser.getStatus() == BeerDispenser.DispenserStatus.CLOSE
                    && dispenser.getLeftVolume() <= dispenser.getTotalVolume()) {
                log.info("beer dispenser status open");
                dispenser.setStatus(BeerDispenser.DispenserStatus.OPEN);
                dispenser.setStartTime(LocalDateTime.now());
                dispenser.setUpdatedDate(new Date());
                beerDispenserRepository.save(dispenser);
            }
            log.info("beer dispenser already open or closed");
            return null;
        }
        log.info("beer dispenser not available with this id: "+dispenserId);
        return null;
    }

}
