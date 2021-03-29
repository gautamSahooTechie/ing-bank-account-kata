package com.ing.fr.app.services;

import com.ing.fr.app.entities.Account;
import com.ing.fr.app.entities.AccountTransaction;
import com.ing.fr.app.entities.Customer;
import com.ing.fr.app.exceptions.EntityAlreadyPresentException;
import com.ing.fr.app.exceptions.EntityNotFoundException;
import com.ing.fr.app.exceptions.ServiceException;
import com.ing.fr.app.models.AccountDto;
import com.ing.fr.app.models.AccountTransactionDto;
import com.ing.fr.app.repositories.AccountRepository;
import com.ing.fr.app.repositories.AccountTransactionRepository;
import com.ing.fr.app.repositories.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 * @author Gautam Sahoo
 * @version 1.0
 * @apiNote AccountService is the class exposes functionality performing action create account , deposit , withdrawal and get transaction details
 */
@Service
public class AccountService {

    private static final Logger logger = LogManager.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * This method is useful to find Account details from the database which is searched by id
     *
     * @param id primary key of table
     * @return Account details information
     * @throws ServiceException EntityNotFoundException if account does not exists
     */

    public AccountDto findAccountById(long id) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter into method AccountService.findAccountById -> params {0}", id);
        }
        return accountRepository.findById(id).map(account -> modelMapper.map(account, AccountDto.class)).orElseThrow(() -> new EntityNotFoundException("Account not present in system with ID " + id));
    }

    /**
     * This method is useful to find Account details from the database which is searched by account number
     *
     * @param accountNumber account number for which information needs to be extracted
     * @return Account details information
     * @throws ServiceException
     */

    public AccountDto findAccountByAccountNumber(String accountNumber) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter into method AccountService.accountNumber -> params {0}", accountNumber);
        }
        return accountRepository.getAccountByNumber(accountNumber).map(account -> mapAccount(account)).orElseThrow(() -> new EntityNotFoundException("Account number not present in system " + accountNumber));
    }

    private AccountDto mapAccount(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setBalance(account.getBalance());
        return accountDto;
    }

    /**
     * This method is useful to create a new Account under the customer if account number does not exists . Account is create into the database
     *
     * @param accountDto  accepts front end objects to create create account informations
     * @param customerCif accepts customer cif
     * @throws ServiceException EntityNotFoundException if account not found and EntityAlreadyPresentException if account number already exists
     */

    public void createAccountUnderCustomer(AccountDto accountDto, String customerCif) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter into method AccountService.createAccountUnderCustomer -> params {0} {1}", accountDto, customerCif);
        }
        Customer customer = customerRepository.getCustomerByCIF(customerCif).orElseThrow(() -> new EntityNotFoundException("Customer not present in system with customerID " + customerCif));
        Account account = new Account();
        account.setBalance(accountDto.getBalance());
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setCurrency(Account.Currency.EUR);
        account.setCustomer(customer);

        // protect DB constraints
        if (accountRepository.getAccountByNumber(accountDto.getAccountNumber()).isPresent()) {
            throw new EntityAlreadyPresentException("Account Number already present in system with customerCif " + accountDto.getAccountNumber());
        }
        accountRepository.save(account);
        if (logger.isDebugEnabled()) {
            logger.debug("Exit from method AccountService.createAccountUnderCustomer -> params {0} {1}", accountDto, customerCif);
        }
    }

    /**
     * This method is useful to find Account transactions details from the database which is searched by account number
     *
     * @param accountNumber accepts account number to find the transactions
     * @return returns a list of all transaction in descending order
     * @throws ServiceException
     */

    public List<AccountTransactionDto> getAccountTransactions(String accountNumber) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter into method AccountService.getAccountTransactions -> params {0}", accountNumber);
        }
        Account account = accountRepository.getAccountByNumber(accountNumber).orElseThrow(() -> new EntityNotFoundException("Account number not present in system " + accountNumber));

        List<AccountTransaction> accountTransactions = accountTransactionRepository.getAccountHistory(account);
        return accountTransactions.stream()
                .map(t -> modelMapper.map(t, AccountTransactionDto.class))
                .collect(Collectors.toList());
    }

    /**
     * This method is useful to find Account balance from the database which is searched by account number
     *
     * @param accountNumber accepts account number for which balance can be extracted
     * @return balance of the account number specified
     * @throws ServiceException throws EntityNotFoundException if account not present in the system
     */

    public Double getAccountBalance(String accountNumber) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter into method AccountService.getAccountBalance -> params {0}", accountNumber);
        }
        Optional<Account> account = accountRepository.getAccountByNumber(accountNumber);
        return account.map(Account::getBalance).orElseThrow(() -> new EntityNotFoundException("Account number not present in system " + accountNumber));
    }

    /**
     * This method is useful to perform deposit action on the Account from the database. Account is searched by account number and deposit amount
     *
     * @param accountNumber accepts account number for which deposit action can be performed
     * @param depositAmount amount needs to be deposited to the account
     * @throws ServiceException throws EntityNotFoundException if account not present
     */

    public void accountDeposit(String accountNumber, double depositAmount) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter into method AccountService.accountDeposit -> params {0} {1}", accountNumber, depositAmount);
        }
        Account account = accountRepository.getAccountByNumber(accountNumber).orElseThrow(() -> new EntityNotFoundException("Account number not present in system " + accountNumber));
        account.setBalance(account.getBalance() + depositAmount);
        accountRepository.save(account);
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setAccount(account);
        accountTransaction.setTransactionAmount(depositAmount);
        accountTransaction.setDateTime(LocalDateTime.now());
        accountTransaction.setTransactionType(AccountTransaction.TransactionType.CREDIT);
        accountTransactionRepository.save(accountTransaction);
        if (logger.isDebugEnabled()) {
            logger.debug("Exit from method AccountService.accountDeposit -> params {0} {1}", accountNumber, depositAmount);
        }
    }

    /**
     * This method is useful to perform withdrawal action on the Account from the database. Account is searched by account number and deposit amount
     *
     * @param accountNumber  accepts account number for which deposit action can be performed
     * @param withdrawAmount amount needs to be deposited to the account
     * @throws ServiceException throws EntityNotFoundException if account not present
     */

    public void accountWithdraw(String accountNumber, double withdrawAmount) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter into method AccountService.accountWithdraw ->  params {0} {1}", accountNumber, withdrawAmount);
        }

        Account account = accountRepository.getAccountByNumber(accountNumber).orElseThrow(() -> new EntityNotFoundException("Account number not present in system " + accountNumber));
        account.setBalance(account.getBalance() - withdrawAmount);
        accountRepository.save(account);
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setAccount(account);
        accountTransaction.setTransactionAmount(withdrawAmount);
        accountTransaction.setDateTime(LocalDateTime.now());
        accountTransaction.setTransactionType(AccountTransaction.TransactionType.DEBIT);
        accountTransactionRepository.save(accountTransaction);
        if (logger.isDebugEnabled()) {
            logger.debug("Exit from method AccountService.accountWithdraw ->  params {0} {1}", accountNumber, withdrawAmount);
        }
    }
}
