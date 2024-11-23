package com.projeto.agendavac.controller;

import com.projeto.agendavac.entity.Usuario;
import com.projeto.agendavac.entity.UsuarioAlergia;
import com.projeto.agendavac.service.UsuarioService;
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
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de Usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Cadastrar novo usuário", description = "Cadastra um novo usuário no sistema, com ou sem alergias associadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos para o cadastro")
    })
    public ResponseEntity<Usuario> salvar(@Valid @RequestBody Usuario usuario) {

        Usuario usuarioNovo = usuarioService.salvar(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNovo);
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado")
    })
    public ResponseEntity<?> listarTodos() {

        try {
            List<Usuario> usuarios = usuarioService.listarTodos();

            return ResponseEntity.ok(usuarios);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário pelo ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário com o ID fornecido não encontrado")
    })
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        try {
            Usuario usuario = usuarioService.buscarPorId(id);

            return ResponseEntity.ok(usuario);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar usuários por nome", description = "Retorna uma lista de usuários cujo nome contenha a sequência fornecida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado com o nome fornecido")
    })
    public ResponseEntity<?> buscarPorNome(@PathVariable String nome) {

        try {
            List<Usuario> usuarios = usuarioService.buscarPorNome(nome);

            return ResponseEntity.ok(usuarios);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente, incluindo associações de alergias. Necessário ir todas as alergias do usuário no Json, mesmo se for alterar. Para excluir, Json em vazio [].")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário com o ID fornecido não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos para atualização")
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuarioAtualizado) {

        try {
            Usuario usuario = usuarioService.atualizar(id, usuarioAtualizado);

            return ResponseEntity.ok(usuario);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema pelo ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário com o ID fornecido não encontrado")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {

        try {

            usuarioService.deletar(id);

            return ResponseEntity.ok("Usuário com ID " + id + " deletado com sucesso.");

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
