package com.projeto.agendavac.repository;

import com.projeto.agendavac.entity.Componente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComponenteRepository extends JpaRepository<Componente, Long> {

    Optional<Componente> findByNomeIgnoreCase(String nome);
}
