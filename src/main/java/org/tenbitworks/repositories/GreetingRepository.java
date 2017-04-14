package org.tenbitworks.repositories;


import org.springframework.data.repository.CrudRepository;
import org.tenbitworks.model.Greeting;


public interface GreetingRepository extends CrudRepository<Greeting, Long> {

}
