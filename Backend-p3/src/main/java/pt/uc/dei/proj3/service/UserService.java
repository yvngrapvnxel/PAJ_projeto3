package pt.uc.dei.proj3.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.uc.dei.proj3.beans.UserBean;
import pt.uc.dei.proj3.dto.UserDto;

@Path("/users")
public class UserService {

    @Inject
    private UserBean userBean;

    //add metodo validacao token
    //add metodo get id pelo token

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserDto user) {
        // Validação básica de campos vazios
        if (user.getUsername() == null || user.getPassword() == null) {
            return Response.status(401).entity("Dados incompletos").build();
        }

        // Chama o novo método que retorna o token
        String token = userBean.loginToken(user.getUsername(), user.getPassword());

        if (token != null) {
            // Devolve o token ao utilizador em formato JSON
            return Response.status(200).entity("{\"token\": \"" + token + "\"}").build();
        }

        // Se a autenticação falhar [cite: 115]
        return Response.status(401).entity("Wrong Username or Password!").build();
    }

    @POST
    @Path("/logout")
    public Response logout() {
        // endpoint e retorna 200 Success .
        return Response.status(200).build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UserDto newUser) {
        // O registo deve retornar 201 em caso de sucesso [cite: 139]
        boolean success = userBean.register(newUser);

        if (success) {
            return Response.status(201).entity("User registered successfully").build();
        }

        return Response.status(400).entity("Registration failed").build(); // [cite: 141]
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfile(@PathParam("username") String username) {
        UserDto user = userBean.findUser(username); // Você pode criar este atalho no UserBean

        if (user == null) {
            return Response.status(400).entity("User not found").build(); // [cite: 169]
        }

        return Response.status(200).entity(user).build(); // [cite: 168]
    }

    @POST // Nota: Pelas boas práticas RESTful[cite: 129], para atualizações deves considerar usar @PUT em vez de @POST
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(
            @PathParam("username") String userAAlterar, // O nome que vem no URL
            @HeaderParam("username") String userAuth,   // O nome que vem no Header
            @HeaderParam("token") String tokenAuth,     // O token que vem no Header (substituiu a password)
            UserDto dadosNovos) {

        // PASSO 1: Verificar se os Headers existem (ERRO 401)
        if (userAuth == null || tokenAuth == null || userAuth.trim().isEmpty() || tokenAuth.trim().isEmpty()) {
            // Retorna 401 se os dados do Header (token) não forem enviados
            return Response.status(401).entity("Faltam credenciais no Header (username ou token)").build();
        }

        // PASSO 2: Verificar se o token é válido (ERRO 403)
        if (!userBean.validarToken(userAuth, tokenAuth)) {
            // Retorna 403 se a autenticação (verificação do token) falhar
            return Response.status(403).entity("Credenciais (Token) inválidas").build();
        }

        // PASSO 3: Verificar se o utilizador está a tentar alterar o seu próprio perfil (ERRO 403)
        if (!userAuth.equals(userAAlterar)) {
            // Retorna 403 porque um utilizador não pode alterar o perfil de outro
            return Response.status(403).entity("Não podes alterar dados de outros utilizadores").build();
        }

        // Se passar tudo, então grava...
        userBean.updateUser(userAAlterar, dadosNovos);
        return Response.status(200).entity("Perfil atualizado com sucesso!").build();
    }

}
