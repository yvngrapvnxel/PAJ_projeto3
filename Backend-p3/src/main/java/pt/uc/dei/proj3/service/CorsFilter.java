package pt.uc.dei.proj3.service;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        // Permite pedidos de qualquer origem (necessário para o fetch do JS funcionar)
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");

        // Permite os métodos que vamos usar
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");

        // Permite os Headers customizados (incluindo username e password que o enunciado pede)
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, username, token");
    }
}