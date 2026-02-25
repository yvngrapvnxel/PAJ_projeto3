package pt.uc.dei.proj3.beans;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pt.uc.dei.proj3.dto.ClientDto;
import pt.uc.dei.proj3.pojo.ClientPojo;
import pt.uc.dei.proj3.pojo.UserPojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ClientBean implements Serializable {

    @Inject
    StorageBean storageBean;

    public ClientPojo registarCliente(ClientDto newClient, String usernameDono) throws Exception {

        if (existeClienteGlobal(newClient.getNome(),newClient.getEmpresa())){
            throw new Exception("Este cliente já está registado nesta empresa.");
        }

        // Chama o método genérico do StorageBean para obter o ID
        // Passamos a lista de todos os clientes do sistema e a regra para ler o ID
        int nextId = storageBean.generateNextId(
                storageBean.getUsers().stream()
                    .flatMap(u -> u.getMeusClientes().stream())
                    .toList(), ClientPojo::getId);

        ClientPojo finalClient = new ClientPojo();

        finalClient.setId(nextId);
        finalClient.setNome(newClient.getNome());
        finalClient.setEmail(newClient.getEmail());
        finalClient.setTelefone(newClient.getTelefone());
        finalClient.setEmpresa(newClient.getEmpresa());
        finalClient.setDono(usernameDono);

        storageBean.addCliente(finalClient, usernameDono);

        return finalClient;
    }

    public boolean existeClienteGlobal(String nome, String empresa) {
        List<UserPojo> todosUsers = storageBean.getUsers();

        // Percorre todos os utilizadores e as suas listas internas de clientes
        for (UserPojo u : todosUsers) {
            for (ClientPojo c : u.getMeusClientes()) {
                if (c.getNome().equalsIgnoreCase(nome) && c.getEmpresa().equalsIgnoreCase(empresa)) {
                    return true; // Encontrou duplicado em algum utilizador
                }
            }
        }
        return false;
    }

    // Adiciona este novo método logo abaixo do existeClienteGlobal
    public boolean existeClienteGlobalParaEdicao(int idAtual, String nome, String empresa) {
        List<UserPojo> todosUsers = storageBean.getUsers();
        for (UserPojo u : todosUsers) {
            for (ClientPojo c : u.getMeusClientes()) {
                // Se encontrar o mesmo nome e empresa...
                if (c.getNome().equalsIgnoreCase(nome) && c.getEmpresa().equalsIgnoreCase(empresa)) {
                    // ...verifica se é o próprio cliente. Se o ID for diferente, é um duplicado real!
                    if (c.getId() != idAtual) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Substitui o teu método editarCliente por este:
    public void editarCliente(int id, ClientDto dto) throws Exception {
        // Agora usamos o novo método que ignora o próprio cliente
        if (existeClienteGlobalParaEdicao(id, dto.getNome(), dto.getEmpresa())){
            throw new Exception("Este cliente já está registado nesta empresa.");
        }
        storageBean.updateClientData(id, dto);
    }

    public List<ClientPojo> listClients(String username) {
        UserPojo user = storageBean.findUser(username);
        return (user != null) ? user.getMeusClientes() : new ArrayList<>();
    }

    public boolean deletClient(int id) {
        return storageBean.deletClient(id);
    }

}
