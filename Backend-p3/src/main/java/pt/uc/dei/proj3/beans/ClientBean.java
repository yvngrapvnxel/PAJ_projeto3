package pt.uc.dei.proj3.beans;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pt.uc.dei.proj3.dao.ClienteDao;
import pt.uc.dei.proj3.dao.UserDao;
import pt.uc.dei.proj3.dto.ClientDto;
import pt.uc.dei.proj3.dto.UserDto;
import pt.uc.dei.proj3.entity.ClienteEntity;
import pt.uc.dei.proj3.entity.UserEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ClientBean implements Serializable {

    @Inject
    ClienteDao clienteDao;

    @Inject
    UserDao userDao;

    public ClientDto registarCliente(ClientDto newClient, String usernameDono) throws Exception {

        UserEntity u = userDao.getUserByUsername(usernameDono);

        if ( u == null ) return null;

        if (clienteDao.existsByNomeAndEmpresa(newClient.getNome(),newClient.getEmpresa())){
            throw new Exception("Este cliente já está registado nesta empresa.");
        }

        clienteDao.guardaCliente(newClient, u);

        return newClient;
    }

    public boolean editarCliente(Long idCliente, ClientDto dtoNovo) throws Exception {

        ClienteEntity clienteAtual = clienteDao.findClienteById(idCliente);

        // Se não existir ou se estiver apagado (soft delete), não podemos editar
        if (clienteAtual == null || !clienteAtual.isAtivo()) {
            throw new Exception("Cliente não encontrado ou inativo.");
        }

        // 2. Verifica se a nova combinação de Nome/Empresa já existe noutro cliente diferente
        // Usamos o método "ForEdit" para não dar erro se ele mantiver o próprio Nome e Empresa
        if (clienteDao.existsByNomeAndEmpresaForEdit(idCliente, dtoNovo.getNome(), dtoNovo.getEmpresa())) {
            throw new Exception("Este cliente já está registado nesta empresa.");
        }

        // 3. Manda para o DAO atualizar a entidade
        clienteDao.atualizaCliente(clienteAtual, dtoNovo);

        return true;
    }

    public List<ClientDto> listClients(String username) {

        UserEntity user = userDao.getUserByUsername(username);

        if (user == null) return new ArrayList<>();

        List<ClienteEntity> entidades = clienteDao.findAllActiveByUser(user);

        List<ClientDto> myClients = new ArrayList<>();

        for(ClienteEntity e : entidades){
            myClients.add(converForDto(e));
        }
        return myClients;

    }

    public boolean deletClient(long id) {
        ClienteEntity clienteAtual = clienteDao.findClienteById(id);

        if (clienteAtual == null || !clienteAtual.isAtivo()) return false;

        clienteDao.deletClient(clienteAtual);
        return true;

    }

    public ClientDto converForDto(ClienteEntity e){
        ClientDto c = new ClientDto();
        c.setId(e.getId().longValue());
        c.setNome(e.getNome());
        c.setTelefone(e.getTelefone());
        c.setEmail(e.getEmail());
        c.setEmpresa(e.getEmpresa());

        if (e.getUser() != null) {
            c.setDono(e.getUser().getUsername());
        } else {
            c.setDono("Sem dono");
        }

        c.setAtivo(e.isAtivo());

        return c;
    }

}
