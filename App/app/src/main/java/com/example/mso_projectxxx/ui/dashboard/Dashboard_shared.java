package com.example.mso_projectxxx.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mso_projectxxx.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import maes.tech.intentanim.CustomIntent;

public class Dashboard_shared extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Fade fade = new Fade();

        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }



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
