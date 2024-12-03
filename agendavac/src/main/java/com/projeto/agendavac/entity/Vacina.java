package com.projeto.agendavac.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name = "vacinas")
public class Vacina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    @NotBlank(message = "O título da vacina não pode estar vazio.")
    @Size(max = 60, message = "O título da vacina deve ter no máximo 60 caracteres.")
    private String titulo;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "A descrição da vacina não pode estar vazia.")
    @Size(max = 200, message = "A descrição da vacina deve ter no máximo 200 caracteres.")
    private String descricao;

    @Column(nullable = false)
    @Min(value = 1, message = "A quantidade de doses deve ser no mínimo 1.")
    private int doses;

    // Tanto a peridicidade quando o intervalo podem ser nulos, pois caso o número de doses seja igual a 1
    // não faz sentido que exista periodicidade ou intervalo entre doses
    @Column(nullable = true)
    @Min(value = 0, message = "A periodicidade não pode ser um valor negativo.")
    @Max(value = 4, message = "A periodicidade deve ser no máximo 4 (anos).")
    private Integer periodicidade;

    // Pode ser nulo quando dose = 1
    @Column(nullable = true)
    @Min(value = 0, message = "O intervalo entre doses não pode ser negativo.")
    private Integer intervalo;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "vacina_componente",
            joinColumns = @JoinColumn(name = "vacina_id"),
            inverseJoinColumns = @JoinColumn(name = "componente_id")
    )
    private List<Componente> componentes;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDoses() {
        return doses;
    }

    public void setDoses(int doses) {
        this.doses = doses;
    }

    public Integer getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(Integer periodicidade) {
        this.periodicidade = periodicidade;
    }

    public Integer getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(Integer intervalo) {
        this.intervalo = intervalo;
    }

    public List<Componente> getComponentes() {
        return componentes;
    }

    public void setComponentes(List<Componente> componentes) {
        this.componentes = componentes;
    }
}
