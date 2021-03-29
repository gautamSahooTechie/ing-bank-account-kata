package com.ing.fr.app.repositories;

import com.ing.fr.app.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query("SELECT cust FROM Customer cust  WHERE cust.customerCif=(:customerCif)")
    Optional<Customer> getCustomerByCIF(@Param("customerCif") String customerCif);

}
