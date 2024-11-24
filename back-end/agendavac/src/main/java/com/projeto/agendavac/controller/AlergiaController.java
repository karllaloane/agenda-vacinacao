package com.projeto.agendavac.controller;

import com.projeto.agendavac.entity.Alergia;
import com.projeto.agendavac.service.AlergiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/alergias")
@Tag(name = "Alergias", description = "Gerenciamento de alergias")
public class AlergiaController {

    @Autowired
    private AlergiaService alergiaService;

    @PostMapping
    @Operation(summary = "Cadastrar nova alergia", description = "Cadastra uma nova alergia no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alergia criada com sucesso"),
            @ApiResponse(responseCode = "409", description = "Alergia com o mesmo nome já cadastrada"),
            @ApiResponse(responseCode = "500", description = "O nome da alergia não pode estar vazio ou Alergia deve ter no máximo 40 caracteres.")
    })
    public ResponseEntity<?> criar(@RequestBody Alergia alergia) {
        try {
            Alergia novaAlergia = alergiaService.salvar(alergia);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaAlergia);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas as alergias", description = "Retorna uma lista de todas as alergias cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alergias retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhuma alergia cadastrada")
    })
    public ResponseEntity<?> listarTodas() {

        List<Alergia> alergias = alergiaService.listarTodas();

        if (alergias.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nenhuma alergia cadastrada.");
        }

        return ResponseEntity.ok(alergias);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alergia por ID", description = "Retorna uma alergia pelo ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alergia encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Alergia com o ID informado não encontrada")
    })
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        try {
            Alergia alergia = alergiaService.buscarPorId(id);
            return ResponseEntity.ok(alergia);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar alergia por nome", description = "Retorna uma alergia pelo nome informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alergia encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Alergia com o nome informado não encontrada")
    })
    public ResponseEntity<?> buscarPorNome(@PathVariable String nome) {
        try {
            Alergia alergia = alergiaService.buscarPorNome(nome);
            return ResponseEntity.ok(alergia);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar alergia", description = "Atualiza os dados de uma alergia existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alergia atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Alergia com o ID informado não encontrada"),
            @ApiResponse(responseCode = "409", description = "Alergia com o mesmo nome já cadastrada")
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Alergia alergiaAtualizada) {

        try {
            Alergia alergiaAtualizadaRetornada = alergiaService.atualizar(id, alergiaAtualizada);

            return ResponseEntity.ok(alergiaAtualizadaRetornada);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alergia com ID " + id + " não encontrada.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar alergia", description = "Remove uma alergia pelo ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alergia deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Alergia com o ID informado não encontrada"),
            @ApiResponse(responseCode = "409", description = "Alergia não pode ser deletada devido a registros associados")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {

        try {
            alergiaService.deletar(id);
            return ResponseEntity.ok("Alergia com ID " + id + " deletada com sucesso.");

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alergia com ID " + id + " não encontrada.");

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Não foi possível deletar a alergia com ID " + id + ": está associada a outros registros.");
        }
    }
}
