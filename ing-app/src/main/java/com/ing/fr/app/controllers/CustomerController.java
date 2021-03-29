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

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    @GetMapping(value = "/{customerCif}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("customerCif") @NotEmpty String customerCif) throws ServiceException{
        CustomerDto customer = customerService.findCustomerByCustomerCif(customerCif);
        return ResponseEntity.ok(customer);
    }

    @PostMapping(value = "/createCustomer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCustomer(@Valid @RequestBody CustomerDto customerDto) throws ServiceException{
        customerService.createCustomer(customerDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "{customerCif}/{amount}/{accountNumber}/deposit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> depositAmount(@PathVariable("customerCif") @NotEmpty String customerCif,
                                              @PathVariable("amount") @Digits(integer = 14, fraction = 3) @Positive Double amount,
                                              @PathVariable("accountNumber") @NotEmpty String accountNumber) throws ServiceException {

        customerService.findCustomerByCustomerCif(customerCif);
        customerService.depositMoney(accountNumber, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "{customerCif}/{amount}/{accountNumber}/withdraw", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> withdrawAmount(@PathVariable("customerCif") @NotEmpty String customerCif,
                                               @PathVariable("amount") @Digits(integer = 14, fraction = 3) @Positive Double amount,
                                               @PathVariable("accountNumber") @NotEmpty String accountNumber) throws ServiceException {

        customerService.findCustomerByCustomerCif(customerCif);
        customerService.withdrawMoney(accountNumber, amount);
        return ResponseEntity.ok().build();
    }


}
