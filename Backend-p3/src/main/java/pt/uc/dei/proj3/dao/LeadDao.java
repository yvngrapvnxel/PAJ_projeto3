package pt.uc.dei.proj3.dao;

import pt.uc.dei.proj3.entity.LeadEntity;
import java.io.Serializable;

public class LeadDao extends DefaultDao<LeadEntity> implements Serializable {
    public LeadDao() {
        super(LeadEntity.class);
    }

    // find lead by id

    // find all by user
}
