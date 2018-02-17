package com.blogspot.salvadorhm.almacendemo00;


import android.app.AlertDialog;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;


public class BuscarProductos extends AppCompatActivity {

    private String URL = "http://demounid.herokuapp.com/api_productos?user_hash=12345&action=get&";
    private String getProductosURL = "";
    private String queryParams = "";
    private Button btn_buscar;
    private EditText et_id_producto;
    private TextView tv_producto;
    private TextView tv_existencias;
    private TextView tv_costo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_productos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        et_id_producto = (EditText) findViewById(R.id.et_id_producto);
        tv_producto = (TextView) findViewById(R.id.tv_producto);
        tv_existencias = (TextView) findViewById(R.id.tv_existencias);
        tv_costo = (TextView) findViewById(R.id.tv_costo);

        btn_buscar = (Button) findViewById(R.id.btn_buscar);
        btn_buscar.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btn_buscar)
                btn_buscar_onClick();
        }
    };


    private void btn_buscar_onClick() {
        String id_producto = et_id_producto.getText().toString();
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("id_producto", id_producto);
        queryParams = builder.build().getEncodedQuery();
        getProductosURL = URL;
        getProductosURL += queryParams;
        Log.d("Parametros", queryParams);
        Log.d("Consulta", getProductosURL);
        webServiceRest(getProductosURL);
    }

    private void webServiceRest(String requestURL){
        try{
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String webServiceResult="";
            while ((line = bufferedReader.readLine()) != null){
                webServiceResult += line;
            }
            bufferedReader.close();
            parseInformation(webServiceResult);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void parseInformation(String jsonResult){
        JSONArray jsonArray = null;
        String id_producto;
        String producto;
        String existencias;
        String costo;
        try{
            jsonArray = new JSONArray(jsonResult);
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (jsonArray != null){
            Log.d("jsonArray ",""+jsonArray.length());
            for(int i=0;i<jsonArray.length();i++){
                try{
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    id_producto = jsonObject.getString("id_producto");
                    producto = jsonObject.getString("producto");
                    existencias = jsonObject.getString("existencias");
                    costo = jsonObject.getString("costo");

                    tv_producto.setText(producto);
                    tv_existencias.setText(existencias);
                    tv_costo.setText(costo);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }else{
            tv_producto.setText("No encontrado");
            tv_existencias.setText("No encontrado");
            tv_costo.setText("No encontrado");
            Message("Error","Registro no encontrado");
        }
    }

    private void Message(String title, String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.show();
    }
}