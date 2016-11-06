package edu.csumb.hashmapsallday.hungrylittlemonsters;

/**
 * Created by karentafolla on 11/5/16.
 */

public class Location {
    private  String name;
    private String place;
    private String latitude;
    private String longitude;
    private String address;

    public Location(){

    }
    public Location(String place){
        this.place =  place;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude(){
        return latitude;
    }

    public String getLongitude(){
        return longitude;
    }


    @Override
    public String toString() {
        return "Monster [Name = " + name + " Latitude= "+ latitude + " Longitude= " + longitude + " Address= " + address + "]";
    }
}
