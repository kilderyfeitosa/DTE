package br.com.appbase.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import br.com.appbase.R;
import br.com.appbase.dominio.model.Local;
import br.com.appbase.dominio.model.Usuario;
import br.com.appbase.view.activity.MainActivity;
import br.com.appbase.view.adapter.LocaisAdapter;
import br.com.appbase.view.adapter.UserAdapter;
import br.com.appbase.view.util.FirebaseAuthApp;
import br.com.appbase.view.viewmodel.BuscaLocalViewModel;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Usuario> mUsers;

    private MaterialSearchView searchViewBuscarLocais;

    private BuscaLocalViewModel buscaLocalViewModel;
    private List<Local> locaisList;

    private RecyclerView recyclerViewLocais;
    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

//        if(FirebaseAuthApp.getUsuarioLogado()!=null)
//            readUsers();

        buscaLocalViewModel = ViewModelProviders.of(this).get(BuscaLocalViewModel.class);
        buscaLocalViewModel.buscarTodosLocais();

//        configurarSearchViewBuscarLocais();
        configurarRecyclerViewLocais();


        observarLocais();

        return view;
    }

    private void configurarRecyclerViewLocais() {
        recyclerViewLocais = view.findViewById(R.id.recycler_view);
        recyclerViewLocais.setHasFixedSize(true);
        recyclerViewLocais.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void popularRecyclerView(List<Local> locais) {
        recyclerViewLocais.setAdapter(new LocaisAdapter(locais, view.getContext()));
    }

    private void observarLocais() {
        buscaLocalViewModel.locais.observe(this, new Observer<List<Local>>() {
            @Override
            public void onChanged(@Nullable List<Local> locais) {
                if (locais != null) {
                    locaisList = new ArrayList<>();
                    locaisList = locais;

                    popularRecyclerView(locais);
                }
            }
        });
    }

//    private void readUsers(){
//        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuarios");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUsers.clear();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Usuario user = snapshot.getValue(Usuario.class);
//
//                    if(!snapshot.getKey().equals(firebaseUser.getUid())){
//                        mUsers.add(user);
//                    }
//                }
//
////                userAdapter = new UserAdapter(getContext(), mUsers,);
////                recyclerView.setAdapter(userAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void configurarSearchViewBuscarLocais() {
        searchViewBuscarLocais = view.findViewById(R.id.search_view);
        searchViewBuscarLocais.setHint(getString(R.string.digite_um_nome_ou_material));
        searchViewBuscarLocais.setHintTextColor(R.color.black_text_color);

        searchViewBuscarLocais.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                buscaLocalViewModel.buscarTodosLocais();
            }
        });

        searchViewBuscarLocais.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String textoInformado) {
                if (textoInformado.isEmpty()){
                    buscaLocalViewModel.buscarTodosLocais();
                } else {
                    List<Local> locaisFiltrados = buscaLocalViewModel.buscarPorNomeOuMaterial(locaisList, textoInformado);
                    popularRecyclerView(locaisFiltrados);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String textoInformado) {
                if (textoInformado.isEmpty()){
                    buscaLocalViewModel.buscarTodosLocais();
                } else {
                    List<Local> locaisFiltrados = buscaLocalViewModel.buscarPorNomeOuMaterial(locaisList, textoInformado);
                    popularRecyclerView(locaisFiltrados);
                }

                return true;
            }
        });


    }

}
