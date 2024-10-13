package org.omega.omegapoisk.repository.user;


import org.omega.omegapoisk.entity.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
