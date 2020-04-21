package org.nearbyshops.enduserappnew.ViewHolders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.R;


public class ViewHolderDeliveryAddress extends RecyclerView.ViewHolder implements View.OnClickListener{



    @BindView(R.id.name) TextView name;
    @BindView(R.id.deliveryAddress) TextView deliveryAddress;
//    @BindView(R.id.city) TextView city;
//    @BindView(R.id.pincode) TextView pincode;
//    @BindView(R.id.landmark) TextView landmark;
//    @BindView(R.id.phoneNumber) TextView phoneNumber;
    @BindView(R.id.editButton) TextView editButton;
    @BindView(R.id.removeButton) TextView removeButton;

    @BindView(R.id.list_item_delivery_address)
    LinearLayout listItemDeliveryAddress;




    private Context context;
    private ViewHolderDeliveryAddress.ListItemClick fragment;
    private DeliveryAddress item;




    public static ViewHolderDeliveryAddress create(ViewGroup parent, Context context, ViewHolderDeliveryAddress.ListItemClick fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_address,parent,false);

        return new ViewHolderDeliveryAddress(view,context,fragment);
    }





    public ViewHolderDeliveryAddress(View itemView, Context context, ViewHolderDeliveryAddress.ListItemClick fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);

        this.context = context;
        this.fragment = fragment;



//        name = itemView.findViewById(R.id.name);
//        deliveryAddress = itemView.findViewById(R.id.deliveryAddress);
//        city = itemView.findViewById(R.id.city);
//        pincode = itemView.findViewById(R.id.pincode);
//        landmark = itemView.findViewById(R.id.landmark);
//        phoneNumber = itemView.findViewById(R.id.phoneNumber);
//
//        editButton = itemView.findViewById(R.id.editButton);
//        removeButton = itemView.findViewById(R.id.removeButton);
//
//        listItemDeliveryAddress = itemView.findViewById(R.id.list_item_delivery_address);



        editButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
        listItemDeliveryAddress.setOnClickListener(this);

    }




    public void setItem(DeliveryAddress address)
    {

        this.item = address;

        name.setText(address.getName());
        deliveryAddress.setText(address.getDeliveryAddress());
//        city.setText(address.getCity());
//        pincode.setText(" - " + address.getPincode());
//        landmark.setText(address.getLandmark());
//        phoneNumber.setText(String.valueOf(address.getPhoneNumber()));


    }









    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.editButton:

                editClick();

                break;

            case R.id.removeButton:

                removeClick();

                break;

            case R.id.list_item_delivery_address:

                listItemClick();

                break;

            case R.id.button_select:


                break;


            default:
                break;
        }

    }



    public void buttonSelectClick()
    {
        if(fragment instanceof ListItemClick)
        {
            fragment.selectButtonClick(item,getLayoutPosition());
        }
    }





    public void listItemClick()
    {
        if(fragment instanceof ListItemClick)
        {
            fragment.notifyListItemClick(item);
        }
    }



    private void removeClick()
    {
        if(fragment instanceof ListItemClick)
        {
            fragment.notifyRemove(item,getLayoutPosition());
        }
    }





    private void editClick()
    {
        if(fragment instanceof ListItemClick)
        {
            fragment.notifyEdit(item);
        }
    }











    public interface ListItemClick {

        void notifyEdit(DeliveryAddress deliveryAddress);
        void notifyRemove(DeliveryAddress deliveryAddress, int position);
        void notifyListItemClick(DeliveryAddress deliveryAddress);
        void selectButtonClick(DeliveryAddress deliveryAddress, int position);
    }

}

