package mso.eventium.model;

import mso.eventium.R;

public enum CategoryEnum {
    BAR(R.drawable.ic_flaschen), DISCO(R.drawable.ic_cocktails), RESTAURANT(R.drawable.ic_best_choice), CULTURE(R.drawable.ic_person_white_24dp);

    private int icon;

    private CategoryEnum(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }
}
