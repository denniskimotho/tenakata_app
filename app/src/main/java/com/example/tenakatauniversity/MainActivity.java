package com.example.tenakatauniversity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String url = "https://mwalimubiashara.com/tenakata/get_student_list.php";
    StudentListAdapter studentListAdapter;
    StudentItem studentItem;
    ListView studentListView;
    public static ArrayList< StudentItem>  studentArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentListView = findViewById(R.id.studentListView);

        studentListAdapter = new StudentListAdapter(this,studentArrayList);
        studentListView.setAdapter(studentListAdapter);
        studentListView.setClickable(true);

        getStudent();
    }
    private void getStudent() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
//                progressBar.setVisibility(View.GONE);
                studentArrayList.clear();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")){

                        int l=0;
                        l=jsonArray.length();

                        for(int i=0;i<l;i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String name = object.getString("name");
                            String gender = object.getString("gender");
                            String gps_location = object.getString("gps_location");
                            String marital_status = object.getString("marital_status");
                            int age = object.getInt("age");
                            int IQ = object.getInt("iq");
                            double adm_score = object.getDouble("adm_score");
                            String photo_url = object.getString("photo_url");

                            studentItem = new StudentItem(name,gender,marital_status,IQ,gps_location,adm_score,photo_url,age);
                            studentArrayList.add(studentItem);
                        }

                    }
                    studentListAdapter.notifyDataSetChanged();

                }catch (Exception e){

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Fail to get response ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };

        queue.add(request);
    }
}