package com.example.mso_projectxxx.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.media.Image;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mso_projectxxx.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import maes.tech.intentanim.CustomIntent;

public class Dashboard_shared extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_dashboard_detail);
        String name = getIntent().getStringExtra("name");
        String age = getIntent().getStringExtra("age");
        int img = getIntent().getIntExtra("pic",0);

        ImageView IV_pic = (ImageView)findViewById(R.id.person_photo);
        TextView TV_name = (TextView)findViewById(R.id.person_name);
        TextView TV_age = (TextView)findViewById(R.id.person_age);

        TV_name.setText(name);
        TV_age.setText(age);
        IV_pic.setImageResource(img);




    }

    @Override
    public void finish(){
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }
}
