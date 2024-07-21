package com.example.tenakatauniversity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class StudentListAdapter extends ArrayAdapter<StudentItem> {

    Context context;
    List<StudentItem> studentList;
    public StudentListAdapter(@NonNull Context context, List<StudentItem>dataClasses) {
        super(context, R.layout.student_item,dataClasses );

        this.context = context;
        this.studentList = dataClasses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,null,true);


        TextView tvName = view.findViewById(R.id.tvName);
        TextView genderTv = view.findViewById(R.id.tvGender);
        TextView tvAge = view.findViewById(R.id.tvAge);
        TextView tvScore = view.findViewById(R.id.tvScore);
        TextView tvMarital_status = view.findViewById(R.id.tvMaritalStatus);
        TextView tvIQ = view.findViewById(R.id.tvIQ);
        TextView tvLocation = view.findViewById(R.id.tvLocation);
        ImageView photo  = view.findViewById(R.id.photo);


        tvName.setText(studentList.get(position).getName());
        genderTv.setText(studentList.get(position).getGender());
        tvAge.setText("Age: "+studentList.get(position).getAge());
        tvScore.setText("Adm Score: "+studentList.get(position).getAdm_score());
        tvMarital_status.setText(studentList.get(position).getMarital_status());
        tvIQ.setText("IQ: "+studentList.get(position).getIq());
        tvLocation.setText(setLatitudeAndLongitude(studentList.get(position).getLocation()));

        return view;
    }

   private String setLatitudeAndLongitude(String gps){
       String[] sp = gps.split(",");
       String latitude = sp[0];
       String longitude = sp[1];

       String countryName = getCountryNameFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude));
       if (countryName == null) {

           countryName="No Country";
       }
        return countryName;
   }

    private String getCountryNameFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getCountryName();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}