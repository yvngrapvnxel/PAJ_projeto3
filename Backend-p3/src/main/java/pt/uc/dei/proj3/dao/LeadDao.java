package pt.uc.dei.proj3.dao;


import jakarta.ejb.Stateless;
import pt.uc.dei.proj3.dto.LeadDto;
import pt.uc.dei.proj3.entity.LeadEntity;
import java.io.Serial;
import java.io.Serializable;


@Stateless
public class LeadDao extends DefaultDao<LeadEntity> implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    public LeadDao() {
        super(LeadEntity.class);
    }


    public void newLead(LeadEntity lead) {
        em.persist(lead);
    }


    public int updateLead(Long id, String titulo, String descricao, int estado) {

        return em.createQuery(
                        "UPDATE LeadEntity l SET l.titulo = :titulo, l.descricao = :descricao, l.estado = :estado " +
                                "WHERE l.id = :id")
                .setParameter("titulo", titulo)
                .setParameter("descricao", descricao)
                .setParameter("estado", estado)
                .setParameter("id", id)
                .executeUpdate();

    }

    public int softDeleteLead(Long id) {

        return em.createQuery("UPDATE LeadEntity l SET l.isAtivo = false WHERE l.id = :id")
                .setParameter("id", id)
                .executeUpdate();

    }

}