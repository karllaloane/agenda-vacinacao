package com.projeto.agendavac.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "componentes") // Nome da tabela no banco de dados
public class Componente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    @NotBlank(message = "O nome do componente não pode estar vazio.")
    @Size(max = 40, message = "O nome do componente deve ter no máximo 40 caracteres.")
    private String nome;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
