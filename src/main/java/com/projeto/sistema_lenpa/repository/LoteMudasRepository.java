package com.projeto.sistema_lenpa.repository;

import com.projeto.sistema_lenpa.model.lote_mudas.LoteMudas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoteMudasRepository extends JpaRepository<LoteMudas, Integer> {
}
