package pt.uc.dei.proj3.beans;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pt.uc.dei.proj3.dto.LeadDto;
import pt.uc.dei.proj3.entity.LeadEntity;
import pt.uc.dei.proj3.pojo.LeadPojo;
import pt.uc.dei.proj3.pojo.UserPojo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Stateless
public class LeadBean implements Serializable {


    // Criar Lead

    public LeadDto createLead(String username, LeadDto leadDto) {


        LeadDto novaLead = new LeadDto();

        novaLead.setTitulo(leadDto.getTitulo());
        novaLead.setDescricao(leadDto.getDescricao());
        novaLead.setEstado(leadDto.getEstado());

        // get user by username? add lead to lista leads?

        //storageBean.addLeads(novaLead,username);

        return novaLead;

    }

    // Listar Leads

    public List<LeadPojo> getLeads(String username) {

        UserPojo user = storageBean.findUser(username);

        if (user == null) return null;

        return user.getMeusLeads();
    }

    // Editar Lead

    public LeadPojo updateLead(String username, int id, LeadDto dto) {

        UserPojo user = storageBean.findUser(username);
        if (user == null) return null;

        for (LeadPojo l : user.getMeusLeads()) {
            if (l.getId() == id) {
                l.setTitulo(dto.getTitulo());
                l.setDescricao(dto.getDescricao());
                l.setEstado(dto.getEstado()); // usa o estado vindo do DTO
                storageBean.save();           // persiste
                return l;
            }
        }
        return null;
    }

    // Apagar Lead

    public boolean deleteLead(String username, int id) {

        UserPojo user = storageBean.findUser(username);
        if (user == null) return false;

        boolean removed = user.getMeusLeads().removeIf(l -> l.getId() == id);

        if (removed) {
            storageBean.save();
        }

        return removed;
    }


}
