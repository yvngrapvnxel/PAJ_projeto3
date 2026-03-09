package pt.uc.dei.proj3.beans;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pt.uc.dei.proj3.dao.AdminDao;

import pt.uc.dei.proj3.dao.ClienteDao;
import pt.uc.dei.proj3.dto.ClientDto;
import pt.uc.dei.proj3.entity.ClienteEntity;

import pt.uc.dei.proj3.dao.TokenDao;

import pt.uc.dei.proj3.dto.UserDto;
import pt.uc.dei.proj3.entity.UserEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Passamos a @Stateless (EJB) para gerir as transações automaticamente com a Base de Dados
@Stateless
public class AdminBean implements Serializable {

    @Inject
    AdminDao adminDao;

    @Inject
    TokenDao tokenDao;

    @Inject
    UserBean userBean;

    @Inject
    ClienteDao clienteDao;

    @Inject
    ClientBean clientBean;

    public UserEntity checkAdmin(String token) throws Exception {

        if (token == null || token.trim().isEmpty()) {
            throw new Exception("401: Token ausente.");
        }
        // Aproveita a validação base de cima para verificar se o token é válido
        UserEntity admin = tokenDao.getUserByToken(token);

        if (admin == null) {
            throw new Exception("401: Token inválido ou sessão expirada.");
        }

        // Verifica a restrição extra do Administrador
        if (!admin.isAdmin()) {
            throw new Exception("403: Acesso negado. Esta funcionalidade é exclusiva para Administradores.");
        }

        return admin;
    }


    // metodos de users

    // Devolve a lista de todos os utilizadores em formato DTO
    public List<UserDto> getAllUsers(String tokenAdmin) throws Exception {
        checkAdmin(tokenAdmin); // Verifica se é o admin

        List<UserEntity> users = adminDao.findAllUsers();
        List<UserDto> dtos = new ArrayList<>();

        for(UserEntity u : users) {
            dtos.add(userBean.converterParaDto(u));
        }
        return dtos;
    }


    public UserDto getProfileUser(String tokenAdmin, String usernameAlvo) throws Exception {
        checkAdmin(tokenAdmin);

        UserEntity alvo = adminDao.checkUsername(usernameAlvo);
        if (alvo == null) {
            throw new Exception("404: Utilizador não encontrado.");
        }

        return userBean.converterParaDto(alvo);
    }

    public void softDeleteUser(String tokenAdmin, String usernameAlvo) throws Exception {
        checkAdmin(tokenAdmin);

        UserEntity alvo = adminDao.checkUsername(usernameAlvo);
        if (alvo == null) {
            throw new Exception("404: Utilizador não encontrado.");
        }

        alvo.setIsAtivo(false);
        adminDao.merge(alvo);
    }

    public void hardDeleteUser(String tokenAdmin, String usernameAlvo) throws Exception {
        checkAdmin(tokenAdmin);

        UserEntity alvo = adminDao.checkUsername(usernameAlvo);
        if (alvo == null) {
            throw new Exception("404: Utilizador não encontrado.");
        }

        adminDao.remove(alvo);
    }


    // metodos de clientes

    public List<ClientDto> getClientFromUser(String tokenAdmin, String usernameAlvo) throws Exception {
        checkAdmin(tokenAdmin);

        UserEntity alvo = adminDao.checkUsername(usernameAlvo);
        if (alvo == null) throw new Exception("404: Utilizador não encontrado");

        List <ClienteEntity> clients = clienteDao.findAllByUserForAdmin(alvo);
        List<ClientDto> dtos = new ArrayList<>();
        for (ClienteEntity c : clients) {
            dtos.add(clientBean.converForDto(c));
        }
        return dtos;
    }

    //Editar Clientes de outros utilizadores
    public void editarClienteAdmin(String tokenAdmin, Long idCliente, ClientDto dtoNovo) throws Exception {
        checkAdmin(tokenAdmin);

        ClienteEntity clienteAtual = clienteDao.findClienteById(idCliente);
        if (clienteAtual == null) throw new Exception("404: Cliente não encontrado.");

        // Verifica duplicados ignorando o próprio cliente
        if (clienteDao.existsByNomeAndEmpresaForEdit(idCliente, dtoNovo.getNome(), dtoNovo.getEmpresa())) {
            throw new Exception("409: Este cliente já está registado nesta empresa.");
        }

        clienteDao.atualizaCliente(clienteAtual, dtoNovo); // ClienteDao faz o update
    }

    // Apagar Cliente (Soft ou Hard Delete)
    public void apagarClienteAdmin(String tokenAdmin, Long idCliente, boolean permanente) throws Exception {
        checkAdmin(tokenAdmin);

        ClienteEntity clienteAtual = clienteDao.findClienteById(idCliente);
        if (clienteAtual == null) throw new Exception("404: Cliente não encontrado.");

        if (permanente) {
            clienteDao.remove(clienteAtual); // Hard delete
        } else {
            clienteDao.deletClient(clienteAtual); // Soft delete (já tens este método no ClienteDao!)
        }
    }

    // Apagar todos os clientes criados por um utilizador
    public void apagarTodosClientesDeUser(String tokenAdmin, String usernameAlvo, boolean permanente) throws Exception {
        checkAdmin(tokenAdmin);

        UserEntity alvo = adminDao.checkUsername(usernameAlvo);
        if (alvo == null) throw new Exception("404: Utilizador alvo não encontrado.");

        List<ClienteEntity> clientes = clienteDao.findAllByUserForAdmin(alvo);
        for (ClienteEntity c : clientes) {
            if (permanente) {
                clienteDao.remove(c);
            } else {
                clienteDao.deletClient(c);
            }
        }
    }
}