package com.ing.fr.app.repositories;

import com.ing.fr.app.entities.Account;
import com.ing.fr.app.entities.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {

    @Query("SELECT tran FROM AccountTransaction tran  WHERE tran.account=(:account) order by tran.dateTime desc")
    List<AccountTransaction> getAccountHistory(@Param("account") Account account);

}
