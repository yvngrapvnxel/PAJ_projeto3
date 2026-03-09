package pt.uc.dei.proj3.beans;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pt.uc.dei.proj3.dao.TokenDao;
import pt.uc.dei.proj3.dao.UserDao;
import pt.uc.dei.proj3.dto.UserDto;
import pt.uc.dei.proj3.entity.UserEntity;

import java.io.Serializable;

// Passamos a @Stateless (EJB) para gerir as transações automaticamente com a Base de Dados
@Stateless
public class UserBean implements Serializable {

    @Inject
    UserDao userDao;

    @Inject
    TokenDao tokenDao;


    public String loginToken(String username, String password) {
        // Vai buscar à BD
        UserEntity u = userDao.getLogin(username, password);

        if (u == null) return null;

        // 1. Gera o token limpo (para devolver no REST)
        String tokenLimpo = TokenBean.generateToken();

        tokenDao.guardarTokenDB(tokenLimpo, u);

        return tokenLimpo; // Retorna a versão não encriptada para o Frontend/Postman
    }

    public boolean validarToken(String username, String token) {
        if (username == null || token == null || username.trim().isEmpty() || token.trim().isEmpty()) {
            return false;
        }

        UserEntity u = tokenDao.getUserByToken(token);

        return (u != null && u.getUsername().equals(username));
    }

    public boolean register(UserDto newUser) {
        // Verifica se já existe alguém com este username na Base de Dados
        if (userDao.checkUsername(newUser.getUsername()) != null) {
            System.out.println("metodo userbean boolean falso");
            return false;
        }

        userDao.novoUserDB(newUser); // Guarda na BD
        return true;
    }

    public void logout(String token) {
        tokenDao.setExpired(token);
    }

    public UserDto getUser(String username) {
        UserEntity entity = userDao.getUserByUsername(username);
        if (entity == null) return null;
        return converterParaDto(entity);
    }

    public boolean updateUser(String token, UserDto novosDados) {
        UserEntity u = tokenDao.getUserByToken(token);
        if (u == null) return false;

        userDao.updateUserDB(u, novosDados); // Faz o UPDATE na BD
        return true;
    }

    public UserDto getUserByToken(String token) {
        UserEntity entity = tokenDao.getUserByToken(token);
        if (entity == null) return null;
        return converterParaDto(entity);
    }

    // Função auxiliar para mapear de Entity (BD) para DTO (Frontend)
    public UserDto converterParaDto(UserEntity e) {
        UserDto dto = new UserDto();
        dto.setId(e.getId());
        dto.setPrimeiroNome(e.getPrimeiroNome());
        dto.setUltimoNome(e.getUltimoNome());
        dto.setEmail(e.getEmail());
        dto.setTelefone(e.getTelefone());
        dto.setUsername(e.getUsername());
        dto.setFotoUrl(e.getFotoUrl());
        dto.setAdmin(e.isAdmin());
        return dto;
    }
}