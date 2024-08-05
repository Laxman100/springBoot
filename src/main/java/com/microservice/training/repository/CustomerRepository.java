package com.microservice.training.repository;


import com.microservice.training.bean.CustomerProfile;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerProfile, Integer> {

    CustomerProfile findByCustomerId(int customerId);
}
