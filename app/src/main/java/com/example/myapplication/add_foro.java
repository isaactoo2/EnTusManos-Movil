package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.entidades.url;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class add_foro extends AppCompatActivity {
    private ProgressDialog progressDialog;
    Spinner spnCateg;
    Button btnAgregar, btnCancelar;
    TextInputLayout titulo, descripcion;
    String[] categorias={"Seleccionar categoria", "Bebés", "Crianza", "Embarazo", "Familia", "Niños", "Mujer", "Salud", "Otro"};
    StringRequest stringRequest;
    String iduser=MainActivity.userId;

    RequestQueue request;
    url server = new url();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_foro);
        request = Volley.newRequestQueue(getApplicationContext());
        intiDialog();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnCateg = findViewById(R.id.sprCategoria);
        titulo = findViewById(R.id.txtTitulo);
        descripcion = findViewById(R.id.txtDescrip);
        btnAgregar = findViewById(R.id.btnPublicar);
        btnCancelar = findViewById(R.id.btnCancelar);


        spnCateg.setAdapter(adapter);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateText(titulo, "Complete el campo") | !validateText(descripcion, "Complete el campo") | !validateSpn(spnCateg, "Selecciona una categoria")){
                    return;
                }

                cargarWebService();

            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void cargarWebService() {
        showDialog();
        String url = "http://"+server.getServer()+"/ws/addForo.php";
        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Registra")){
                    titulo.getEditText().setText("");
                    descripcion.getEditText().setText("");

                    Toast.makeText(add_foro.this, "Foro publicado", Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(add_foro.this, "No se ha podido publicar", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(add_foro.this, "Error al publicar", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                String tituloE = titulo.getEditText().getText().toString();
                String descripcionE = descripcion.getEditText().getText().toString();
                String categ = spnCateg.getSelectedItem().toString();

                Map<String,String> parametros = new HashMap<>();
                parametros.put("iduser", iduser);
                parametros.put("titulo", tituloE);
                parametros.put("descripcion", descripcionE);
                parametros.put("categoria", categ);
                return parametros;
            }
        };

        request.add(stringRequest);
    }

    private boolean validateSpn(Spinner spn, String error){
        String item = spn.getSelectedItem().toString();

        if (item == "Seleccionar categoria"){
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateText(TextInputLayout textInputLayout, String error){
        String edidText = textInputLayout.getEditText().getText().toString().trim();

        if (edidText.isEmpty()){
            textInputLayout.setError(error);
            return false;
        }else {
            textInputLayout.setError(null);
            return true;
        }
    }

    private void intiDialog(){
        this.progressDialog=new ProgressDialog(this);
    }
    private void showDialog(){
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Por favor, espere...");
        progressDialog.show();
    }
}
