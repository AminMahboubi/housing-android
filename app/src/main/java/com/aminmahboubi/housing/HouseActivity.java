package com.aminmahboubi.housing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TextView;

import com.aminmahboubi.housing.model.Favourite;
import com.aminmahboubi.housing.model.House;
import com.aminmahboubi.housing.model.UniqueIdentifier;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class HouseActivity extends AppCompatActivity {
    final String TAG = "HouseActivity";

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);

    FloatingActionButton fab;

    TextView name;
    TextView email;
    TextView phone;

    TextView address;
    TextView campus;
    TextView houseType;
    TextView bed;

    TextView numberOfRooms;
    TextView numberOfPeoples;
    TextView floor;
    TextView area;

    TextView preferredSex;

    TextView price;
    CheckBox billsCheckbox;
    TextView bills;
    TextView deposit;

    TextView availability;
    TextView minimumStayRequired;

    CheckBox pet;
    CheckBox english;
    CheckBox lift;

    TextView description;

    View editableCard;
    Button edit;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUI();

        House house = (House) getIntent().getExtras().getSerializable("house");

        initData(getApplicationContext(), house, getIntent().getExtras().getBoolean("editable"));


    }

    private void initUI() {
        fab = findViewById(R.id.fav);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        address = findViewById(R.id.address);

        campus = findViewById(R.id.campus);
        houseType = findViewById(R.id.house);
        bed = findViewById(R.id.bed);

        numberOfRooms = findViewById(R.id.numberOfRooms);
        numberOfPeoples = findViewById(R.id.numberOfPeoples);
        floor = findViewById(R.id.floor);
        area = findViewById(R.id.area);

        preferredSex = findViewById(R.id.preferredSex);

        price = findViewById(R.id.price);
        billsCheckbox = findViewById(R.id.billsCheck);
        bills = findViewById(R.id.bills);
        deposit = findViewById(R.id.deposit);


        availability = findViewById(R.id.available);
        minimumStayRequired = findViewById(R.id.stay);

        pet = findViewById(R.id.pet);
        english = findViewById(R.id.english);
        lift = findViewById(R.id.lift);

        description = findViewById(R.id.description);

        editableCard = findViewById(R.id.editable);
        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);
    }

    @SuppressLint("SetTextI18n")
    private void initData(Context context, House house, Boolean editable){
        name.setText(String.format("%s %s", house.getName(), house.getSurname()));
        email.setText(house.getEmail());
        phone.setText(house.getPhone());

        address.setText(house.getAddress());

        campus.setText(house.getCampus().toString());
        houseType.setText(house.getHouseType().toString());
        bed.setText(house.getBed().toString());

        numberOfRooms.setText(house.getNumberOfRooms().toString());
        numberOfPeoples.setText(house.getNumberOfPeoples().toString());
        floor.setText(house.getFloor().toString());
        area.setText(house.getArea().toString());

        preferredSex.setText(house.getPreferredSex().toString());

        price.setText(house.getPrice().toString());
        billsCheckbox.setChecked(house.getInclusive());
        bills.setText(house.getBills().toString());
        deposit.setText(house.getDeposit().toString());

        availability.setText(dateFormat.format(house.getAvailability()));
        minimumStayRequired.setText(house.getMinimumStayRequired().toString());

        pet.setChecked(house.getPet());
        english.setChecked(house.getEnglish());
        lift.setChecked(house.getLift());

        description.setText(house.getDescription());

        fab.setImageResource(Favourite.getInstance(context).getFav(house.get_id())?R.drawable.ic_star_filled_24dp:R.drawable.ic_star_empty_24dp);
        fab.setOnClickListener(v -> {
            fab.setImageResource(Favourite.getInstance(context).flipFav(house.get_id())?R.drawable.ic_star_filled_24dp:R.drawable.ic_star_empty_24dp);
        });

        if (editable){
            editableCard.setVisibility(View.VISIBLE);
            edit.setOnClickListener( v -> {});
            delete.setOnClickListener( v -> {
                String _uid = UniqueIdentifier.getUniqueID(context);


            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
