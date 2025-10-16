package com.projeto.sistema_lenpa.repository;

import com.projeto.sistema_lenpa.model.entrega.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
    @Query("SELECT e FROM Entrega e WHERE " +
            "e.dataEntrega >= COALESCE(:dataInicio, CAST('1900-01-01' AS date)) AND " +
            "e.dataEntrega <= COALESCE(:dataFim, CAST('2999-12-31' AS date))")
    List<Entrega> findByFilter(@Param("dataInicio") LocalDate dataInicio,
                               @Param("dataFim") LocalDate dataFim);

    boolean existsByCompradorId(Integer compradorId);
    boolean existsByPlantaId(Integer plantaId);
    boolean existsByUsuarioId(Integer usuarioId);
}
