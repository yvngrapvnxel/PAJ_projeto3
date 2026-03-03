package pt.uc.dei.proj3.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name="user")
// named queries
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // --- COLUNAS

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private Long id;


    @Column(name="primeiroNome", nullable = false)
    private String primeiroNome;


    @Column(name="ultimoNome", nullable = false)
    private String ultimoNome;


    @Column(name="email", nullable = false)
    private String email;


    @Column(name="telefone", nullable = false)
    private String telefone;


    @Column(name="username", nullable = false, updatable = false)
    private String username;


    @Column(name="password", nullable = false)
    private String password;


    @Column(name="fotoUrl", nullable = false)
    private String fotoUrl;


    @Column(name="isAdmin", nullable = false, updatable = false)
    private boolean isAdmin;


    @Column(name = "isAtivo", nullable = false)
    private boolean isAtivo;



    @OneToMany(mappedBy = "user")
    private List<TokenEntity> tokens;

    @OneToMany(mappedBy = "user")
    private List<ClienteEntity> clientes;

    @OneToMany(mappedBy = "user")
    private List<LeadEntity> leads;




    // --- MÉTODOS


    // getters

    public Long getId() {
        return id;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public boolean isAdmin() {
        return isAdmin;
    }


    // setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
