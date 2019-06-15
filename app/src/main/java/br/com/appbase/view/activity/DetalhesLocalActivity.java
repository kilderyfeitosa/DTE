package br.com.appbase.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.appbase.R;
import br.com.appbase.dominio.model.Local;

public class DetalhesLocalActivity extends AppCompatActivity {
    Intent intent;
    Local local;

    TextView nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_local);

        intent = getIntent();
        local = intent.getParcelableExtra("local");

        nome = findViewById(R.id.nome_local);

        nome.setText(local.getNome());

    }
}
