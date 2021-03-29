package com.ing.fr.app.exceptions.common;

import com.ing.fr.app.exceptions.EntityAlreadyPresentException;
import com.ing.fr.app.exceptions.EntityNotFoundException;
import com.ing.fr.app.exceptions.MinDepositAmountValidationException;
import com.ing.fr.app.exceptions.OverDraftFacilityValidationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ExceptionWrapper extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MinDepositAmountValidationException.class)
    protected ResponseEntity<Object> handleMinimumDepositAmountConditionBreached(
            MinDepositAmountValidationException ex) {
        ServiceError serviceError = new ServiceError(HttpStatus.BAD_REQUEST);
        serviceError.setMessage(ex.getMessage());
        serviceError.setErrorCode(ErrorCodes.MINIMUM_DEPOSIT_AMOUNT_VALIDATION_FAILED);
        return new ResponseEntity<>(serviceError, serviceError.getStatus());
    }

    @ExceptionHandler(OverDraftFacilityValidationException.class)
    protected ResponseEntity<Object> handleOverDraftFacilityExceeded(
            OverDraftFacilityValidationException ex) {
        ServiceError serviceError = new ServiceError(HttpStatus.BAD_REQUEST);
        serviceError.setMessage(ex.getMessage());
        serviceError.setErrorCode(ErrorCodes.OVERDRAFT_FACILITY_VALIDATION_FAILED);
        return new ResponseEntity<>(serviceError, serviceError.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleOverDraftFacilityExceeded(
            EntityNotFoundException ex) {
        ServiceError serviceError = new ServiceError(HttpStatus.BAD_REQUEST);
        serviceError.setMessage(ex.getMessage());
        serviceError.setErrorCode(ErrorCodes.ENTITY_NOT_FOUND);
        return new ResponseEntity<>(serviceError, serviceError.getStatus());
    }

    @ExceptionHandler(EntityAlreadyPresentException.class)
    protected ResponseEntity<Object> handleOverDraftFacilityExceeded(
            EntityAlreadyPresentException ex) {
        ServiceError serviceError = new ServiceError(HttpStatus.BAD_REQUEST);
        serviceError.setMessage(ex.getMessage());
        serviceError.setErrorCode(ErrorCodes.ENTITY_NOT_FOUND);
        return new ResponseEntity<>(serviceError, serviceError.getStatus());
    }
}
