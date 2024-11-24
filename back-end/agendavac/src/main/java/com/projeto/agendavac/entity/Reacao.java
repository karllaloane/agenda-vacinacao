package com.projeto.agendavac.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "reacoes")
public class Reacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "A descrição da reação não pode estar vazia.")
    @Size(max = 200, message = "A descrição da reação deve ter no máximo 200 caracteres.")
    private String descricao;

    @Column(nullable = false)
    @NotNull(message = "A data da reação é obrigatória.")
    private LocalDate dataReacao;

    @ManyToOne
    @JoinColumn(name = "agenda_id", nullable = false)
    private Agenda agenda;
}
