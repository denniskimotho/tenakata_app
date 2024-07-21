package com.example.tenakatauniversity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddStudent extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_PERMISSIONS = 2;
    private ImageView imageView;
    private String currentPhotoPath;
    Spinner sp_marital_status, sp_gender;
    Button btn_calc_adm_score, btn_get_gps_location, btn_Add;
    TextInputEditText ed_fname, ed_lname, ed_iq, ed_age, ed_latitude, ed_longitude;
    TextView tvAdmScore,tvCountry;
    double adm_score;
    ArrayAdapter<String> dataAdapterGender, dataAdapter_marital_status;
    ArrayList<String> genderList = new ArrayList<String>();
    ArrayList<String> marital_statusList = new ArrayList<String>();
    LocationManager locationManager;
    String latitude, longitude;
    private static final int REQUEST_LOCATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");

        marital_statusList.add("Select Status");
        marital_statusList.add("Married");
        marital_statusList.add("Single");
        marital_statusList.add("Divorced");

        dataAdapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genderList);
        dataAdapter_marital_status = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, marital_statusList);

        sp_gender = findViewById(R.id.gender_sp);
        sp_marital_status = findViewById(R.id.marital_status_sp);
        btn_Add = findViewById(R.id.btnSave);
        btn_calc_adm_score = findViewById(R.id.btnCalScore);
        btn_Add = findViewById(R.id.btnSave);
        btn_get_gps_location = findViewById(R.id.btnGetLocation);
        ed_fname = findViewById(R.id.et_fname);
        ed_lname = findViewById(R.id.et_lname);
        ed_age = findViewById(R.id.et_age);
        ed_iq = findViewById(R.id.et_IQ);
        tvCountry = findViewById(R.id.tvCountry);
        ed_latitude = findViewById(R.id.et_latitude);
        ed_longitude = findViewById(R.id.et_longitude);
        tvAdmScore = findViewById(R.id.tvScore);

        dataAdapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_marital_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_gender.setAdapter(dataAdapterGender);
        sp_marital_status.setAdapter(dataAdapter_marital_status);

        ActivityCompat.requestPermissions( this,
                new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        btn_calc_adm_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = sp_gender.getSelectedItem().toString();
                String str_age = ed_age.getText().toString();
                String str_iq = ed_iq.getText().toString();
                String str_fname = ed_fname.getText().toString();
                String str_lname = ed_lname.getText().toString();
                String marital_status = sp_marital_status.getSelectedItem().toString();

                if(str_age.isEmpty()||str_iq.isEmpty()||str_fname.isEmpty()||str_lname.isEmpty()||marital_status.equals("")||gender.equals("")){
                    Toast.makeText(AddStudent.this, "Fill in all spaces ", Toast.LENGTH_SHORT).show();
                }else {

                    int age = Integer.parseInt(str_age);
                    int iq = Integer.parseInt(str_iq);


                    String countryName = getCountryNameFromLocation(Double.parseDouble(ed_latitude.getText().toString()),
                            Double.parseDouble(ed_longitude.getText().toString()));
                    if (countryName != null) {

                        tvCountry.setText("Country : " + countryName);
                    } else {

                        tvCountry.setText("Country not found");
                    }

                    if (tvCountry.getText().toString().equals("Country : Kenya")) {
                        adm_score = calculateAdmScore(gender, age, iq, marital_status);
                        tvAdmScore.setText("Admission Score: " + adm_score);
                        btn_Add.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "This admission is not Available in your country", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        btn_get_gps_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GPSLocator gpsLocator = new GPSLocator(getApplicationContext());
                Location location = gpsLocator.GetLocation();
                if(location != null){
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
//                    Toast.makeText(getApplicationContext(), "Location latitude is :"+latitude+" longitude is :"+longitude, Toast.LENGTH_SHORT).show();
                    ed_latitude.setText(""+latitude);
                    ed_longitude.setText(""+longitude);
                    String countryName = getCountryNameFromLocation(latitude, longitude);
                if (countryName != null) {
                    Toast.makeText(AddStudent.this, "Country: " + countryName, Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "Country is " + countryName);
                    tvCountry.setText("Country : "+countryName);
                } else {

//                    Toast.makeText(AddStudent.this, "Country not found", Toast.LENGTH_SHORT).show();
                    tvCountry.setText("Country not found");
                }
                }
            }
        });
    }

    private String getCountryNameFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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

    private double calculateAdmScore(String gender, int age, int iq, String maritalStatus) {


        if (iq < 100) {
            return 0.000;
        }

        double score = 1.000;

        if (gender.equals("Female")) {
            score = score * 1.565; // Female candidates have a 56.5% higher chance
        }

        if (age > 43) {
            score *= 2; // People above 43 have 2 times the chances
        } else if (age < 26) {
            score /= 2; // People below 26 have half the chances
        }

        // Marital status is not considered in the score but mentioned for completeness
        if (maritalStatus.toLowerCase().equals("married")) {
            // Additional logic can be added here if the concern needs addressing
        }

        return score;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
        }
        imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
            } else {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle error
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "YOUR_PACKAGE_NAME.provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }
}