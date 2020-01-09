package mso.eventium.ui.map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.button.MaterialButton;

import mso.eventium.R;
import mso.eventium.datastorage.BackendService;
import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinCreateDialog extends DialogFragment {

    private final String token;
    private final MutableLiveData<PinEntity> pin;

    public PinCreateDialog(PinEntity pin, String token) {
        super();
        this.pin = new MutableLiveData<>(pin);
        this.token = token;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.dialog_pin_create, container, false);

        setUpEditText(pin, root);
        setUpButton(root);

        return root;

    }

    private void setUpEditText(MutableLiveData<PinEntity> pin, View root) {
        final EditText mName = root.findViewById(R.id.editTextPinName);
        final EditText mDesc = root.findViewById(R.id.editTextPinDesc);

        final TextWatcher createTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final PinEntity pinEntity = pin.getValue();
                pinEntity.setName(mName.getText().toString());
                pinEntity.setDescription(mDesc.getText().toString());
                pin.setValue(pinEntity);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        mName.addTextChangedListener(createTextWatcher);
        mDesc.addTextChangedListener(createTextWatcher);

    }


    private void setUpButton(View root) {

        final MaterialButton submit = root.findViewById(R.id.buttonCreatePin);

        pin.observe(getViewLifecycleOwner(), pin -> {
            submit.setEnabled(!TextUtils.isEmpty(pin.getName()) && !TextUtils.isEmpty(pin.getDescription()));
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (token == null) {
                    Toast.makeText(getContext(), "Nicht eingeloggt", Toast.LENGTH_LONG).show();

                } else {
                    final BackendService backendService = BackendService.getInstance(getContext());
                    final Call<PinEntity> eventCall = backendService.createPin(pin.getValue(), token);
                    eventCall.enqueue(new Callback<PinEntity>() {

                        @Override
                        public void onResponse(Call<PinEntity> call, Response<PinEntity> response) {
                            PinCreateDialog.this.dismiss();
                            Toast.makeText(getContext(), "Pin erstellt mit ID: " + response.body().getId(), Toast.LENGTH_LONG).show();
                            Log.i("PIN CREATE", "created pin with id: " + response.body().getId());
                        }

                        @Override
                        public void onFailure(Call<PinEntity> call, Throwable t) {
                            Log.e("PIN CREATE", "ERROR", t);

                        }
                    });

                }
            }
        });
    }
}
