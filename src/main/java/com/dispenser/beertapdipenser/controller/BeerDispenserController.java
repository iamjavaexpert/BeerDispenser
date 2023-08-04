package com.dispenser.beertapdipenser.controller;

import com.dispenser.beertapdipenser.dtos.ApiResponse;
import com.dispenser.beertapdipenser.dtos.DispenserRequest;
import com.dispenser.beertapdipenser.model.BeerDispenser;
import com.dispenser.beertapdipenser.service.BeerDispenserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class BeerDispenserController {

    private @Autowired BeerDispenserService beerDispenserService;
    private @Autowired MessageSource messageSource;

    @PostMapping("/addBeerDispenser")
    public ResponseEntity createDispenser(@RequestBody DispenserRequest dispenserRequest){
        try {
            ApiResponse apiResponse = ApiResponse.builder().data(beerDispenserService.createDispenser(dispenserRequest))
                    .description(
                            messageSource.getMessage("create.dispenser.success.description", new Object[]{}, null))
                    .status(messageSource.getMessage("success.status", new Object[]{}, null))
                    .title(messageSource.getMessage("create.dispenser.success.title", new Object[]{}, null)).build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Creation of dispenser failed : ", e);
            ApiResponse apiResponse = ApiResponse.builder().data(-1)
                    .description(messageSource.getMessage("create.dispenser.fail.description", new Object[]{}, null))
                    .status(messageSource.getMessage("fail.status", new Object[]{}, null))
                    .title(messageSource.getMessage("create.dispenser.fail.title", new Object[]{}, null)).build();
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateDispenserStatus")
    public ResponseEntity updateDispenserStatus(@RequestParam(name = "dispenserId",required = true) long dispenserId,
                                                @RequestParam(name = "status",required = true) BeerDispenser.DispenserStatus status){
        try {
            ApiResponse apiResponse = ApiResponse.builder().data(beerDispenserService.updateDispenserStatus(dispenserId,status))
                    .description(
                            messageSource.getMessage("update.dispenser.status.success.description", new Object[]{}, null))
                    .status(messageSource.getMessage("success.status", new Object[]{}, null))
                    .title(messageSource.getMessage("update.dispenser.status.success.title", new Object[]{}, null)).build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Update dispenser status failed : ", e);
            ApiResponse apiResponse = ApiResponse.builder().data(-1)
                    .description(messageSource.getMessage("update.dispenser.status.fail.description", new Object[]{}, null))
                    .status(messageSource.getMessage("fail.status", new Object[]{}, null))
                    .title(messageSource.getMessage("update.dispenser.status.fail.title", new Object[]{}, null)).build();
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateBeerDispenser/{dispenserId}")
    public ResponseEntity updateDispenser(@PathVariable(name = "dispenserId") long dispenserId,
                                          @RequestBody DispenserRequest dispenserRequest){
        try {
            ApiResponse apiResponse = ApiResponse.builder().data(beerDispenserService.updateDispenser(dispenserId,dispenserRequest))
                    .description(
                            messageSource.getMessage("update.dispenser.success.description", new Object[]{}, null))
                    .status(messageSource.getMessage("success.status", new Object[]{}, null))
                    .title(messageSource.getMessage("update.dispenser.success.title", new Object[]{}, null)).build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Updation of dispenser failed : ", e);
            ApiResponse apiResponse = ApiResponse.builder().data(-1)
                    .description(messageSource.getMessage("update.dispenser.fail.description", new Object[]{}, null))
                    .status(messageSource.getMessage("fail.status", new Object[]{}, null))
                    .title(messageSource.getMessage("update.dispenser.fail.title", new Object[]{}, null)).build();
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{dispenserId}")
    public ResponseEntity getDispenser(@PathVariable(name = "dispenserId") long dispenserId){
        try {
            ApiResponse apiResponse = ApiResponse.builder().data(beerDispenserService.getDispenser(dispenserId))
                    .description(
                            messageSource.getMessage("fetch.dispenser.success.description", new Object[]{}, null))
                    .status(messageSource.getMessage("success.status", new Object[]{}, null))
                    .title(messageSource.getMessage("fetch.dispenser.success.title", new Object[]{}, null)).build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get of dispenser failed : ", e);
            ApiResponse apiResponse = ApiResponse.builder().data(-1)
                    .description(messageSource.getMessage("fetch.dispenser.fail.description", new Object[]{}, null))
                    .status(messageSource.getMessage("fail.status", new Object[]{}, null))
                    .title(messageSource.getMessage("fetch.dispenser.fail.title", new Object[]{}, null)).build();
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
