package com.ing.fr.app.exceptions.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/***
 * @author Gautam Sahoo
 * @version 1.0
 * @apiNote ServiceError is the class returned to UI if in case there is conditional error
 */
@Data
public class ServiceError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String errorCode;

    private ServiceError() {
        timestamp = LocalDateTime.now();
    }

    public ServiceError(HttpStatus status) {
        this();
        this.status = status;
    }

}
