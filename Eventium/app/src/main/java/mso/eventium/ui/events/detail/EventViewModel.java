package mso.eventium.ui.events.detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import mso.eventium.datastorage.entity.EventEntity;

public class EventViewModel extends ViewModel {
    private MutableLiveData<EventEntity> event = new MutableLiveData<>();

    public MutableLiveData<EventEntity> getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event.setValue(event);
    }
}
