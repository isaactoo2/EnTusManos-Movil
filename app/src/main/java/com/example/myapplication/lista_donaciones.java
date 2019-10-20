package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.adapter.donaciones_adapter;

import com.example.myapplication.entidades.donaciones;

import com.example.myapplication.entidades.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link lista_donaciones.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link lista_donaciones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class lista_donaciones extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btn;

    url server = new url();

    private lista_donaciones.OnFragmentInteractionListener mListener;

    RecyclerView recyclerDonacion;
    ArrayList<donaciones> listaDonaciones;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    SwipeRefreshLayout refreshLayout;

    public lista_donaciones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment lista_donaciones.
     */
    // TODO: Rename and change types and number of parameters
    public static lista_donaciones newInstance(String param1, String param2) {
        lista_donaciones fragment = new lista_donaciones();
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
        View vista = inflater.inflate(R.layout.fragment_lista_donaciones, container, false);
        listaDonaciones= new ArrayList<>();

        recyclerDonacion=vista.findViewById(R.id.recyclerDonaciones);
        recyclerDonacion.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerDonacion.setHasFixedSize(true);

        request= Volley.newRequestQueue(getContext());




        refreshLayout = vista.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                cargarWebService();
            }
        });

        cargarWebService();

        btn=vista.findViewById(R.id.btnPublicarArticulo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent donacion = new Intent(getContext(), add_donacion.class);

                startActivityForResult(donacion, 1);
            }
        });



        return vista;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==1){
            cargarWebService();
        }
    }

    private void cargarWebService() {
        String url = "http://"+server.getServer()+"/login/getDonacion.php";
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        donaciones donacion =null;
        JSONArray json = response.optJSONArray("datos");

        listaDonaciones.clear();
        try {

            for (int i = 0; i<json.length(); i++){
                donacion = new donaciones();
                JSONObject jsonObject=null;

                jsonObject=json.getJSONObject(i);
                donacion.setTituloDonacion(jsonObject.optString("titulo"));
                donacion.setCuerpoDonacion(jsonObject.optString("descripcion"));
                donacion.setPhotoDonacion(jsonObject.optString("imagen"));
                donacion.setUserDonacion(jsonObject.optString("nombre"));
                donacion.setUserUbicacion(jsonObject.optString("ubicacion"));

                listaDonaciones.add(donacion);
                refreshLayout.setRefreshing(false);
            }

            donaciones_adapter adapter=new donaciones_adapter(listaDonaciones, getContext());
            recyclerDonacion.setAdapter(adapter);
        } catch (JSONException e) {
            Toast.makeText(this.getContext(), "Error "+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
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
