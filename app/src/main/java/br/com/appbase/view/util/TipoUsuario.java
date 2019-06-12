package br.com.appbase.view.util;

public enum TipoUsuario {

    EMPRESA("empresa"), CLIENTE("cliente");

    public String tipoPermissaoUsuario;

    TipoUsuario(String permissao) {
        tipoPermissaoUsuario = permissao;
    }
}
