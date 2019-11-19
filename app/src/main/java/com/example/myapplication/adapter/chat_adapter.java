package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entidades.mensajes;

import java.util.ArrayList;
import java.util.List;

public class chat_adapter extends RecyclerView.Adapter<chat_adapter.chat_holder> {
    List<mensajes> ListaChat;
    Boolean isLeft;

    public chat_adapter(List<mensajes> listaChat){
        this.ListaChat=listaChat;
    }

    @NonNull
    @Override
    public chat_adapter.chat_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rowView;

        rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_form,parent,false);

        return new chat_adapter.chat_holder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull chat_adapter.chat_holder holder, int position) {

        if (ListaChat.get(position).isLeft()){
            holder.msgL.setText(ListaChat.get(position).getMsg());
            holder.layoutL.setVisibility(View.VISIBLE);
        } else
        {
            holder.msgR.setText(ListaChat.get(position).getMsg());
            holder.layoutR.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return ListaChat.size();
    }

    public class chat_holder extends RecyclerView.ViewHolder{
        TextView msgL, msgR;
        LinearLayout layoutR, layoutL;

        public chat_holder(@NonNull View itemView) {
            super(itemView);
            msgL = itemView.findViewById(R.id.msgL);
            msgR = itemView.findViewById(R.id.msgR);
            layoutL=itemView.findViewById(R.id.layoutL);
            layoutR=itemView.findViewById(R.id.layoutR);
        }
    }
}
