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
import com.example.myapplication.entidades.profesionales;
import com.example.myapplication.entidades.url;

import java.util.List;

public class profesional_adapter extends RecyclerView.Adapter<profesional_adapter.profesional_holder> {
    List<profesionales> ListaProfesional;
    RequestQueue request;
    Context context;
    url server = new url();

    public profesional_adapter(List<profesionales> ListaProfesional, Context context){
        this.ListaProfesional=ListaProfesional;
        this.context=context;
        request= Volley.newRequestQueue(context);
    }


    @NonNull
    @Override
    public profesional_adapter.profesional_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView;
        rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.profesional_list,parent,false);
        return new profesional_holder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull profesional_adapter.profesional_holder holder, int position) {
        holder.txtProfesional.setText(ListaProfesional.get(position).getProfesional());
        holder.txtEsp.setText("Especialidad: "+ListaProfesional.get(position).getEspecialidad());
        holder.txtGen.setText("Genero: "+ListaProfesional.get(position).getGenero());
        holder.txtCorreo.setText("Correo electrónico: "+ListaProfesional.get(position).getCorreo());
        holder.txtTelefono.setText("Teléfono: "+ListaProfesional.get(position).getTelefono());

        holder.txtUbicacion.setText("Ubicación: "+ListaProfesional.get(position).getUbicacion());
        if (ListaProfesional.get(position).getPhotoPro()!=null){
            cargarImagenWebService(ListaProfesional.get(position).getPhotoPro(), holder);
        }

    }

    private void cargarImagenWebService(String photoPro, final profesional_adapter.profesional_holder holder) {
        String urlImage="http://"+server.getServer()+"/photos/"+photoPro;

        ImageRequest imageRequest=new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.img.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return ListaProfesional.size();
    }
    public class profesional_holder extends  RecyclerView.ViewHolder{
        TextView txtProfesional, txtEsp, txtGen, txtUbicacion, txtTelefono, txtCorreo;
        ImageView img;
        public profesional_holder(@NonNull View itemView) {
            super(itemView);
            txtProfesional = itemView.findViewById(R.id.tituloPro);
            txtEsp = itemView.findViewById(R.id.especialidadPro);
            txtGen = itemView.findViewById(R.id.generoPro);
            txtTelefono = itemView.findViewById(R.id.telefonoPro);
            txtCorreo = itemView.findViewById(R.id.correoPro);
            txtUbicacion = itemView.findViewById(R.id.ubicacionPro);
            img = itemView.findViewById(R.id.imgPro);
        }
    }
}
