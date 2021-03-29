package com.ing.fr.app.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(uniqueConstraints = { @UniqueConstraint(name = "uni_acc", columnNames = { "accountNumber" }) })
public class Account implements Serializable{


	private static final long serialVersionUID = 3624882877278539618L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	private double balance;

	@NotNull
	private String accountNumber;

	@NotNull
    private Currency currency;

	@NotNull
	@ManyToOne
	private Customer customer;

	@OneToMany(fetch = FetchType.LAZY)
	private List<AccountTransaction> transactions;

	public enum Currency{
		EUR
	}
}
