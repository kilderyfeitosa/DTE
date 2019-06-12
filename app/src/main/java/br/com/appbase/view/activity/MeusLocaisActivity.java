package br.com.appbase.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import br.com.appbase.R;
import br.com.appbase.dominio.model.Local;
import br.com.appbase.view.adapter.LocaisAdapter;
import br.com.appbase.view.viewmodel.BuscaLocalViewModel;

public class MeusLocaisActivity extends AppCompatActivity {

    private DrawerLayout navigationDrawer;
    private Toolbar toolbar;

    private MaterialSearchView searchViewBuscarLocais;

    private RecyclerView recyclerViewLocais;

    private BuscaLocalViewModel buscaLocalViewModel;

    private List<Local> locaisList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_locais);

        configurarToolbar();
        configurarDrawerLayout();
        configurarSearchViewBuscarLocais();
        configurarRecyclerViewLocais();

        buscaLocalViewModel = ViewModelProviders.of(this).get(BuscaLocalViewModel.class);
        buscaLocalViewModel.buscarTodosMeusLocais();

        observarLocais();
    }

    private void configurarToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MeusLocaisActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void configurarDrawerLayout() {
        navigationDrawer = findViewById(R.id.drawer_layout);
    }

    private void configurarSearchViewBuscarLocais() {
        searchViewBuscarLocais = findViewById(R.id.search_view);
        searchViewBuscarLocais.setHint(getString(R.string.digite_um_nome_ou_material));
        searchViewBuscarLocais.setHintTextColor(R.color.black_text_color);

        searchViewBuscarLocais.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                buscaLocalViewModel.buscarTodosMeusLocais();
            }
        });

        searchViewBuscarLocais.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String textoInformado) {
                if (textoInformado.isEmpty()){
                    buscaLocalViewModel.buscarTodosMeusLocais();
                } else {
                    List<Local> locaisFiltrados = buscaLocalViewModel.buscarPorNomeOuMaterial(locaisList, textoInformado);
                    popularRecyclerView(locaisFiltrados);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String textoInformado) {
                if (textoInformado.isEmpty()){
                    buscaLocalViewModel.buscarTodosMeusLocais();
                } else {
                    List<Local> locaisFiltrados = buscaLocalViewModel.buscarPorNomeOuMaterial(locaisList, textoInformado);
                    popularRecyclerView(locaisFiltrados);
                }

                return true;
            }
        });


    }

    private void configurarRecyclerViewLocais() {
        recyclerViewLocais = findViewById(R.id.locais_recyclerView);
        recyclerViewLocais.setHasFixedSize(true);
        recyclerViewLocais.setLayoutManager(new LinearLayoutManager(this));
    }

    private void observarLocais() {
        buscaLocalViewModel.meusLocais.observe(this, new Observer<List<Local>>() {
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

    private void popularRecyclerView(List<Local> locais) {
        recyclerViewLocais.setAdapter(new LocaisAdapter(locais, MeusLocaisActivity.this));
    }

    private void configurarOpcoesDoMenuLateral(NavigationView navigationView) {
        FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioLogado != null) { //TEM USUÁRIO LOGADO
            MenuItem itemEntrar = navigationView.getMenu().findItem(R.id.nav_entrar);
            itemEntrar.setVisible(false);

        } else { //NÃO TEM USUÁRIO LOGADO
            MenuItem itemMeusLocais = navigationView.getMenu().findItem(R.id.nav_meus_locais);
            itemMeusLocais.setVisible(false);

            MenuItem itemCadastrarLocal = navigationView.getMenu().findItem(R.id.nav_cadastrar_locais);
            itemCadastrarLocal.setVisible(false);

            MenuItem itemSair = navigationView.getMenu().findItem(R.id.nav_sair);
            itemSair.setVisible(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_buscar_locais);
        searchViewBuscarLocais.setMenuItem(menuItem);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (searchViewBuscarLocais != null && searchViewBuscarLocais.isSearchOpen()) {
            searchViewBuscarLocais.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
