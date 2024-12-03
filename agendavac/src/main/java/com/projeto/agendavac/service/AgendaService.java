package com.projeto.agendavac.service;

import com.projeto.agendavac.entity.Agenda;
import com.projeto.agendavac.entity.Usuario;
import com.projeto.agendavac.entity.Vacina;
import com.projeto.agendavac.enums.Situacao;
import com.projeto.agendavac.exception.AlergiaException;
import com.projeto.agendavac.repository.AgendaRepository;
import com.projeto.agendavac.repository.UsuarioRepository;
import com.projeto.agendavac.repository.VacinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VacinaRepository vacinaRepository;

    public List<Agenda> agendar(Long usuarioId, Long vacinaId, LocalDate dataInicial, LocalTime hora, String observacoes) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário com ID " + usuarioId + " não encontrado."));

        Vacina vacina = vacinaRepository.findById(vacinaId)
                .orElseThrow(() -> new NoSuchElementException("Vacina com ID " + vacinaId + " não encontrada."));

        // se o usuário tiver alergia a algum compoente, não deixa realizar o agendamento
        boolean temAlergia = vacina.getComponentes().stream()
                .anyMatch(componente -> usuario.getAlergias().stream()
                        .anyMatch(alergia -> alergia.getNome().equalsIgnoreCase(componente.getNome())));

        if (temAlergia) {
            throw new AlergiaException("Usuário não pode ser agendado para esta vacina, pois possui alergia a um ou mais componentes.");
        }

        int doses = vacina.getDoses();

        Integer intervalo = vacina.getIntervalo();
        Integer periodicidade = vacina.getPeriodicidade();

        if (doses <= 0)
            throw new IllegalArgumentException("A vacina precisa ter pelo menos uma dose.");

        List<Agenda> agendas = new ArrayList<>();
        LocalDate data = dataInicial;

        for (int i = 0; i < doses; i++) {
            Agenda agenda = new Agenda();
            agenda.setUsuario(usuario);
            agenda.setVacina(vacina);
            agenda.setData(data);
            agenda.setHora(hora);
            agenda.setSituacao(Situacao.AGENDADO);
            agenda.setObservacoes(observacoes);

            // adicionar na lista de agendas.
            agendas.add(agenda);

            // para agendar as próximas horas
            if(intervalo != null && periodicidade != null && intervalo!= 0 && periodicidade != 0)
                data = calcularProximaData(data, intervalo, periodicidade);
        }

        return agendaRepository.saveAll(agendas);
    }

    public Agenda darBaixa(Long agendaId, Situacao novaSituacao) {

        if (novaSituacao != Situacao.REALIZADO && novaSituacao != Situacao.CANCELADO) {
            throw new IllegalArgumentException("Situação inválida para baixa.");
        }

        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new NoSuchElementException("Agenda com ID " + agendaId + " não encontrada."));

        agenda.setSituacao(novaSituacao);
        agenda.setDataSituacao(LocalDate.now());

        return agendaRepository.save(agenda);
    }

    public List<Agenda> buscarTodas() {

        List<Agenda> agendas = agendaRepository.findAll();

        if (agendas.isEmpty())
            throw new NoSuchElementException("Nenhuma agenda encontrada.");


        return agendas;
    }

    public List<Agenda> buscarPorSituacao(Situacao situacao) {

        List<Agenda> agendas = agendaRepository.findBySituacao(situacao);

        if (agendas.isEmpty())
            throw new NoSuchElementException("Nenhuma agenda encontrada com a situação: " + situacao.name());


        return agendas;
    }

    public Agenda buscarPorId(Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agenda com ID " + id + " não encontrada."));
    }


    public void excluir(Long agendaId) {
        if (!agendaRepository.existsById(agendaId)) {
            throw new NoSuchElementException("Agenda com ID " + agendaId + " não encontrada.");
        }

        agendaRepository.deleteById(agendaId);
    }

    public List<Agenda> listarPorUsuario(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário com ID " + usuarioId + " não encontrado."));

        return agendaRepository.findByUsuario(usuario);
    }

    private LocalDate calcularProximaData(LocalDate dataAtual, int intervalo, int periodicidade) {

        switch (periodicidade) {
            case 1: // dias
                return dataAtual.plusDays(intervalo);
            case 2: // semanas
                return dataAtual.plusWeeks(intervalo);
            case 3: // meses
                return dataAtual.plusMonths(intervalo);
            case 4: // anos
                return dataAtual.plusYears(intervalo);
            default:
                throw new IllegalArgumentException("Periodicidade inválida. Valores válidos: 1 (dias), 2 (semanas), 3 (meses), 4 (anos).");
        }

    }
}

