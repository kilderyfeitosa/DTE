package br.com.appbase.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.com.appbase.R;
import br.com.appbase.view.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private TextView email;
    private TextView senha;
    private Button btFazerLogin;
    private ProgressBar progressBar;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        configurarViews();

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        observarLoginRealizado();
    }

    private void configurarViews() {
        email = findViewById(R.id.login_email);
        senha = findViewById(R.id.login_senha);

        btFazerLogin = findViewById(R.id.bt_fazer_login);
        btFazerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fazerLogin();
            }
        });

        TextView btIrParaCadastro = findViewById(R.id.bt_ir_para_cadastro);
        btIrParaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaATelaDeCadastro();
            }
        });

        progressBar = findViewById(R.id.progress_login);
    }

    private void fazerLogin() {
        String emailLogin = email.getText().toString().trim();
        String senhaLogin = senha.getText().toString().trim();

        if (TextUtils.isEmpty(emailLogin) || TextUtils.isEmpty(senhaLogin)) {
            Toast.makeText(this, R.string.informe_seu_email_e_senha, Toast.LENGTH_SHORT).show();
        } else {
            configurarViewsRealizandoLogin(true);
            loginViewModel.fazerLogin(emailLogin, senhaLogin);
        }
    }

    private void observarLoginRealizado() {
        loginViewModel.loginRealizado.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loginRelizado) {
                if (loginRelizado != null) {
                    configurarViewsRealizandoLogin(false);
                    if (loginRelizado) {
                        Toast.makeText(LoginActivity.this, getString(R.string.bem_vindo_ao_aplicativo), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.falha_na_autenticacao),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void irParaATelaDeCadastro() {
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void irParaATelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void configurarViewsRealizandoLogin(Boolean fazendoLogin){
        if (fazendoLogin){
            progressBar.setVisibility(View.VISIBLE);
            btFazerLogin.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btFazerLogin.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        irParaATelaPrincipal();
    }
}
