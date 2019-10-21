package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.adapter.noticias_adapter;
import com.example.myapplication.entidades.noticias;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements home.OnFragmentInteractionListener,lista_foros.OnFragmentInteractionListener , asesorias.OnFragmentInteractionListener ,lista_donaciones.OnFragmentInteractionListener ,perfil.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener, Response.Listener<JSONObject>, Response.ErrorListener  {
    public static final String usuario = "usuario";
    public static final String email = "email";
    TextView lblnombre, lblemail;
    public LinearLayout headerL;

    RecyclerView recyclerNoticias;
    ArrayList<noticias> listaNoticias;
    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    SwipeRefreshLayout refreshLayout;

    home home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        home=new home();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);

        String nombre = getIntent().getStringExtra("usuario");
        String navEmail = getIntent().getStringExtra("email");
        try {
            lblnombre=(TextView)hView.findViewById(R.id.lblUser);
            lblnombre.setText(nombre);
            lblemail=(TextView)hView.findViewById(R.id.lblEmail);
            lblemail.setText(navEmail);
        }catch (Exception e){
            Toast.makeText(this, "error "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }



        /*noticias header = new noticias();
        header.setHeader(true);
        listaNoticias.add(header);*/




    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("¿Está seguro de cerrar sesión?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Si",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment miFragment=null;
        Class fragmentClass = null;
        boolean fragmentSelecionado=false;


        if (id == R.id.nav_home) {
            miFragment=new home();
            fragmentSelecionado=true;

        } else if (id == R.id.nav_profile) {
            miFragment=new perfil();
            fragmentSelecionado=true;

        } else if (id == R.id.nav_forum) {
            miFragment=new lista_foros();
            fragmentSelecionado=true;
        } else if (id == R.id.nav_inter) {
            miFragment=new lista_donaciones();
            fragmentSelecionado=true;

        } else if (id == R.id.nav_donaciones) {

            /*Intent donacion = new Intent(MainActivity.this, add_donacion.class);
            startActivity(donacion);*/



        } else if (id == R.id.nav_exit) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("¿Está seguro de cerrar sesión?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Si",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                            Intent intent=new Intent(MainActivity.this, login.class);
                            startActivity(intent);
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else if (id == R.id.nav_asesoria){
            miFragment=new asesorias();
            fragmentSelecionado=true;

        } else if (id == R.id.nav_faq) {

            Intent faqs = new Intent(MainActivity.this, faqs.class);
            startActivity(faqs);

        } else if (id == R.id.nav_setts) {

        }
        

        if (fragmentSelecionado==true){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, miFragment).commit();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onResponse(JSONObject response) {


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
