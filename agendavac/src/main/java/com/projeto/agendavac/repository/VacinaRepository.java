package com.projeto.agendavac.repository;

import com.projeto.agendavac.entity.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VacinaRepository extends JpaRepository<Vacina, Long> {

    Optional<Vacina> findByTituloIgnoreCase(String titulo);
}
