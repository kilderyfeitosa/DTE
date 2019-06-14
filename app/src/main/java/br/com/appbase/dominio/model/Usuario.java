package br.com.appbase.dominio.model;

public class Usuario {
    private String email;
    private String senha;
    private String nome;
    private String imgURL;
    private String userKey;

    public Usuario(){

    }

    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public Usuario(String email, String nome, String imgURL, String userKey) {
        this.email = email;
        this.nome = nome;
        this.imgURL = imgURL;
        this.userKey = userKey;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome()  {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
