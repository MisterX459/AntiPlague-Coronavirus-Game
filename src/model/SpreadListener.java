package model;

public interface SpreadListener {
    void onAirSpread(Country fromCountry, Country toCountry);
    void onLandSpread(Country fromCountry, Country toCountry);
    void onSeaSpread(Country fromCountry, Country toCountry);
}