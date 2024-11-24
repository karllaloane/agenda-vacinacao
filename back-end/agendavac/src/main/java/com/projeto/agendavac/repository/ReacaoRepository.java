package com.projeto.agendavac.repository;

import com.projeto.agendavac.entity.Agenda;
import com.projeto.agendavac.entity.Reacao;
import com.projeto.agendavac.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReacaoRepository extends JpaRepository<Reacao, Long> {

    List<Reacao> findByAgendaId(Long agendaId);

    @Query("SELECT r FROM Reacao r WHERE r.agenda.usuario.id = :usuarioId")
    List<Reacao> findByAgendaUsuarioId(@Param("usuarioId") Long usuarioId);
}

