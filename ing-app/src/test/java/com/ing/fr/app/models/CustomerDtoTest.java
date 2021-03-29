package com.ing.fr.app.models;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Calendar;
import java.util.Set;
import com.ing.fr.app.config.AppConfig;
import com.ing.fr.app.entities.AccountTransaction;
import com.ing.fr.app.exceptions.EntityAlreadyPresentException;
import com.ing.fr.app.exceptions.EntityNotFoundException;
import com.ing.fr.app.exceptions.MinDepositAmountValidationException;
import com.ing.fr.app.exceptions.OverDraftFacilityValidationException;
import com.ing.fr.app.models.AccountDto;
import com.ing.fr.app.models.AccountTransactionDto;
import com.ing.fr.app.models.CustomerDto;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;

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
        assert(violations.isEmpty());
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
