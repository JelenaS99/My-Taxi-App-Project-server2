package com.taxiapp.repository;


import com.taxiapp.model.entity.RoleEnum;
import com.taxiapp.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("""
                select u.username
                from User u
                where not exists (
                    select r from u.roles r where r.name = 'ROLE_ADMIN'
                )
            """)
    List<String> findAllNonAdminUsernames();


}
