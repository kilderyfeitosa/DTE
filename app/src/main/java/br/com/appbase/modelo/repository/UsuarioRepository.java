package br.com.appbase.modelo.repository;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.appbase.dominio.model.Usuario;

public class UsuarioRepository {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();



    public UsuarioRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios");
    }

    public void fazerLogin(final String email, final String senha, final FirebaseLoginUserCallback loginUserCallback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginUserCallback.onSuccess(true);
                        } else {
                            loginUserCallback.onError(task.getException());
                        }
                    }
                });

    }

    public void cadastrarUsuario(final Usuario usuario, final FirebaseRegisterUserCallback registerUserCallback) {

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //CRIAR NOVO USUÁRIO NO FIREBASE
        mAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //SALVAR DADOS DO USUARIO NO BANCO DE DADOS (SOMENTE E-MAIL)
                            String userid = mAuth.getCurrentUser().getUid();
                            if (userid != null) {
                                Usuario novoUsuario = new Usuario(usuario.getEmail(), null);
                                novoUsuario.setUserKey(mAuth.getCurrentUser().getUid());
                                novoUsuario.setImgURL("default");
                                novoUsuario.setNome(usuario.getNome());
                                databaseReference.child(userid).setValue(novoUsuario)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                registerUserCallback.onSuccess(true);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        registerUserCallback.onError(e);
                                    }
                                });
                            }
                        } else {
                            registerUserCallback.onError(task.getException());
                        }

                    }
                });

    }

    public interface FirebaseRegisterUserCallback {
        void onSuccess(Boolean result);

        void onError(Exception e);
    }

    public interface FirebaseLoginUserCallback {
        void onSuccess(Boolean result);

        void onError(Exception e);
    }
}
