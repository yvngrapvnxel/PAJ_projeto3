package pt.uc.dei.proj3.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.uc.dei.proj3.beans.TokenBean;
import pt.uc.dei.proj3.beans.UserBean;
import pt.uc.dei.proj3.dto.TokenDto;
import pt.uc.dei.proj3.dto.UserDto;

import java.util.Collections;

@Path("/users")
public class UserService {

    @Inject
    UserBean userBean;

    @Inject
    TokenBean tokenBean;

    //add metodo validacao token
    //add metodo get id pelo token

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserDto user) {
        // Validação básica de campos vazios
        if (user.getUsername() == null || user.getPassword() == null) {
            return Response.status(401).entity("Dados incompletos!").build();
        }

        // Chama o novo método que retorna o token
        String token = userBean.loginToken(user.getUsername(), user.getPassword());

        if (token != null) {
            // Devolve o token ao utilizador em formato JSON
            return Response.status(200).entity(Collections.singletonMap("token", token)).build();
        }

        // Se a autenticação falhar [cite: 115]
        return Response.status(401).entity("Dados incorretos!").build();
    }


    @POST
    @Path("/logout")
    public Response logout() {
        // endpoint e retorna 200 Success
        return Response.status(200).build();
    }


    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UserDto newUser) {
        boolean success = userBean.register(newUser);

        if (!success) return Response.status(400).entity("Ocorreu um erro no registo de utilizador.").build(); // [cite: 141]

        // O registo deve retornar 201 em caso de sucesso [cite: 139]
        return Response.status(201).entity("Utilizador registado com sucesso!").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfile(@HeaderParam("token") String token) {

        UserDto user = userBean.getUserByToken(token);

        if (user == null) {
            return Response.status(400).entity("Utilizador não encontrado.").build(); // [cite: 169]
        }

        return Response.status(200).entity(user).build(); // [cite: 168]
    }

    @POST // Nota: Pelas boas práticas RESTful[cite: 129], para atualizações deves considerar usar @PUT em vez de @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(@HeaderParam("token") String token,
                                    UserDto dadosNovos) {

        // PASSO 1: Verificar se os Headers existem (ERRO 401)
        if (token == null || token.trim().isEmpty()) {
            // Retorna 401 se os dados do Header (token) não forem enviados
            return Response.status(401).entity("O seu token é inválido ou não existe.").build();
        }

        // Se passar tudo, então grava...
        boolean isUpdated = userBean.updateUser(token, dadosNovos);
        if (!isUpdated) return Response.status(403).entity("Ocorreu um erro ao atualizar o perfil.").build();
        return Response.status(200).entity("Perfil atualizado com sucesso!").build();
    }

}
