package com.ing.fr.app.controllers;

import com.ing.fr.app.exceptions.ServiceException;
import com.ing.fr.app.models.AccountDto;
import com.ing.fr.app.models.AccountTransactionDto;
import com.ing.fr.app.services.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/***
 * @author Gautam Sahoo
 * @version 1.0
 * @apiNote AccountController is the Controller class which exposes API endpoints and functionality around customer accounts
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    private static final Logger logger = LogManager.getLogger(AccountController.class);
    @Autowired
    private AccountService accountService;

    /**
     * This controller method exposes /account/{id} URI which is useful to fetch account details by database primary key
     * @param id Databse primary key
     * @return Account Details
     * @throws ServiceException EntityNotFoundException if account does not exists
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> getAccountByID(@PathVariable("id") Long id) throws ServiceException {
        long startTime = System.currentTimeMillis();
        AccountDto account = accountService.findAccountById(id);
        logger.info("AccountController.getAccountByID method took time in millis " + (System.currentTimeMillis() - startTime));
        return ResponseEntity.ok(account);
    }

    /**
     * This controller method exposes /account/{accountNumber}/accountNumber URI which is useful to fetch account details by account number
     * @param accountNumber account number of customer
     * @return Account Details
     * @throws ServiceException EntityNotFoundException if account does not exists
     */
    @GetMapping(value = "/{accountNumber}/accountNumber", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> getAccountByAccountNumber(@PathVariable("accountNumber") @NotEmpty String accountNumber) throws ServiceException {
        long startTime = System.currentTimeMillis();
        AccountDto account = accountService.findAccountByAccountNumber(accountNumber);
        logger.info("AccountController.getAccountByAccountNumber method took time in millis " + (System.currentTimeMillis() - startTime));
        return ResponseEntity.ok(account);
    }

    /**
     * This controller method exposes /account/{accountNumber}/accountBalance URI which is useful to fetch account balance by account number
     * @param accountNumber account number of customer
     * @return Account Details
     * @throws ServiceException EntityNotFoundException if account does not exists
     */
    @GetMapping(value = "/{accountNumber}/accountBalance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getAccountBalanceByAccountNumber(@PathVariable("accountNumber") @NotEmpty String accountNumber) throws ServiceException {
        long startTime = System.currentTimeMillis();
        Double balance = accountService.getAccountBalance(accountNumber);
        logger.info("AccountController.getAccountBalanceByAccountNumber method took time in millis " + (System.currentTimeMillis() - startTime));
        return ResponseEntity.ok(balance);
    }

    /**
     * This controller method exposes /account/{accountNumber}/transactions URI which is useful to fetch account transaction entries (Credits, Debits) by account number, order by time latest first
     * @param accountNumber account number of customer
     * @return Account Details
     * @throws ServiceException EntityNotFoundException if account does not exists
     */
    @GetMapping(value = "/{accountNumber}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountTransactionDto[]> getAccountTransactions(@PathVariable("accountNumber") @NotEmpty String accountNumber) throws ServiceException {
        long startTime = System.currentTimeMillis();
        List<AccountTransactionDto> accountTransactionDtos = accountService.getAccountTransactions(accountNumber);
        logger.info("AccountController.getAccountTransactions method took time in millis " + (System.currentTimeMillis() - startTime));
        return ResponseEntity.ok(accountTransactionDtos.stream().toArray(AccountTransactionDto[]::new));
    }

    /***
     * This controller method exposes /account/{customerCif}/createAccountUnderCustomer URI which is useful to create a account number under a customer
     * @param accountDto All mandatory account informations
     * @param customerCif Customer cif information under which account is opened
     * @return
     * @throws ServiceException  EntityNotFoundException if customer is not found
     */
    @PostMapping(value = "{customerCif}/createAccountUnderCustomer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createAccountUnderCustomer(@Valid @RequestBody AccountDto accountDto, @PathVariable("customerCif") @NotEmpty String customerCif) throws ServiceException {
        long startTime = System.currentTimeMillis();
        accountService.createAccountUnderCustomer(accountDto, customerCif);
        logger.info("AccountController.createAccountUnderCustomer method took time in millis " + (System.currentTimeMillis() - startTime));
        return ResponseEntity.ok().build();
    }
}
