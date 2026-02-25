package pt.uc.dei.proj3.pojo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;


@XmlRootElement
public class UserPojo {

    private int id;
    private String primeiroNome;
    private String ultimoNome;
    private String email;
    private String telefone;
    private String username;
    private String password;
    private String fotoUrl; // Requisito: URL da imagem [cite: 57]

    private List <LeadPojo> meusLeads;
    private List<ClientPojo> meusClientes;
    private List<Object> projectsList;

    public UserPojo(){
        this.meusLeads = new ArrayList<>();
        this.meusClientes = new ArrayList<>();
        this.projectsList = new ArrayList<>();
    }

    public UserPojo(int id,String primeiroNome, String ultimoNome, String email, String telefone, String username, String password, String fotoUrl) {
        this();
        this.id = id;
        this.primeiroNome = primeiroNome;
        this.ultimoNome = ultimoNome;
        this.email = email;
        this.telefone = telefone;
        this.username = username;
        this.password = password;
        this.fotoUrl = fotoUrl;
    }

    @XmlElement
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    @XmlElement
    public String getUltimoNome() {
        return ultimoNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    @XmlElement
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @XmlElement
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement
    public String getPassword() {
        return password;
    }

    public void setPassword(String senha) {
        this.password = senha;
    }

    @XmlElement
    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    @XmlElement
    public List<LeadPojo> getMeusLeads() {
        return meusLeads;
    }

    public void setMeusLeads(List<LeadPojo> meusLeads) {
        this.meusLeads = meusLeads;
    }

    @XmlElement
    public List<ClientPojo> getMeusClientes() {
        return meusClientes;
    }

    public void setMeusClientes(List<ClientPojo> meusClientes) {
        this.meusClientes = meusClientes;
    }
}
