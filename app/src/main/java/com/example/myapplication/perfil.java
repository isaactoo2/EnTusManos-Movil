package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.myapplication.adapter.foros_adapter;
import com.example.myapplication.entidades.foros;
import com.example.myapplication.entidades.mensajes;
import com.example.myapplication.entidades.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link perfil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class perfil extends Fragment implements View.OnClickListener, foros_adapter.OnForoListener {

    String iduser=MainActivity.userId;
    RequestQueue request;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    ArrayList<foros> listaForos;
    url server = new url();
    RecyclerView recyclerForos;
    TextView usuario, correo, ubicacion;
    CircularImageView photo;
    SwipeRefreshLayout refreshLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public perfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment perfil.
     */
    // TODO: Rename and change types and number of parameters
    public static perfil newInstance(String param1, String param2) {
        perfil fragment = new perfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vista = inflater.inflate(R.layout.fragment_perfil, container, false);
        listaForos= new ArrayList<>();

        usuario=vista.findViewById(R.id.periflUsuario);
        correo=vista.findViewById(R.id.correoUsuario);
        ubicacion=vista.findViewById(R.id.direccionUsuario);
        photo=vista.findViewById(R.id.userImage);

        request = Volley.newRequestQueue(getContext());

        recyclerForos=vista.findViewById(R.id.recyclerForos);
        recyclerForos.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerForos.setHasFixedSize(true);


        cargarInformacion();

        refreshLayout = vista.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                cargarInformacion();
            }
        });


        return vista;
    }

    private void cargarFoto(String userFoto) {
        String urlImage="http://"+server.getServer()+"/photos/"+userFoto;

        ImageRequest imageRequest=new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                photo.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);
    }


    private void cargarInformacion() {
        String url = "http://"+server.getServer()+"/ws/getUser.php?id="+iduser;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json = response.optJSONArray("datos");
                JSONObject jsonObject = null;
                try {
                    jsonObject=json.getJSONObject(0);
                    usuario.setText(jsonObject.optString("nombre"));
                    correo.setText(jsonObject.optString("usuario"));
                    ubicacion.setText(jsonObject.optString("ubicacion"));
                    cargarFoto(jsonObject.optString("photo"));
                    cargarForos(iduser);


                }catch (Exception e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.add(jsonObjectRequest);
    }

    private void cargarForos(String iduser) {
        String url = "http://"+server.getServer()+"/ws/getForoUsuario.php?id="+iduser;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                foros foro =null;
                JSONArray json = response.optJSONArray("datos");

                listaForos.clear();
                try {
                    foros data = new foros();
                    data.setHeader(true);
                    listaForos.add(data);

                    for (int i = 0; i<json.length(); i++){
                        foro = new foros();
                        JSONObject jsonObject=null;

                        jsonObject=json.getJSONObject(i);

                        foro.setTituloForo(jsonObject.optString("titulo"));
                        foro.setCuerpoForo(jsonObject.optString("cuerpo"));
                        foro.setCategoriaForo(jsonObject.optString("categoria"));
                        foro.setUserForo(jsonObject.optString("nombre"));
                        foro.setFechaForo(jsonObject.optString("fecha_foro"));
                        foro.setIdForo(jsonObject.optString("idForo"));
                        foro.setIdUserForo(jsonObject.optString("iduser"));


                        listaForos.add(foro);
                        refreshLayout.setRefreshing(false);
                    }

                    foros_adapter adapter = new foros_adapter(listaForos,perfil.this, getContext());
                    recyclerForos.setAdapter(adapter);
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error "+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.add(jsonObjectRequest);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onForoClick(int position) {
        if (position>0){
            Intent intent = new Intent(getContext(), ver_foro.class);
            intent.putExtra("idForo", listaForos.get(position).getIdForo());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
