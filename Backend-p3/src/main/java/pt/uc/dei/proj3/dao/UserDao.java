package pt.uc.dei.proj3.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import pt.uc.dei.proj3.dto.UserDto;
import pt.uc.dei.proj3.entity.UserEntity;
import java.io.Serializable;
import java.util.List;

@Stateless
public class UserDao extends DefaultDao<UserEntity> implements Serializable {

    public UserDao() {
        super(UserEntity.class);
    }

    // Procura um utilizador pelo seu username
    public UserEntity getLogin(String username, String password) {
        try {
            return em.createQuery("SELECT u FROM UserEntity u WHERE u.username = :username AND u.password = :password AND u.isAtivo", UserEntity.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Retorna null se não encontrar ninguém (útil para o login ou registo)
        }
    }

    public UserEntity getUserByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM UserEntity u WHERE u.username = :username AND u.isAtivo", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Retorna null se não encontrar ninguém
        }
    }

    public UserEntity checkUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    // 3. Lista todos os utilizadores ATIVOS (para o Administrador)
    public List<UserEntity> findAllActiveUsers() {
        return em.createQuery("SELECT u FROM UserEntity u WHERE u.isAtivo = true", UserEntity.class)
                .getResultList();
    }


    public void novoUserDB(UserDto novoUser) {

        UserEntity user = new UserEntity();
        user.setPrimeiroNome(novoUser.getPrimeiroNome());
        user.setUltimoNome(novoUser.getUltimoNome());
        user.setTelefone(novoUser.getTelefone());
        user.setEmail(novoUser.getEmail());
        user.setFotoUrl(novoUser.getFotoUrl());
        user.setUsername(novoUser.getUsername());
        user.setPassword(novoUser.getPassword());
        user.setIsAtivo(true); // Fica logo ativo

        persist(user);
    }


    public void updateUserDB(UserEntity u, UserDto novosDados){

        u.setPrimeiroNome(novosDados.getPrimeiroNome());
        u.setUltimoNome(novosDados.getUltimoNome());
        u.setEmail(novosDados.getEmail());
        u.setTelefone(novosDados.getTelefone());
        u.setFotoUrl(novosDados.getFotoUrl());

        if (novosDados.getPassword() != null && !novosDados.getPassword().trim().isEmpty()) {
            u.setPassword(novosDados.getPassword());
        }

        merge(u);
    }

    // find user by token

    // find user by username?

}
