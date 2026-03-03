package pt.uc.dei.proj3.dao;

import pt.uc.dei.proj3.entity.UserEntity;
import java.io.Serializable;

public class UserDao extends DefaultDao<UserEntity> implements Serializable {
    public UserDao() {
        super(UserEntity.class);
    }



    // find user by token

    // find user by username?

}
