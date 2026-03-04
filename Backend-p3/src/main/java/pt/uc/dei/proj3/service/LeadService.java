package pt.uc.dei.proj3.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.uc.dei.proj3.beans.LeadBean;
import pt.uc.dei.proj3.beans.UserBean;
import pt.uc.dei.proj3.dao.LeadDao;
import pt.uc.dei.proj3.dto.LeadDto;
import pt.uc.dei.proj3.dto.UserDto;

//test
import java.util.List;

@Path("/leads")
public class LeadService {

    @Inject
    private LeadBean leadBean;
    @Inject
    private UserBean userBean;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLead(@HeaderParam("username") String username,@HeaderParam("token") String token, LeadDto leadDto) {

        // 1. Usar o novo método validarToken em vez do login
        if (username == null || token == null || !userBean.validarToken(username, token)) {
            return Response.status(401).entity("Acesso negado - Token inválido ou ausente").build(); // Retorna 401 conforme o enunciado
        }

        if (leadDto == null ||
                leadDto.getTitulo() == null || leadDto.getTitulo().trim().isEmpty() ||
                leadDto.getDescricao() == null || leadDto.getDescricao().trim().isEmpty()) {
            return Response.status(400).entity("Dados incompletos: Título e Descrição são obrigatórios").build();
        }

        try {
            UserDto novo = leadBean.createLead(username, leadDto);
            return Response.status(201).entity(novo).build();
        } catch (Exception e) {
            return Response.status(409).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLeads(@HeaderParam("username") String username, @HeaderParam("token") String token) {

        // 1. Usar o novo método validarToken em vez do login
        if (username == null || token == null || !userBean.validarToken(username, token)) {
            return Response.status(401).entity("Acesso negado - Token inválido ou ausente").build(); // Retorna 401 conforme o enunciado
        }

        List<LeadDto> leads = leadBean.getLeads(username);
        return Response.status(200).entity(leads).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editarLead(@PathParam("id") int id,
                               @HeaderParam("username") String username, @HeaderParam("token") String token,
                               LeadDto dto) {

        // 1. Usar o novo método validarToken em vez do login
        if (username == null || token == null || !userBean.validarToken(username, token)) {
            return Response.status(401).entity("Acesso negado - Token inválido ou ausente").build(); // Retorna 401 conforme o enunciado
        }

        if (dto == null ||
                dto.getTitulo() == null || dto.getTitulo().trim().isEmpty() ||
                dto.getDescricao() == null || dto.getDescricao().trim().isEmpty()) {
            return Response.status(400).entity("Erro: Título e Descrição são obrigatórios para a edição").build();
        }

        LeadPojo updated = leadBean.updateLead(username, id, dto);

        if (updated == null) {
            return Response.status(404).entity("Lead não encontrada com o ID: " + id).build();
        }

        return Response.status(200).entity("Lead atualizada com sucesso").build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarLead(@PathParam("id") int id,
                                 @HeaderParam("username") String username, @HeaderParam("token") String token) {

        // 1. Usar o novo método validarToken em vez do login
        if (username == null || token == null || !userBean.validarToken(username, token)) {
            return Response.status(401).entity("Acesso negado - Token inválido ou ausente").build(); // Retorna 401 conforme o enunciado
        }

        boolean success = leadBean.deleteLead(username, id);

        if (success) {
            return Response.status(200).entity("Lead removida com sucesso").build();
        } else {
            return Response.status(404).entity("Lead não encontrada com o ID: " + id).build();
        }
    }
}