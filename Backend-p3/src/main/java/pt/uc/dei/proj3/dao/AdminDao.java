package pt.uc.dei.proj3.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import pt.uc.dei.proj3.entity.ClienteEntity;
import pt.uc.dei.proj3.entity.UserEntity;

import java.io.Serializable;
import java.util.List;

@Stateless
public class AdminDao extends DefaultDao<UserEntity> implements Serializable {

    public AdminDao() {
        super(UserEntity.class);
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

    // Vai buscar TODOS os utilizadores à base de dados (Ativos e Inativos)
    public List<UserEntity> findAllUsers() {
        return em.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
    }

    // Lista TODOS os clientes de um utilizador (Ativos e Inativos) - Exclusivo para Admin
//    public List<ClienteEntity> findAllByUserForAdmin(UserEntity user) {
//        return em.createQuery("SELECT c FROM ClienteEntity c WHERE c.user = :user", ClienteEntity.class)
//                .setParameter("user", user)
//                .getResultList();
//    }
//
//    // Exemplo: O Admin apaga todos os clientes de um utilizador de uma só vez (muito eficiente)
//    public void softDeleteAllClientes(UserEntity user) {
//        em.createQuery("UPDATE ClienteEntity c SET c.isAtivo = false WHERE c.user = :user")
//                .setParameter("user", user)
//                .executeUpdate();
//    }
}