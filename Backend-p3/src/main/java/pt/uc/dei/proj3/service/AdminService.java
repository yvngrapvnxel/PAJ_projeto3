package pt.uc.dei.proj3.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.uc.dei.proj3.beans.AdminBean;
import pt.uc.dei.proj3.dto.LeadDto;
import pt.uc.dei.proj3.dto.UserDto;

@Path("/admin")
public class AdminService {

    @Inject
    AdminBean adminBean;

    @GET
    @Path("/users/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfileUser(@HeaderParam("token") String token,
                                   @PathParam("username") String usernameAlvo) {
        try {
            UserDto alvo = adminBean.getProfileUser(token, usernameAlvo);
            return Response.status(200).entity(alvo).build();
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    // Rota para listar TODOS os utilizadores
    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@HeaderParam("token") String token) {
        try {
            return Response.status(200).entity(adminBean.getAllUsers(token)).build();
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @DELETE
    @Path("/users/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@HeaderParam("token") String token,
                               @PathParam("username") String usernameAlvo,
                               @QueryParam("permanente") boolean permanente) {
        try {
            if (permanente) {
                adminBean.hardDeleteUser(token, usernameAlvo);
                return Response.status(200).entity("Utilizador excluído permanentemente da BD.").build();
            } else {
                adminBean.softDeleteUser(token, usernameAlvo);
                return Response.status(200).entity("Utilizador inativado com sucesso.").build();
            }
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    // ========================================================
    // ROTAS DE GESTÃO DE CLIENTES (ADMIN)
    // ========================================================

    //Obter ou Apagar TODOS os clientes de um utilizador específico
    @GET
    @Path("/users/{username}/clients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientesDeUser(@HeaderParam("token") String token,
                                      @PathParam("username") String usernameAlvo) {
        try {
            return Response.status(200).entity(adminBean.getClientFromUser(token, usernameAlvo)).build();
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @DELETE
    @Path("/users/{username}/clients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response apagarTodosClientesDeUser(@HeaderParam("token") String token,
                                              @PathParam("username") String usernameAlvo,
                                              @QueryParam("permanente") boolean permanente) {
        try {
            adminBean.apagarTodosClientesDeUser(token, usernameAlvo, permanente);
            String msg = permanente ? "Todos os clientes excluídos permanentemente." : "Todos os clientes inativados.";
            return Response.status(200).entity(msg).build();
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    // Editar um cliente específico
    @PUT
    @Path("/clients/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editarClienteAdmin(@HeaderParam("token") String token,
                                       @PathParam("id") Long idCliente,
                                       pt.uc.dei.proj3.dto.ClientDto dto) {
        if (dto.getNome() == null || dto.getEmpresa() == null) {
            return Response.status(400).entity("Nome e Empresa são obrigatórios.").build();
        }

        try {
            adminBean.editarClienteAdmin(token, idCliente, dto);
            return Response.status(200).entity("Cliente atualizado com sucesso (Admin).").build();
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    // Apagar um cliente específico
    @DELETE
    @Path("/clients/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response apagarClienteAdmin(@HeaderParam("token") String token,
                                       @PathParam("id") Long idCliente,
                                       @QueryParam("permanente") boolean permanente) {
        try {
            adminBean.apagarClienteAdmin(token, idCliente, permanente);
            String msg = permanente ? "Cliente excluído permanentemente." : "Cliente inativado com sucesso.";
            return Response.status(200).entity(msg).build();
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    // ========================================================
    // ROTAS DE GESTÃO DE LEADS (ADMIN)
    // ========================================================

    @GET
    @Path("/users/{username}/leads")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLeadsDeUser(@HeaderParam("token") String token,
                                   @PathParam("username") String usernameAlvo) {
        try {
            return Response.status(200).entity(adminBean.getLeadsFromUser(token, usernameAlvo)).build();
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    // Editar uma lead específica (Admin)
    @PUT
    @Path("/leads/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editarLeadAdmin(@HeaderParam("token") String token,
                                    @PathParam("id") Long idLead,
                                    LeadDto dto) {
        if (dto.getTitulo() == null || dto.getDescricao() == null) {
            return Response.status(400).entity("Título e Descrição são obrigatórios.").build();
        }

        try {
            adminBean.editarLeadAdmin(token, idLead, dto);
            return Response.status(200).entity("Lead atualizada com sucesso (Admin).").build();
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @DELETE
    @Path("/users/{username}/leads")
    @Produces(MediaType.APPLICATION_JSON)
    public Response apagarTodasLeadsDeUser(@HeaderParam("token") String token,
                                           @PathParam("username") String usernameAlvo,
                                           @QueryParam("permanente") boolean permanente) {
        try {
            adminBean.apagarTodasLeadsDeUser(token, usernameAlvo, permanente);
            String msg = permanente ? "Todas as leads excluídas." : "Todas as leads inativadas.";
            return Response.status(200).entity(msg).build();
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @DELETE
    @Path("/leads/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response apagarLeadAdmin(@HeaderParam("token") String token,
                                    @PathParam("id") Long idLead,
                                    @QueryParam("permanente") boolean permanente) {
        try {
            adminBean.apagarLeadAdmin(token, idLead, permanente);
            String msg = permanente ? "Lead excluída permanentemente." : "Lead inativada com sucesso.";
            return Response.status(200).entity(msg).build();
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    // Centraliza o tratamento de erros para não repetires if/elses
    private Response tratarExcecao(Exception e) {
        String erro = e.getMessage();
        if (erro.startsWith("401")) return Response.status(401).entity(erro.substring(5)).build();
        if (erro.startsWith("403")) return Response.status(403).entity(erro.substring(5)).build();
        if (erro.startsWith("404")) return Response.status(404).entity(erro.substring(5)).build();

        return Response.status(400).entity(erro).build();
    }
}
