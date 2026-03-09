package pt.uc.dei.proj3.dto;


import jakarta.xml.bind.annotation.XmlElement;
import pt.uc.dei.proj3.entity.UserEntity;
import java.time.LocalDate;


public class LeadDto {

    private Long id;
    private String titulo;
    private String descricao;
    private int estado;
    private LocalDate dataCriacao;
    private UserEntity user;

    public LeadDto() {
    }

    public LeadDto(Long id, String titulo, String descricao, int estado, LocalDate dataCriacao, UserEntity user) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.estado = estado;
        this.dataCriacao = dataCriacao;
        this.user = user;
    }

    @XmlElement
    public Long getId() {
        return id;
    }

    public void setId(Long id) { // Added setter
        this.id = id;
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

    @XmlElement
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

}