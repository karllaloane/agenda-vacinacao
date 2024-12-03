package com.projeto.agendavac.repository;

import com.projeto.agendavac.entity.Alergia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlergiaRepository extends JpaRepository<Alergia, Long> {

    // Para ignorar o case sensitive do banco
    Optional<Alergia> findByNomeIgnoreCase(String nome);
}
