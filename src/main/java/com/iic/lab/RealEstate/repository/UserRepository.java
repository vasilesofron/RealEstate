package com.iic.lab.RealEstate.repository;

import com.iic.lab.RealEstate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query to find the user via his email.
    User findByEmail(String email);

    // Custom query to find all the Real Estate Listings associated to a user.
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.listings WHERE u.id = :userId")
    Optional<User> findByIdWithRealEstateListing(@Param("userId") Long userId);

}
