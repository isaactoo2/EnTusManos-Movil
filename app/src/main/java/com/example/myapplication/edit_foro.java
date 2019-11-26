package com.example.myapplication;

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

public class edit_foro extends AppCompatActivity {
    private ProgressDialog progressDialog;
    Spinner spnCateg;
    Button btnAgregar, btnCancelar;
    TextInputLayout titulo, descripcion;
    StringRequest stringRequest;
    String iduser=MainActivity.userId;

    RequestQueue request;
    url server = new url();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_foro);
        request = Volley.newRequestQueue(getApplicationContext());
        intiDialog();

        final String idForo=getIntent().getStringExtra("idForo");
        String editTitulo=getIntent().getStringExtra("titulo");
        String editCuerpo=getIntent().getStringExtra("cuerpo");
        String editCategoria=getIntent().getStringExtra("categoria");

        String[] categorias={editCategoria, "Bebés", "Crianza", "Embarazo", "Familia", "Niños", "Mujer", "Salud", "Otro"};


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnCateg = findViewById(R.id.sprCategoria);
        titulo = findViewById(R.id.txtTitulo);
        titulo.getEditText().setText(editTitulo);
        descripcion = findViewById(R.id.txtDescrip);
        descripcion.getEditText().setText(editCuerpo);

        btnAgregar = findViewById(R.id.btnPublicar);
        btnCancelar = findViewById(R.id.btnCancelar);


        spnCateg.setAdapter(adapter);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateText(titulo, "Complete el campo") | !validateText(descripcion, "Complete el campo") | !validateSpn(spnCateg, "Selecciona una categoria")){
                    return;
                }

                cargarWebService(idForo);

            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void cargarWebService(final String idForo) {
        showDialog();
        String url = "http://"+server.getServer()+"/ws/editForo.php";
        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Registra")){
                    titulo.getEditText().setText("");
                    descripcion.getEditText().setText("");

                    Toast.makeText(edit_foro.this, "Foro actualizado", Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(edit_foro.this, "No se ha podido actualizar", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(edit_foro.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                String tituloE = titulo.getEditText().getText().toString();
                String descripcionE = descripcion.getEditText().getText().toString();
                String categ = spnCateg.getSelectedItem().toString();

                Map<String,String> parametros = new HashMap<>();
                parametros.put("idForo", idForo);
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
