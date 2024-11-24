package com.projeto.agendavac.controller;

import com.projeto.agendavac.entity.Reacao;
import com.projeto.agendavac.service.ReacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/reacoes")
@Tag(name = "Reações", description = "Gerenciamento de Reações a Vacinas")
public class ReacaoController {

    @Autowired
    private ReacaoService reacaoService;

    @PostMapping
    @Operation(summary = "Registrar reação", description = "Registra uma reação a uma vacina de uma agenda de um usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reação registrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário ou agenda não encontrados."),
            @ApiResponse(responseCode = "400", description = "Não é possível inserir reação em uma agenda não realizada.")
    })
    public ResponseEntity<Reacao> incluirReacao(
            @RequestParam Long agendaId,
            @RequestParam String descricao,
            @RequestParam LocalDate dataReacao) {

        Reacao reacao = reacaoService.incluirReacao(agendaId, descricao, dataReacao);

        return ResponseEntity.status(HttpStatus.CREATED).body(reacao);
    }

    @GetMapping
    @Operation(summary = "Listar todas as reações", description = "Retorna uma lista de todas as reações cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reações listadas com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhuma reação encontrada.")
    })
    public ResponseEntity<List<Reacao>> listarTodas() {

        List<Reacao> reacoes = reacaoService.listarTodas();

        return ResponseEntity.ok(reacoes);
    }

    @GetMapping("/agenda/{agendaId}")
    @Operation(summary = "Listar reações por agenda", description = "Retorna uma lista de reações para uma agenda específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reações listadas com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhuma reação encontrada para a agenda.")
    })
    public ResponseEntity<List<Reacao>> buscarPorAgenda(@PathVariable Long agendaId) {

        List<Reacao> reacoes = reacaoService.buscarPorAgenda(agendaId);

        return ResponseEntity.ok(reacoes);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar reações por usuário", description = "Retorna todas as reações associadas a um usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reações encontradas com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário ou reações não encontradas.")
    })
    public ResponseEntity<List<Reacao>> buscarPorUsuario(@PathVariable Long usuarioId) {

        List<Reacao> reacoes = reacaoService.buscarPorUsuario(usuarioId);

        return ResponseEntity.ok(reacoes);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir reação", description = "Exclui uma reação cadastrada pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reação excluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Reação não encontrada.")
    })
    public ResponseEntity<String> excluirReacao(@PathVariable Long id) {

        try {
            reacaoService.excluirReacao(id);

            return ResponseEntity.ok("Reação com ID " + id + " excluída com sucesso.");

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}

