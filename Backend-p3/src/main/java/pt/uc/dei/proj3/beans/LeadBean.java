package pt.uc.dei.proj3.beans;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pt.uc.dei.proj3.dao.LeadDao;
import pt.uc.dei.proj3.dao.UserDao;
import pt.uc.dei.proj3.dto.LeadDto;
import pt.uc.dei.proj3.entity.LeadEntity;
import pt.uc.dei.proj3.entity.UserEntity;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;


@Stateless
public class LeadBean implements Serializable {


    @Inject
    UserDao userDao;

    @Inject
    LeadDao leadDao;

    @Inject
    UserBean userBean;


    public LeadDto converterParaDto(LeadEntity lead) {
        if (lead == null) return null;
        LeadDto dto = new LeadDto(
                lead.getId(),
                lead.getTitulo(),
                lead.getDescricao(),
                lead.getEstado(),
                lead.getDataCriacao(),
                userBean.converterParaDto(lead.getUser())
        );
        dto.setAtivo(lead.isAtivo());

        return dto;
    }

    public LeadEntity converterParaEntity(LeadDto dto) {
        if (dto == null) return null;

        LeadEntity entity = new LeadEntity();
        entity.setTitulo(dto.getTitulo());
        entity.setDescricao(dto.getDescricao());
        entity.setEstado(dto.getEstado());
        entity.setIsAtivo(true);

        if (dto.getUser() != null && dto.getUser().getUsername() != null) {
            UserEntity user = userDao.getUserByUsername(dto.getUser().getUsername());
            entity.setUser(user);
        }

        return entity;
    }


    // Criar Lead
    public void createLead(LeadDto leadDto) {
        LeadEntity lead = converterParaEntity(leadDto);
        leadDao.newLead(lead);
    }


    // Listar Leads
    public List<LeadDto> getAllLeads(UserEntity user) {

        List<LeadEntity> leads = userDao.getAllActiveLeads(user);
        List<LeadDto> dtoLeads = new ArrayList<>();

        if (leads != null) {
            for (LeadEntity l : leads) {
                dtoLeads.add(converterParaDto(l));
            }
        }

        return dtoLeads;
    }


    // Editar Lead
    public boolean updateLead(Long id, LeadDto dto) {
        // Extract data from DTO and pass to DAO
        int rows = leadDao.updateLead(
                id,
                dto.getTitulo(),
                dto.getDescricao(),
                dto.getEstado()
        );

        return rows > 0;
    }


    // Apagar Lead
    public int softDeleteLead(Long id) {
        return leadDao.softDeleteLead(id);
    }

}