package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.adapter.comentarios_adapter;
import com.example.myapplication.adapter.mensajes_adapter;
import com.example.myapplication.entidades.comentarios;
import com.example.myapplication.entidades.misMensajes;
import com.example.myapplication.entidades.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_mensajes extends AppCompatActivity implements View.OnClickListener, mensajes_adapter.OnMensajeListener {
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    String id;
    String iduser=MainActivity.userId;
    RequestQueue request;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    ArrayList<misMensajes> listaMensajes;
    url server = new url();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajes);
        request = Volley.newRequestQueue(getApplicationContext());
        listaMensajes = new ArrayList<>();
        cargarUI();
        cargarMensajes();
    }

    private void cargarUI() {
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarMensajes();
            }
        });
        recyclerView = findViewById(R.id.recyclerMensajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
    }

    private void cargarMensajes() {
        String url = "http://"+server.getServer()+"/ws/getConversacion.php?id="+iduser;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json = response.optJSONArray("datos");
                listaMensajes.clear();
                try {
                    for (int i = 0; i<json.length(); i++){
                        misMensajes data = new misMensajes();
                        JSONObject jsonObject=null;

                        jsonObject=json.getJSONObject(i);

                        data.setUserName(jsonObject.optString("nombre"));
                        data.setIdUser(jsonObject.optString("user2_id"));
                        data.setLastMessage(jsonObject.optString("ultimo_mensaje"));
                        data.setPhoto(jsonObject.optString("photo"));


                        listaMensajes.add(data);
                        refreshLayout.setRefreshing(false);

                    }

                    mensajes_adapter adapter=new mensajes_adapter(listaMensajes, lista_mensajes.this,getApplicationContext());
                    recyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error "+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(lista_mensajes.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.add(jsonObjectRequest);
    }

    @Override
    public void onMensajeClick(int position) {
        Toast.makeText(this, listaMensajes.get(position).getUserName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }
}
