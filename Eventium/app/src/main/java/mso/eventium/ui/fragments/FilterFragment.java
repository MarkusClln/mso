package mso.eventium.ui.fragments;
;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;


import com.google.android.material.slider.Slider;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.ui.events.EventFragment;


public class FilterFragment extends Fragment {

    private boolean isVisible = false;
    final Calendar myCalendar = Calendar.getInstance();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_filter, container, false);



        final ToggleButton filterButton = root.findViewById(R.id.filter_button);


        final ConstraintLayout constraintLayout = root.findViewById(R.id.constraint_layout);
        final int targtetHeight = constraintLayout.getMeasuredHeight();
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConstraintLayout layout = root.findViewById(R.id.constraint_layout);

                if(isVisible){
                    Resources res = getResources();
                    Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.ic_filter, null);
                    filterButton.setBackgroundDrawable(drawable);
                    collapse(constraintLayout);
                    isVisible =false;
                }else{
                    Resources res = getResources();
                    Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.ic_filter_filled, null);
                    filterButton.setBackgroundDrawable(drawable);
                    expand(constraintLayout, 300, 500);
                    isVisible =true;
                }




            }
        });

        setUpCalenderPicker(root);
        setUpTimePicker(root);
        setUpRangeSlider(root);
        return root;



    }



    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void expand(final View v, int duration, int targetHeight) {

        int prevHeight  = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }


    public void setUpCalenderPicker(View v){
        final TextView textView= (TextView) v.findViewById(R.id.dateText);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

                textView.setText(sdf.format(myCalendar.getTime()));
            }

        };

        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    public void setUpTimePicker(View v){

        final TextView textView= (TextView) v.findViewById(R.id.timeText);
        final TimePickerDialog.OnTimeSetListener date = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                myCalendar.set(Calendar.HOUR, hour);
                myCalendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                textView.setText(sdf.format(myCalendar.getTime()));
            }

        };

        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(),AlertDialog.THEME_HOLO_LIGHT, date, myCalendar.get(Calendar.HOUR),myCalendar.get(Calendar.MINUTE),true).show();
            }
        });
    }

    public void setUpRangeSlider(View v){
        final Slider slider= (Slider) v.findViewById(R.id.rangeSlider);
        final TextView textView= (TextView) v.findViewById(R.id.slider_value);
        slider.setValueFrom(0);
        slider.setValueTo(100);


        slider.setOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value) {
                if((int)value ==0){
                    textView.setText("default");
                }else{
                    textView.setText((int)value+" km");
                }

            }
        });

    }
}
