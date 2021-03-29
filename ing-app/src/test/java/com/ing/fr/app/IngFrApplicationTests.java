package com.ing.fr.app;

import com.ing.fr.app.controllers.CustomerController;
import com.ing.fr.app.entities.AccountTransaction;
import com.ing.fr.app.models.AccountDto;
import com.ing.fr.app.models.AccountTransactionDto;
import com.ing.fr.app.models.CustomerDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngFrApplicationTests {

    @Autowired
    private CustomerController customerController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void contextLoads() {
        assertThat(customerController).isNotNull();
    }

    @Test
    @Order(2)
    public void verifyCustomerCreation() throws Exception {
        CustomerDto requestDto = new CustomerDto();
        requestDto.setCustomerCif("111");
        requestDto.setCustomerName("GKS");
        requestDto.setDateOfBirth(new Date());
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/customer/createCustomer", requestDto, Void.class));

        CustomerDto response = this.restTemplate.getForObject("http://localhost:" + port + "/customer/" + requestDto.getCustomerCif(), CustomerDto.class);
        assertThat(response.getCustomerCif().equals(requestDto.getCustomerCif()));
        assertThat(response.getCustomerName().equals(requestDto.getCustomerName()));
        assertThat(response.getDateOfBirth().equals(requestDto.getDateOfBirth()));
    }

    @Test
    @Order(3)
    public void verifyAccountCreation() throws Exception {

        CustomerDto responseCustomer = this.restTemplate.getForObject("http://localhost:" + port + "/customer/" + "111", CustomerDto.class);
        assertThat(responseCustomer.getCustomerCif().equals("111"));

        String POST_URL_Account = "http://localhost:" + port + "/account/" + responseCustomer.getCustomerCif() + "/createAccountUnderCustomer";
        AccountDto requestDto = new AccountDto();
        requestDto.setAccountNumber("XYZ");
        requestDto.setBalance(50);
        assertThat(this.restTemplate.postForObject(POST_URL_Account, requestDto, Void.class));

        String GET_URL_Account = "http://localhost:" + port + "/account/" + requestDto.getAccountNumber() + "/accountNumber";
        AccountDto responseAccount = this.restTemplate.getForObject(GET_URL_Account, AccountDto.class);
        assertThat(responseAccount.getAccountNumber().equals(requestDto.getAccountNumber()));
        assertThat(responseAccount.getBalance() == (requestDto.getBalance()));
    }

    @Test
    @Order(3)
    public void createAccountDeposit() throws Exception {

        CustomerDto responseCustomer = this.restTemplate.getForObject("http://localhost:" + port + "/customer/" + "111", CustomerDto.class);
        assertThat(responseCustomer.getCustomerCif().equals("111"));

        String GET_URL_Account = "http://localhost:" + port + "/account/" + "XYZ" + "/accountNumber";
        AccountDto responseAccount = this.restTemplate.getForObject(GET_URL_Account, AccountDto.class);
        assertThat(responseAccount.getAccountNumber().equals("XYZ"));

        double depositAmount = 30;
        String POST_URL_Deposit = "http://localhost:" + port + "/customer/" + responseCustomer.getCustomerCif() + "/" + depositAmount + "/" + responseAccount.getAccountNumber() + "/deposit";
        this.restTemplate.postForObject(POST_URL_Deposit, null, Void.class);

        String GET_URL_Account_Transactions = "http://localhost:" + port + "/account/" + responseAccount.getAccountNumber() + "/transactions";
        ResponseEntity<AccountTransactionDto[]> transactionDtos =
                restTemplate.getForEntity(
                        GET_URL_Account_Transactions,
                        AccountTransactionDto[].class);
        //AccountTransactionDto[] transactionDtos = this.restTemplate.getForObject(GET_URL_Account, AccountTransactionDto[].class);
        AccountTransactionDto[] accountTransactionDtos = transactionDtos.getBody();
        assertThat(accountTransactionDtos != null && accountTransactionDtos.length >= 1);
        for (AccountTransactionDto accountTransactionDto : accountTransactionDtos) {
            assertThat(accountTransactionDto.getTransactionAmount() == depositAmount);
            assertThat(accountTransactionDto.getTransactionType() == AccountTransaction.TransactionType.CREDIT);
        }
    }
}
