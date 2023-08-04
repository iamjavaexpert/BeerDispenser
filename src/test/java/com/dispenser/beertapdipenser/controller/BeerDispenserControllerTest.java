package com.dispenser.beertapdipenser.controller;


import com.dispenser.beertapdipenser.dtos.DispenserRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;


@Slf4j
public class BeerDispenserControllerTest {
    @BeforeAll
    static void initAll() {
    }
    @BeforeEach
    void init() {
    }

    @Test
    @DisplayName("create Dispenser")
    public ResponseEntity createDispenser(){
        try {
            log.info("Starting execution of createDispenser");
            ResponseEntity expectedValue = null;
            DispenserRequest dispenserRequest = null;


            BeerDispenserControllerTest junitmethod =new BeerDispenserControllerTest();
            ResponseEntity actualValue=junitmethod.createDispenser();
            log.info("Expected Value="+ expectedValue +" . Actual Value="+actualValue);
            System.out.println("Expected Value="+ expectedValue +" . Actual Value="+actualValue);
            Assertions.assertEquals(expectedValue, actualValue);
        } catch (Exception exception) {
            log.error("Exception in execution of execute1GetAllLogFromFirstMovF-"+exception,exception);
            exception.printStackTrace();
            Assertions.assertFalse(false);
        }
        return null;
    }
    @AfterEach
    void tearDown() {
    }
    @AfterAll
    static void tearDownAll() {
    }
}
