package br.com.appbase.dominio.model;

import java.util.ArrayList;
import java.util.List;

public class Local {
    private String nome;
    private String local;
    private String contato;
    private String userKey;

    private List<TipoMaterial> materiaisAceitos = new ArrayList<>();

    public Local() {
    }

    public Local(String nome, String local, String contato, List<TipoMaterial> materiaisAceitos, String userKey) {
        this.nome = nome;
        this.local = local;
        this.contato = contato;
        this.materiaisAceitos = materiaisAceitos;
        this.userKey = userKey;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public List<TipoMaterial> getMateriaisAceitos() {
        return materiaisAceitos;
    }

    public void setMateriaisAceitos(List<TipoMaterial> materiaisAceitos) {
        this.materiaisAceitos = materiaisAceitos;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
