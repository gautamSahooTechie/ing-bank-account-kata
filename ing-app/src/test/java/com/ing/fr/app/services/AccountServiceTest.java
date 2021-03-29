package com.ing.fr.app.services;

import com.ing.fr.app.exceptions.EntityNotFoundException;
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

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Test
    void testFindAccountByIdWithException() {
        long accountId = 5;
        EntityNotFoundException entityNotFoundException =  assertThrows(EntityNotFoundException.class, () -> accountService.findAccountById(5));
        String expectedMessage = "Account not present in system with ID " + accountId;
        String actualMessage = entityNotFoundException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testFindAccountByAccountNumberWithException() {
        String accountNumber = "XyZ";
        EntityNotFoundException entityNotFoundException =  assertThrows(EntityNotFoundException.class, () -> accountService.findAccountByAccountNumber(accountNumber));
        String expectedMessage = "Account number not present in system " + accountNumber;
        String actualMessage = entityNotFoundException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCreateAccountUnderCustomerNotFound() {
        String customerCif = "cif002";
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("2121");
        accountDto.setBalance(8888.3);

        EntityNotFoundException entityNotFoundException =  assertThrows(EntityNotFoundException.class, () -> accountService.createAccountUnderCustomer(accountDto, customerCif));
        String expectedMessage = "Customer not present in system with customerID " + customerCif;
        String actualMessage = entityNotFoundException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(1)
    void testCreateAccountUnderCustomer() {

        // create Customer
        CustomerDto requestDto = new CustomerDto();
        requestDto.setCustomerCif("cif001");
        requestDto.setCustomerName("GKS");
        requestDto.setDateOfBirth(new Date());
        assertDoesNotThrow(() -> customerService.createCustomer(requestDto));

        //Create Account under customer
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("2121");
        accountDto.setBalance(100);
        assertDoesNotThrow( () -> accountService.createAccountUnderCustomer(accountDto, requestDto.getCustomerCif()));

        // verify account present in database
        AccountDto dbAccount = assertDoesNotThrow(() -> accountService.findAccountByAccountNumber(accountDto.getAccountNumber()));

        assertEquals(dbAccount.getAccountNumber(), accountDto.getAccountNumber());
        assertEquals(dbAccount.getBalance(), accountDto.getBalance());

    }


    @Test
    void getAccountBalanceWithException() {
        String accountNumber = "55";
        EntityNotFoundException entityNotFoundException =  assertThrows(EntityNotFoundException.class, () -> accountService.getAccountBalance(accountNumber));
        String expectedMessage = "Account number not present in system " + accountNumber;
        String actualMessage = entityNotFoundException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(2)
    void accountDeposit() {
        assertDoesNotThrow(() -> accountService.accountDeposit("2121", 50));
    }

    @Test
    @Order(3)
    void accountWithdraw() {
        assertDoesNotThrow(() -> accountService.accountWithdraw("2121", 10));
    }

    @Test
    @Order(4)
    void getAccountTransactions() {

        List<AccountTransactionDto> accountTransactionDtos =  assertDoesNotThrow(() -> accountService.getAccountTransactions("2121"));

        assertThat(accountTransactionDtos, not(IsEmptyCollection.empty()));
        assertThat(accountTransactionDtos, hasSize(2));
        assertTrue(accountTransactionDtos.stream().anyMatch( accountTransactionDto -> accountTransactionDto.getTransactionAmount() == 50));
        assertTrue(accountTransactionDtos.stream().anyMatch( accountTransactionDto -> accountTransactionDto.getTransactionAmount() == 10));

        double balance = assertDoesNotThrow(() -> accountService.getAccountBalance("2121"));

        assertEquals(balance, 140);
    }
}
