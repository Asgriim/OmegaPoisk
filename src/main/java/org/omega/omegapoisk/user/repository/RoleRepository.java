package org.omega.omegapoisk.user.repository;

import org.omega.omegapoisk.user.entity.RoleEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    @Query("SELECT * from role_type where name = :name")
    Optional<RoleEntity> findByName(@Param("name") String name);

}
