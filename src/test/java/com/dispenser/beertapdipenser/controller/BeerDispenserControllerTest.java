package com.dispenser.beertapdipenser.controller;



import com.dispenser.beertapdipenser.dtos.DispenserRequest;
import com.dispenser.beertapdipenser.dtos.DispenserUsageResponse;
import com.dispenser.beertapdipenser.model.BeerDispenser;
import com.dispenser.beertapdipenser.service.BeerDispenserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@ExtendWith(SpringExtension.class)
@WebMvcTest(BeerDispenserController.class)
class BeerDispenserControllerTest {



    @Autowired
    private MockMvc mockMvc;



    @MockBean
    private BeerDispenserService mockBeerDispenserService;
    @MockBean
    private MessageSource mockMessageSource;



    // Test case to check if a new dispenser can be created successfully.
    @Test
    void testCreateDispenser() throws Exception {
        // Prepare the input request for creating a dispenser
        final DispenserRequest dispenserRequest = new DispenserRequest();
        dispenserRequest.setTotalVolume(2000.0);
        dispenserRequest.setFlowVolume(10.0);
        dispenserRequest.setPricePerML(5.0f);



        // Set up the mock service to return a dispenser ID (0L) when the createDispenser method is called.
        when(mockBeerDispenserService.createDispenser(dispenserRequest)).thenReturn(0L);



        // Convert the request object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(dispenserRequest);



        // Perform the POST request to add a new dispenser and capture the response
        final MockHttpServletResponse response = mockMvc.perform(post("/addBeerDispenser")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();



        // Verify that the response status code is 200 (OK) indicating success.
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }



    // Test case to check if dispenser status can be updated successfully.
    @Test
    void testUpdateDispenserStatus() throws Exception {
        // Prepare the expected response for updating the dispenser status
        final DispenserUsageResponse dispenserUsageResponse = new DispenserUsageResponse();
        dispenserUsageResponse.setAmount(0.0);
        dispenserUsageResponse.setVolume(0.0);



        // Set up the mock service to return the expected response when updateDispenserStatus is called.
        when(mockBeerDispenserService.updateDispenserStatus(0L, BeerDispenser.DispenserStatus.OPEN))
                .thenReturn(dispenserUsageResponse);



        // Perform the PUT request to update the dispenser status and capture the response
        final MockHttpServletResponse response = mockMvc.perform(put("/updateDispenserStatus")
                        .param("dispenserId", "0")
                        .param("status", "OPEN")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();



        // Verify that the response status code is 200 (OK) indicating success.
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }



    // Test case to check if dispenser details can be updated successfully.
    @Test
    void testUpdateDispenser() throws Exception {
        // Prepare the input request for updating dispenser details
        final DispenserRequest dispenserRequest = new DispenserRequest();
        dispenserRequest.setTotalVolume(0.0);
        dispenserRequest.setFlowVolume(0.0);
        dispenserRequest.setPricePerML(0.0f);



        // Set up the mock service to return a dispenser ID (0L) when the updateDispenser method is called.
        when(mockBeerDispenserService.updateDispenser(0L, dispenserRequest)).thenReturn(0L);



        // Convert the request object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(dispenserRequest);



        // Perform the PUT request to update a dispenser and capture the response
        final MockHttpServletResponse response = mockMvc.perform(put("/updateBeerDispenser/{dispenserId}", 0)
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();



        // Verify that the response status code is 200 (OK) indicating success.
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }



    // Test case to check if dispenser details can be retrieved successfully.
    @Test
    void testGetDispenser() throws Exception {
        // Prepare the expected dispenser object
        final BeerDispenser beerDispenser = new BeerDispenser();
        beerDispenser.setId(0L);
        beerDispenser.setTotalVolume(0.0);
        beerDispenser.setLeftVolume(0.0);
        beerDispenser.setFlowVolume(0.0);
        beerDispenser.setTotalSpend(0.0);



        // Set up the mock service to return the expected dispenser object when getDispenser is called with ID 0L.
        when(mockBeerDispenserService.getDispenser(0L)).thenReturn(beerDispenser);



        // Perform the GET request to retrieve a dispenser by ID and capture the response
        final MockHttpServletResponse response = mockMvc.perform(get("/getDispenser/{dispenserId}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();



        // Verify that the response status code is 200 (OK) indicating success.
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }



    @Test
    void testGetData() throws Exception {
        final DispenserUsageResponse dispenserUsageResponse = new DispenserUsageResponse();
        dispenserUsageResponse.setAmount(0.0);
        dispenserUsageResponse.setVolume(0.0);



        when(mockBeerDispenserService.getData()).thenReturn(dispenserUsageResponse);



        // Perform the GET request to retrieve a dispenser by ID and capture the response
        final MockHttpServletResponse response = mockMvc.perform(get("/getLiveData")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();



        // Verify that the response status code is 200 (OK) indicating success.
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}