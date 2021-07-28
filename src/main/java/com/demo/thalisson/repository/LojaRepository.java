package com.demo.thalisson.repository;

import com.demo.thalisson.domain.Loja;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Loja entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LojaRepository extends JpaRepository<Loja, Long> {}
