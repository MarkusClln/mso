package mso.eventium.ui.map;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import mso.eventium.R;
import mso.eventium.client.backendClient;

public class createPinPopup extends Activity {

    public static String ARG_LAT = "arg_lat";
    public static String ARG_LNG = "arg_lng";
    public static String ARG_TOKEN = "arg_token";

    private double mLat;
    private double mLng;
    private String mToken;

    private EditText mName;
    private EditText mDesc;
    private MaterialButton mButton;

    private String backendServerIp;
    public backendClient backendClient;
    public RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_create_pin);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.5));


        mLat = getIntent().getDoubleExtra(ARG_LAT, -1);
        mLng = getIntent().getDoubleExtra(ARG_LNG, -1);
        mToken = getIntent().getStringExtra(ARG_TOKEN);

        if(mLat == -1 || mLng == -1 || mToken == null){
            finish();
        }

        setUpEditText();
        setUpButton();
        setUpQueue();

    }

    private void setUpQueue(){
        backendServerIp = getResources().getString(R.string.IP_Server);
        backendClient = new backendClient(backendServerIp);
        queue = Volley.newRequestQueue(this);
    }

    private void setUpEditText(){
        mName = findViewById(R.id.editTextPinName);
        mDesc = findViewById(R.id.editTextPinDesc);
        mName.addTextChangedListener(createTextWatcher);
        mDesc.addTextChangedListener(createTextWatcher);

    }
    private TextWatcher createTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pinName = mName.getText().toString().trim();
            String pinDesc = mDesc.getText().toString().trim();
            mButton.setEnabled(!pinName.isEmpty() && !pinDesc.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setUpButton(){
        mButton = findViewById(R.id.buttonCreatePin);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinName = mName.getText().toString();
                String pinDesc = mDesc.getText().toString();

                Response.Listener rl = new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        finish();
                    }
                };

                JsonRequest request = backendClient.createPin(mToken, mLat, mLng, pinName, pinDesc, rl);
                queue.add(request);

            }
        });
    }
}
