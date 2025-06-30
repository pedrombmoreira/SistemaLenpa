package com.projeto.sistema_lenpa.repository;

import com.projeto.sistema_lenpa.model.planta.Planta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantaRepository extends JpaRepository<Planta, Integer> {
    @Query("SELECT p FROM Planta p WHERE " +
            "(p.foto_url IS NOT NULL AND p.foto_url <> '') AND " +
            "(:termo is null OR :termo = '' OR p.nome_popular ILIKE %:termo% OR p.nome_cientifico ILIKE %:termo%) AND " +
            "(:tipo is null OR :tipo = '' OR p.tipo = :tipo)")
    List<Planta> search(@Param("termo") String termo, @Param("tipo") String tipo);
}
