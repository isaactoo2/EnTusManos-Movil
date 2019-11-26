package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.myapplication.R;
import com.example.myapplication.entidades.misMensajes;
import com.example.myapplication.entidades.url;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class mensajes_adapter extends RecyclerView.Adapter<mensajes_adapter.mensajes_holder> {
    List<misMensajes> ListaMensajes;
    RequestQueue request;
    Context context;
    url server = new url();
    private OnMensajeListener mOnMensajeListener;

    public mensajes_adapter(List<misMensajes> listaMensajes, OnMensajeListener onMensajeListener,Context context){
        this.ListaMensajes=listaMensajes;
        this.context=context;
        this.mOnMensajeListener=onMensajeListener;
        request= Volley.newRequestQueue(context);
    }



    @NonNull
    @Override
    public mensajes_adapter.mensajes_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView;
        rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.mensaje_list,parent,false);
        return new mensajes_adapter.mensajes_holder(rowView, mOnMensajeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull mensajes_adapter.mensajes_holder holder, int position) {
        holder.lblUser.setText(ListaMensajes.get(position).getUserName());
        holder.lblLastMessage.setText(ListaMensajes.get(position).getLastMessage());
        if (ListaMensajes.get(position).getPhoto()!=null){
            cargarImagenWebService(ListaMensajes.get(position).getPhoto(), holder);
        }
    }

    private void cargarImagenWebService(String photo, final mensajes_holder holder) {
        String urlImage="http://"+server.getServer()+"/photos/"+photo;
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
        return ListaMensajes.size();
    }

    public class mensajes_holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView lblUser, lblLastMessage;
        CircularImageView userPhoto;
        OnMensajeListener onMensajeListener;
        public mensajes_holder(@NonNull View itemView, OnMensajeListener onMensajeListener) {
            super(itemView);
            lblUser = itemView.findViewById(R.id.lblUser);
            lblLastMessage = itemView.findViewById(R.id.lblUltimoMensaje);
            userPhoto = itemView.findViewById(R.id.userImage);
            this.onMensajeListener=onMensajeListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMensajeListener.onMensajeClick(getAdapterPosition());
        }
    }

    public interface OnMensajeListener{
        void onMensajeClick(int position);
    }
}
