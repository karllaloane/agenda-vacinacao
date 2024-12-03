package com.projeto.agendavac.repository;

import com.projeto.agendavac.entity.Agenda;
import com.projeto.agendavac.entity.Usuario;
import com.projeto.agendavac.enums.Situacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    List<Agenda> findByUsuario(Usuario usuario);

    List<Agenda> findAll();

    // filtrar agendas por situação
    List<Agenda> findBySituacao(Situacao situacao);
}

