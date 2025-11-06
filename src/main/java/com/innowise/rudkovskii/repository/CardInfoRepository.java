package com.innowise.rudkovskii.repository;

import com.innowise.rudkovskii.entity.CardInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Integer> {

    Page<CardInfo> findAll(Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM card_info WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") int id);

    boolean existsByNumber(String number);

    Optional<CardInfo> findByNumber(String number);
}
