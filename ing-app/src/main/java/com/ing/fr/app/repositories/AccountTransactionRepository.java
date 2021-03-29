package com.ing.fr.app.repositories;

import com.ing.fr.app.entities.Account;
import com.ing.fr.app.entities.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/***
 * @author Gautam Sahoo
 * @version 1.0
 * @apiNote AccountTransactionRepository is the class exposes functionality to maintain DB queries and CRUD operations with AccountTransaction Entity
 */
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {

    /**
     * This method returns list of account transactions happened on account ordered by time descending order
     * @param account
     * @return
     */
    @Query("SELECT tran FROM AccountTransaction tran  WHERE tran.account=(:account) order by tran.dateTime desc")
    List<AccountTransaction> getAccountHistory(@Param("account") Account account);

}
