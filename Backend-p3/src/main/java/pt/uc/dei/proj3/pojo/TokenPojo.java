package pt.uc.dei.proj3.pojo;

import pt.uc.dei.proj3.entity.TokenEntity;

import java.time.LocalDateTime;

public class TokenPojo {

    private long userId;
    private String token;
    private LocalDateTime dataSessao;
    private LocalDateTime expireTime;
    private boolean isValido;


    public TokenPojo() {}

    public TokenPojo(long userId, String token, LocalDateTime dataSessao, boolean isValido) {
        this.userId = userId;
        this.token = token;
        this.dataSessao = dataSessao;
        this.expireTime = dataSessao.plusHours(1);
        this.isValido = isValido;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDataSessao() {
        return dataSessao;
    }

    public void setDataSessao(LocalDateTime dataSessao) {
        this.dataSessao = dataSessao;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isValido() {
        if (isValido && LocalDateTime.now().isAfter(expireTime)) isValido = false;
        return isValido;
    }

    public void setValido(boolean valido) {
        isValido = valido;
    }
}
