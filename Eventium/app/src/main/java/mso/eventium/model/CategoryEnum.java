package mso.eventium.model;

import mso.eventium.R;

public enum CategoryEnum {
    BAR(R.mipmap.bar_round), DISCO(R.mipmap.disco_round), RESTAURANT(R.mipmap.food_round), CULTURE(R.mipmap.culture_round);

    private int icon;

    private CategoryEnum(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }
}
