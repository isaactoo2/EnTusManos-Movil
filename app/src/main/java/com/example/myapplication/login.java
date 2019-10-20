package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.entidades.url;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class login extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener{

    Button register;
    Button btn;
    TextInputLayout user, pwd;
    TextView recovery_pwd;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String pwdPattern="(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}";
    private ProgressDialog progressDialog;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    url server = new url();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        intiDialog();
        btn= (Button) findViewById(R.id.btnLogin);

        user = (TextInputLayout) findViewById(R.id.txtLUser);
        pwd = (TextInputLayout) findViewById(R.id.txtLPassword);
        recovery_pwd= (TextView)findViewById(R.id.lblFPassword);
        request = Volley.newRequestQueue(getApplicationContext());

        register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(login.this, register.class);
                startActivity(register);

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateText(user, "Complete el campo") | !validateText(pwd, "Complete el campo")){
                    return;
                }
                if (!user.getEditText().getText().toString().trim().matches(emailPattern)){
                    user.setError("Correo electrónico no válido");
                    return;
                }
               cargarWebService();
            }
        });



        recovery_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recovery = new Intent(login.this, recovery_pwd.class);
                startActivity(recovery);
            }
        });

    }
    private void intiDialog(){
        this.progressDialog=new ProgressDialog(this);
    }
    private void showDialog(){
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Iniciando sesión");
        progressDialog.show();
    }

    private void cargarWebService(){

        String url = "http://"+server.getServer()+"/login/login.php?email="+user.getEditText().getText().toString()+"&pwd="+pwd.getEditText().getText().toString();
        url=url.replace(" ", "%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);

        showDialog();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        if (error instanceof TimeoutError || error instanceof NoConnectionError){

            Toast.makeText(this, "Error de conexión, intentelo de nuevo", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError){

            Toast toast= Toast.makeText(getApplicationContext(),
                    "Datos incorrectos, intentalo otra vez", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            user.getEditText().setText("");
            pwd.getEditText().setText("");
            user.getEditText().requestFocus();
    }
    }

    @Override
    public void onResponse(JSONObject response) {

        /*com.example.entusmanos.user myUser= new user();
        JSONArray json = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try {
            jsonObject=json.getJSONObject(0);
            myUser.setUser(jsonObject.optString("user"));
            myUser.setPwd(jsonObject.optString("pass"));
            myUser.setId(jsonObject.optString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        usuario miusuario = new usuario();
        JSONArray json = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try {
            jsonObject=json.getJSONObject(0);
            miusuario.setUser(jsonObject.optString("usuario"));
            miusuario.setPwd(jsonObject.optString("password"));
            miusuario.setId(jsonObject.optString("iduser"));
            miusuario.setPhoto(jsonObject.optString("photo"));
            miusuario.setName(jsonObject.optString("nombre"));
            miusuario.setLname(jsonObject.optString("apellido"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Toast.makeText(this, "Bienvenido" , Toast.LENGTH_SHORT).show();
        Intent main = new Intent(login.this, MainActivity.class);
        main.putExtra(MainActivity.usuario, miusuario.getName() +" "+ miusuario.getLname());
        main.putExtra(MainActivity.email, miusuario.getUser());
        startActivity(main);
        finish();
        progressDialog.dismiss();
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
}
