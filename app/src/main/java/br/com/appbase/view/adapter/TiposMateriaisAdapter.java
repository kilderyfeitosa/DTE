package br.com.appbase.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.appbase.R;
import br.com.appbase.dominio.model.TipoMaterial;

public class TiposMateriaisAdapter extends RecyclerView.Adapter<TiposMateriaisAdapter.ViewHolderOpcoes> {

    private List<TipoMaterial> tipoMateriais;

    public TiposMateriaisAdapter(List<TipoMaterial> tipoMateriais) {
        this.tipoMateriais = tipoMateriais;
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public ViewHolderOpcoes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tipo_material_item, null, false);
        return new ViewHolderOpcoes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderOpcoes holder, int position) {
        final TipoMaterial tipoMaterial = tipoMateriais.get(position);

        holder.setDescricaoTipoMaterial(tipoMaterial.getDescricao());
        holder.checkTipoMaterial.setOnCheckedChangeListener(null);
        holder.checkTipoMaterial.setChecked(tipoMaterial.isSelecionado());
        holder.checkTipoMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                tipoMaterial.setSelecionado(isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tipoMateriais.size();
    }

    class ViewHolderOpcoes extends RecyclerView.ViewHolder {

        View item;
        CheckBox checkTipoMaterial;

        private ViewHolderOpcoes(View itemView) {
            super(itemView);

            item = itemView;
            checkTipoMaterial = item.findViewById(R.id.check_tipo_material);
        }

        private void setDescricaoTipoMaterial(String descricao) {
            TextView item_descricao = item.findViewById(R.id.descricao_tipo_material_item);
            item_descricao.setText(descricao);
        }

    }

}