package org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.API.DeliverySlotService;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.Model.DeliverySlot;
import org.nearbyshops.whitelabelapp.R;

import java.sql.Time;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewHolderDeliverySlot extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {


    @BindView(R.id.slot_name) TextView slotName;
    @BindView(R.id.is_enabled) Switch isEnabled;
    @BindView(R.id.slot_timings) TextView slotTimings;

    @BindView(R.id.more_options) ImageView moreOptions;
    @BindView(R.id.check_icon) ImageView checkIcon;



    public static int MODE_SHOP_ADMIN = 1;
    public static int MODE_END_USER = 2;



    private Context context;
    private DeliverySlot deliverySlot;
    private Fragment fragment;


    private RecyclerView.Adapter adapter;
//    private Integer selectedSlotID;
    private SlotSelection slotSelection;


    int currentMode;


    public ViewHolderDeliverySlot(@NonNull View itemView, Context context, Fragment fragment, int mode,
                                  SlotSelection slotSelection, RecyclerView.Adapter adapter) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
        this.currentMode=mode;
        this.slotSelection = slotSelection;
        this.adapter = adapter;


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }







    public static ViewHolderDeliverySlot create(ViewGroup parent, Context context, Fragment fragment,int mode,
            SlotSelection slotSelection, RecyclerView.Adapter adapter)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_delivery_slot,parent,false);
        return new ViewHolderDeliverySlot(view,context,fragment, mode,slotSelection,adapter);
    }





    public void setItem(DeliverySlot deliverySlot)
    {
        this.deliverySlot = deliverySlot;
//        this.currentMode = deliverySlotMode;


        slotName.setText(deliverySlot.getSlotName());

        if(deliverySlot.getRt_order_count()>0)
        {
            slotName.append(" (" + deliverySlot.getRt_order_count() + ")");
        }



//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(deliverySlot.getSlotTime().getTime());
//        calendar.add(Calendar.HOUR,deliverySlot.getDurationInHours());


        Time endTime = new Time(0);
        endTime.setHours(deliverySlot.getSlotTime().getHours() + deliverySlot.getDurationInHours());

        String startTimeString =  deliverySlot.getSlotTime().getHours() + ":" + deliverySlot.getSlotTime().getMinutes();
        String endTimeString = endTime.getHours() + ":" + endTime.getMinutes();

        slotTimings.setText("Delivery from " + startTimeString + " to " + endTimeString);

        isEnabled.setChecked(deliverySlot.isEnabled());

        if(currentMode==MODE_END_USER)
        {
            moreOptions.setVisibility(View.GONE);
            isEnabled.setVisibility(View.GONE);
        }
        else if(currentMode==MODE_SHOP_ADMIN)
        {
            moreOptions.setVisibility(View.VISIBLE);
            isEnabled.setVisibility(View.VISIBLE);
        }

        bindCheckIcon(false);
    }







    void bindCheckIcon(boolean notifyChange)
    {
        if(currentMode==MODE_END_USER)
        {
            if(deliverySlot.getSlotID()== slotSelection.getDeliverySlotID())
            {
                checkIcon.setVisibility(View.VISIBLE);
            }
            else
            {
                checkIcon.setVisibility(View.INVISIBLE);
            }


            if(notifyChange)
            {
                adapter.notifyDataSetChanged();
            }
        }
    }










    @OnClick(R.id.list_item)
    public void itemCategoryListItemClick()
    {



        if(deliverySlot.getSlotID()==slotSelection.getDeliverySlotID())
        {
            slotSelection.setDeliverySlotID(-1);
        }
        else
        {
            slotSelection.setDeliverySlotID(deliverySlot.getSlotID());


        }


        bindCheckIcon(true);



        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).listItemClick(slotSelection.getDeliverySlotID());
        }

    }







    @OnClick(R.id.more_options)
    void optionsOverflowClick(View v)
    {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_delivery_slot_list_item_overflow, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }






    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.action_remove:


                if(fragment instanceof ListItemClick)
                {
                    ((ListItemClick) fragment).removeDeliverySlot(deliverySlot,getAdapterPosition());
                }


                break;

            case R.id.action_edit:

                if(fragment instanceof ListItemClick)
                {
                    ((ListItemClick) fragment).editDeliverySlot(deliverySlot,getAdapterPosition());
                }


                break;


            default:
                break;

        }

        return false;
    }




    void showToast(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }





    public interface ListItemClick
    {
        void listItemClick(int deliverySlotID);
        void editDeliverySlot(DeliverySlot deliverySlot, int position);
        void removeDeliverySlot(DeliverySlot deliverySlot, int position);
    }






    public interface SlotSelection
    {
        void setDeliverySlotID(int deliverySlotID);
        int getDeliverySlotID();
    }





    @OnClick(R.id.is_enabled)
    void switchClick()
    {
        if(isEnabled.isChecked())
        {
            enableSlot(deliverySlot.getSlotID(),true);
        }
        else
        {
            enableSlot(deliverySlot.getSlotID(),false);
        }



    }





    @Inject
    DeliverySlotService slotService;





    public void enableSlot(int slotID, boolean isEnabledLocal)
    {

        Call<ResponseBody> call = slotService.enableSlot(
                slotID,isEnabledLocal
        );




        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    if(isEnabledLocal)
                    {
                        showToast("Enabled !");

                    }
                    else
                    {
                        showToast("Disabled !");
                    }

                }
                else
                {

                    showToast("Failed Code : " + String.valueOf(response.code()));


                    isEnabled.setChecked(!isEnabled.isChecked());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToast("Failed !");

                isEnabled.setChecked(!isEnabled.isChecked());
            }
        });
    }






}// ViewHolder Class declaration ends

