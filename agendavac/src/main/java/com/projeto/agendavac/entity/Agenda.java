package com.projeto.agendavac.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projeto.agendavac.enums.Situacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "agendas")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", format = "HH:mm", example = "17:15")
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Situacao situacao;

    @Column(name = "data_situacao")
    private LocalDate dataSituacao;

    @Column(length = 200)
    private String observacoes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vacina_id")
    private Vacina vacina;

    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL)
    private List<Reacao> reacoes;
}

