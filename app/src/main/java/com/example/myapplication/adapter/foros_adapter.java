package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.myapplication.R;
import com.example.myapplication.entidades.asesoria;
import com.example.myapplication.entidades.foros;
import com.example.myapplication.entidades.url;
import com.example.myapplication.ver_foro;

import java.util.List;

public class foros_adapter extends RecyclerView.Adapter<foros_adapter.foros_holder> {
    List<foros> ListaForos;
    RequestQueue request;
    Context context;
    url server = new url();
    private OnForoListener mOnForoListener;

    public foros_adapter(List<foros> listaForos, OnForoListener onForoListener) {
        this.ListaForos = listaForos;
        this.mOnForoListener = onForoListener;
    }

    private class VIEWS_TYPES{
        public static final int Header=1;
        public static final int Normal=2;
    }

    public int getItemViewType(int position) {
        if (ListaForos.get(position).isHeader()){
            return foros_adapter.VIEWS_TYPES.Header;
        }else {
            return foros_adapter.VIEWS_TYPES.Normal;
        }
    }
    Boolean isHeader=false;
    @NonNull
    @Override
    public foros_adapter.foros_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView;
        switch (viewType){
            case foros_adapter.VIEWS_TYPES.Normal:
                isHeader=false;
                rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.foros_list,parent,false);
                break;
            case foros_adapter.VIEWS_TYPES.Header:
                isHeader=true;
                rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.foros_header,parent,false);
                break;
            default:
                isHeader=false;
                rowView=LayoutInflater.from(parent.getContext()).inflate(R.layout.foros_list,parent,false);
        }
        return new foros_adapter.foros_holder(rowView, mOnForoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull foros_adapter.foros_holder holder, final int position) {
        if (!isHeader){
            String cuerpo, encabezado, detalle;
            encabezado=ListaForos.get(position).getTituloForo()+" | "+ListaForos.get(position).getCategoriaForo();
            cuerpo=ListaForos.get(position).getCuerpoForo();
            detalle="Publicado por: "+ ListaForos.get(position).getUserForo() + " el " + ListaForos.get(position).getFechaForo();

            holder.txtEncabezado.setText(encabezado);
            if (cuerpo.length()>0 && cuerpo.length()<=100){
                holder.txtCuerpo.setText(Html.fromHtml(cuerpo));
            }else{
                cuerpo=cuerpo.substring(0,100);
                holder.txtCuerpo.setText(Html.fromHtml(cuerpo)+"...");
            }
            holder.txtDetalle.setText(detalle);


        }
    }

    @Override
    public int getItemCount() {
        return ListaForos.size();
    }

    public class foros_holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtEncabezado, txtDetalle, txtCuerpo;
        CardView foroCard;
        OnForoListener onForoListener;


        public foros_holder(@NonNull View itemView, OnForoListener onForoListener) {
            super(itemView);

            txtEncabezado = itemView.findViewById(R.id.encabezadoForo);
            txtDetalle = itemView.findViewById(R.id.detalleForo);
            txtCuerpo = itemView.findViewById(R.id.cuerpoForo);

            this.onForoListener=onForoListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onForoListener.onForoClick(getAdapterPosition());
        }
    }

    public interface OnForoListener{
        void onForoClick(int position);
    }
}
