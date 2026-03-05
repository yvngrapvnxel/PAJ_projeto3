package pt.uc.dei.proj3.dao;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pt.uc.dei.proj3.dto.ClientDto;
import pt.uc.dei.proj3.entity.ClienteEntity;
import pt.uc.dei.proj3.entity.UserEntity;

import java.io.Serializable;
import java.util.List;

@Stateless
public class ClienteDao extends DefaultDao<ClienteEntity> implements Serializable {


    public ClienteDao() {
        super(ClienteEntity.class);
    }

    public void guardaCliente(ClientDto newClient,UserEntity u){

        ClienteEntity finalClient = new ClienteEntity();

        finalClient.setNome(newClient.getNome());
        finalClient.setEmail(newClient.getEmail());
        finalClient.setTelefone(newClient.getTelefone());
        finalClient.setEmpresa(newClient.getEmpresa());
        finalClient.setUser(u);

        persist(finalClient);
    }

    public void atualizaCliente(ClienteEntity clienteAtual, ClientDto dtoNovo) {

        // Atualiza a entidade existente com os dados que vieram do DTO
        clienteAtual.setNome(dtoNovo.getNome());
        clienteAtual.setEmail(dtoNovo.getEmail());
        clienteAtual.setTelefone(dtoNovo.getTelefone());
        clienteAtual.setEmpresa(dtoNovo.getEmpresa());

        // Usa o 'merge' para guardar as alterações na base de dados
        merge(clienteAtual);
    }

    // Procura um cliente pelo ID
    public ClienteEntity findClienteById(Long id) {
        return em.find(ClienteEntity.class, id);
    }

    // Lista apenas os clientes de um determinado utilizador
    public List<ClienteEntity> findAllActiveByUser(UserEntity user) {
        return em.createQuery("SELECT c FROM ClienteEntity c WHERE c.user = :user AND c.isAtivo = true", ClienteEntity.class)
                .setParameter("user", user)
                .getResultList();
    }

    // Lista os clientes APAGADOS de um utilizador (útil para a funcionalidade de restaurar )
    public List<ClienteEntity> findAllDeletedByUser(UserEntity user) {
        return em.createQuery("SELECT c FROM ClienteEntity c WHERE c.user = :user AND c.isAtivo = false", ClienteEntity.class)
                .setParameter("user", user)
                .getResultList();
    }

    // Substitui o "existeClienteGlobal" - Pergunta à BD se já existe este nome E empresa
    public boolean existsByNomeAndEmpresa(String nome, String empresa) {
        Long count = em.createQuery(
                        "SELECT COUNT(c) FROM ClienteEntity c WHERE LOWER(c.nome) = LOWER(:nome) AND LOWER(c.empresa) = LOWER(:empresa) AND c.isAtivo = true", Long.class)
                .setParameter("nome", nome)
                .setParameter("empresa", empresa)
                .getSingleResult();
        return count > 0;
    }

    // Ignora o cliente que estamos a editar
    public boolean existsByNomeAndEmpresaForEdit(Long idToIgnore, String nome, String empresa) {
        Long count = em.createQuery(
                        "SELECT COUNT(c) FROM ClienteEntity c WHERE LOWER(c.nome) = LOWER(:nome) AND LOWER(c.empresa) = LOWER(:empresa) AND c.id != :id AND c.isAtivo = true", Long.class)
                .setParameter("nome", nome)
                .setParameter("empresa", empresa)
                .setParameter("id", idToIgnore)
                .getSingleResult();
        return count > 0;
    }

    public void deletClient(ClienteEntity c){

        c.setAtivo(false); // Apaga de forma lógica (soft delete)
        merge(c); // Grava a alteração

    }
}
