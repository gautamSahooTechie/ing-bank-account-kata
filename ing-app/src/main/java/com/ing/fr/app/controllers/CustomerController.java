package com.ing.fr.app.controllers;

import com.ing.fr.app.exceptions.ServiceException;
import com.ing.fr.app.models.CustomerDto;
import com.ing.fr.app.services.AccountService;
import com.ing.fr.app.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

/***
 * @author Gautam Sahoo
 * @version 1.0
 * @apiNote CustomerController is the Controller class which exposes API endpoints and functionality around customer
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    /**
     * This controller method exposes /customer/{customerCif} URI which is useful to fetch customer details by customer cif number
     * @param customerCif Customer Cif number
     * @return Customer details
     * @throws ServiceException EntityNotFoundException if Customer cif not found
     */
    @GetMapping(value = "/{customerCif}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("customerCif") @NotEmpty String customerCif) throws ServiceException {
        CustomerDto customer = customerService.findCustomerByCustomerCif(customerCif);
        return ResponseEntity.ok(customer);
    }

    /**
     * This controller method exposes /customer/createCustomer URI which is useful to create a new customer in system
     * @param customerDto All mandatory customer information
     * @return return nothing
     * @throws ServiceException EntityAlreadyPresentException if Customer cif already found to avoid duplicates
     */
    @PostMapping(value = "/createCustomer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCustomer(@Valid @RequestBody CustomerDto customerDto) throws ServiceException {
        customerService.createCustomer(customerDto);
        return ResponseEntity.ok().build();
    }

    /**
     * This controller method exposes /customer/{customerCif}/{amount}/{accountNumber}/deposit URI which is useful to perform account deposit for a customer.
     * @param customerCif  customer unique identification number
     * @param amount  transaction amount
     * @param accountNumber account number to customer where transaction has to happen
     * @return
     * @throws ServiceException MinDepositAmountValidationException if amount deposited less than 0.01 Euros
     */
    @PostMapping(value = "{customerCif}/{amount}/{accountNumber}/deposit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> depositAmount(@PathVariable("customerCif") @NotEmpty String customerCif,
                                              @PathVariable("amount") @Digits(integer = 14, fraction = 3) @Positive Double amount,
                                              @PathVariable("accountNumber") @NotEmpty String accountNumber) throws ServiceException {

        customerService.findCustomerByCustomerCif(customerCif);
        customerService.depositMoney(accountNumber, amount);
        return ResponseEntity.ok().build();
    }

    /**
     * This controller method exposes /customer/{customerCif}/{amount}/{accountNumber}/withdraw URI which is useful to perform account withdrawal for a customer.
     * @param customerCif  customer unique identification number
     * @param amount  transaction amount
     * @param accountNumber account number to customer where transaction has to happen
     * @return
     * @throws ServiceException OverDraftFacilityValidationException if amount of withdrawal causes overdraft
     */
    @PostMapping(value = "{customerCif}/{amount}/{accountNumber}/withdraw", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> withdrawAmount(@PathVariable("customerCif") @NotEmpty String customerCif,
                                               @PathVariable("amount") @Digits(integer = 14, fraction = 3) @Positive Double amount,
                                               @PathVariable("accountNumber") @NotEmpty String accountNumber) throws ServiceException {

        customerService.findCustomerByCustomerCif(customerCif);
        customerService.withdrawMoney(accountNumber, amount);
        return ResponseEntity.ok().build();
    }

}
