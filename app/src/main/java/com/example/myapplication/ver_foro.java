package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.adapter.asesorias_adapter;
import com.example.myapplication.adapter.comentarios_adapter;
import com.example.myapplication.entidades.asesoria;
import com.example.myapplication.entidades.comentarios;
import com.example.myapplication.entidades.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ver_foro extends AppCompatActivity {
    TextView txtEncabezado, txtDetalle, txtCuerpo, txtComentar;
    ImageButton btnEnviar;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    String id;
    String iduser=MainActivity.userId;
    RequestQueue request;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    ArrayList<comentarios> listaComentarios;
    url server = new url();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_foro);
        request = Volley.newRequestQueue(getApplicationContext());
        listaComentarios= new ArrayList<>();
        obtenerId();
        cargarUI();
        cargarForo();
        cargarComentarios();
        comentar();

    }

    private void comentar() {
        txtComentar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!txtComentar.getText().toString().trim().isEmpty()){
                    btnEnviar.setVisibility(View.VISIBLE);
                }else {
                    btnEnviar.setVisibility(View.GONE);
                }
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarComentario();
            }
        });
    }

    private void enviarComentario() {
        String url = "http://"+server.getServer()+"/ws/addForoComentario.php";
        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Registra")){
                    cargarComentarios();
                    txtComentar.setText("");
                    closeKeyBoard();

                }else {
                    Toast.makeText(getApplicationContext(), "No se ha podido comentar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al publicar comentar", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                String comentario = txtComentar.getText().toString().trim();
                String idForo = id;


                Map<String,String> parametros = new HashMap<>();
                parametros.put("iduser", iduser);
                parametros.put("idforo", idForo);
                parametros.put("comentario", comentario);

                return parametros;
            }
        };

        request.add(stringRequest);
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }

    private void cargarComentarios() {
        String url = "http://"+server.getServer()+"/ws/getForoComentario.php?id="+id;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json = response.optJSONArray("datos");
                listaComentarios.clear();
                try {
                    for (int i = 0; i<json.length(); i++){
                        comentarios data = new comentarios();
                        JSONObject jsonObject=null;

                        jsonObject=json.getJSONObject(i);

                        data.setUserName(jsonObject.optString("nombre"));
                        data.setComenario(jsonObject.optString("fr_comentario"));
                        data.setFecha(jsonObject.optString("fecha_comentario"));
                        data.setFoto(jsonObject.optString("photo"));


                        listaComentarios.add(data);
                        refreshLayout.setRefreshing(false);

                    }

                    comentarios_adapter adapter=new comentarios_adapter(listaComentarios, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.smoothScrollToPosition((adapter.getItemCount()>0)?recyclerView.getAdapter().getItemCount():0);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error "+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.add(jsonObjectRequest);
    }

    private void obtenerId() {
        Bundle parametros = this.getIntent().getExtras();
        id=parametros.getString("idForo");
    }

    private void cargarForo() {

        String url = "http://"+server.getServer()+"/ws/getForo.php?id="+id;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json = response.optJSONArray("datos");
                String cuerpo, encabezado, detalle;
                try {
                    JSONObject jsonObject=null;
                    jsonObject=json.getJSONObject(0);
                    encabezado=jsonObject.optString("titulo")+" | "+jsonObject.optString("categoria");
                    cuerpo=jsonObject.optString("cuerpo");
                    detalle="Publicado por: "+jsonObject.optString("nombre")+" el "+ jsonObject.optString("fecha_foro");
                    txtEncabezado.setText(encabezado);
                    txtCuerpo.setText(cuerpo);
                    txtDetalle.setText(detalle);

                }catch (JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ver_foro.this, "Error "+ error, Toast.LENGTH_SHORT).show();
            }
        });
        request.add(jsonObjectRequest);

    }

    private void cargarUI() {
        txtEncabezado = findViewById(R.id.encabezadoForo);
        txtDetalle = findViewById(R.id.detalleForo);
        txtCuerpo = findViewById(R.id.cuerpoForo);
        txtComentar = findViewById(R.id.txtComentar);
        btnEnviar = findViewById(R.id.btnEnviar);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarComentarios();
            }
        });
        recyclerView = findViewById(R.id.recyclerComentarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);


    }
}
