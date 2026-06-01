package com.ayush.library.repository;

import com.ayush.library.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Admin> findByEmailAndActiveTrue(String email);

    boolean existsByIdAndActiveTrue(Long id);
}
