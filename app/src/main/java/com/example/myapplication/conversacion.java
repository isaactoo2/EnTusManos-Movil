package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.myapplication.adapter.chat_adapter;
import com.example.myapplication.adapter.mensajes_adapter;
import com.example.myapplication.entidades.mensajes;
import com.example.myapplication.entidades.misMensajes;
import com.example.myapplication.entidades.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class conversacion extends AppCompatActivity {
    String userName, userFoto, idPara;
    String iduser=MainActivity.userId;
    RequestQueue request;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    url server = new url();
    CircularImageView toolbarImage;
    EditText txtMensaje;
    ImageButton btnEnviar;
    ArrayList<mensajes> listaChat;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;

    chat_adapter chat_adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversacion);
        request = Volley.newRequestQueue(getApplicationContext());
        final Toolbar toolbar = (Toolbar) findViewById(R.id.mCustonToolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cargarUI();
        obtenerDatos();
        obtenerImagen(userFoto);
        toolbarImage = toolbar.findViewById(R.id.toolbarImage);
        TextView toolbarTitulo = toolbar.findViewById(R.id.toolbarTitulo);
        toolbarTitulo.setText(userName);
        cargarMensajes();
        enviarMensaje();
        tick();


    }
    public boolean isVisible(){
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int positionOfLastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int itemCount = recyclerView.getAdapter().getItemCount();
        return (positionOfLastVisibleItem>=itemCount);
    }

    private void tick() {
        final Handler handler = new Handler();
        Timer timer = new Timer(false);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        };
        timer.schedule(timerTask, 500, 500);
    }

    private void cargarMensajes() {
        String url = "http://"+server.getServer()+"/ws/getMensajes.php?f="+iduser+"&t="+idPara;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json = response.optJSONArray("datos");
                listaChat.clear();
                try {
                    for (int i = 0; i<json.length(); i++){
                        mensajes data = new mensajes();
                        JSONObject jsonObject=null;

                        jsonObject=json.getJSONObject(i);


                        data.setMsg(jsonObject.optString("mensaje"));
                        if (jsonObject.optString("deIdUser").equals(iduser)){
                            data.setLeft(false);
                        } else {
                            data.setLeft(true);
                        }

                        listaChat.add(data);
                        refreshLayout.setRefreshing(false);
                        chat_adapter.notifyDataSetChanged();
                        if (isVisible()){
                            recyclerView.smoothScrollToPosition(chat_adapter.getItemCount()-1);
                        }


                    }



                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error "+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(conversacion.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.add(jsonObjectRequest);

    }

    @SuppressLint("WrongConstant")
    private void cargarUI() {
        txtMensaje = findViewById(R.id.txtMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);

        listaChat =new ArrayList<>();
        chat_adapter = new chat_adapter(listaChat);
        recyclerView=findViewById(R.id.recyclerConversacion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(chat_adapter);

    }

    private void enviarMensaje() {
        txtMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!txtMensaje.getText().toString().trim().isEmpty()){
                    btnEnviar.setVisibility(View.VISIBLE);
                }else {
                    btnEnviar.setVisibility(View.GONE);
                }
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                envioMensaje();
                cargarMensajes();
            }
        });
    }

    private void envioMensaje() {
        String url = "http://"+server.getServer()+"/ws/addMensaje.php";
        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Registra")){
                    cargarMensajes();
                    txtMensaje.setText("");
                    closeKeyBoard();

                }else {
                    Toast.makeText(getApplicationContext(), "No se ha podido enviar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al enviar mensaje", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                String mensaje = txtMensaje.getText().toString().trim();
                String idto = idPara;


                Map<String,String> parametros = new HashMap<>();
                parametros.put("deIdUser", iduser);
                parametros.put("paraIdUser", idto);
                parametros.put("mensaje", mensaje);

                return parametros;
            }
        };

        request.add(stringRequest);
    }

    private void obtenerImagen(String userFoto) {
        String urlImage="http://"+server.getServer()+"/photos/"+userFoto;

        ImageRequest imageRequest=new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                toolbarImage.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);
    }

    private void obtenerDatos() {
        Bundle parametros = this.getIntent().getExtras();
        userName=parametros.getString("userName");
        userFoto=parametros.getString("userPhoto");
        idPara=parametros.getString("userId");

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarMensajes();
            }
        });

    }
    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }

}
