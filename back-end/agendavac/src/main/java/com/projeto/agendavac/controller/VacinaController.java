package com.projeto.agendavac.controller;

import com.projeto.agendavac.entity.Vacina;
import com.projeto.agendavac.service.VacinaService;
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
@RequestMapping("/vacinas")
@Tag(name = "Vacinas", description = "Gerenciamento de Vacinas")
public class VacinaController {

    @Autowired
    private VacinaService vacinaService;

    @PostMapping
    @Operation(summary = "Cadastrar nova vacina", description = "Cadastra uma nova vacina no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vacina criada com sucesso"),
            @ApiResponse(responseCode = "409", description = "Vacina com o mesmo título já existe"),
            @ApiResponse(responseCode = "500", description = "Campo obrigatório vazio ou quantidade de caracteres superior ao permitido.")
    })
    public ResponseEntity<?> criar(@Valid @RequestBody Vacina vacina) {

        try {
            Vacina novaVacina = vacinaService.salvar(vacina);

            return ResponseEntity.status(HttpStatus.CREATED).body(novaVacina);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas as vacinas", description = "Retorna uma lista de todas as vacinas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vacinas retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhuma vacina cadastrada")
    })
    public ResponseEntity<?> listarTodas() {
        List<Vacina> vacinas = vacinaService.listarTodas();
        if (vacinas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nenhuma vacina cadastrada.");
        }
        return ResponseEntity.ok(vacinas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vacina por ID", description = "Retorna uma vacina pelo ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacina encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vacina com o ID informado não encontrada")
    })
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Vacina vacina = vacinaService.buscarPorId(id);
            return ResponseEntity.ok(vacina);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar vacina", description = "Atualiza os dados de uma vacina existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacina atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vacina com o ID informado não encontrada"),
            @ApiResponse(responseCode = "500", description = "Campo obrigatório vazio ou quantidade de caracteres superior ao permitido.")
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Vacina vacinaAtualizada) {

        try {
            Vacina vacina = vacinaService.atualizar(id, vacinaAtualizada);
            return ResponseEntity.ok(vacina);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar vacina", description = "Remove uma vacina pelo ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacina deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vacina com o ID informado não encontrada"),
            @ApiResponse(responseCode = "409", description = "Vacina não pode ser deletada devido a registros associados")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {

        try {
            vacinaService.deletar(id);
            return ResponseEntity.ok("Vacina com ID " + id + " deletada com sucesso.");

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
