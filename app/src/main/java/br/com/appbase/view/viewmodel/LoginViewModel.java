package br.com.appbase.view.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import br.com.appbase.dominio.model.Usuario;
import br.com.appbase.modelo.repository.UsuarioRepository;

public class LoginViewModel extends ViewModel {

    private UsuarioRepository usuarioRepository = new UsuarioRepository();
    public MutableLiveData<Boolean> loginRealizado = new MutableLiveData<>();

    public void fazerLogin(final String email, String senha){
        usuarioRepository.fazerLogin(email, senha, new UsuarioRepository.FirebaseLoginUserCallback() {
            @Override
            public void onSuccess(Boolean result) {
                loginRealizado.setValue(true);
            }

            @Override
            public void onError(Exception e) {
                loginRealizado.setValue(false);
            }
        });
    }

}
