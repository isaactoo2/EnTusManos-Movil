package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.example.myapplication.entidades.comentarios;
import com.example.myapplication.entidades.url;

import java.util.List;

public class comentarios_adapter extends RecyclerView.Adapter<comentarios_adapter.comentarios_holder> {
    List<comentarios> ListaComentarios;
    RequestQueue request;
    Context context;
    url server = new url();

    public comentarios_adapter(List<comentarios> listaComentarios, Context context){
        this.ListaComentarios=listaComentarios;
        context=context;
        request= Volley.newRequestQueue(context);
    }


    @NonNull
    @Override
    public comentarios_adapter.comentarios_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView;
        rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.comentario_item,parent,false);
        return new comentarios_holder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull comentarios_adapter.comentarios_holder holder, int position) {
        holder.lblUser.setText(ListaComentarios.get(position).getUserName());
        holder.lblComentario.setText(ListaComentarios.get(position).getComenario());
        holder.lblFecha.setText("Fecha de comentario: "+ListaComentarios.get(position).getFecha());
        if ( ListaComentarios.get(position).getFoto()!=null){
            cargarImagenWebService(ListaComentarios.get(position).getFoto(), holder);
        }
    }

    private void cargarImagenWebService(String foto, final comentarios_holder holder) {
        String urlImage="http://"+server.getServer()+"/yourhands2/photos/"+foto;
        ImageRequest imageRequest = new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                holder.userPhoto.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return ListaComentarios.size();
    }

    public class comentarios_holder extends RecyclerView.ViewHolder {

        TextView lblUser, lblComentario, lblFecha;
        ImageView userPhoto;


        public comentarios_holder(@NonNull View itemView) {
            super(itemView);
            lblComentario= itemView.findViewById(R.id.lblComentario);
            lblUser=itemView.findViewById(R.id.lblUser);
            lblFecha=itemView.findViewById(R.id.lblFechaComentario);
            userPhoto=itemView.findViewById(R.id.userImage);

        }
    }
}
