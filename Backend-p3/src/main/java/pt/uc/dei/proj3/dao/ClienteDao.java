package pt.uc.dei.proj3.dao;

import pt.uc.dei.proj3.entity.ClienteEntity;
import java.io.Serializable;

public class ClienteDao extends DefaultDao<ClienteEntity> implements Serializable {
    public ClienteDao() {
        super(ClienteEntity.class);
    }

    // find cliente by id

    // find all by user
}
