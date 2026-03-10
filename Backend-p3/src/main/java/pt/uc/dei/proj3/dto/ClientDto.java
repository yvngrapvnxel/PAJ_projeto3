package pt.uc.dei.proj3.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClientDto {
    private long id;
    private String nome;
    private String email;
    private String telefone;
    private String empresa;
    private String dono;
    private boolean ativo;

    public ClientDto() {}

    // Getters e Setters
    @XmlElement
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @XmlElement
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    @XmlElement
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @XmlElement
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    @XmlElement
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    @XmlElement
    public String getDono() {
        return dono;
    }
    public void setDono(String dono) {
        this.dono = dono;
    }

    @XmlElement
    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}