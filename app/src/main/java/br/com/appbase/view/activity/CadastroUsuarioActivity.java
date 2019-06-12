package br.com.appbase.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.com.appbase.R;
import br.com.appbase.dominio.model.Usuario;
import br.com.appbase.view.viewmodel.CadastroUsuarioViewModel;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private TextView email;
    private TextView senha;
    private Button btCadastrar;
    private ProgressBar progressBar;

    private CadastroUsuarioViewModel cadastroUsuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        configurarToolbar();
        configurarViews();

        cadastroUsuarioViewModel = ViewModelProviders.of(this).get(CadastroUsuarioViewModel.class);

        observarCadastroRealizado();
    }

    private void configurarToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar_cadastro_usuario);
        mToolbar.setTitle(R.string.cadastrar_usuario);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaATelaDeLogin();
            }
        });
    }

    private void configurarViews() {
        email = findViewById(R.id.email_cadastro_usuario);
        senha = findViewById(R.id.senha_cadastro_usuario);

        btCadastrar = findViewById(R.id.bt_cadastrar_usuario);
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarUsuario();
            }
        });

        progressBar = findViewById(R.id.progress_cadastro_usuario);

    }

    private void cadastrarUsuario() {
        configurarViewsRealizandoCadastro(true);

        String emailUsuario = email.getText().toString().trim();
        String senhaUsuario = senha.getText().toString().trim();

        cadastroUsuarioViewModel.cadastrarUsuario(new Usuario(emailUsuario, senhaUsuario));
    }

    private void observarCadastroRealizado() {
        cadastroUsuarioViewModel.usuarioCadastrado.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean usuarioCadastrado) {
                if (usuarioCadastrado != null) {
                    if (usuarioCadastrado) {
                        Toast.makeText(CadastroUsuarioActivity.this, R.string.usuario_cadastrado_com_sucesso,
                                Toast.LENGTH_SHORT).show();

                        irParaATelaPrincipal();
                    } else {
                        configurarViewsRealizandoCadastro(false);
                    }
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        irParaATelaDeLogin();
    }

    private void irParaATelaDeLogin() {
        Intent i = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void irParaATelaPrincipal(){
        Intent i = new Intent(CadastroUsuarioActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void configurarViewsRealizandoCadastro(Boolean fazendoCadastro) {
        if (fazendoCadastro) {
            progressBar.setVisibility(View.VISIBLE);
            btCadastrar.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            btCadastrar.setVisibility(View.VISIBLE);
        }
    }

}
