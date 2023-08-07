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
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class BeerDispenserService {
    private @Autowired BeerDispenserRepository beerDispenserRepository;

    private @Autowired BeerDispenserUsageRepository beerDispenserUsageRepository;

    public double usedDispensed = 0.0;
    Flux<Integer> flux;
    Disposable subscription;
    private double currentVolumeConsumed = 0.0;
    private double currentAmountConsumed = 0.0;


    public long createDispenser(DispenserRequest dispenserRequest) {
        BeerDispenser beerDispenser = new BeerDispenser();
        beerDispenser.setTotalVolume(dispenserRequest.getTotalVolume());
        beerDispenser.setFlowVolume(dispenserRequest.getFlowVolume());
        beerDispenser.setPricePerML(dispenserRequest.getPricePerML());
        beerDispenser.setCreatedDate(new Date());
        beerDispenser.setUpdatedDate(new Date());
        beerDispenser.setStatus(BeerDispenser.DispenserStatus.CLOSE);
        beerDispenser.setLeftVolume(dispenserRequest.getTotalVolume());
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

    public DispenserUsageResponse getData() {
        return  createDispenserResponse(currentAmountConsumed,currentVolumeConsumed,"beer tap dispenser status open");
    }

    public DispenserUsageResponse updateDispenserStatus(Long dispenserId, BeerDispenser.DispenserStatus status) {
        BeerDispenser dispenser = beerDispenserRepository.findById(dispenserId).orElse(null);
        if (dispenser != null) {
            if (status == BeerDispenser.DispenserStatus.CLOSE && dispenser.getStatus() == BeerDispenser.DispenserStatus.OPEN) {
                log.info("beer dispenser status close");
                currentVolumeConsumed = 0.0;
                currentAmountConsumed = 0.0;
                return closeDispenser(dispenser);
            } else if(status == BeerDispenser.DispenserStatus.OPEN && dispenser.getStatus() == BeerDispenser.DispenserStatus.CLOSE
                    && dispenser.getLeftVolume() <= dispenser.getTotalVolume() && dispenser.getLeftVolume() != 0) {
                log.info("beer dispenser status open");
                currentVolumeConsumed = 0.0;
                currentAmountConsumed = 0.0;
                dispenser.setStatus(BeerDispenser.DispenserStatus.OPEN);
                dispenser.setStartTime(LocalDateTime.now());
                dispenser.setUpdatedDate(new Date());
                beerDispenserRepository.save(dispenser);
                // Cancel any existing subscription if present
                if (subscription != null) {
                    subscription.dispose();
                }
                flux = Flux.range(1, (int) ((int)(dispenser.getTotalVolume()-usedDispensed)/dispenser.getFlowVolume()))
                        .delayElements(Duration.ofSeconds(1))
                        .doOnCancel(() -> log.error("Drink Stopped."))
                        .doOnComplete(() -> {
                            log.info("Drink Out of Stock.");
                            closeDispenser(dispenser);
                        });
                subscription = flux.subscribe(i -> {
                            log.info("Received: " + i + "ML");
                            usedDispensed+=dispenser.getFlowVolume();
                            if (usedDispensed >= dispenser.getTotalVolume() && dispenser.getStatus()== BeerDispenser.DispenserStatus.OPEN) {
                                // Close the dispenser when the remaining volume reaches zero
                                currentVolumeConsumed += dispenser.getFlowVolume();
                                double currentAmount = currentVolumeConsumed * dispenser.getPricePerML();
                                currentAmountConsumed = BigDecimal.valueOf(currentAmount).setScale(3, RoundingMode.HALF_UP).doubleValue();
                                closeDispenser(dispenser);
                            }else {
                                // Update the current consumption details while the dispenser is open
                                currentVolumeConsumed += dispenser.getFlowVolume();
                                double currentAmount = currentVolumeConsumed * dispenser.getPricePerML();
                                currentAmountConsumed = BigDecimal.valueOf(currentAmount).setScale(3, RoundingMode.HALF_UP).doubleValue();
                            }
                        }, e -> log.error("Error: " + e.getMessage())
                );
                return createDispenserResponse(0.0,0.0,"beer tap dispenser status open");
            }
            log.info("beer dispenser already open or closed");
            return createDispenserResponse(0.0,0.0,"beer tap dispenser already open or closed");
        }
        log.info("beer dispenser not available with this id: "+dispenserId);
        return null;
    }

    public DispenserUsageResponse closeDispenser(BeerDispenser dispenser){
        dispenser.setStatus(BeerDispenser.DispenserStatus.CLOSE);
        if(subscription != null) {
            subscription.dispose();
        }
        LocalDateTime endTime = LocalDateTime.now();
        dispenser.setEndTime(endTime);

        double flowVolume = dispenser.getFlowVolume();
        double timeInSeconds = Duration.between(dispenser.getStartTime(), endTime).toSeconds();
        double totalLitersDispensed;

        // Update totalLitersDispensed when userDispensed is equal to totalVolume
        if (usedDispensed >= dispenser.getTotalVolume()) {
            totalLitersDispensed = dispenser.getTotalVolume();
            dispenser.setLeftVolume(0);
        }else {
            totalLitersDispensed = flowVolume * timeInSeconds;
            if (dispenser.getTotalDispensedUsed() > 0) {
                dispenser.setLeftVolume(dispenser.getLeftVolume() - totalLitersDispensed);
            } else {
                dispenser.setLeftVolume(dispenser.getTotalVolume() - totalLitersDispensed);
            }
        }
        double totalAmountCharged = totalLitersDispensed * dispenser.getPricePerML();
        totalAmountCharged = BigDecimal.valueOf(totalAmountCharged).setScale(3, RoundingMode.HALF_UP).doubleValue();

        dispenser.setTotalSpend(dispenser.getTotalSpend() + totalAmountCharged);

        BeerDispenserUsage beerUsage = new BeerDispenserUsage();
        beerUsage.setDispenser(dispenser);
        beerUsage.setTotalLitersDispensed(totalLitersDispensed);
        beerUsage.setTotalAmountCharged(totalAmountCharged);
        log.info("created beer usage dispenser : ");
        dispenser.setTotalDispensedUsed(dispenser.getTotalDispensedUsed() + 1);
        beerDispenserUsageRepository.save(beerUsage);
        dispenser.setUpdatedDate(new Date());
        beerDispenserRepository.save(dispenser);
        return createDispenserResponse(totalAmountCharged,totalLitersDispensed,"beer tap dispenser close");
    }

    private DispenserUsageResponse createDispenserResponse(double amount,double volume,String statusMessage) {
        DispenserUsageResponse dispenserUsageResponse = new DispenserUsageResponse();
        dispenserUsageResponse.setAmount(amount);
        dispenserUsageResponse.setVolume(volume);
        dispenserUsageResponse.setStatusMessage(statusMessage);
        return dispenserUsageResponse;
    }


}