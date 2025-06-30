package com.projeto.sistema_lenpa.repository;

import com.projeto.sistema_lenpa.model.entrega.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
}
