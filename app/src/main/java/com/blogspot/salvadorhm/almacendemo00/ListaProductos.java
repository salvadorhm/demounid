package com.blogspot.salvadorhm.almacendemo00;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ListaProductos extends AppCompatActivity {

    private ListView lv_productos_list;
    private ArrayAdapter adapter;
    private String getAllProductosURL = "https://demounid.herokuapp.com/api_productos?user_hash=12345&action=get";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        lv_productos_list = (ListView)findViewById(R.id.lv_contacts_list1);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        webServiceRest(getAllProductosURL);
        lv_productos_list.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        for(int i=0;i<jsonArray.length();i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                id_producto = jsonObject.getString("id_producto");
                producto = jsonObject.getString("producto");
                existencias = jsonObject.getString("existencias");
                costo = jsonObject.getString("costo");
                adapter.add(id_producto + ": " + producto);
                Log.d("producto",producto);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
