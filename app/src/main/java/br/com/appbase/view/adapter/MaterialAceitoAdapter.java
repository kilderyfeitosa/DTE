package br.com.appbase.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.appbase.R;
import br.com.appbase.dominio.model.TipoMaterial;
import br.com.appbase.dominio.model.Usuario;
import br.com.appbase.view.activity.MessageActivity;

public class MaterialAceitoAdapter extends RecyclerView.Adapter<MaterialAceitoAdapter.ViewHolder> {

    private Context mContext;
    private List<TipoMaterial> mTipoMaterials;

    public MaterialAceitoAdapter(Context mContext, List<TipoMaterial> mTipoMaterials){
        this.mTipoMaterials = mTipoMaterials;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.material_aceito_item, parent, false);
        return new MaterialAceitoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final TipoMaterial tipoMaterial = mTipoMaterials.get(position);
        holder.descricao_tipo_material_item.setText(tipoMaterial.getDescricao());

        if(tipoMaterial.getImgUrl() == null)
            tipoMaterial.setImgUrl("default");


        if(tipoMaterial.getImgUrl().equals("default")){
            holder.img_tipo_material_item.setImageResource(R.drawable.ic_recycle_24dp);

            if(tipoMaterial.getDescricao().equals("Baterias"))
                holder.img_tipo_material_item.setImageResource(R.drawable.ic_battery_24dp);
            if(tipoMaterial.getDescricao().equals("Cabos"))
                holder.img_tipo_material_item.setImageResource(R.drawable.ic_cabo_24dp);
            if(tipoMaterial.getDescricao().equals("Celulares"))
                holder.img_tipo_material_item.setImageResource(R.drawable.ic_smartphone_24dp);
            if(tipoMaterial.getDescricao().equals("Componentes de Computadores"))
                holder.img_tipo_material_item.setImageResource(R.drawable.ic_memory_24dp);
            if(tipoMaterial.getDescricao().equals("Eletrodomésticos"))
                holder.img_tipo_material_item.setImageResource(R.drawable.ic_kitchen_24dp);
            if(tipoMaterial.getDescricao().equals("Eletrônicos"))
                holder.img_tipo_material_item.setImageResource(R.drawable.ic_devices_other_24dp);
        } else {
            Glide.with(mContext).load(tipoMaterial.getImgUrl()).into(holder.img_tipo_material_item);
        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent abrirMessage = new Intent(mContext, MessageActivity.class);
//                abrirMessage.putExtra("userkey", user.getUserKey());
//                mContext.startActivity(abrirMessage);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mTipoMaterials.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView descricao_tipo_material_item;
        public ImageView img_tipo_material_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            descricao_tipo_material_item = itemView.findViewById(R.id.descricao_tipo_material_item);
            img_tipo_material_item = itemView.findViewById(R.id.img_tipo_material_item);


        }
    }
}
