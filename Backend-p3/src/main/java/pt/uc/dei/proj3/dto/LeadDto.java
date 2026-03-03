package pt.uc.dei.proj3.dto;

import jakarta.xml.bind.annotation.XmlElement;

import java.time.LocalDate;

public class LeadDto {

    private int id;
    private String titulo;
    private String descricao;
    private int estado;
    private LocalDate dataCriacao;

    public LeadDto() {
    }

    public LeadDto(String titulo, String descricao, int estado) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.estado = estado;
    }

    @XmlElement
    public int getId() {
        return id;
    }


    @XmlElement
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @XmlElement
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlElement
    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @XmlElement
    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

}
