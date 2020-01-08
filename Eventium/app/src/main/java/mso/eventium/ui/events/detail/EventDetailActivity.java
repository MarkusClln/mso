package mso.eventium.ui.events.detail;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import mso.eventium.R;
import mso.eventium.datastorage.BackendService;
import mso.eventium.datastorage.entity.EventEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventDetailActivity extends AppCompatActivity {

    public static final String ARG_EVENT_ID = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_event_detail);

        final View eventDetailFragment = findViewById(R.id.fragment_event_detail);
        eventDetailFragment.setVisibility(View.INVISIBLE);
        postponeEnterTransition();


        final EventViewModel model = ViewModelProviders.of(this).get(EventViewModel.class);

        final BackendService backendService = BackendService.getInstance(getApplicationContext());
        final Call<EventEntity> eventCall = backendService.getEventById(getIntent().getStringExtra(ARG_EVENT_ID));
        eventCall.enqueue(new Callback<EventEntity>() {

            @Override
            public void onResponse(Call<EventEntity> call, Response<EventEntity> response) {
                final EventEntity event = response.body();
                model.setEvent(event);
                eventDetailFragment.setVisibility(View.VISIBLE);
                startPostponedEnterTransition();
            }

            @Override
            public void onFailure(Call<EventEntity> call, Throwable t) {

            }
        });
    }
}
