package com.ing.fr.app.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ing.fr.app.entities.AccountTransaction.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountTransactionDto implements Serializable {

	private static final long serialVersionUID = -1832973080479696704L;

	public AccountTransactionDto() {}

	private double transactionAmount;

	private LocalDateTime dateTime;

	private TransactionType transactionType;

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

}
