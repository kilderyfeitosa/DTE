package br.com.appbase.view.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import br.com.appbase.dominio.model.Usuario;
import br.com.appbase.modelo.repository.UsuarioRepository;

public class CadastroUsuarioViewModel extends ViewModel {

    private UsuarioRepository usuarioRepository = new UsuarioRepository();
    public MutableLiveData<Boolean> usuarioCadastrado = new MutableLiveData<>();

    public void cadastrarUsuario(final Usuario usuario){
        usuarioRepository.cadastrarUsuario(usuario, new UsuarioRepository.FirebaseRegisterUserCallback() {
            @Override
            public void onSuccess(Boolean result) {
                usuarioCadastrado.setValue(true);
            }

            @Override
            public void onError(Exception e) {
                usuarioCadastrado.setValue(false);
            }
        });
    }

}
