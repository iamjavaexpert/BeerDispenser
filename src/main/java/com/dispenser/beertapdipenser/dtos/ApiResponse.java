package com.dispenser.beertapdipenser.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private Object data;
    private String status;
    private String description;
    private String title;
}
