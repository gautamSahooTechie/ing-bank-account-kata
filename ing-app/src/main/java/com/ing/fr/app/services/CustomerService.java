package com.ing.fr.app.services;

import com.ing.fr.app.config.AppConfig;
import com.ing.fr.app.entities.Customer;
import com.ing.fr.app.exceptions.*;
import com.ing.fr.app.models.CustomerDto;
import com.ing.fr.app.repositories.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/***
 * @author Gautam Sahoo
 * @version 1.0
 * @apiNote CustomerService is the class exposes functionality performing action create customer  and deposit , withdrawal actions from Customer
 */
@Service
public class CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountService accountService;

    private final ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * This method is useful in creating new customer in database with customer information
     *
     * @param customerDto Customer details information need to be onboarded in the system
     * @throws ServiceException EntityAlreadyPresentException thrown if customer already present
     */

    public void createCustomer(CustomerDto customerDto) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter into method CustomerService.createCustomer -> params {0}", customerDto);
        }
        Customer customer = new Customer();
        customer.setCustomerCif(customerDto.getCustomerCif());
        customer.setCustomerName(customerDto.getCustomerName());
        customer.setDateOfBirth(customerDto.getDateOfBirth());

        // protect DB constraints
        if (customerRepository.getCustomerByCIF(customerDto.getCustomerCif()).isPresent()) {
            throw new EntityAlreadyPresentException("Customer already present in system with customerCif " + customerDto.getCustomerCif());
        }

        customerRepository.save(customer);
        if (logger.isDebugEnabled()) {
            logger.debug("Exit from method CustomerService.createCustomer -> params {0}", customerDto);
        }
    }

    /**
     * Finds the Customer by database primary key ID which uniquely identifies the customer record
     *
     * @param id Database primary key
     * @return Customer details information
     * @throws ServiceException throws EntityNotFoundException if customer not found in system
     */

    public CustomerDto findCustomerById(long id) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter into method CustomerService.findCustomerById -> params {0}", id);
        }
        return customerRepository.findById(id).map(customer -> modelMapper.map(customer, CustomerDto.class)).orElseThrow(() -> new EntityNotFoundException("Customer not present in system with customerID " + id));
    }

    /**
     * Finds the Customer by Customer Cif number which uniquely identifies the customer record , Customer also has this information
     *
     * @param customerCif Customer cif number
     * @return Customer details information
     * @throws ServiceException throws EntityNotFoundException if customer not found in system
     */

    public CustomerDto findCustomerByCustomerCif(@NotNull String customerCif) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter into method CustomerService.findCustomerByCustomerCif -> params {0}", customerCif);
        }
        return customerRepository.getCustomerByCIF(customerCif).map(customer -> modelMapper.map(customer, CustomerDto.class)).orElseThrow(() -> new EntityNotFoundException("Customer not present in system with customerCif " + customerCif));
    }

    /**
     * This method is useful to perform deposit action on the Account from the database. Account is searched by account number and deposit amount.
     * Please note only amount above 0.01 EURO can be deposited as per bank rules
     *
     * @param accountNumber accepts account number for which deposit action can be performed
     * @param depositAmount amount needs to be deposited to the account
     * @throws ServiceException throws EntityNotFoundException if account not present
     */

    public void depositMoney(@NotNull String accountNumber, double depositAmount) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter into method CustomerService.depositMoney -> params {0} , {1} ", accountNumber, depositAmount);
        }
        double minimumDepositAmount = context.getBean("minimumDepositAmount", Double.class);
        // Applying bank conditions
        if (minimumDepositAmount > depositAmount) {
            String errorMessage = "As per bank rules customer should deposit more than " + minimumDepositAmount + " EUR to their bank accounts";
            logger.error(errorMessage);
            throw new MinDepositAmountValidationException(errorMessage);
        }
        accountService.accountDeposit(accountNumber, depositAmount);
        if (logger.isDebugEnabled()) {
            logger.debug("Exit from method CustomerService.depositMoney -> params {0} , {1} ", accountNumber, depositAmount);
        }
    }

    /**
     * This method is useful to perform withdrawal action on the Account from the database. Account is searched by account number and deposit amount
     * Please note as per bank rules overdraft is not allowed in withdrawal action
     *
     * @param accountNumber  accepts account number for which deposit action can be performed
     * @param withdrawAmount amount needs to be deposited to the account
     * @throws ServiceException throws EntityNotFoundException if account not present
     */

    public void withdrawMoney(@NotNull String accountNumber, double withdrawAmount) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter into method CustomerService.withdrawMoney -> params {0} , {1} ", accountNumber, withdrawAmount);
        }
        long startTime = System.currentTimeMillis();
        boolean overDraftAllowed = context.getBean("overDraftAllowed", Boolean.class);
        Double accountBalance = accountService.getAccountBalance(accountNumber);
        // Applying bank conditions
        if (!overDraftAllowed && ((accountBalance == 0) || (accountBalance - withdrawAmount <= 0))) {
            String errorMessage = "As per bank rules overdraft is not allowed";
            logger.error(errorMessage);
            throw new OverDraftFacilityValidationException(errorMessage);
        }
        accountService.accountWithdraw(accountNumber, withdrawAmount);
        if (logger.isDebugEnabled()) {
            logger.debug("Exit from method CustomerService.withdrawMoney -> params {0} , {1} ", accountNumber, withdrawAmount);
        }
    }
}
