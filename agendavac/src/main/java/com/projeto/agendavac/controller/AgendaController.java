package com.projeto.agendavac.controller;

import com.projeto.agendavac.entity.Agenda;
import com.projeto.agendavac.enums.Situacao;
import com.projeto.agendavac.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/agendas")
@Tag(name = "Agendas", description = "Gerenciamento de agendas de vacinação")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @PostMapping
    @Operation(summary = "Agendar vacinação", description = "Cria agendas para uma vacinação considerando doses, periodicidade e intervalo da vacina.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agenda(s) criada(s) com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário ou vacina não encontrada."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos para a agenda.")
    })
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<Agenda>> agendar(
            @RequestParam Long usuarioId,
            @RequestParam Long vacinaId,
            @RequestParam LocalDate dataInicial,
            @RequestParam String horaString,
            @RequestParam(required = false) String observacoes) {

        // Conversão de String para LocalTime
        LocalTime hora;
        hora = LocalTime.parse(horaString);

        List<Agenda> agendas = agendaService.agendar(usuarioId, vacinaId, dataInicial, hora, observacoes);

        return ResponseEntity.ok(agendas);
    }

    @PutMapping("/{agendaId}/baixa")
    @Operation(summary = "Dar baixa em agenda", description = "Atualiza a situação de uma agenda para 'Realizado' ou 'Cancelado'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Situação da agenda atualizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agenda não encontrada."),
            @ApiResponse(responseCode = "400", description = "Situação inválida fornecida."),
            @ApiResponse(responseCode = "422", description = "Usuário possui alergia a componentes da vacina.")
    })
    @CrossOrigin(origins = "*")
    public ResponseEntity<Agenda> darBaixa(@PathVariable Long agendaId, @RequestParam Situacao situacao) {

        Agenda agendaAtualizada = agendaService.darBaixa(agendaId, situacao);

        return ResponseEntity.ok(agendaAtualizada);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar agendas por usuário", description = "Retorna todas as agendas associadas a um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendas retornadas com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário ou agendas não encontradas.")
    })
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<Agenda>> listarPorUsuario(@PathVariable Long usuarioId) {

        List<Agenda> agendas = agendaService.listarPorUsuario(usuarioId);

        return ResponseEntity.ok(agendas);
    }

    @GetMapping
    @Operation(summary = "Buscar todas as agendas", description = "Retorna todas as agendas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendas retornadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma agenda encontrada")
    })
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> buscarTodas() {

        try {
            List<Agenda> agendas = agendaService.buscarTodas();

            return ResponseEntity.ok(agendas);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/situacao/{situacao}")
    @Operation(summary = "Buscar agendas por situação", description = "Retorna agendas filtradas por situação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendas filtradas retornadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma agenda encontrada com a situação especificada")
    })
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> buscarPorSituacao(@PathVariable Situacao situacao) {

        try {

            List<Agenda> agendas = agendaService.buscarPorSituacao(situacao);

            return ResponseEntity.ok(agendas);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agenda por ID", description = "Retorna os detalhes de uma agenda específica pelo ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agenda encontrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agenda com o ID fornecido não encontrada.")
    })
    @CrossOrigin(origins = "*")
    public ResponseEntity<Agenda> buscarPorId(@PathVariable Long id) {
        try {
            Agenda agenda = agendaService.buscarPorId(id);
            return ResponseEntity.ok(agenda);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("/{agendaId}")
    @Operation(summary = "Excluir agenda", description = "Exclui uma agenda pelo ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agenda excluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agenda com o ID fornecido não encontrada.")
    })
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> excluirAgenda(@PathVariable Long agendaId) {
        try {
            agendaService.excluir(agendaId);
            return ResponseEntity.ok("Agenda com ID " + agendaId + " excluída com sucesso.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

