package com.ing.fr.app.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCustomerDtoValidations() {

        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerCif("ss");
        customerDto.setCustomerName("23232ff");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1980);
        calendar.set(Calendar.MONTH, 2);
        calendar.set(Calendar.DAY_OF_MONTH, 20);

        customerDto.setDateOfBirth(new Date(calendar.getTimeInMillis()));
        Set<ConstraintViolation<CustomerDto>> violations = validator.validate(customerDto);
        assert (violations.isEmpty());
    }

    @Test
    public void testCustomerDtoValidationsWithErrors() {

        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerCif("s.s");
        customerDto.setCustomerName("23232ff");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1980);
        calendar.set(Calendar.MONTH, 2);
        calendar.set(Calendar.DAY_OF_MONTH, 20);
        Set<ConstraintViolation<CustomerDto>> violations = validator.validate(customerDto);

        assertTrue(violations.stream().anyMatch(customerDtoConstraintViolation -> customerDtoConstraintViolation.getPropertyPath().toString().equals("customerCif")));

        customerDto.setCustomerCif("23232ff43784374837287432832343xczxzds2");
        violations = validator.validate(customerDto);

        assertTrue(violations.stream().anyMatch(customerDtoConstraintViolation -> customerDtoConstraintViolation.getPropertyPath().toString().equals("customerCif")));

        customerDto.setCustomerName("23232ff437843748372874328323432");
        violations = validator.validate(customerDto);

        assertTrue(violations.stream().anyMatch(customerDtoConstraintViolation -> customerDtoConstraintViolation.getPropertyPath().toString().equals("customerName")));

        customerDto.setCustomerName("2323@2ff");

        violations = validator.validate(customerDto);
        assertTrue(violations.stream().anyMatch(customerDtoConstraintViolation -> customerDtoConstraintViolation.getPropertyPath().toString().equals("customerName")));

    }
}
