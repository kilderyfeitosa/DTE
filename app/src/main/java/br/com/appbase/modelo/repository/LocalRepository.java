package br.com.appbase.modelo.repository;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.appbase.dominio.model.Local;
import br.com.appbase.dominio.model.TipoMaterial;
import br.com.appbase.view.util.FirebaseAuthApp;

public class LocalRepository {

    private DatabaseReference databaseReference;
    private ValueEventListener localValueEventListener;
    private ValueEventListener meuLocalValueEventListener;
    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

    public LocalRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("locais");
    }

    public void buscarLocais(final FirebaseDatabaseListCallback<Local> firebaseCallback) {
        localValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Local> locais = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Local local = snapshot.getValue(Local.class);
                    if(FirebaseAuthApp.getUsuarioLogado() != null){
                        if(!local.getUserKey().equals(mAuth.getUid()))
                            locais.add(local);
                    } else {
                        locais.add(local);
                    }

                }
                firebaseCallback.onSuccess(locais);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseCallback.onError(databaseError.toException());
            }
        };

        databaseReference.addValueEventListener(localValueEventListener);
    }

    public void buscarMeusLocais(final FirebaseDatabaseListCallback<Local> firebaseCallback) {
        meuLocalValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Local> locais = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Local local = snapshot.getValue(Local.class);
                    if(local.getUserKey().equals(mAuth.getUid()))
                        locais.add(local);
                }
                firebaseCallback.onSuccess(locais);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseCallback.onError(databaseError.toException());
            }
        };

        databaseReference.addValueEventListener(meuLocalValueEventListener);
    }

    public void salvarLocal(final Local local, final FirebaseDatabasePersistenceCallback firebaseCallback) {

        final String localKey = databaseReference.push().getKey();
        if (localKey != null) {
            databaseReference.child(localKey).setValue(local).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    //SALVAR MATERIAIS ACEITOS
                   /* for (TipoMaterial material : local.getMateriaisAceitos()) {
                        final String materiaisKey = databaseReference.child(localKey).push().getKey();
                        if (materiaisKey != null) {
                            databaseReference.child(localKey).child("materiais_aceitos").child(materiaisKey).setValue(material);
                        }
                    }*/

                    firebaseCallback.onSuccess(true);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    firebaseCallback.onError(e);
                }
            });
        }
    }

    public void removeListener() {
        if (localValueEventListener != null) {
            databaseReference.removeEventListener(localValueEventListener);
        }
    }

    public interface FirebaseDatabasePersistenceCallback {
        void onSuccess(Boolean result);

        void onError(Exception e);
    }

    public interface FirebaseDatabaseListCallback<T> {
        void onSuccess(List<T> result);

        void onError(Exception e);
    }

}
