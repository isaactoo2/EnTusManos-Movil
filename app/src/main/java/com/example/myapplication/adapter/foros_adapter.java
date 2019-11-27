package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.add_foro;
import com.example.myapplication.edit_foro;
import com.example.myapplication.entidades.asesoria;
import com.example.myapplication.entidades.comentarios;
import com.example.myapplication.entidades.foros;
import com.example.myapplication.entidades.url;
import com.example.myapplication.lista_foros;
import com.example.myapplication.ver_foro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class foros_adapter extends RecyclerView.Adapter<foros_adapter.foros_holder> {
    List<foros> ListaForos;
    RequestQueue request;
    Context context;
    url server = new url();
    public OnForoListener mOnForoListener;
    String iduser= MainActivity.userId;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    public foros_adapter(List<foros> listaForos, OnForoListener onForoListener, Context context) {
        this.ListaForos = listaForos;
        this.mOnForoListener = onForoListener;
        this.context = context;
        this.request= Volley.newRequestQueue(context);

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
    public void onBindViewHolder(@NonNull final foros_adapter.foros_holder holder, final int position) {
        if (!isHeader){
            String cuerpo, encabezado, detalle;
            final foros listItem = ListaForos.get(position);
            encabezado=listItem.getTituloForo()+" | "+listItem.getCategoriaForo();
            cuerpo=listItem.getCuerpoForo();
            detalle="Publicado por: "+ listItem.getUserForo() + " el " + listItem.getFechaForo();

            holder.txtEncabezado.setText(encabezado);
            if (cuerpo.length()>0 && cuerpo.length()<=100){
                holder.txtCuerpo.setText(Html.fromHtml(cuerpo));
            }else{
                cuerpo=cuerpo.substring(0,100);
                holder.txtCuerpo.setText(Html.fromHtml(cuerpo)+"...");
            }
            holder.txtDetalle.setText(detalle);
            if (listItem.getIdUserForo().equals(iduser)){
                holder.menu.setVisibility(View.VISIBLE);
            }
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.menu);
                    popupMenu.inflate(R.menu.menu_foro);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()){
                                    case R.id.menu_edit:
                                        Intent intent = new Intent(context, edit_foro.class);
                                        intent.putExtra("idForo", listItem.getIdForo());
                                        intent.putExtra("titulo", listItem.getTituloForo());
                                        intent.putExtra("cuerpo", listItem.getCuerpoForo());
                                        intent.putExtra("categoria", listItem.getCategoriaForo());

                                        context.startActivity(intent);

                                        break;
                                    case R.id.menu_delete:



                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                            builder1.setMessage("¿Está seguro de eliminar el foro?");
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "Si",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            deleteForo(ListaForos.get(position).getIdForo());

                                                        }
                                                    });

                                            builder1.setNegativeButton(
                                                    "No",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();

                                                        }
                                                    });

                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();




                                        break;
                                    default:
                                        break;
                                }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

        }
    }


    public void deleteForo(final String idForo) {
        String url = "http://"+server.getServer()+"/ws/deleteForo.php";
        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Registra")){

                    Toast.makeText(context, "Foro eliminado", Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se ha podido conectar al servidor", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("idForo", idForo);
                return parametros;
            }
        };

        request.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return ListaForos.size();
    }

    public class foros_holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtEncabezado, txtDetalle, txtCuerpo;
        CardView foroCard;
        OnForoListener onForoListener;
        ImageView menu;


        public foros_holder(@NonNull View itemView, OnForoListener onForoListener) {
            super(itemView);

            txtEncabezado = itemView.findViewById(R.id.encabezadoForo);
            txtDetalle = itemView.findViewById(R.id.detalleForo);
            txtCuerpo = itemView.findViewById(R.id.cuerpoForo);

            menu = itemView.findViewById(R.id.menuForo);


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
