package pt.uc.dei.proj3.dao;

import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public abstract class DefaultDao<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Class<T> clazz;

    @PersistenceContext(unitName = "projeto3")
    protected EntityManager em;




    public DefaultDao(Class<T> clazz) {
        this.clazz = clazz;
    }
}
