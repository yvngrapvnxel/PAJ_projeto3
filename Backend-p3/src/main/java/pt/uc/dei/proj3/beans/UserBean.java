package pt.uc.dei.proj3.beans;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pt.uc.dei.proj3.dto.UserDto;
import pt.uc.dei.proj3.pojo.UserPojo;

import java.io.Serializable;


@RequestScoped
public class UserBean implements Serializable {

    @Inject
    StorageBean storageBean;

    public String loginToken(String username, String password){
        UserPojo u = storageBean.findUser(username);
        if (u != null && u.getPassword().equals(password)) {
            String token = TokenBean.generateToken();
            u.setToken(token);
            storageBean.save();
            return token;
        }
        return null;
    }

    // 2. Novo método para validar o token nos restantes pedidos
    public boolean validarToken(String username, String token) {
        if (username == null || token == null) return false;

        UserPojo u = storageBean.findUser(username);
        // Verifica se o utilizador existe e se o token coincide
        return u != null && token.equals(u.getToken());
    }

    public boolean register(UserDto newUser) {
        // 1. Verificar se o utilizador já existe no storage
        UserPojo existing = storageBean.findUser(newUser.getUsername());
        if (existing != null) {
            return false;
        }

        int nextId = storageBean.generateNextId(storageBean.getUsers(), UserPojo::getId);

        UserPojo user = new UserPojo();

        user.setId(nextId);
        user.setPrimeiroNome(newUser.getPrimeiroNome());
        user.setUltimoNome(newUser.getUltimoNome());
        user.setTelefone(newUser.getTelefone());
        user.setEmail(newUser.getEmail());
        user.setFotoUrl(newUser.getFotoUrl());
        user.setUsername(newUser.getUsername());
        user.setPassword(newUser.getPassword());


        // 2. Adicionar ao storage e gravar no ficheiro JSON
        storageBean.addUser(user);
        return true;
    }

    public UserPojo findUser(String username) {
        return storageBean.findUser(username);
    }

    public void updateUser(String username, UserPojo newData){
        storageBean.updateUserData(username, newData);
    }



}
