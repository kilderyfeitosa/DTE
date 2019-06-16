package br.com.appbase.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.appbase.R;
import br.com.appbase.dominio.model.Local;
import br.com.appbase.dominio.model.TipoMaterial;
import br.com.appbase.dominio.model.Usuario;
import br.com.appbase.view.adapter.MaterialAceitoAdapter;
import br.com.appbase.view.adapter.UserAdapter;
import br.com.appbase.view.util.FirebaseAuthApp;

public class DetalhesLocalActivity extends AppCompatActivity {
    Intent intent;
    Local local;
    Usuario usuario;
    String[] materiais;

    private RecyclerView recyclerView;
    private MaterialAceitoAdapter materialAceitoAdapter;

    private List<TipoMaterial> mTipoMaterials;

    TextView nome;
    TextView nome_usuario;

    Button btnSelecionar;
    List<TipoMaterial> tiposMateriais = new ArrayList<>();

    FirebaseUser fuser;
    DatabaseReference reference;

    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_local);


        intent = getIntent();
        local = intent.getParcelableExtra("local");
//        materiais = intent.getSerializableExtra("materias");

//        tiposMateriais.addAll(local.getMateriaisAceitos());

        listItems = getResources().getStringArray(R.array.shopping_item);
        checkedItems = new boolean[listItems.length];

        nome = findViewById(R.id.nome_local);
        nome_usuario = findViewById(R.id.nome_usuario);
        btnSelecionar = findViewById(R.id.btn_selecionar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetalhesLocalActivity.this));

        //Layout Grid - o "3" é o número de colunas
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DetalhesLocalActivity.this, 3);

        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerViewProdutos.setHasFixedSize(true);
//        recyclerViewProdutos.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        nome.setText(local.getNome());

        reference = FirebaseDatabase.getInstance().getReference("usuarios").child(local.getUserKey());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                nome_usuario.setText(usuario.getNome());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        reference = FirebaseDatabase.getInstance().getReference("locais").child(local.getLocalKey());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Local l = dataSnapshot.getValue(Local.class);
                    local.setMateriaisAceitos(l.getMateriaisAceitos());
//                mTipoMaterials.clear();
//                mTipoMaterials.addAll(l.getMateriaisAceitos());

                materialAceitoAdapter = new MaterialAceitoAdapter(DetalhesLocalActivity.this, local.getMateriaisAceitos());
                recyclerView.setAdapter(materialAceitoAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



                btnSelecionar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (FirebaseAuthApp.getUsuarioLogado() == null) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(DetalhesLocalActivity.this);
                            builder.setTitle("CADASTRE-SE")
                                    .setMessage(R.string.txt_continue_cadastro)
                                    .setPositiveButton(R.string.txt_cont, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent abrirCadastro = new Intent(getBaseContext(), CadastroUsuarioActivity.class);
                                            startActivity(abrirCadastro);
                                        }
                                    })
                                    .setNegativeButton(R.string.txt_canc, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                        }
                                    });
                            AlertDialog mDialog = builder.create();
                            mDialog.show();

                        } else {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetalhesLocalActivity.this);
                            mBuilder.setTitle(R.string.txt_selecione_descarte);
                            mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                    if (isChecked) {
                                        if (!mUserItems.contains(position)) {
                                            mUserItems.add(position);
                                        }
                                    } else if (mUserItems.contains(position)) {
                                        mUserItems.remove(position);
                                    }
                                }
                            });

                            mBuilder.setCancelable(false);
                            mBuilder.setPositiveButton(R.string.descartar_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String item = "";
                                    for (int i = 0; i < mUserItems.size(); i++) {
                                        item += listItems[mUserItems.get(i)];
                                        if (i != mUserItems.size() - 1) {
                                            item += "; \n";
                                        } else {
                                            item += ". \n\n";
                                        }
                                    }

                                    Intent abrirMessage = new Intent(DetalhesLocalActivity.this, MessageActivity.class);
                                    abrirMessage.putExtra("userkey", local.getUserKey());
                                    abrirMessage.putExtra("local", local);
                                    abrirMessage.putExtra("msg", "Olá, desejo descartar: \n\n" + item + "Está disponível?");
                                    startActivity(abrirMessage);

                                }
                            });

                            mBuilder.setNegativeButton(R.string.cancelar_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

//                mBuilder.setNeutralButton(R.string.limpar_selecao, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        for(int i=0; i < checkedItems.length; i++){
//                            checkedItems[i] = false;
//                            mUserItems.clear();
//                            nome.setText("");
//                        }
//                    }
//                });

                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();
                        }
                    }
                });

    }


}
