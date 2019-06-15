package br.com.appbase.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import br.com.appbase.R;
import br.com.appbase.dominio.model.Local;
import br.com.appbase.dominio.model.TipoMaterial;
import br.com.appbase.view.activity.DetalhesLocalActivity;
import br.com.appbase.view.activity.MensagemActivity;
import br.com.appbase.view.util.FirebaseAuthApp;

public class LocaisAdapter extends RecyclerView.Adapter<LocaisAdapter.ViewHolderOpcoes> {

    private List<Local> locais;
    private Context context;
    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

    public LocaisAdapter(List<Local> locais, Context context) {
        this.locais = locais;
        this.context = context;
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public ViewHolderOpcoes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_item, parent, false);
        return new ViewHolderOpcoes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderOpcoes holder, int position) {
        final Local local = locais.get(position);

        holder.setNomeLocal(local.getNome());
        holder.setLocalLocal(local.getLocal());
        holder.setMateriaisAceitos(local.getMateriaisAceitos());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuthApp.getUsuarioLogado() != null){
                    Toast.makeText(context, "Abriu o " + local.getNome(), Toast.LENGTH_LONG).show();

                    Intent abrirChat = new Intent(context, DetalhesLocalActivity.class);
                    abrirChat.putExtra("local", local);
                    context.startActivity(abrirChat);
                } else {
                    Toast.makeText(context, "Cadastre-se para prosseguir!", Toast.LENGTH_LONG);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return locais.size();
    }

    class ViewHolderOpcoes extends RecyclerView.ViewHolder implements View.OnClickListener {

        View item;

        private ViewHolderOpcoes(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            item = itemView;
        }

        private void setNomeLocal(String nomeLocal) {
            TextView item_nome = item.findViewById(R.id.nome_local_item);
            item_nome.setText(nomeLocal);
        }

        private void setLocalLocal(String localLocal) {
            TextView item_endereco = item.findViewById(R.id.endereco_local_item);
            item_endereco.setText(new StringBuilder().append("Endereço: ").append(localLocal));
        }

        private void setMateriaisAceitos(List<TipoMaterial> materiaisAceitos) {
            TextView item_materiais = item.findViewById(R.id.materiais_local_item);

            if (!materiaisAceitos.isEmpty()) {
                item_materiais.setVisibility(View.VISIBLE);

                StringBuilder materiais = new StringBuilder();
                materiais.append("Materiais aceitos: ");
                for (TipoMaterial materialAceito : materiaisAceitos) {
                    materiais.append(materialAceito.getDescricao());
                    materiais.append(", ");
                }

                item_materiais.setText(materiais.deleteCharAt(materiais.length() - 2).toString()); //Remove virgula do Final
            } else {
                item_materiais.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            Log.d("CLICK", "Elemento " + getAdapterPosition() + " clicado.");
        }
    }

}