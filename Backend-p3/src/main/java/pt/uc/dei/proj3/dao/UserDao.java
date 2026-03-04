package pt.uc.dei.proj3.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import pt.uc.dei.proj3.entity.UserEntity;
import java.io.Serializable;
import java.util.List;

@Stateless
public class UserDao extends DefaultDao<UserEntity> implements Serializable {

    public UserDao() {
        super(UserEntity.class);
    }

    // Procura um utilizador pelo seu username
    public UserEntity findUserByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Retorna null se não encontrar ninguém (útil para o login ou registo)
        }
    }

    public UserEntity findUserByToken(String tokenEncriptado) {
        try {
            // Faz um JOIN com a tabela de tokens para encontrar o dono do token válido
            return em.createQuery(
                            "SELECT u FROM UserEntity u JOIN u.tokens t WHERE t.token = :token AND t.expireTime > CURRENT_TIMESTAMP",
                            UserEntity.class)
                    .setParameter("token", tokenEncriptado)
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

    // find user by token

    // find user by username?

}
