package com.hoangsong.zumechat.models;

import com.mukesh.countrypicker.models.Country;

/**
 * Created by pvthiendeveloper on 3/23/2017.
 */

public class CountryInfo {
    private Country country;
    private boolean selected;

    public CountryInfo(Country country, boolean selected) {
        this.country = country;
        this.selected = selected;
    }

    public CountryInfo() {
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
