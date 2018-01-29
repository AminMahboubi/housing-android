package com.aminmahboubi.housing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aminmahboubi.housing.R;
import com.aminmahboubi.housing.api.HouseAPI;
import com.aminmahboubi.housing.model.House;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.helper.CustomValidation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.common.collect.Range;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddHouseActivity extends AppCompatActivity {
    final String TAG = "AddHouseActivity";

    AwesomeValidation validation;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);

    EditText name;
    EditText surname;
    EditText email;
    EditText phone;

    PlaceAutocompleteFragment placeAutocompleteFragment;
    EditText addressValidator;
    Place address;
    Spinner campus;
    Spinner houseType;
    Spinner bed;

    EditText numberOfRooms;
    EditText numberOfPeoples;
    EditText floor;
    EditText area;

    Spinner preferredSex;

    EditText price;
    CheckBox billsCheckbox;
    EditText bills;
    EditText deposit;

    EditText availability;
    CheckBox minimumStayRequiredCheckBox;
    EditText minimumStayRequired;

    CheckBox pet;
    CheckBox english;
    CheckBox lift;

    EditText description;

    Button save;

    Boolean minimumStayRequiredCheck = false;
    Boolean billsCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUI();
        initValidation();

        save.setOnClickListener(v -> {
            try {
                if (validation.validate()) {
                    House newHouse = newHouse();
                    Toast.makeText(getApplicationContext(), "Saving House", Toast.LENGTH_SHORT).show();
                    HouseAPI.getInstance(getApplicationContext()).postHouse(newHouse);
                    Log.d(TAG, "onCreate: " + newHouse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    private void initUI() {
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        addressValidator = findViewById(R.id.address);
        placeAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        placeAutocompleteFragment.setFilter(new AutocompleteFilter.Builder().setCountry("IT").build());
        placeAutocompleteFragment.setBoundsBias(new LatLngBounds(new LatLng(45.399976, 9.049262), new LatLng(45.576915, 9.315196)));

        placeAutocompleteFragment.setHint("Search House Address");
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                address = place;
            }

            @Override
            public void onError(Status status) {
                address = null;
                Toast.makeText(getApplicationContext(), "An error occurred while getting address: " + status, Toast.LENGTH_SHORT).show();
            }
        });
        placeAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)
                .setOnClickListener(view -> {
                    placeAutocompleteFragment.setText("");
                    view.setVisibility(View.GONE);
                    address = null;
                });
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
        billsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            billsCheck = isChecked;
            bills.setEnabled(!isChecked);
            if (!isChecked)
                bills.requestFocus();
        });

        availability = findViewById(R.id.available);
        minimumStayRequiredCheckBox = findViewById(R.id.stayCheck);
        minimumStayRequired = findViewById(R.id.stay);
        minimumStayRequiredCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            minimumStayRequiredCheck = isChecked;
            minimumStayRequired.setEnabled(isChecked);
            if (isChecked)
                minimumStayRequired.requestFocus();
        });

        pet = findViewById(R.id.pet);
        english = findViewById(R.id.english);
        lift = findViewById(R.id.lift);

        description = findViewById(R.id.description);

        save = findViewById(R.id.save);
    }

    private void initValidation() {
        validation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        validation.setContext(this);

        validation.addValidation(name, "[a-zA-Z\\s]+", "Enter a Valid Name");
        validation.addValidation(surname, "[a-zA-Z\\s]+", "Enter a Valid Surname");
        validation.addValidation(email, android.util.Patterns.EMAIL_ADDRESS, "Valid Email Required");
        validation.addValidation(phone, "Valid Phone Required", new CustomValidation() {
            @Override
            public boolean compare(String input) {
                if (input.length() < 9)
                    return false;
                return PhoneNumberUtils.isGlobalPhoneNumber(input);
            }
        });
        validation.addValidation(addressValidator, "Enter a valid Address", new CustomValidation() {
            @Override
            public boolean compare(String input) {
                if (address == null)
                    return false;
                return true;
            }
        });
        validation.addValidation(numberOfRooms, Range.closed(1, 10), "Total Room Numbers in House");
        validation.addValidation(numberOfPeoples, Range.closed(1, 10), "Total People Numbers in House");
        validation.addValidation(floor, Range.closed(0, 20), "Which floor is the house?");
        validation.addValidation(area, Range.closed(5, 500), "Area of the House/Room");
        validation.addValidation(price, Range.closed(1, 3000), "Rent price between 1 and 3000 Euro");
        validation.addValidation(bills, "Bills between 0 and 500 Euro", new CustomValidation() {
            @Override
            public boolean compare(String input) {
                if (!billsCheck) {
                    if (input.length() == 0)
                        return false;
                    else {
                        int value = Integer.valueOf(input);
                        if (value < 0 || value > 500)
                            return false;
                    }
                }
                return true;
            }
        });
        validation.addValidation(deposit, Range.closed(0, 10000), "Deposit between 0 and 10k Euro");
        validation.addValidation(availability, "House become free from ...?", new CustomValidation() {
            @Override
            public boolean compare(String input) {
                String dateRegex = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
                if (!input.matches(dateRegex))
                    return false;
                try {
                    Date date = dateFormat.parse(input);
                    date.setTime(date.getTime() + 1);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    if (calendar.getTime().after(dateFormat.parse(input)))
                        return false;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        });
        validation.addValidation(minimumStayRequired, "Minimum Stay between 0 and 365 Day", new CustomValidation() {
            @Override
            public boolean compare(String input) {
                if (minimumStayRequiredCheck) {
                    if (input.length() == 0)
                        return false;
                    else {
                        int value = Integer.valueOf(input);
                        if (value < 0 || value > 365)
                            return false;
                    }
                }
                return true;
            }
        });

    }

    private House newHouse() {
        House newHouse = new House();

        newHouse.setName(name.getText().toString());
        newHouse.setSurname(surname.getText().toString());
        newHouse.setPhone(phone.getText().toString());
        newHouse.setEmail(email.getText().toString());

        newHouse.setAddress(address.getName().toString());
        newHouse.setLat(address.getLatLng().latitude);
        newHouse.setLng(address.getLatLng().longitude);

        newHouse.setCampus(campus.getSelectedItem().toString());
        newHouse.setHouseType(houseType.getSelectedItem().toString());
        newHouse.setBed(bed.getSelectedItem().toString());

        newHouse.setNumberOfRooms(Integer.valueOf(numberOfRooms.getText().toString()));
        newHouse.setNumberOfPeoples(Integer.valueOf(numberOfPeoples.getText().toString()));
        newHouse.setFloor(Integer.valueOf(floor.getText().toString()));
        newHouse.setArea(Integer.valueOf(area.getText().toString()));

        newHouse.setPreferredSex(preferredSex.getSelectedItem().toString());

        newHouse.setPrice(Integer.valueOf(price.getText().toString()));
        newHouse.setDeposit(Integer.valueOf(deposit.getText().toString()));
        newHouse.setInclusive(billsCheckbox.isChecked());

        if (billsCheckbox.isChecked() || bills.getText().toString().equals(""))
            newHouse.setBills(0);
        else
            newHouse.setBills(Integer.valueOf(bills.getText().toString()));

        if (minimumStayRequiredCheckBox.isChecked() && !minimumStayRequired.getText().toString().equals(""))
            newHouse.setMinimumStayRequired(Integer.valueOf(minimumStayRequired.getText().toString()));
        else
            newHouse.setMinimumStayRequired(0);

        try {
            newHouse.setAvailability(dateFormat.parse(availability.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        newHouse.setPet(pet.isChecked());
        newHouse.setEnglish(english.isChecked());
        newHouse.setLift(lift.isChecked());

        newHouse.setDescription(description.getText().toString());

        return newHouse;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
