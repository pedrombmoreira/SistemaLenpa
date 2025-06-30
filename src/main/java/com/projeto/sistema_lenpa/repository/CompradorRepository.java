package com.projeto.sistema_lenpa.repository;

import com.projeto.sistema_lenpa.model.comprador.Comprador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompradorRepository extends JpaRepository<Comprador, Integer> {

    public Comprador findByCpf(String cpf);
}
