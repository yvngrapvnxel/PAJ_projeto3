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


    public LeadDto converterParaDto(LeadEntity lead) {
        return new LeadDto(
                lead.getId(),
                lead.getTitulo(),
                lead.getDescricao(),
                lead.getEstado(),
                lead.getDataCriacao(),
                lead.getUser()
        );
    }


    // Criar Lead
    public void createLead(LeadDto leadDto) {
        leadDao.newLead(leadDto);
    }


    // Listar Leads
    public List<LeadDto> getAllLeads(UserEntity user) {

        List<LeadEntity> leads = userDao.getAllLeads(user);
        if (leads.isEmpty()) return null;
        List<LeadDto> dtoLeads = new ArrayList<>();

        for (LeadEntity l : leads) {
            dtoLeads.add(converterParaDto(l));
        }

        return dtoLeads;
    }


    // Editar Lead
    public boolean updateLead(UserEntity user, int id, LeadDto dto) {
        int rows = leadDao.updateLead(id, dto);
        return rows > 0;
    }


    // Apagar Lead
    public int softDeleteLead(int id) {
        return leadDao.softDeleteLead(id);
    }

}