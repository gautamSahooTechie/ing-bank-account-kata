package com.ing.fr.app.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(uniqueConstraints = { @UniqueConstraint(name = "uni_customer_cif", columnNames = { "customerCif" }),
		                     @UniqueConstraint(name = "uni_customername_dob", columnNames = { "customerName","dateOfBirth" })})
public class Customer implements Serializable{

	private static final long serialVersionUID = 4752078406806523818L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	private String customerCif;

	@NotNull
	private String customerName;

	@NotNull
	private Date dateOfBirth;

	@OneToMany(fetch = FetchType.LAZY)
	private List<Account> accounts;

}
