package com.ing.fr.app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/***
 * @author Gautam Sahoo
 * @version 1.0
 * @apiNote Account is the entity class maintains all account details in Database
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(uniqueConstraints = {@UniqueConstraint(name = "uni_acc", columnNames = {"accountNumber"})})
public class Account implements Serializable {


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

    public enum Currency {
        EUR
    }
}
