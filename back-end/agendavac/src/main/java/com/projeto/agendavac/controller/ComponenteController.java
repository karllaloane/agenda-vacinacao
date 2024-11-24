package com.projeto.agendavac.controller;

import com.projeto.agendavac.entity.Componente;
import com.projeto.agendavac.service.ComponenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/componentes")
@Tag(name = "Componentes", description = "Gerenciamento de componentes")
public class ComponenteController {

    @Autowired
    private ComponenteService componenteService;

    @PostMapping
    @Operation(summary = "Cadastrar novo componente", description = "Cadastra um novo componente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Componente criado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Componente com o mesmo nome já existe / Integridade de dados"),
            @ApiResponse(responseCode = "500", description = "O nome da alergia não pode estar vazio ou Alergia deve ter no máximo 40 caracteres.")
    })
    public ResponseEntity<?> criar(@Valid @RequestBody Componente componente) {

        try {
            Componente novoComponente = componenteService.salvar(componente);

            return ResponseEntity.status(HttpStatus.CREATED).body(novoComponente);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar todos os componentes", description = "Retorna uma lista de todos os componentes cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de componentes retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum componente cadastrado")
    })
    public ResponseEntity<?> listarTodos() {

        List<Componente> componentes = componenteService.listarTodos();

        if (componentes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nenhum componente cadastrado.");
        }

        return ResponseEntity.ok(componentes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar componente por ID", description = "Retorna um componente pelo ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Componente encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Componente com o ID informado não encontrado")
    })
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        try {
            Componente componente = componenteService.buscarPorId(id);

            return ResponseEntity.ok(componente);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar componente", description = "Atualiza os dados de um componente existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Componente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Componente com o ID informado não encontrado")
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Componente componenteAtualizado) {
        try {
            Componente componente = componenteService.atualizar(id, componenteAtualizado);
            return ResponseEntity.ok(componente);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar componente", description = "Remove um componente pelo ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Componente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Componente com o ID informado não encontrado"),
            @ApiResponse(responseCode = "409", description = "Componente não pode ser deletado devido a registros associados")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            componenteService.deletar(id);
            return ResponseEntity.ok("Componente com ID " + id + " deletado com sucesso.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}

