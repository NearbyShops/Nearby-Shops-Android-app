package org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.EditDeliverySlot;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.Model.DeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.ViewModelDeliverySlot;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
//import org.nearbyshops.enduserappnew.LocationPicker.PickLocation;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefShopAdminHome;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;

import java.sql.Time;


public class EditDeliverySlotFragment extends Fragment {



    private DeliverySlot deliverySlot;

    public static final String DELIVERY_SLOT_INTENT_KEY = "edit_delivery_slot_intent_key";



    @BindView(R.id.saveButton) TextView updateButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    // address Fields


    @BindView(R.id.delivery_slot_id) EditText deliverySlotID;
    @BindView(R.id.delivery_slot_name) EditText deliverySlotName;
    @BindView(R.id.delivery_slot_timings) EditText deliverySlotTimings;
    @BindView(R.id.max_orders_per_day) EditText maxOrdersPerDay;

    @BindView(R.id.delivery_time) TextView deliveryTime;

    @BindView(R.id.duration_one) TextView durationOne;
    @BindView(R.id.duration_two) TextView durationTwo;
    @BindView(R.id.duration_three) TextView durationThree;

    @BindView(R.id.set_delivery_time) TextView setDeliveryTime;


    @BindView(R.id.radio_delivery_slot) CheckBox radioDeliverySlot;
    @BindView(R.id.radio_pickup_slot) CheckBox radioPickupSlot;
    @BindView(R.id.slot_type_block) LinearLayout slotTypeBlock;

    @BindView(R.id.is_enabled) Switch isEnabled;


    ViewModelDeliverySlot viewModel;



    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";
    public static final String ACCESS_MODE_INTENT_KEY = "edit_access_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;


    public static final int MODE_ACCESS_BY_SHOP_ADMIN = 10;
    public static final int MODE_ACCESS_BY_MARKET_ADMIN = 11;


    int current_mode = MODE_ADD;
    int current_mode_access = MODE_ACCESS_BY_SHOP_ADMIN;




    public EditDeliverySlotFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_delivery_slot, container, false);
        ButterKnife.bind(this,rootView);

