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
import com.example.myapplication.adapter.asesorias_adapter;
import com.example.myapplication.adapter.donaciones_adapter;
import com.example.myapplication.entidades.asesoria;
import com.example.myapplication.entidades.donaciones;
import com.example.myapplication.entidades.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link asesorias.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link asesorias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class asesorias extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btn;
    RecyclerView recyclerAsesoria;
    ArrayList<asesoria> listaAsesorias;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    SwipeRefreshLayout refreshLayout;
    url server = new url();

    String iduser=MainActivity.userId;

    private OnFragmentInteractionListener mListener;

    public asesorias() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment asesorias.
     */
    // TODO: Rename and change types and number of parameters
    public static asesorias newInstance(String param1, String param2) {
        asesorias fragment = new asesorias();
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
        View vista = inflater.inflate(R.layout.fragment_asesorias, container, false);
        listaAsesorias= new ArrayList<>();

        recyclerAsesoria=vista.findViewById(R.id.recyclerAsesorias);
        recyclerAsesoria.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerAsesoria.setHasFixedSize(true);

        request= Volley.newRequestQueue(getContext());
        cargarWebService();

        refreshLayout = vista.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                cargarWebService();
            }
        });

        Button btnN=vista.findViewById(R.id.btnNuevaAsesoria);
        btnN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listapro = new Intent(getContext(), lista_profesional.class);
                startActivityForResult(listapro, 1);
            }
        });

        return vista;
    }

    private void cargarWebService() {
        asesoria data = new asesoria();
        data.setHeader(true);
        listaAsesorias.add(data);

        String url = "http://"+server.getServer()+"/ws/getAsesorias.php?user="+iduser;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==1){
            cargarWebService();
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

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Aun no has realizado una consulta", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        asesoria as =null;
        JSONArray json = response.optJSONArray("datos");

        listaAsesorias.clear();
        try {
            asesoria data = new asesoria();
            data.setHeader(true);
            listaAsesorias.add(data);

            for (int i = 0; i<json.length(); i++){
                as = new asesoria();
                JSONObject jsonObject=null;

                jsonObject=json.getJSONObject(i);


                as.setCuerpoAsesoria(jsonObject.optString("causa"));
                as.setEspecialidad(jsonObject.optString("especialidad"));
                as.setIdAsesoria(jsonObject.optString("idAsesoria"));
                as.setFecha(jsonObject.optString("fecha"));

                listaAsesorias.add(as);
                refreshLayout.setRefreshing(false);
            }

            asesorias_adapter adapter=new asesorias_adapter(listaAsesorias);
            recyclerAsesoria.setAdapter(adapter);
        } catch (JSONException e) {
            Toast.makeText(this.getContext(), "Error "+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();


        }
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
