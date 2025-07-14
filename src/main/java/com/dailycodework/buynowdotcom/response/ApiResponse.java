package com.dailycodework.buynowdotcom.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//class to send response from API to front end
public class ApiResponse {

    private String message;
    private Object data;
}