//        setContentView(R.layout.activity_edit_address);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);
        current_mode_access=getActivity().getIntent().getIntExtra(ACCESS_MODE_INTENT_KEY,MODE_ACCESS_BY_SHOP_ADMIN);




        if(current_mode ==MODE_UPDATE)
        {
            String jsonString = getActivity().getIntent().getStringExtra("delivery_slot_json");
            deliverySlot = UtilityFunctions.provideGson().fromJson(jsonString,DeliverySlot.class);

            bindDataToViews();
        }
        else if(current_mode==MODE_ADD)
        {
            deliverySlot=new DeliverySlot();
        }





        setActionBarTitle();


        setupViewModel();

        return rootView;
    }




    private void setActionBarTitle()
    {
        if(getActivity() instanceof AppCompatActivity)
        {
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

            if(actionBar!=null)
            {
                if(current_mode == MODE_ADD)
                {
                    actionBar.setTitle("Add Delivery Slot");
                }
                else if(current_mode==MODE_UPDATE)
                {
                    actionBar.setTitle("Edit Delivery Slot");
                }

            }
        }


        if(current_mode==MODE_ADD)
        {
            updateButton.setText("Add");
            deliverySlotID.setVisibility(View.GONE);
        }
        else if(current_mode==MODE_UPDATE)
        {
            updateButton.setText("Save");
            deliverySlotID.setVisibility(View.VISIBLE);
        }

    }





    private void getDataFromViews()
    {


        if(deliverySlot ==null)
        {
            deliverySlot= new DeliverySlot();
        }

        deliverySlot.setSlotName(deliverySlotName.getText().toString());

        if(!maxOrdersPerDay.getText().toString().equals(""))
        {
            deliverySlot.setMaxOrdersPerDay(Integer.parseInt(maxOrdersPerDay.getText().toString()));
        }


        deliverySlot.setSlotTime(selectedTime);


        deliverySlot.setDeliverySlot(radioDeliverySlot.isChecked());
        deliverySlot.setPickupSlot(radioPickupSlot.isChecked());


        deliverySlot.setEnabled(isEnabled.isChecked());
    }



    private void bindDataToViews()
    {

        if(deliverySlot == null)
        {
            return;
        }


        deliverySlotID.setText(String.valueOf(deliverySlot.getSlotID()));
        deliverySlotName.setText(deliverySlot.getSlotName());

        selectedTime.setTime(deliverySlot.getSlotTime().getTime());


        maxOrdersPerDay.setText(String.valueOf(deliverySlot.getMaxOrdersPerDay()));


        radioDeliverySlot.setChecked(deliverySlot.isDeliverySlot());
        radioPickupSlot.setChecked(deliverySlot.isPickupSlot());


        isEnabled.setChecked(deliverySlot.isEnabled());


        if(deliverySlot.getDurationInHours()==2)
        {
            durationOneClick();
        }
        else if(deliverySlot.getDurationInHours()==3)
        {
            durationTwoClick();
        }
        else if(deliverySlot.getDurationInHours()==4)
        {
            durationThreeClick();
        }


        bindDeliveryTime();

    }



    void bindDeliveryTime()
    {

        deliveryTime.setText(selectedTime.toString());
        deliveryTime.append(" for " + String.valueOf(deliverySlot.getDurationInHours()) + " Hours");
    }



    void clearDuration()
    {
        durationOne.setBackgroundColor(getResources().getColor(R.color.light_grey));
        durationOne.setTextColor(getResources().getColor(R.color.blueGrey800));

        durationTwo.setBackgroundColor(getResources().getColor(R.color.light_grey));
        durationTwo.setTextColor(getResources().getColor(R.color.blueGrey800));

        durationThree.setBackgroundColor(getResources().getColor(R.color.light_grey));
        durationThree.setTextColor(getResources().getColor(R.color.blueGrey800));
    }





    @OnClick(R.id.radio_pickup_slot)
    void radioPickupClick()
    {
        setDeliveryTime.setText("Set Pickup Time");
    }



    @OnClick(R.id.radio_delivery_slot)
    void radioDeliveryClick()
    {
        setDeliveryTime.setText("Set Delivery Time");
    }





    @OnClick(R.id.duration_one)
    void durationOneClick()
    {
        clearDuration();

        durationOne.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        durationOne.setTextColor(getResources().getColor(R.color.white));

        deliverySlot.setDurationInHours(2);

        bindDeliveryTime();
    }



    @OnClick(R.id.duration_two)
    void durationTwoClick()
    {
        clearDuration();

        durationTwo.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        durationTwo.setTextColor(getResources().getColor(R.color.white));

        deliverySlot.setDurationInHours(3);

        bindDeliveryTime();
    }




    @OnClick(R.id.duration_three)
    void durationThreeClick()
    {
        clearDuration();

        durationThree.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        durationThree.setTextColor(getResources().getColor(R.color.white));

        deliverySlot.setDurationInHours(4);

        bindDeliveryTime();
    }








    Time selectedTime = new Time(System.currentTimeMillis());


    @OnClick(R.id.set_delivery_time)
    void setDeliveryTime()
    {

//        Calendar mcurrentTime = Calendar.getInstance();
//        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                deliveryTime.setText( selectedHour + ":" + selectedMinute);

                selectedTime.setHours(selectedHour);
                selectedTime.setMinutes(selectedMinute);

//                deliveryTime.setText(selectedTime.toString());

                bindDeliveryTime();

            }
        }, selectedTime.getHours(), selectedTime.getMinutes(), true);//Yes 24 hour time


        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }






    @OnClick(R.id.saveButton)
    void updateAddressClick(View view)
    {

        if(!validateData())
        {
            return;
        }

        if(current_mode == MODE_ADD)
        {
            addDeliverySlot();
        }
        else if(current_mode == MODE_UPDATE)
        {
            updateDeliverySlot();
        }

    }



    void addDeliverySlot()
    {
        getDataFromViews();


        if(current_mode_access==MODE_ACCESS_BY_SHOP_ADMIN)
        {
            Shop currentShop = PrefShopAdminHome.getShop(getActivity());
            deliverySlot.setShopID(currentShop.getShopID());
        }
        else if(current_mode_access==MODE_ACCESS_BY_MARKET_ADMIN)
        {
            deliverySlot.setShopID(0);
        }



        progressBar.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.INVISIBLE);

        viewModel.createDeliverySlot(deliverySlot);
    }




    void updateDeliverySlot()
    {

        getDataFromViews();

        if(current_mode_access==MODE_ACCESS_BY_SHOP_ADMIN)
        {
            Shop currentShop = PrefShopAdminHome.getShop(getActivity());
            deliverySlot.setShopID(currentShop.getShopID());
        }
        else if(current_mode_access==MODE_ACCESS_BY_MARKET_ADMIN)
        {
            deliverySlot.setShopID(0);
        }


        progressBar.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.INVISIBLE);

        viewModel.updateDeliverySlot(deliverySlot);
    }







    private boolean validateData()
    {
        boolean isValid = true;




        return isValid;
    }










    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }





    private void setupViewModel()
    {

        viewModel = new ViewModelDeliverySlot(MyApplication.application);


        viewModel.getDeliverySlot().observe(getViewLifecycleOwner(), new Observer<DeliverySlot>() {
            @Override
            public void onChanged(DeliverySlot deliverySlot) {

                current_mode=MODE_UPDATE;
                EditDeliverySlotFragment.this.deliverySlot = deliverySlot;


                setActionBarTitle();
                bindDataToViews();

            }
        });




        viewModel.getEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {



                if(integer==ViewModelDeliverySlot.EVENT_DELIVERY_SLOT_UPDATED)
                {
                    progressBar.setVisibility(View.GONE);
                    updateButton.setVisibility(View.VISIBLE);
                }
                else if(integer==ViewModelDeliverySlot.EVENT_DELIVERY_SLOT_ADDED)
                {
                    progressBar.setVisibility(View.GONE);
                    updateButton.setVisibility(View.VISIBLE);
                }
                else if(integer==ViewModelDeliverySlot.EVENT_DELIVERY_SLOT_DELETED)
                {

                }
                else if(integer==ViewModelDeliverySlot.EVENT_NETWORK_FAILED)
                {

                    progressBar.setVisibility(View.GONE);
                    updateButton.setVisibility(View.VISIBLE);
                }
            }
        });





        viewModel.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showToastMessage(s);

            }
        });

    }


}
