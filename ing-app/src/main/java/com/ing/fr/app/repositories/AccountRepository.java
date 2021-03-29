package com.ing.fr.app.repositories;

import com.ing.fr.app.entities.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/***
 * @author Gautam Sahoo
 * @version 1.0
 * @apiNote AccountRepository is the class exposes functionality to maintain DB queries and CRUD operations
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

    /**
     * This method returns DB Account entity searched by account number
     * @param accountNumber
     * @return Account entity
     */
    @Query("SELECT acc FROM Account acc WHERE acc.accountNumber=(:accountNumber)")
    Optional<Account> getAccountByNumber(@Param("accountNumber") String accountNumber);
}
