package br.com.appbase.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.appbase.R;
import br.com.appbase.dominio.model.Local;
import br.com.appbase.dominio.model.TipoMaterial;
import br.com.appbase.view.adapter.TiposMateriaisAdapter;
import br.com.appbase.view.util.MascaraEditUtil;
import br.com.appbase.view.viewmodel.CadastroLocalViewModel;

public class CadastroLocalActivity extends AppCompatActivity {

    private CadastroLocalViewModel viewModel;

    private ConstraintLayout layoutCadastroLocal;
    private ProgressBar progressBarCadastrando;
    private ProgressBar progressBarCarregando;

    private RecyclerView tiposMateriaisRecyclerView;

    private Button btCadastroLocal;

    private List<TipoMaterial> tiposMateriais = new ArrayList<>();
    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_local);

        FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioLogado == null) { //CASO O USUÁRIO NÃO ESTEJA LOGADO - IR PARA O LOGIN
            Intent loginIntent = new Intent(CadastroLocalActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else {
            configurarToolbar();
            configurarViews();

            viewModel = ViewModelProviders.of(this).get(CadastroLocalViewModel.class);
            viewModel.buscarTiposMateriais();

            observarCarregando();
            observarTiposDeMateriais();
            observarLocalSalvo();
        }

    }

    private void observarCarregando() {
        viewModel.carregando.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean carregando) {
                if (carregando != null && carregando) {
                    layoutCadastroLocal.setVisibility(View.INVISIBLE);
                    progressBarCarregando.setVisibility(View.VISIBLE);
                } else {
                    progressBarCarregando.setVisibility(View.GONE);
                    layoutCadastroLocal.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void observarTiposDeMateriais() {
        viewModel.tiposMateriais.observe(this, new Observer<List<TipoMaterial>>() {
            @Override
            public void onChanged(@Nullable List<TipoMaterial> materiais) {
                if (materiais != null) {
                    Collections.sort(materiais);
                    tiposMateriais = materiais;
                    tiposMateriaisRecyclerView.setAdapter(
                            new TiposMateriaisAdapter(tiposMateriais));
                }
            }
        });
    }

    private void observarLocalSalvo() {
        viewModel.localSalvo.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean localSalvo) {
                if (localSalvo != null) {
                    if (localSalvo) {
                        Toast.makeText(CadastroLocalActivity.this, "Local cadastrado com sucesso!",
                                Toast.LENGTH_SHORT).show();

                        irParaATelaPrincipal();
                    } else {
                        configurarViewsCadastrando(false);
                    }
                }
            }
        });
    }

    private void irParaATelaPrincipal() {
        Intent i = new Intent(CadastroLocalActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void configurarToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar_cadastro_local);
        mToolbar.setTitle(R.string.cadastrar_local);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CadastroLocalActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void configurarViews() {
        layoutCadastroLocal = findViewById(R.id.layout_cadastro_local);

        progressBarCadastrando = findViewById(R.id.progress_cadastro_local);
        progressBarCarregando = findViewById(R.id.progress_carregando_cadastro_local);

        EditText contato = findViewById(R.id.local_contato_cadastro);
        contato.addTextChangedListener(MascaraEditUtil.mask(contato, MascaraEditUtil.FORMAT_FONE));

        btCadastroLocal = findViewById(R.id.bt_cadastrar_local);
        btCadastroLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarLocal();
            }
        });

        tiposMateriaisRecyclerView = findViewById(R.id.tipos_materiais_recycler);
        tiposMateriaisRecyclerView.setHasFixedSize(true);
        tiposMateriaisRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configurarViewsCadastrando(Boolean cadastrando) {
        if (cadastrando) {
            progressBarCadastrando.setVisibility(View.VISIBLE);
            btCadastroLocal.setVisibility(View.INVISIBLE);
        } else {
            progressBarCadastrando.setVisibility(View.GONE);
            btCadastroLocal.setVisibility(View.INVISIBLE);
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro_local, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.nav_cadastrar_local:
                salvarLocal();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void salvarLocal() {
        configurarViewsCadastrando(true);

        EditText nome = findViewById(R.id.nome_local_cadastro);
        EditText local = findViewById(R.id.local_local_cadastro);
        EditText contato = findViewById(R.id.local_contato_cadastro);


        List<TipoMaterial> materiaisAceitos = new ArrayList<>();
        for (TipoMaterial materialAceito : tiposMateriais) {
            if(materialAceito.isSelecionado()){
                materiaisAceitos.add(materialAceito);
            }
        }

        Local novoLocal = new Local(
                nome.getText().toString().trim(),
                local.getText().toString().trim(),
                contato.getText().toString().trim(),
                materiaisAceitos,
                mAuth.getUid()
        );

        viewModel.salvarLocal(novoLocal);
    }

}
