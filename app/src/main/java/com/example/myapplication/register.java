package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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

import org.json.JSONObject;

public class register extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener{
    Spinner spnGender;
    ImageView imgBack;
    String[] gender={"Seleccionar genero", "Femenino", "Masculino"};
    TextInputLayout name, lname, email, pwd, pwd2, address;
    Button btnRegister;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String pwdPattern="(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}";
    private ProgressDialog progressDialog;
    url server = new url();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        intiDialog();
        name= (TextInputLayout)findViewById(R.id.txtName);
        lname= (TextInputLayout)findViewById(R.id.txtLastName);
        email=(TextInputLayout)findViewById(R.id.txtEmail);
        pwd=(TextInputLayout)findViewById(R.id.txtPassword);
        pwd2=(TextInputLayout)findViewById(R.id.txtPassword2);
        address=(TextInputLayout)findViewById(R.id.txtAddress);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spnGender = (Spinner) findViewById(R.id.sprGender);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(adapter);

        request = Volley.newRequestQueue(getApplicationContext());

        btnRegister=(Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                register();
            }
        });
        //Clean errors
        name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    unsetError(name);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    unsetError(lname);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    unsetError(email);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pwd.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    unsetError(pwd);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pwd2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    unsetError(pwd2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        address.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    unsetError(address);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //End clean errors
    }

    private void intiDialog(){
        this.progressDialog=new ProgressDialog(this);
    }
    private void showDialog(){
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Registrando usuario");
        progressDialog.setMessage("Por favor, espere un momento");
        progressDialog.show();
    }

    public void register(){
        if (!validateText(name, "Complete el campo") | !validateText(lname, "Complete el campo") | !validateText(email, "Complete el campo") | !validateText(pwd, "Complete el campo") | !validateText(pwd2, "Complete el campo") | !validateText(address, "Complete el campo") | !validateSpn(spnGender, "Selecciona un genero")) {
            return;
        }
        if (!email.getEditText().getText().toString().trim().matches(emailPattern)){
            email.setError("Correo electrónico no válido");
            return;
        }
        if (!validatePwd(pwd, getString(R.string.pwdError)) | !validatePwd(pwd2, getString(R.string.pwdError))){
            return;
        }
        String pwdConfirm = pwd.getEditText().getText().toString().trim();
        String pwdConfirm2 = pwd2.getEditText().getText().toString().trim();
        if ( pwdConfirm.equals(pwdConfirm2) ){

            cargarWebService();
        }else{
            Toast.makeText(this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }


    }

    private void cargarWebService() {
        try {
            String url = "http://"+server.getServer()+"/login/registro.php?name="+name.getEditText().getText().toString()+"&lname="+lname.getEditText().getText().toString()+"&email="+email.getEditText().getText().toString()+"&pwd="+pwd.getEditText().getText().toString()+"&pwd2="+pwd2.getEditText().getText().toString()+"&address="+address.getEditText().getText().toString()+"&gender="+spnGender.getSelectedItem().toString();
            url=url.replace(" ", "%20");
            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            request.add(jsonObjectRequest);
            showDialog();
        } catch (Exception e){
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }


    }

    private void unsetError(TextInputLayout textInputLayout){
        textInputLayout.setError(null);
    }
    //Validacion de texto
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

    private boolean validatePwd(TextInputLayout textInputLayout, String error){
        String edidText = textInputLayout.getEditText().getText().toString().trim();

        if (!edidText.matches(pwdPattern)){
            textInputLayout.setError(error);
            return false;
        }else {
            textInputLayout.setError(null);
            return true;
        }
    }
    //Validacion de combo
    private boolean validateSpn(Spinner spn, String error){
        String item = spn.getSelectedItem().toString();

        if (item == "Seleccionar genero"){
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        if (error instanceof TimeoutError || error instanceof NoConnectionError){
            Toast.makeText(this, "Error al registrar, intentelo de nuevo", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError){
            email.setError("Correo electrónico ya registrado");
        }



    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialog.dismiss();
        Toast toast= Toast.makeText(getApplicationContext(),
                "¡REGRISTRADO CORRECTAMENTE! INICIA SESIÓN", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        finish();
    }

}
