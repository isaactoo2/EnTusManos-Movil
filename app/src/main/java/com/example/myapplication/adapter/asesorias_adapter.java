package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.entidades.asesoria;
import com.example.myapplication.entidades.noticias;
import com.example.myapplication.entidades.url;

import java.util.List;

public class asesorias_adapter extends RecyclerView.Adapter<asesorias_adapter.asesorias_holder>{
    List<asesoria> ListaAsesorias;
    RequestQueue request;
    Context context;
    url server = new url();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private class VIEWS_TYPES{
        public static final int Header=1;
        public static final int Normal=2;
    }

    Boolean isHeader=false;

    @Override
    public int getItemViewType(int position) {
        if (ListaAsesorias.get(position).isHeader()){
            return VIEWS_TYPES.Header;
        }else {
            return VIEWS_TYPES.Normal;
        }
    }



    public asesorias_adapter(List<asesoria> listaAsesorias) {
        this.ListaAsesorias = listaAsesorias;

    }

    @NonNull
    @Override
    public asesorias_adapter.asesorias_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noticias_list,parent,false);
        RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new noticias_holder(view);*/

        View rowView;
        switch (viewType){
            case asesorias_adapter.VIEWS_TYPES.Normal:
                isHeader=false;
                rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.asesorias_list,parent,false);
                break;
            case asesorias_adapter.VIEWS_TYPES.Header:
                isHeader=true;
                rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.asesorias_header,parent,false);
                break;
            default:
                isHeader=false;
                rowView=LayoutInflater.from(parent.getContext()).inflate(R.layout.asesorias_list,parent,false);
        }
        return new asesorias_holder(rowView);





    }

    @Override
    public void onBindViewHolder(@NonNull asesorias_holder holder, int position) {
        if (!isHeader){
            String cuerpo;
            cuerpo=ListaAsesorias.get(position).getCuerpoAsesoria();

            holder.txtTituloAsesoria.setText(ListaAsesorias.get(position).getTituloAsesoria());
            if (cuerpo.length()>0 && cuerpo.length()<=100){
                holder.txtCuerpoAsesoria.setText(Html.fromHtml(cuerpo));
            }else{
                cuerpo=cuerpo.substring(0,100);
                holder.txtCuerpoAsesoria.setText(Html.fromHtml(cuerpo)+"...");
            }
            holder.txtEspecialidad.setText("Categoria: "+ListaAsesorias.get(position).getIdAsesoria()+"    Fecha: "+ListaAsesorias.get(position).getFecha());

        }
    }

   @Override
    public int getItemCount() {

        return ListaAsesorias.size();
    }

    public class asesorias_holder extends RecyclerView.ViewHolder {

        TextView txtTituloAsesoria, txtCuerpoAsesoria, txtEspecialidad;


        public asesorias_holder(@NonNull View itemView) {
            super(itemView);
            txtTituloAsesoria= itemView.findViewById(R.id.tituloConsulta);
            txtCuerpoAsesoria=itemView.findViewById(R.id.cuerpoConsulta);
            txtEspecialidad=itemView.findViewById(R.id.categoria);

        }
    }
}
