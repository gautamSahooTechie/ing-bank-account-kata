package com.ing.fr.app.services;

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

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    private ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @Order(1)
    void testCreateCustomer() {

        // create Customer
        CustomerDto requestDto = new CustomerDto();
        requestDto.setCustomerCif("cif006");
        requestDto.setCustomerName("GKS");
        requestDto.setDateOfBirth(new Date());
        assertDoesNotThrow(() -> customerService.createCustomer(requestDto));

        // verify duplicate Customer is not allowed
        EntityAlreadyPresentException entityAlreadyPresentException = assertThrows(EntityAlreadyPresentException.class, () -> customerService.createCustomer(requestDto));
        String expectedMessage = "Customer already present in system with customerCif " + requestDto.getCustomerCif();
        String actualMessage = entityAlreadyPresentException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void findCustomerByCustomerCif() {
        CustomerDto customerDto = assertDoesNotThrow(() -> customerService.findCustomerByCustomerCif("cif006"));
        assertEquals("cif006", customerDto.getCustomerCif());
    }

    @Test
    @Order(2)
    void testCreateAccountUnderCustomer(){
        //Create Account under customer
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("3223");
        accountDto.setBalance(100);
        assertDoesNotThrow( () -> accountService.createAccountUnderCustomer(accountDto, "cif006"));
    }
    @Test
    void testDepositMoneyWithValidationException() {
        double minimumDepositAmount = context.getBean("minimumDepositAmount", Double.class);
        MinDepositAmountValidationException minDepositAmountValidationException = assertThrows(MinDepositAmountValidationException.class, () -> customerService.depositMoney("3223", 0.001));
        String expectedMessage = "As per bank rules customer should deposit more than " + minimumDepositAmount + " EUR to their bank accounts";
        String actualMessage = minDepositAmountValidationException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testWithdrawMoneyWithValidationException() {
        OverDraftFacilityValidationException overDraftFacilityValidationException = assertThrows(OverDraftFacilityValidationException.class, () -> customerService.withdrawMoney("3223", 1000));
        String expectedMessage = "As per bank rules overdraft is not allowed";
        String actualMessage = overDraftFacilityValidationException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDepositMoney() {
        //deposit money in acccount
        assertDoesNotThrow(() -> customerService.depositMoney("3223", 30));

        // verify transaction is registered
        List<AccountTransactionDto> accountTransactionDtos =  assertDoesNotThrow(() -> accountService.getAccountTransactions("3223"));
        assertThat(accountTransactionDtos, not(IsEmptyCollection.empty()));
        assertTrue(accountTransactionDtos.stream().anyMatch( accountTransactionDto -> accountTransactionDto.getTransactionAmount() == 30 && accountTransactionDto.getTransactionType() == AccountTransaction.TransactionType.CREDIT));
    }

    @Test
    void testWithdrawMoney() {
        //deposit money in acccount
        assertDoesNotThrow(() -> customerService.withdrawMoney("3223", 20));

        // verify transaction is registered
        List<AccountTransactionDto> accountTransactionDtos =  assertDoesNotThrow(() -> accountService.getAccountTransactions("3223"));
        assertThat(accountTransactionDtos, not(IsEmptyCollection.empty()));
        assertTrue(accountTransactionDtos.stream().anyMatch( accountTransactionDto -> accountTransactionDto.getTransactionAmount() == 20 && accountTransactionDto.getTransactionType() == AccountTransaction.TransactionType.DEBIT));

    }

}
