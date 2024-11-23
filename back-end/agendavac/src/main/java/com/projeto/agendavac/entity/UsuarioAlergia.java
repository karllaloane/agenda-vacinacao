package com.projeto.agendavac.entity;

import jakarta.persistence.*;
import lombok.Data;

/*
* O relacionamento de usuário e alergias é mantido por uma tabela intermediária
* */

@Data
@Entity
@Table(name = "usuario_alergia")
public class UsuarioAlergia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "alergia_id", nullable = false)
    private Alergia alergia;
}

