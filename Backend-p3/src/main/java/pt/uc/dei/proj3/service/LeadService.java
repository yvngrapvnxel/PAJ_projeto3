package pt.uc.dei.proj3.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.uc.dei.proj3.beans.LeadBean;
import pt.uc.dei.proj3.beans.UserBean;
import pt.uc.dei.proj3.dao.LeadDao;
import pt.uc.dei.proj3.dao.TokenDao;
import pt.uc.dei.proj3.dto.LeadDto;
import pt.uc.dei.proj3.dto.UserDto;
import pt.uc.dei.proj3.entity.UserEntity;

//test
import java.util.List;

@Path("/leads")
public class LeadService {

    @Inject
    private LeadBean leadBean;
    @Inject
    private UserBean userBean;
    @Inject
    private TokenDao tokenDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLead(@HeaderParam("token") String token, LeadDto leadDto) {


        UserEntity user = tokenDao.getUserByToken(token);

        if (token == null || user == null) {
            return Response.status(401).entity("Acesso negado - Token inválido ou ausente").build(); // Retorna 401 conforme o enunciado
        }

        if (leadDto == null ||
                leadDto.getTitulo() == null || leadDto.getTitulo().trim().isEmpty() ||
                leadDto.getDescricao() == null || leadDto.getDescricao().trim().isEmpty()) {
            return Response.status(400).entity("Dados incompletos: Título e Descrição são obrigatórios").build();
        }

        try {
            leadBean.createLead(leadDto);
            return Response.status(201).entity("Lead adicionada com sucesso!").build();
        } catch (Exception e) {
            return Response.status(409).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLeads(@HeaderParam("token") String token) {

        UserEntity user = tokenDao.getUserByToken(token);

        if (token == null || user == null) {
            return Response.status(401).entity("Acesso negado - Token inválido ou ausente").build(); // Retorna 401 conforme o enunciado
        }

        List<LeadDto> leads = leadBean.getAllLeads(user);
        return Response.status(200).entity(leads).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editarLead(@PathParam("id") int id,
                               @HeaderParam("token") String token,
                               LeadDto dto) {


        UserEntity user = tokenDao.getUserByToken(token);

        if (user == null || token == null) {
            return Response.status(401).entity("Acesso negado - Token inválido ou ausente").build(); // Retorna 401 conforme o enunciado
        }

        if (dto == null ||
                dto.getTitulo() == null || dto.getTitulo().trim().isEmpty() ||
                dto.getDescricao() == null || dto.getDescricao().trim().isEmpty()) {
            return Response.status(400).entity("Erro: Título e Descrição são obrigatórios para a edição").build();
        }

        boolean updated = leadBean.updateLead(user, id, dto);

        if (!updated) {
            return Response.status(404).entity("Lead não encontrada com o ID: " + id).build();
        }

        return Response.status(200).entity("Lead atualizada com sucesso").build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarLead(@PathParam("id") int id,
                                 @HeaderParam("token") String token) {


        UserEntity user = tokenDao.getUserByToken(token); // TODO enviar token para tokenBean e lá encriptar e enviar para tokenDao

        if (user == null || token == null) {
            return Response.status(401).entity("Acesso negado - Token inválido ou ausente").build(); // Retorna 401 conforme o enunciado
        }

        int success = leadBean.softDeleteLead(id);

        if (success > 0) {
            return Response.status(200).entity("Lead removida com sucesso").build();
        } else {
            return Response.status(404).entity("Lead não encontrada com o ID: " + id).build();
        }
    }
}