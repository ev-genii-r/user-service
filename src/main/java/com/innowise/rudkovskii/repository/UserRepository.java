package com.innowise.rudkovskii.repository;

import com.innowise.rudkovskii.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    void create(User user);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE users u SET u.name = :name, u.surname = :surname, " +
            "u.birthDate = :birthDate, u.email = :email " +
            "WHERE u.id = :id")
    User updateUser(@Param("id") Integer id,
                   @Param("name") String name,
                   @Param("surname") String surname,
                   @Param("birthDate") LocalDate birthDate,
                   @Param("email") String email);

    @Modifying
    @Query(value = "DELETE FROM User WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") int id);

    boolean existsByEmail(String email);
}
