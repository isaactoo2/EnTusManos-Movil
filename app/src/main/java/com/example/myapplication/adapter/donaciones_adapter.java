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
import com.example.myapplication.entidades.donaciones;

import com.example.myapplication.entidades.url;

import java.util.List;

public class donaciones_adapter extends RecyclerView.Adapter<donaciones_adapter.donaciones_holder> {

    List<donaciones> ListaDonaciones;
    RequestQueue request;
    Context context;
    url server = new url();
    private OnDonacionListener mOnDonacionListener;


    public donaciones_adapter(List<donaciones> listaDonaciones, Context context, OnDonacionListener onDonacionListener) {
        this.ListaDonaciones = listaDonaciones;
        this.context=context;
        this.mOnDonacionListener=onDonacionListener;

        request= Volley.newRequestQueue(context);

    }

    @NonNull
    @Override
    public donaciones_adapter.donaciones_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donaciones_list,parent,false);
        RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new donaciones_holder(view,mOnDonacionListener);

    }

    @Override
    public void onBindViewHolder(@NonNull donaciones_holder holder, int position) {

            String cuerpo;
            cuerpo=ListaDonaciones.get(position).getCuerpoDonacion();

            holder.txtTituloD.setText(ListaDonaciones.get(position).getTituloDonacion());
            holder.txtCuerpoD.setText(Html.fromHtml(cuerpo));
            holder.txtUser.setText("Publicado por: "+ListaDonaciones.get(position).getUserDonacion());
            holder.txtUbicacion.setText("Ubicación: "+ListaDonaciones.get(position).getUserUbicacion());

            if ( ListaDonaciones.get(position).getPhotoDonacion()!=null){
                cargarImagenWebService(ListaDonaciones.get(position).getPhotoDonacion(), holder);
            }
    }


    private void cargarImagenWebService(String photoDonacion, final donaciones_holder holder) {
        String urlImage="http://"+server.getServer()+"/photosintercambios/"+photoDonacion;

        ImageRequest imageRequest=new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.img.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               //Toast.makeText(context, "Error al cargar la imgane", Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);
    }


    @Override
    public int getItemCount() {

        return ListaDonaciones.size();
    }

    public class donaciones_holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtTituloD, txtCuerpoD, txtUser, txtUbicacion;
        ImageView img;
        OnDonacionListener onDonacionListener;

        public donaciones_holder(@NonNull View itemView, OnDonacionListener onDonacionListener) {
            super(itemView);
            txtTituloD= itemView.findViewById(R.id.tituloDonacion);
            txtCuerpoD=itemView.findViewById(R.id.cuerpoDonacion);
            txtUser=itemView.findViewById(R.id.userDonacion);
            txtUbicacion=itemView.findViewById(R.id.userUbicacion);
            img=(ImageView)itemView.findViewById(R.id.imgDonacion);

            this.onDonacionListener=onDonacionListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onDonacionListener.onDonacionClick(getAdapterPosition());
        }
    }

    public interface OnDonacionListener{
        void onDonacionClick(int position);
    }

}
