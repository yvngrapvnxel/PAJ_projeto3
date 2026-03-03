package pt.uc.dei.proj3.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Table(name="lead")
// named queries
public class LeadEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    @Column(name = "isAtivo", nullable = false)
    private boolean isAtivo;


    @Column(name = "titulo", nullable = false)
    private String titulo;


    @Column(name = "descricao", nullable = false)
    private String descricao;


    @Column(name = "estado", nullable = false)
    private int estado;


    @CreationTimestamp
    @Column(name = "dataCriacao", nullable = false, updatable = false)
    private LocalDate dataCriacao;


    @ManyToOne
    @JoinColumn(name = "userId", nullable = false) // FK
    private UserEntity user;




    // --- MÉTODOS


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
