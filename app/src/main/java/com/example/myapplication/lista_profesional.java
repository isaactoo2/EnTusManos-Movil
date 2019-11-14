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
import com.example.myapplication.adapter.profesional_adapter;
import com.example.myapplication.entidades.profesionales;
import com.example.myapplication.entidades.url;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_profesional extends AppCompatActivity {


    RecyclerView recyclerView;
    String id;
    String iduser=MainActivity.userId;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ArrayList<profesionales> listaProfesionales;
    url server = new url();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profesional);
        request = Volley.newRequestQueue(getApplicationContext());
        listaProfesionales= new ArrayList<>();

        cargarUI();
        cargarLista();
    }

    private void cargarLista() {
        String url = "http://"+server.getServer()+"/ws/getPro.php";
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json = response.optJSONArray("datos");

                try {
                    for (int i = 0; i<json.length(); i++){
                        profesionales data = new profesionales();
                        JSONObject jsonObject=null;

                        jsonObject=json.getJSONObject(i);

                        data.setProfesional(jsonObject.optString("nombre"));
                        data.setEspecialidad(jsonObject.optString("especialidad"));
                        data.setGenero(jsonObject.optString("genero"));
                        data.setUbicacion(jsonObject.optString("ubicacion"));
                        data.setPhotoPro(jsonObject.optString("photo"));


                        listaProfesionales.add(data);
                    }

                    profesional_adapter adapter=new profesional_adapter(listaProfesionales, getApplicationContext());
                    recyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error "+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.add(jsonObjectRequest);
    }

    private void cargarUI() {
        recyclerView = findViewById(R.id.recyclerPro);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        FloatingActionButton fab=findViewById(R.id.fabClose);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
