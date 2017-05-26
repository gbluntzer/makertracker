package org.tenbitworks.repositories;


import org.springframework.data.repository.CrudRepository;
import org.tenbitworks.model.User;

public interface UserRepository extends CrudRepository<User, String> {

}
