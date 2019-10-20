package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.entidades.noticias;
import com.example.myapplication.entidades.url;

import java.util.ArrayList;
import java.util.List;

public class noticias_adapter extends RecyclerView.Adapter<noticias_adapter.noticias_holder> {

    List<noticias> ListaNoticias;
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
        if (ListaNoticias.get(position).isHeader()){
            return VIEWS_TYPES.Header;
        }else {
            return VIEWS_TYPES.Normal;
        }
    }

    public noticias_adapter(List<noticias> listaNoticias, Context context) {
        this.ListaNoticias = listaNoticias;
        context=context;
        request= Volley.newRequestQueue(context);

    }

    @NonNull
    @Override
    public noticias_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noticias_list,parent,false);
        RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new noticias_holder(view);*/

        View rowView;
        switch (viewType){
            case VIEWS_TYPES.Normal:
                isHeader=false;
                rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.noticias_list,parent,false);
                break;
            case VIEWS_TYPES.Header:
                isHeader=true;
                rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_header,parent,false);
                break;
            default:
                isHeader=false;
                rowView=LayoutInflater.from(parent.getContext()).inflate(R.layout.noticias_list,parent,false);
        }
        return new noticias_holder(rowView);





    }

    @Override
    public void onBindViewHolder(@NonNull noticias_holder holder, int position) {

            if (!isHeader){
                String cuerpo;
                cuerpo=ListaNoticias.get(position).getCuerpoNoticia();

                holder.txtTituloNoticia.setText(ListaNoticias.get(position).getTituloNoticia());
                if (cuerpo.length()>0 && cuerpo.length()<=100){
                    holder.txtCuerpoNoticia.setText(Html.fromHtml(cuerpo));
                }else{
                    cuerpo=cuerpo.substring(0,100);
                    holder.txtCuerpoNoticia.setText(Html.fromHtml(cuerpo)+"...");
                }
                if ( ListaNoticias.get(position).getPhotoNoticia()!=null){
                    cargarImagenWebService(ListaNoticias.get(position).getPhotoNoticia(), holder);
                }
            }



    }

    private void cargarImagenWebService(String photoNoticia, final noticias_holder holder) {
        String urlImage="http://"+server.getServer()+"/login/photos-noticias/"+photoNoticia;

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

        return ListaNoticias.size();
    }

    public class noticias_holder extends RecyclerView.ViewHolder {

        TextView txtTituloNoticia, txtCuerpoNoticia;
        ImageView img;

        public noticias_holder(@NonNull View itemView) {
            super(itemView);
            txtTituloNoticia= itemView.findViewById(R.id.tituloNoticia);
            txtCuerpoNoticia=itemView.findViewById(R.id.cuerpoNoticia);
            img=(ImageView)itemView.findViewById(R.id.imgNoticia);

        }
    }

}
