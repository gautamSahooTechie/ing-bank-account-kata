package com.ing.fr.app.repositories;

import com.ing.fr.app.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/***
 * @author Gautam Sahoo
 * @version 1.0
 * @apiNote CustomerRepository is the class exposes functionality to maintain DB queries and CRUD operations with Customer Entity
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    /**
     * This method returns Customer details searched by  Cif number stored in database
     * @param customerCif Unique customer cif number
     * @return Customer entity stored in Database
     */
    @Query("SELECT cust FROM Customer cust  WHERE cust.customerCif=(:customerCif)")
    Optional<Customer> getCustomerByCIF(@Param("customerCif") String customerCif);

}
