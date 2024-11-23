package com.projeto.agendavac.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    @NotBlank(message = "O nome não pode estar vazio.")
    @Size(max = 60, message = "O nome deve ter no máximo 60 caracteres.")
    private String nome;

    @Column(nullable = false)
    @NotNull(message = "A data de nascimento é obrigatória.")
    private LocalDate dataNascimento;

    @Column(nullable = false, length = 1)
    @NotNull(message = "O sexo é obrigatório.")
    @Pattern(regexp = "[MF]", message = "O sexo deve ser 'M' (masculino) ou 'F' (feminino).")
    private String sexo;

    @Column(nullable = false, length = 60)
    @NotBlank(message = "O logradouro é obrigatório.")
    @Size(max = 60, message = "O logradouro deve ter no máximo 60 caracteres.")
    private String logradouro;

    @Column(nullable = false, length = 40)
    @NotBlank(message = "O setor é obrigatório.")
    @Size(max = 40, message = "O setor deve ter no máximo 40 caracteres.")
    private String setor;

    @Column(nullable = false, length = 40)
    @NotBlank(message = "A cidade é obrigatória.")
    @Size(max = 40, message = "A cidade deve ter no máximo 40 caracteres.")
    private String cidade;

    @Column(nullable = false, length = 2)
    @NotBlank(message = "A UF é obrigatória.")
    @Pattern(regexp = "^[A-Z]{2}$", message = "A UF deve ter exatamente 2 letras maiúsculas.")
    private String uf;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "usuario_alergia",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "alergia_id")
    )
    private List<Alergia> alergias;
}

