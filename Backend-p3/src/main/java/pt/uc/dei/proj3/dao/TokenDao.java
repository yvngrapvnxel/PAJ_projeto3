package pt.uc.dei.proj3.dao;

import jakarta.ejb.Stateless;
import pt.uc.dei.proj3.entity.TokenEntity;
import java.io.Serializable;

@Stateless
public class TokenDao extends DefaultDao<TokenEntity> implements Serializable {

    private static final long serialVersionUID = 1L;

    public TokenDao() {
        super(TokenEntity.class);
    }


    public boolean validateToken(String token, Long userID) {
        Long resultado = em.createQuery("SELECT COUNT(t) FROM TokenEntity t WHERE t.token = :token AND t.user.id = :userID", Long.class)
                .setParameter("token", token)
                .setParameter("userID", userID)
                .getSingleResult();
        // verificar data expirar
        return resultado > 0;
    }

}
