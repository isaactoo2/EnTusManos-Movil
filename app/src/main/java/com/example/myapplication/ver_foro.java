package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.myapplication.entidades.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ver_foro extends AppCompatActivity {
    TextView txtEncabezado, txtDetalle, txtCuerpo;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    String id;
    RequestQueue request;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    url server = new url();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_foro);
        request = Volley.newRequestQueue(getApplicationContext());
        obtenerId();
        cargarUI();
        cargarForo();

    }

    private void obtenerId() {
        Bundle parametros = this.getIntent().getExtras();
        id=parametros.getString("idForo");
    }

    private void cargarForo() {

        String url = "http://"+server.getServer()+"/yourhands2/ws/getForo.php?id="+id;
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
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerComentarios);

    }
}
