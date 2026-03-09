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


    private LeadEntity converterParaEntity(LeadDto dto) {

        LeadEntity novaLead = new LeadEntity();
        novaLead.setTitulo(dto.getTitulo());
        novaLead.setDescricao(dto.getDescricao());
        novaLead.setEstado(dto.getEstado());
        novaLead.setIsAtivo(true);
        novaLead.setUser(dto.getUser());

        return novaLead;
    }


    public void newLead(LeadDto dto) {
        LeadEntity lead = converterParaEntity(dto);
        em.persist(lead);
    }


    public int updateLead(int id, LeadDto dto) {

        return em.createQuery(
                        "UPDATE LeadEntity l SET l.titulo = :titulo, l.descricao = :descricao, l.estado = :estado " +
                                "WHERE l.id = :id")
                .setParameter("titulo", dto.getTitulo())
                .setParameter("descricao", dto.getDescricao())
                .setParameter("estado", dto.getEstado())
                .setParameter("id", id)
                .executeUpdate();

    }

    public int softDeleteLead(int id) {

        return em.createQuery("UPDATE LeadEntity l SET l.isAtivo = false WHERE l.id = :id")
                .setParameter("id", id)
                .executeUpdate();

    }

}