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

        UserEntity u = UserDao.findUserByUsername(usernameDono);

        if ( u == null ) return null;

        if (clienteDao.existsByNomeAndEmpresa(newClient.getNome(),newClient.getEmpresa())){
            throw new Exception("Este cliente já está registado nesta empresa.");
        }

        clienteDao.guardaCliente(newClient, u);

        return newClient;
    }

    public ClientDto editarCliente(Long idCliente, ClientDto dtoNovo) throws Exception {

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

        return dtoNovo;
    }

    public List<ClientDto> listClients(String username) {


        UserDto user = storageBean.findUser(username);
        return (user != null) ? user.getMeusClientes() : new ArrayList<>();
    }

    public boolean deletClient(int id) {
        return storageBean.deletClient(id);
    }

}
