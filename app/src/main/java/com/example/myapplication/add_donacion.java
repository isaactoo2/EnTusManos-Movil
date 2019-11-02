package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.entidades.url;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class add_donacion extends AppCompatActivity {
    private ProgressDialog progressDialog;
    StringRequest stringRequest;
    ImageView imgDonacion;
    TextInputLayout titulo, descripcion;
    String iduser=MainActivity.userId;
    Button btn, cancelar;
    private final String CAMERA_RAIZ="misDonaciones/";
    private final String RUTA_IMAGEM=CAMERA_RAIZ+"fotos";

    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;
    public String foto;
    Bitmap bitmap;
    Bitmap emptyBitmap;

    String path;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    url server = new url();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_donacion);
        request = Volley.newRequestQueue(getApplicationContext());
        intiDialog();



        titulo = (TextInputLayout) findViewById(R.id.txtTitulo);
        descripcion = (TextInputLayout) findViewById(R.id.txtDescrip);
        btn=findViewById(R.id.btnPublicar);
        cancelar=findViewById(R.id.btnCancelar);


        imgDonacion = findViewById(R.id.imgDonacion);
        imgDonacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagenes();
            }
        });

        if (validaPermisos()){
            imgDonacion.setEnabled(true);
        } else {
            imgDonacion.setEnabled(false);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateText(titulo, "Complete el campo") | !validateText(descripcion, "Complete el campo")){
                    return;
                }
                cargarWebService();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void cargarWebService(){
        showDialog();
        String url = "http://"+server.getServer()+"/ws/addDonacionMovil.php";
        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Registra")){
                    titulo.getEditText().setText("");
                    descripcion.getEditText().setText("");
                    imgDonacion.setImageResource(R.drawable.ic_menu_camera);
                    Toast.makeText(add_donacion.this, "Publicado", Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(add_donacion.this, "No se ha podido registrar", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(add_donacion.this, "Error al publicar", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String tituloE = titulo.getEditText().getText().toString();
                String descripcionE = descripcion.getEditText().getText().toString();

                String image = convertirImgString(bitmap);

                Map<String,String> parametros = new HashMap<>();
                parametros.put("iduser", iduser);
                parametros.put("titulo", tituloE);
                parametros.put("descripcion", descripcionE);
                parametros.put("ruta", image);


                return parametros;
            }
        };
        request.add(stringRequest);

    }

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,array);
        byte[] imagenByte=array.toByteArray();
        String imageString= Base64.encodeToString(imagenByte,Base64.DEFAULT);
        return imageString;
    }

    private boolean validaPermisos() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        if ((shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100){
            if (grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                imgDonacion.setEnabled(true);
            }else {
                solicitarPermisos();
            }
        }
    }

    private void solicitarPermisos() {
        final CharSequence[] opciones ={"Si", "No"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(add_donacion.this);
        alertOpciones.setTitle("¿Desea configurar los permisos?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }else {
                    Toast.makeText(add_donacion.this, "Permisos no aceptados", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder alert = new AlertDialog.Builder(add_donacion.this);
        alert.setTitle("Permisos desactivados");
        alert.setMessage("Debe aceptar los permisos");
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        alert.show();
    }

    private void cargarImagenes() {

        final CharSequence[] opciones ={"Tomar foto", "Cargar imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(add_donacion.this);
        alertOpciones.setTitle("Seleciona una opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar foto")){
                    tomarFoto();
                }else {
                    if (opciones[i].equals("Cargar imagen")){
                        Intent intent= new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"),COD_SELECCIONA);
                    }else {
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();

    }

    private void tomarFoto() {
        File file = new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEM);
        boolean isCreada=file.exists();
        String nombre="";
        if (!isCreada){
            isCreada=file.mkdirs();
        }else if (isCreada==true){
            nombre = (System.currentTimeMillis()/1000)+".jpg";


        }

        path=Environment.getExternalStorageDirectory()+File.separator+RUTA_IMAGEM+File.separator+nombre;

        File imagen = new File(path);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager())!= null){
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(add_donacion.this, BuildConfig.APPLICATION_ID + ".provider",imagen));
            startActivityForResult(intent, COD_FOTO);
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", path);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        path = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case COD_FOTO:
                    MediaScannerConnection.scanFile(this,
                            new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    foto = path;
                    bitmap = BitmapFactory.decodeFile(path);
                    imgDonacion.setImageBitmap(bitmap);
                    break;
                case COD_SELECCIONA:

                    Uri path = data.getData();
                    foto = path.toString();
                    imgDonacion.setImageURI(path);
                    try {
                        bitmap=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), path);
                        imgDonacion.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
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
        progressDialog.setMessage("Publicando");
        progressDialog.show();
    }
}
