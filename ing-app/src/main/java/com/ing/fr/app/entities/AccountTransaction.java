package com.ing.fr.app.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountTransaction implements Serializable{

	private static final long serialVersionUID = 2506803248968895792L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	private double transactionAmount;
	
	@NotNull
	private LocalDateTime dateTime; 
	
	@NotNull
	private TransactionType transactionType;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Account account;
	
	public enum TransactionType{
		DEBIT, CREDIT
	}
	
}
