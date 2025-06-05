package com.projeto.sistema_lenpa.model.comprador;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompradorRepository extends JpaRepository<Comprador, Integer> {

    public Comprador findByCpf(String cpf);
}
