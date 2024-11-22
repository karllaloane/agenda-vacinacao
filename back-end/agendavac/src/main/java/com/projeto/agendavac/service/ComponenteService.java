package com.projeto.agendavac.service;

import com.projeto.agendavac.entity.Componente;
import com.projeto.agendavac.repository.ComponenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ComponenteService {

    @Autowired
    private ComponenteRepository componenteRepository;

    public Componente salvar(Componente componente) {

        // Verificar duplicidade ignorando case
        if (componenteRepository.findByNomeIgnoreCase(componente.getNome()).isPresent()) {
            throw new IllegalArgumentException("Componente com o nome '" + componente.getNome() + "' já existe.");
        }
        return componenteRepository.save(componente);
    }

    public List<Componente> listarTodos() {
        return componenteRepository.findAll();
    }

    public Componente buscarPorId(Long id) {

        return componenteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Componente com ID " + id + " não encontrado."));
    }

    public Componente atualizar(Long id, Componente componenteAtualizado) {

        Componente componente = componenteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Componente com ID " + id + " não encontrado."));

        // se já existir um componente com o nome informado, não irá atualizar
        if (componenteRepository.findByNomeIgnoreCase(componenteAtualizado.getNome()).isPresent()) {
            throw new IllegalArgumentException("Alergia com o nome '" + componenteAtualizado.getNome() + "' já existe.");
        }

        componente.setNome(componenteAtualizado.getNome());

        return componenteRepository.save(componente);
    }

    public void deletar(Long id) {
        if (!componenteRepository.existsById(id)) {
            throw new NoSuchElementException("Componente com ID " + id + " não encontrado.");
        }

        try {
            componenteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível deletar o componente com ID " + id + " porque está associado a outros registros.", e);
        }
    }
}
