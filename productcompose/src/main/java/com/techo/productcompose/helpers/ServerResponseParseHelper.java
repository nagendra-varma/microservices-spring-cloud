package com.techo.productcompose.helpers;

import com.techo.productcompose.exceptions.ServerUnSuccessfulResponseException;
import org.springframework.http.ResponseEntity;

public class ServerResponseParseHelper {
    public static void ensureSuccessResponse(ResponseEntity responseEntity) {
        if (!isSuccess(responseEntity)) {
            throw new ServerUnSuccessfulResponseException(responseEntity.getStatusCode(),
                    responseEntity.getBody().toString());
        }
    }

    public static boolean isSuccess(ResponseEntity responseEntity) {
        return responseEntity.getStatusCode().value() >= 200
                && responseEntity.getStatusCode().value() <= 299;
    }
}
