package pt.uc.dei.proj3.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.uc.dei.proj3.beans.ClientBean;
import pt.uc.dei.proj3.beans.UserBean;
import pt.uc.dei.proj3.dto.ClientDto;
import pt.uc.dei.proj3.dto.UserDto;

import java.util.List;

@Path("/clients")
public class ClientService {

    @Inject
    private ClientBean clientBean;

    @Inject
    private UserBean userBean;

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCliente(@HeaderParam("token") String token, ClientDto dto) {

        UserDto user = userBean.getUserByToken(token);

        // 1. Verificação de Autenticação (como fazes no login/register)
        // 1. Usar o novo método validarToken em vez do login
        if (user == null || token == null ) {
            return Response.status(401).entity("Acesso negado - Token inválido ou ausente").build(); // Retorna 401 conforme o enunciado
        }

        // 2. Validação básica de campos (Nome e Empresa obrigatórios + pelo menos UM contacto)
        boolean semNome = dto.getNome() == null || dto.getNome().trim().isEmpty();
        boolean semEmpresa = dto.getEmpresa() == null || dto.getEmpresa().trim().isEmpty();
        boolean semEmail = dto.getEmail() == null || dto.getEmail().trim().isEmpty();
        boolean semTelefone = dto.getTelefone() == null || dto.getTelefone().trim().isEmpty();

        if (semNome || semEmpresa || (semEmail && semTelefone)) {
            return Response.status(400).entity("Dados incompletos: Nome, Empresa e pelo menos um contacto (Email ou Telefone) são obrigatórios.").build();
        }

        try {
            // 2. Tenta registar (o Bean vai validar duplicados Nome+Empresa e gerar o ID)
            ClientDto novo = clientBean.registarCliente(dto, user.getUsername());

            // Retorna 201 Created com o objeto criado
            return Response.status(201).entity(novo).build();

        } catch (Exception e) {
            // 3. Se o Bean lançar exceção (ex: cliente já existe), retorna 409 Conflict
            return Response.status(409).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientes(@HeaderParam("token") String token) {

        UserDto user = userBean.getUserByToken(token);

        // 1. Verificação de Autenticação (como fazes no login/register)
        if (user == null || token == null ) {
            return Response.status(401).entity("Acesso negado - Token inválido ou ausente").build(); // Retorna 401 conforme o enunciado
        }

        // Retorna a lista de clientes do utilizador logado
        List<ClientDto> clientes = clientBean.listClients(user.getUsername());

        return Response.status(200).entity(clientes).build();
    }

    @PATCH
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editClient(@HeaderParam("token") String token, ClientDto dto) {

        UserDto user = userBean.getUserByToken(token);

        // 1. Verificação de Autenticação (como fazes no login/register)
        if (user == null || token == null ) {
            return Response.status(401).entity("Acesso negado - Token inválido ou ausente").build(); // Retorna 401 conforme o enunciado
        }

        // 2. Validação básica de campos (Nome e Empresa obrigatórios + pelo menos UM contacto)
        boolean semNome = dto.getNome() == null || dto.getNome().trim().isEmpty();
        boolean semEmpresa = dto.getEmpresa() == null || dto.getEmpresa().trim().isEmpty();
        boolean semEmail = dto.getEmail() == null || dto.getEmail().trim().isEmpty();
        boolean semTelefone = dto.getTelefone() == null || dto.getTelefone().trim().isEmpty();

        if (semNome || semEmpresa || (semEmail && semTelefone)) {
            return Response.status(400).entity("Dados incompletos: Nome, Empresa e pelo menos um contacto (Email ou Telefone) são obrigatórios.").build();
        }

        try {
            clientBean.editarCliente(dto.getId(), dto);
            return Response.status(200).entity("Cliente atualizado com sucesso").build();
        } catch (Exception e) {
            return Response.status(409).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/remove")
    public Response removeClient(@HeaderParam("token") String token, ClientDto dto) {

        UserDto user = userBean.getUserByToken(token);

        // Verificação de segurança básica
        // 1. Usar o novo método validarToken em vez do login
        if (user == null || token == null ) {
            return Response.status(401).entity("Acesso negado - Token inválido ou ausente").build(); // Retorna 401 conforme o enunciado
        }

        boolean sucess = clientBean.deletClient(dto.getId());

        if (sucess) {
            return Response.status(200).entity("Cliente removido com sucesso").build();
        } else {
            return Response.status(404).entity("Cliente não encontrado com o ID: " + dto.getId()).build();
        }
    }
}