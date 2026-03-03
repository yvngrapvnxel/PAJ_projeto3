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




}
