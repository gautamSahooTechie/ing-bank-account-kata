package com.ing.fr.app.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testAccountDtoValidations() {
        AccountDto accountDto = new AccountDto();
        accountDto.setBalance(100);
        accountDto.setAccountNumber("sdsds");
        Set<ConstraintViolation<AccountDto>> violations = validator.validate(accountDto);
        assert (violations.isEmpty());
    }

    @Test
    public void testAccountDtoValidationsWithErrors() {

        AccountDto accountDto = new AccountDto();
        accountDto.setBalance(100);
        accountDto.setAccountNumber("sds.ds");
        Set<ConstraintViolation<AccountDto>> violations = validator.validate(accountDto);

        assertTrue(violations.stream().anyMatch(customerDtoConstraintViolation -> customerDtoConstraintViolation.getPropertyPath().toString().equals("accountNumber")));

    }
}
