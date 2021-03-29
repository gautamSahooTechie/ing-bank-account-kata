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

@Service
public class CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountService accountService;

    private ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    private ModelMapper modelMapper = new ModelMapper();

    public void createCustomer(CustomerDto customerDto) throws ServiceException{
        if(logger.isDebugEnabled()){
            logger.debug("Enter into method CustomerService.createCustomer -> params" + customerDto );
        }
        Customer customer = new Customer();
        customer.setCustomerCif(customerDto.getCustomerCif());
        customer.setCustomerName(customerDto.getCustomerName());
        customer.setDateOfBirth(customerDto.getDateOfBirth());

        // protect DB constraints
        if(customerRepository.getCustomerByCIF(customerDto.getCustomerCif()).isPresent()){
            throw new EntityAlreadyPresentException("Customer already present in system with customerCif " + customerDto.getCustomerCif());
        }

        customerRepository.save(customer);
        if(logger.isDebugEnabled()){
            logger.debug("Exit from method CustomerService.createCustomer -> params" + customerDto );
        }
    }

    public CustomerDto findCustomerById(long id) throws ServiceException{
        if(logger.isDebugEnabled()){
            logger.debug("Enter into method CustomerService.findCustomerById -> params" + id );
        }
        return customerRepository.findById(id).map(customer -> modelMapper.map(customer, CustomerDto.class)).orElseThrow(() -> new EntityNotFoundException("Customer not present in system with customerID " + id));
    }

    public CustomerDto findCustomerByCustomerCif(@NotNull String customerCif) throws ServiceException{
        if(logger.isDebugEnabled()){
            logger.debug("Enter into method CustomerService.findCustomerByCustomerCif -> params" + customerCif );
        }
        return customerRepository.getCustomerByCIF(customerCif).map(customer -> modelMapper.map(customer, CustomerDto.class)).orElseThrow(() -> new EntityNotFoundException("Customer not present in system with customerCif " + customerCif));
    }

    public void depositMoney(@NotNull String accountNumber, double depositAmount) throws ServiceException {
        if(logger.isDebugEnabled()){

        }
        double minimumDepositAmount = context.getBean("minimumDepositAmount", Double.class);
        // Applying bank conditions
        if (minimumDepositAmount > depositAmount) {
            String errorMessage = "As per bank rules customer should deposit more than "+ minimumDepositAmount + " EUR to their bank accounts";
            logger.error(errorMessage);
            throw new MinDepositAmountValidationException(errorMessage);
        }
        accountService.accountDeposit(accountNumber, depositAmount);
        if(logger.isDebugEnabled()){

        }
    }

    public void withdrawMoney(@NotNull String accountNumber, double withdrawAmount) throws ServiceException{
        if(logger.isDebugEnabled()){

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
        if(logger.isDebugEnabled()){

        }
        logger.debug("CustomerService for withdrawMoney took time in millis " + (System.currentTimeMillis() - startTime));
    }
}