package mso.eventium.model;

import mso.eventium.R;

public enum CategoryEnum {
    BAR(R.mipmap.bar_round, R.drawable.img_drink),
    DISCO(R.mipmap.disco_round, R.drawable.img_disco),
    RESTAURANT(R.mipmap.food_round, R.drawable.img_restaurant),
    CULTURE(R.mipmap.culture_round, R.drawable.img_culture);

    private final int icon;
    private final int picture;

    private CategoryEnum(int icon, int picture) {
        this.icon = icon;
        this.picture = picture;
    }

    public int getIcon() {
        return icon;
    }

    public int getPicture() {
        return picture;
    }
}
