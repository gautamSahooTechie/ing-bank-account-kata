package com.ing.fr.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ing.fr.app.entities.Account;
import com.ing.fr.app.entities.AccountTransaction;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long>{

	@Query("SELECT tran FROM AccountTransaction tran  WHERE tran.account=(:account) order by tran.dateTime desc")
	public List<AccountTransaction> getAccountHistory(@Param("account") Account account);

}
