package pt.uc.dei.proj3.beans;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pt.uc.dei.proj3.dao.UserDao;
import pt.uc.dei.proj3.dao.TokenDao;
import pt.uc.dei.proj3.dto.UserDto;
import pt.uc.dei.proj3.entity.TokenEntity;
import pt.uc.dei.proj3.entity.UserEntity;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;

// Passamos a @Stateless (EJB) para gerir as transações automaticamente com a Base de Dados
@Stateless
public class UserBean implements Serializable {

    @Inject
    UserDao userDao;

    @Inject
    TokenDao tokenDao;

    // 1. Função auxiliar para encriptar o token (SHA-256)
    private String encriptar(String textoPlano) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(textoPlano.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao encriptar token", e);
        }
    }

    public String loginToken(String username, String password) {
        // Vai buscar à BD
        UserEntity u = userDao.findUserByUsername(username);

        // Confirma se o user existe, se a password está correta e se está ativo
        if (u != null && u.getPassword().equals(password) && u.isAtivo()) {

            // 1. Gera o token limpo (para devolver no REST)
            String tokenLimpo = TokenBean.generateToken();

            // 2. Encripta o token para guardar na BD em segurança
            String tokenEncriptado = encriptar(tokenLimpo);

            // 3. Guarda o token na tabela 'token' associado a este utilizador
            TokenEntity tokenEntity = new TokenEntity();
            tokenEntity.setToken(tokenEncriptado); // A Primary Key é o token encriptado
            tokenEntity.setUserId(u);
            tokenEntity.setDataSessao(LocalDateTime.now());
            tokenEntity.setExpireTime(LocalDateTime.now().plusHours(1)); // Expira numa hora

            tokenDao.persist(tokenEntity); // Guarda na BD

            return tokenLimpo; // Retorna a versão não encriptada para o Frontend/Postman
        }
        return null;
    }

    public boolean validarToken(String username, String tokenLimpo) {
        if (username == null || tokenLimpo == null || username.trim().isEmpty() || tokenLimpo.trim().isEmpty()) return false;

        // Para validar, temos de encriptar o token limpo que o postman enviou
        String tokenRecebidoEncriptado = encriptar(tokenLimpo);

        UserEntity u = userDao.findUserByToken(tokenRecebidoEncriptado);

        return (u != null && u.getUsername().equals(username) && u.isAtivo());
    }

    public boolean register(UserDto newUser) {
        // Verifica se já existe alguém com este username na Base de Dados
        if (userDao.findUserByUsername(newUser.getUsername()) != null) {
            return false;
        }

        // Converte o DTO numa Entidade para ir para a BD
        UserEntity user = new UserEntity();
        user.setPrimeiroNome(newUser.getPrimeiroNome());
        user.setUltimoNome(newUser.getUltimoNome());
        user.setTelefone(newUser.getTelefone());
        user.setEmail(newUser.getEmail());
        user.setFotoUrl(newUser.getFotoUrl());
        user.setUsername(newUser.getUsername());
        user.setPassword(newUser.getPassword());
        user.setIsAtivo(true); // Fica logo ativo

        userDao.persist(user); // Guarda na BD
        return true;
    }

    public UserDto findUser(String username) {
        UserEntity entity = userDao.findUserByUsername(username);
        if (entity == null || !entity.isAtivo()) return null;

        return converterParaDto(entity);
    }

    public void updateUser(String username, UserDto newData) {
        UserEntity u = userDao.findUserByUsername(username);
        if (u == null || !u.isAtivo()) return;

        u.setPrimeiroNome(newData.getPrimeiroNome());
        u.setUltimoNome(newData.getUltimoNome());
        u.setEmail(newData.getEmail());
        u.setTelefone(newData.getTelefone());
        u.setFotoUrl(newData.getFotoUrl());

        if (newData.getPassword() != null && !newData.getPassword().trim().isEmpty()) {
            u.setPassword(newData.getPassword());
        }

        userDao.merge(u); // Faz o UPDATE na BD
    }

    // Função auxiliar para mapear de Entity (BD) para Pojo (Frontend)
    private UserDto converterParaDto(UserEntity e) {
        UserDto dto = new UserDto();
        dto.setId(e.getId().intValue());
        dto.setPrimeiroNome(e.getPrimeiroNome());
        dto.setUltimoNome(e.getUltimoNome());
        dto.setEmail(e.getEmail());
        dto.setTelefone(e.getTelefone());
        dto.setUsername(e.getUsername());
        dto.setFotoUrl(e.getFotoUrl());
        return dto;
    }
}