package com.aminmahboubi.housing.model;

import android.content.Context;
import android.provider.Settings;

import com.aminmahboubi.housing.MainActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by amin on 1/26/18.
 */


public class House implements Serializable{

    String _id;
    String name;
    String surname;
    Integer price;
    Boolean inclusive;
    Integer bills;
    Integer deposit;
    String address;
    Double lat;
    Double lng;
    Campus campus;
    String email;
    String phone;
    PreferredSex preferredSex;
    String description;
    Integer minimumStayRequired;
    Boolean english;
    HouseType houseType;
    Bed bed;
    Integer numberOfRooms;
    Integer numberOfPeoples;
    Boolean pet;
    Integer floor;
    Boolean lift;
    Integer area;
    List<String> imageLinks;
    Date availability;
    Date insertDate;
    Date updateDate;

    public House() {
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public LatLng getLatLng(){
        return new LatLng(this.getLat(), this.getLng());
    }

    public JSONObject toJSON(Context context) throws JSONException {
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(gson.toJson(this));
        jsonObject.remove("_id");
        jsonObject.remove("insertDate");
        jsonObject.put("_uid", FirebaseAuth.getInstance().getCurrentUser().getUid());

        return jsonObject;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getInclusive() {
        return inclusive;
    }

    public void setInclusive(Boolean inclusive) {
        this.inclusive = inclusive;
    }

    public Integer getBills() {
        return bills;
    }

    public void setBills(Integer bills) {
        this.bills = bills;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public void setCampus(String campus) {
        if (campus.equals("Bovisa"))
            this.campus = Campus.Bovisa;
        else if (campus.equals("Leonardo"))
            this.campus = Campus.Leonardo;
        else if (campus.equals("None"))
            this.campus = Campus.None;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PreferredSex getPreferredSex() {
        return preferredSex;
    }

    public void setPreferredSex(PreferredSex preferredSex) {
        this.preferredSex = preferredSex;
    }
    public void setPreferredSex(String preferredSex) {
        if (preferredSex.equals("Girl"))
            this.preferredSex = PreferredSex.Girl;
        else if (preferredSex.equals("Both"))
            this.preferredSex = PreferredSex.Both;
        else if (preferredSex.equals("Boy"))
            this.preferredSex = PreferredSex.Boy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinimumStayRequired() {
        return minimumStayRequired;
    }

    public void setMinimumStayRequired(Integer minimumStayRequired) {
        this.minimumStayRequired = minimumStayRequired;
    }

    public Boolean getEnglish() {
        return english;
    }

    public void setEnglish(Boolean english) {
        this.english = english;
    }

    public HouseType getHouseType() {
        return houseType;
    }

    public void setHouseType(HouseType houseType) {
        this.houseType = houseType;
    }

    public void setHouseType(String houseType) {
        if (houseType.equals("Bed"))
            this.houseType = HouseType.Bed;
        else if (houseType.equals("Bedroom"))
            this.houseType = HouseType.Bedroom;
        else if (houseType.equals("House"))
            this.houseType = HouseType.House;
    }

    public Bed getBed() {
        return bed;
    }

    public void setBed(Bed bed) {
        this.bed = bed;
    }

    public void setBed(String bed) {
        if (bed.equals("Single"))
            this.bed = Bed.Single;
        else if (bed.equals("King"))
            this.bed = Bed.King;
        else if (bed.equals("Double"))
            this.bed = Bed.Double;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Integer getNumberOfPeoples() {
        return numberOfPeoples;
    }

    public void setNumberOfPeoples(Integer numberOfPeoples) {
        this.numberOfPeoples = numberOfPeoples;
    }

    public Boolean getPet() {
        return pet;
    }

    public void setPet(Boolean pet) {
        this.pet = pet;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Boolean getLift() {
        return lift;
    }

    public void setLift(Boolean lift) {
        this.lift = lift;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public List<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public Date getAvailability() {
        return availability;
    }

    public void setAvailability(Date availability) {
        this.availability = availability;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "House{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", price=" + price +
                ", inclusive=" + inclusive +
                ", bills=" + bills +
                ", deposit=" + deposit +
                ", address='" + address + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", campus=" + campus +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", preferredSex=" + preferredSex +
                ", description='" + description + '\'' +
                ", minimumStayRequired=" + minimumStayRequired +
                ", english=" + english +
                ", houseType=" + houseType +
                ", bed=" + bed +
                ", numberOfRooms=" + numberOfRooms +
                ", numberOfPeoples=" + numberOfPeoples +
                ", pet=" + pet +
                ", floor=" + floor +
                ", lift=" + lift +
                ", area=" + area +
                ", imageLinks=" + imageLinks +
                ", availability=" + availability +
                ", insertDate=" + insertDate +
                ", updateDate=" + updateDate +
                '}';
    }

    public enum Campus {
        @SerializedName("0")Leonardo,
        @SerializedName("1")Bovisa,
        @SerializedName("2")None;
    }

    public enum PreferredSex {
        @SerializedName("0")Girl,
        @SerializedName("1")Boy,
        @SerializedName("2")Both;
    }

    public enum HouseType {
        @SerializedName("0")Bed,
        @SerializedName("1")Bedroom,
        @SerializedName("2")House;
    }

    public enum Bed {
        @SerializedName("0")Single,
        @SerializedName("1")Double,
        @SerializedName("2")King;
    }
}
