package com.example.Polinom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PolinomRepository extends CrudRepository<Polinom, Long> {

    Optional<Polinom> findById(Long id);
}
