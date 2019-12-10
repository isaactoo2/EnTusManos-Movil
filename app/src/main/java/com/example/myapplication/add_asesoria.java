package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class add_asesoria extends AppCompatActivity {
    private ProgressDialog progressDialog;
    Spinner spnCateg;
    Button btnAgregar, btnLista;
    TextInputLayout titulo, descripcion;
    TextView esp;
    String[] categorias={"Seleccionar categoria", "Psicología", "Salud", "Nutrición", "Pediatría", "Neurología", "Dermatología"};
    StringRequest stringRequest;
    String iduser=MainActivity.userId;
    RequestQueue request;
    url server = new url();
    String idPro, idEsp, pro;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_asesoria);
        request = Volley.newRequestQueue(getApplicationContext());
        intiDialog();


        descripcion = findViewById(R.id.txtDescrip);
        btnAgregar = findViewById(R.id.btnPublicar);
        obtenerDatos();

        esp=findViewById(R.id.especialista);
        esp.setText("Especialista seleccionado: "+pro);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateText(descripcion, "Complete el campo")){
                    return;
                }


                cargarWebService();
            }
        });
        FloatingActionButton fab=findViewById(R.id.fabClose);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void cargarWebService() {
        showDialog();
        String url = "http://"+server.getServer()+"/ws/addAsesoria.php";
        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Registra")){

                    descripcion.getEditText().setText("");

                    Toast.makeText(add_asesoria.this, "Consulta realizada", Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(add_asesoria.this, "No se ha podido realizar", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(add_asesoria.this, "Error al realizar la consulta", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {


                String descripcionE = descripcion.getEditText().getText().toString();


                Map<String,String> parametros = new HashMap<>();
                parametros.put("iduser", iduser);
                parametros.put("descripcion", descripcionE);
                parametros.put("idTipoEsp", idEsp);
                parametros.put("idPro", idPro);
                return parametros;
            }
        };

       request.add(stringRequest);
    }

    //Validacion de combo
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

    private void obtenerDatos() {
        Bundle parametros = this.getIntent().getExtras();
        idPro = parametros.getString("idPro");
        idEsp = parametros.getString("idEsp");
        pro = parametros.getString("profesional");

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
