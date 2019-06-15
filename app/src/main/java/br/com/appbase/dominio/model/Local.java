package br.com.appbase.dominio.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Local implements Parcelable {
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

    private Local(Parcel in) {
        nome = in.readString();
    }

    public static final Parcelable.Creator<Local> CREATOR = new Parcelable.Creator<Local>() {
        @Override
        public Local createFromParcel(Parcel in) {
            return new Local(in);
        }

        @Override
        public Local[] newArray(int size) {
            return new Local[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nome);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Local local1 = (Local) o;
        return Objects.equals(nome, local1.nome) &&
                Objects.equals(local, local1.local) &&
                Objects.equals(contato, local1.contato) &&
                Objects.equals(userKey, local1.userKey) &&
                Objects.equals(materiaisAceitos, local1.materiaisAceitos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, local, contato, userKey, materiaisAceitos);
    }
}
