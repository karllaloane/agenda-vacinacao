package com.projeto.agendavac.service;

import com.projeto.agendavac.entity.Alergia;
import com.projeto.agendavac.repository.AlergiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AlergiaService {

    @Autowired
    private AlergiaRepository alergiaRepository;

    public Alergia salvar(Alergia alergia) {

        // se já existir uma alergia com aquele nome, não irá salvar
        if (alergiaRepository.findByNomeIgnoreCase(alergia.getNome()).isPresent()) {
            throw new IllegalArgumentException("Alergia com o nome '" + alergia.getNome() + "' já existe.");
        }

        return alergiaRepository.save(alergia);
    }

    public List<Alergia> listarTodas() {
        return alergiaRepository.findAll();
    }

    public Alergia buscarPorId(Long id) {

        Optional<Alergia> alergiaOptional = alergiaRepository.findById(id);

        if(alergiaOptional.isEmpty())
            throw new NoSuchElementException("Alergia com ID " + id + " não encontrada.");

        return alergiaOptional.get();
    }

    public Alergia buscarPorNome(String nome) {

        Optional<Alergia> alergiaOptional = alergiaRepository.findByNomeIgnoreCase(nome);

        if(alergiaOptional.isEmpty())
            throw new NoSuchElementException("Alergia com o nome '" + nome + "' não encontrada.");

        return alergiaOptional.get();
    }

    public Alergia atualizar(Long id, Alergia alergiaAtualizada) {
        Alergia alergia = alergiaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Alergia com ID " + id + " não encontrada."));

        // se já existir uma alergia com o nome informado, não irá atualizar
        if (alergiaRepository.findByNomeIgnoreCase(alergiaAtualizada.getNome()).isPresent()) {
            throw new IllegalArgumentException("Alergia com o nome '" + alergiaAtualizada.getNome() + "' já existe.");
        }

        alergia.setNome(alergiaAtualizada.getNome());

        return alergiaRepository.save(alergia);
    }


    public void deletar(Long id) {
        if (!alergiaRepository.existsById(id)) {
            throw new NoSuchElementException("Alergia com ID " + id + " não encontrada.");
        }

        try {
            alergiaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível deletar a alergia com ID " + id + " porque está associada a outros registros.", e);
        }
    }

}
