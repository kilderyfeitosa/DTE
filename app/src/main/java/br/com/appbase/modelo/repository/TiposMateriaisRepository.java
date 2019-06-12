package br.com.appbase.modelo.repository;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.appbase.dominio.model.TipoMaterial;

public class TiposMateriaisRepository {

    private DatabaseReference databaseReference;
    private ValueEventListener tiposMateriaisValueEventListener;

    public TiposMateriaisRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("tipos_materiais");
    }

    public void buscarTodos(final FirebaseDatabaseListCallback<TipoMaterial> firebaseCallback) {
        tiposMateriaisValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TipoMaterial> tipoMateriais = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TipoMaterial tipoMaterial = snapshot.getValue(TipoMaterial.class);
                    tipoMateriais.add(tipoMaterial);
                }
                firebaseCallback.onSuccess(tipoMateriais);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseCallback.onError(databaseError.toException());
            }
        };

        databaseReference.addValueEventListener(tiposMateriaisValueEventListener);
    }

    public void removeListener() {
        if (tiposMateriaisValueEventListener != null) {
            databaseReference.removeEventListener(tiposMateriaisValueEventListener);
        }
    }

    public interface FirebaseDatabaseListCallback<T> {
        void onSuccess(List<T> result);

        void onError(Exception e);
    }

}
