package com.ing.fr.app.repositories;

import com.ing.fr.app.entities.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query("SELECT acc FROM Account acc WHERE acc.accountNumber=(:accountNumber)")
    Optional<Account> getAccountByNumber(@Param("accountNumber") String accountNumber);
}
