package com.aminmahboubi.housing.model;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by amin on 2/6/18.
 */

@RunWith(MockitoJUnitRunner.class)
public class HouseTest extends TestCase {

    House house;

    @Before
    public void initMocks() {
        house = new House();
    }

    @Test
    public void toJsonTest() throws JSONException {
        assertFalse(house.toJSON().has("_id"));
        assertFalse(house.toJSON().has("insertDate"));
        assertFalse(house.toJSON().has("updateDate"));
    }

    @Test
    public void getLatLngTest() {
        house.setLat(33.33);
        house.setLng(44.44);

        assertEquals(house.getLatLng(), new LatLng(33.33, 44.44));
    }

    @Test
    public void campusTest() {
        assertEquals(House.Campus.Leonardo.toString(), "Leonardo");
        assertEquals(House.Campus.Bovisa.toString(), "Bovisa");
        assertEquals(House.Campus.None.toString(), "None");

        house.setCampus(House.Campus.Bovisa);
        assertEquals(house.getCampus(), House.Campus.Bovisa);

        house.setCampus(House.Campus.Leonardo);
        assertEquals(house.getCampus(), House.Campus.Leonardo);

        house.setCampus(House.Campus.None);
        assertEquals(house.getCampus(), House.Campus.None);
    }

    @Test
    public void bedTest() {
        assertEquals(House.Bed.Single.toString(), "Single");
        assertEquals(House.Bed.Double.toString(), "Double");
        assertEquals(House.Bed.King.toString(), "King");

        house.setBed(House.Bed.Single);
        assertEquals(house.getBed(), House.Bed.Single);

        house.setBed(House.Bed.Double);
        assertEquals(house.getBed(), House.Bed.Double);

        house.setBed(House.Bed.King);
        assertEquals(house.getBed(), House.Bed.King);
    }

    @Test
    public void preferredSexTest() {
        assertEquals(House.PreferredSex.Boy.toString(), "Boy");
        assertEquals(House.PreferredSex.Girl.toString(), "Girl");
        assertEquals(House.PreferredSex.Both.toString(), "Both");

        house.setPreferredSex(House.PreferredSex.Boy);
        assertEquals(house.getPreferredSex(), House.PreferredSex.Boy);

        house.setPreferredSex(House.PreferredSex.Girl);
        assertEquals(house.getPreferredSex(), House.PreferredSex.Girl);

        house.setPreferredSex(House.PreferredSex.Both);
        assertEquals(house.getPreferredSex(), House.PreferredSex.Both);

        house.setPreferredSex("Boy");
        assertEquals(house.getPreferredSex(), House.PreferredSex.Boy);

        house.setPreferredSex("Girl");
        assertEquals(house.getPreferredSex(), House.PreferredSex.Girl);

        house.setPreferredSex("Both");
        assertEquals(house.getPreferredSex(), House.PreferredSex.Both);
    }

    @Test
    public void houseTypeTest() {
        assertEquals(House.HouseType.Bed.toString(), "Bed");
        assertEquals(House.HouseType.Bedroom.toString(), "Bedroom");
        assertEquals(House.HouseType.House.toString(), "House");

        house.setHouseType(House.HouseType.Bed);
        assertEquals(house.getHouseType(), House.HouseType.Bed);

        house.setHouseType(House.HouseType.Bedroom);
        assertEquals(house.getHouseType(), House.HouseType.Bedroom);

        house.setHouseType(House.HouseType.House);
        assertEquals(house.getHouseType(), House.HouseType.House);


        house.setHouseType("Bed");
        assertEquals(house.getHouseType(), House.HouseType.Bed);

        house.setHouseType("Bedroom");
        assertEquals(house.getHouseType(), House.HouseType.Bedroom);

        house.setHouseType("House");
        assertEquals(house.getHouseType(), House.HouseType.House);
    }

}
