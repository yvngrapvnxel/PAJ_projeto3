package pt.uc.dei.proj3.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.uc.dei.proj3.beans.UserBean;
import pt.uc.dei.proj3.dto.UserDto;
import pt.uc.dei.proj3.pojo.UserPojo;

@Path("/users")
public class UserService {

    @Inject
    private UserBean userBean;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserDto user) {
        // Validação básica de campos vazios
        if (user.getUsername() == null || user.getPassword() == null) {
            return Response.status(401).entity("Dados incompletos").build();
        }

        if (userBean.login(user.getUsername(), user.getPassword())) {
            return Response.status(200).entity("Login Successful!").build(); // [cite: 114]
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
        UserPojo user = userBean.findUser(username); // Você pode criar este atalho no UserBean

        if (user == null) {
            return Response.status(400).entity("User not found").build(); // [cite: 169]
        }

        return Response.status(200).entity(user).build(); // [cite: 168]
    }

    @POST
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(
            @PathParam("username") String userAAlterar, // O nome que vem no URL
            @HeaderParam("username") String userAuth,   // O nome que vem no Header
            @HeaderParam("password") String passAuth,   // A pass que vem no Header
            UserPojo dadosNovos) {

        // PASSO 1: Verificar se os Headers existem (ERRO 401)
        if (userAuth == null || passAuth == null || userAuth.isEmpty()) {
            return Response.status(401).entity("Faltam credenciais no Header").build();
        }

        // PASSO 2: Verificar se a senha está correta (ERRO 403)
        if (!userBean.login(userAuth, passAuth)) {
            return Response.status(403).entity("Credenciais inválidas").build();
        }

        // PASSO 3: Verificar se o "Joao" está a tentar mudar o perfil do "Joao" (ERRO 403)
        if (!userAuth.equals(userAAlterar)) {
            return Response.status(403).entity("Não podes alterar dados de outros utilizadores").build();
        }

        // Se passar tudo, então grava...
        userBean.updateUser(userAAlterar, dadosNovos);
        return Response.ok("Atualizado!").build();
    }

}
