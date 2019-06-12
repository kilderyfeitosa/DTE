package br.com.appbase.dominio.model;

import com.google.firebase.database.Exclude;

public class TipoMaterial implements Comparable<TipoMaterial> {

    private String descricao;

    @Exclude //Ignora quando envia para o Firebase
    private boolean selecionado;

    public TipoMaterial() {
    }

    public TipoMaterial(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }

    @Override
    public int compareTo(TipoMaterial tipoMaterial) {
        return this.getDescricao().compareTo(tipoMaterial.getDescricao());
    }
}