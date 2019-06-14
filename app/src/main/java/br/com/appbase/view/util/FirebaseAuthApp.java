package br.com.appbase.view.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthApp {
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseUser getUsuarioLogado() {
        return mAuth.getInstance().getCurrentUser();
    }

}
