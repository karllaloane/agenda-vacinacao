package com.projeto.agendavac.service;

import com.projeto.agendavac.entity.Vacina;
import com.projeto.agendavac.repository.VacinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class VacinaService {

    @Autowired
    private VacinaRepository vacinaRepository;

    public Vacina salvar(Vacina vacina) {

        if (vacinaRepository.findByTituloIgnoreCase(vacina.getTitulo()).isPresent()) {
            throw new IllegalArgumentException("Vacina com o título '" + vacina.getTitulo() + "' já existe.");
        }

        validarVacina(vacina);

        // após a vacina ter sido validada, adicionamos null nestes campos para garantir a consistência do banco
        // (caso venha 0, por exemplo)
        if (vacina.getDoses() == 1) {
            vacina.setPeriodicidade(null);
            vacina.setIntervalo(null);
        }

        return vacinaRepository.save(vacina);
    }

    public List<Vacina> listarTodas() {
        return vacinaRepository.findAll();
    }

    public Vacina buscarPorId(Long id) {

        return vacinaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Vacina com ID " + id + " não encontrada."));
    }

    public Vacina atualizar(Long id, Vacina vacinaAtualizada) {

        Vacina vacina = vacinaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Vacina com ID " + id + " não encontrada."));

        // Verifica se o título já existe em outra vacina
        Optional<Vacina> vac = vacinaRepository.findByTituloIgnoreCase(vacinaAtualizada.getTitulo());
        if (vac.isPresent() && vac.get().getId() != id) {
            throw new IllegalArgumentException("Vacina com o título '" + vacinaAtualizada.getTitulo() + "' já existe.");
        }

        validarVacina(vacina);

        // após a vacina ter sido validada, adicionamos null nestes campos para garantir a consistência do banco
        // (caso venha 0, por exemplo)
        if (vacina.getDoses() == 1) {
            vacina.setPeriodicidade(null);
            vacina.setIntervalo(null);
        }

        vacina.setTitulo(vacinaAtualizada.getTitulo());
        vacina.setDescricao(vacinaAtualizada.getDescricao());
        vacina.setDoses(vacinaAtualizada.getDoses());
        vacina.setPeriodicidade(vacinaAtualizada.getPeriodicidade());
        vacina.setIntervalo(vacinaAtualizada.getIntervalo());
        vacina.setComponentes(vacinaAtualizada.getComponentes());

        return vacinaRepository.save(vacina);
    }

    public void deletar(Long id) {

        if (!vacinaRepository.existsById(id)) {
            throw new NoSuchElementException("Vacina com ID " + id + " não encontrada.");
        }

        try {
            vacinaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível deletar a vacina com ID " + id + " porque está associada a outros registros.", e);
        }
    }

    private void validarVacina(Vacina vacina) {

        if (vacina.getDoses() == 1) {

            // se dose for 1, periodicidade e intervalo não fazem sentido
            if ((vacina.getPeriodicidade() != null && vacina.getPeriodicidade() > 0)
                    || (vacina.getIntervalo() != null && vacina.getIntervalo() > 0) ) {
                throw new IllegalArgumentException("Para vacinas com apenas 1 dose, periodicidade e intervalo não devem ser informadas.");
            }

        } else if (vacina.getDoses() > 1) {

            // para dose > 1, periodicidade e intervalo devem ser válidos
            if (vacina.getPeriodicidade() < 1 || vacina.getPeriodicidade() > 4) {
                throw new IllegalArgumentException("Periodicidade deve ser informada: 1 - dias, 2 - semanas, 3 - meses e 4 - anos.");
            }

            if (vacina.getIntervalo() < 1) {
                throw new IllegalArgumentException("O intervalo entre as doses deve ser maior ou igual a 1.");
            }

        } else {
            throw new IllegalArgumentException("O número de doses deve ser no mínimo 1.");
        }

    }
}

