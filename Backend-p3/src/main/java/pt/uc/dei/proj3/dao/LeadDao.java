package pt.uc.dei.proj3.dao;

import jakarta.ejb.Stateless;
import pt.uc.dei.proj3.entity.LeadEntity;
import pt.uc.dei.proj3.entity.TokenEntity;

import java.io.Serial;
import java.io.Serializable;

@Stateless
public class LeadDao extends DefaultDao<LeadEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public LeadDao() {
        super(LeadEntity.class);
    }

    // find lead by id

    // find all by user
}
