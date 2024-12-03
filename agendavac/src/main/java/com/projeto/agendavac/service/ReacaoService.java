package com.projeto.agendavac.service;

import com.projeto.agendavac.entity.Agenda;
import com.projeto.agendavac.entity.Reacao;
import com.projeto.agendavac.entity.Usuario;
import com.projeto.agendavac.enums.Situacao;
import com.projeto.agendavac.repository.AgendaRepository;
import com.projeto.agendavac.repository.ReacaoRepository;
import com.projeto.agendavac.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReacaoService {

    @Autowired
    private ReacaoRepository reacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    public Reacao incluirReacao(Long agendaId, String descricao, LocalDate dataReacao) {

        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new NoSuchElementException("Agenda com ID " + agendaId + " não encontrada."));

        if(agenda.getSituacao() != Situacao.REALIZADO)
            throw new IllegalArgumentException("Não é possível inserir reação em uma agenda não realizada!");

        Reacao reacao = new Reacao();
        reacao.setAgenda(agenda);
        reacao.setDescricao(descricao);
        reacao.setDataReacao(dataReacao);

        return reacaoRepository.save(reacao);
    }

    public List<Reacao> listarTodas() {

        List<Reacao> reacoes = reacaoRepository.findAll();

        if (reacoes.isEmpty()) {
            throw new NoSuchElementException("Nenhuma reação cadastrada.");
        }

        return reacoes;
    }

    public List<Reacao> buscarPorAgenda(Long agendaId) {
        List<Reacao> reacoes = reacaoRepository.findByAgendaId(agendaId);

        if (reacoes.isEmpty()) {
            throw new NoSuchElementException("Nenhuma reação encontrada para a agenda com ID " + agendaId + ".");
        }

        return reacoes;
    }

    public List<Reacao> buscarPorUsuario(Long usuarioId) {
        List<Reacao> reacoes = reacaoRepository.findByAgendaUsuarioId(usuarioId);

        if (reacoes.isEmpty()) {
            throw new NoSuchElementException("Nenhuma reação encontrada para o usuário com ID " + usuarioId);
        }

        return reacoes;
    }


    public void excluirReacao(Long id) {

        if (!reacaoRepository.existsById(id)) {
            throw new NoSuchElementException("Reação com ID " + id + " não encontrada.");
        }
        reacaoRepository.deleteById(id);
    }

}

