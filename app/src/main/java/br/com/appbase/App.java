package br.com.appbase;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //INIT PERSISTENCIA OFF-LINE FIREBASE
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
