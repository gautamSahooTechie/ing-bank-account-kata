package com.ing.fr.app.models;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
public class AccountDto implements Serializable{

	private static final long serialVersionUID = 919836451836282574L;

	public AccountDto() {}

	@Digits(integer = 14, fraction = 2)
	@Positive
	private double balance;

	@Pattern(regexp = "^[A-Za-z0-9]*$")
	@NotNull
	private String accountNumber;

	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

}
