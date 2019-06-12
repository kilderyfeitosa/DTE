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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout navigationDrawer;
    private Toolbar toolbar;

    private MaterialSearchView searchViewBuscarLocais;

    private RecyclerView recyclerViewLocais;

    private BuscaLocalViewModel buscaLocalViewModel;

    private List<Local> locaisList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configurarToolbar();
        configurarDrawerLayout();
        configurarSearchViewBuscarLocais();
        configurarRecyclerViewLocais();

        buscaLocalViewModel = ViewModelProviders.of(this).get(BuscaLocalViewModel.class);
        buscaLocalViewModel.buscarTodosLocais();

        observarLocais();
    }

    private void configurarToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void configurarDrawerLayout() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationDrawer = findViewById(R.id.drawer_layout);

        configurarOpcoesDoMenuLateral(navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navigationDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navigationDrawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
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

    private void configurarRecyclerViewLocais() {
        recyclerViewLocais = findViewById(R.id.locais_recyclerView);
        recyclerViewLocais.setHasFixedSize(true);
        recyclerViewLocais.setLayoutManager(new LinearLayoutManager(this));
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

    private void popularRecyclerView(List<Local> locais) {
        recyclerViewLocais.setAdapter(new LocaisAdapter(locais, MainActivity.this));
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                if (!menuItem.isChecked()) {
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
            case R.id.nav_cadastrar_locais:
                Intent cadastrarUmLocalIntent = new Intent(this, CadastroLocalActivity.class);
                startActivity(cadastrarUmLocalIntent);
                finish();
                break;
            case R.id.nav_meus_locais:
                Intent meusLocaisIntent = new Intent(this, MeusLocaisActivity.class);
                startActivity(meusLocaisIntent);
                break;
            case R.id.nav_sobre:
                Toast.makeText(this, "A Fazer", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_entrar:
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
            case R.id.nav_sair:
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(this, "Logout Realizado", Toast.LENGTH_SHORT).show();
                Intent sairIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(sairIntent);
                finish();

                break;
            default:
                break;
        }

        navigationDrawer.closeDrawer(GravityCompat.START);

        return true;
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
