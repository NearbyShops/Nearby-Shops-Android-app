package org.nearbyshops.enduserappnew.aSellerModule.FilterDeliveryGuy;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 19/12/15.
 */


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    // keeping track of selections
//    Map<Integer, Vehicle> selectedVehicleTypes = new HashMap<>();


    private List<Object> dataset;
    private Context context;
    private NotificationsFromAdapter notificationReceiver;
    private Fragment fragment;

    public static final int VIEW_TYPE_TRIP_REQUEST = 1;
    public static final int VIEW_TYPE_HEADER = 5;
    private final static int VIEW_TYPE_PROGRESS_BAR = 6;
//    private final static int VIEW_TYPE_FILTER = 7;
//    private final static int VIEW_TYPE_FILTER_SUBMISSIONS = 8;


    public Adapter(List<Object> dataset, Context context, NotificationsFromAdapter notificationReceiver, Fragment fragment) {

//        DaggerComponentBuilder.getInstance()
//                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == VIEW_TYPE_TRIP_REQUEST) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_staff_new, parent, false);

            return new ViewHolderTripRequest(view);
        }
        else if (viewType == VIEW_TYPE_HEADER) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_header_type_simple, parent, false);

            return new ViewHolderHeader(view);

        } else if (viewType == VIEW_TYPE_PROGRESS_BAR) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar, parent, false);

            return new LoadingViewHolder(view);

        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolderTripRequest) {

            bindTripRequest((ViewHolderTripRequest) holder, position);
        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof HeaderTitle) {
                HeaderTitle header = (HeaderTitle) dataset.get(position);

                ((ViewHolderHeader) holder).header.setText(header.getHeading());
            }

        } else if (holder instanceof LoadingViewHolder) {

            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;

            int itemCount = 0;


            if (fragment instanceof FilterDeliveryFragment) {
                itemCount = (((FilterDeliveryFragment) fragment).item_count_vehicle + 1 );
            }


//            itemCount = dataset.size();

            if (position == 0 || position == itemCount) {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setIndeterminate(true);
            }

        }
    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if (position == dataset.size()) {
            return VIEW_TYPE_PROGRESS_BAR;
        } else if (dataset.get(position) instanceof HeaderTitle) {
            return VIEW_TYPE_HEADER;
        } else if (dataset.get(position) instanceof User) {
            return VIEW_TYPE_TRIP_REQUEST;
        }

        return -1;
    }


    @Override
    public int getItemCount() {

        return (dataset.size() + 1);
    }






    void bindTripRequest(ViewHolderTripRequest holder, int position)
    {
        if(dataset.get(position) instanceof User)
        {
            User tripRequest = (User) dataset.get(position);


//            DeliveryGuyData permissions = tripRequest.getRt_delivery_guy_data();

            holder.staffUserID.setText("Staff User ID : " + tripRequest.getUserID());
            holder.staffName.setText(tripRequest.getName());
//            holder.designation.setText("Delivery Guy Self");
//            holder.distance.setText("Distance : " + String.format("%.2f Km",permissions.getRt_distance()));
            holder.phone.setText(tripRequest.getPhone());


            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_nature_people_white_48px);

            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/User/Image/" + "five_hundred_"+ tripRequest.getProfileImagePath() + ".jpg";
            String image_url = PrefGeneral.getServiceURL(context) + "/api/v1/User/Image/" + tripRequest.getProfileImagePath();


            Picasso.get()
                    .load(imagePath)
                    .placeholder(drawable)
                    .into(holder.profileImage);




//            if(selectedVehicleTypes.containsKey(vehicleType.getVehicleID()))
//            {
//                //context.getResources().getColor(R.color.gplus_color_2)
//                holder.listItem.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_2));
//            }
//            else
//            {
//                //context.getResources().getColor(R.color.white)
//                holder.listItem.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
//            }

        }
    }








    class ViewHolderTripRequest extends RecyclerView.ViewHolder{




        @BindView(R.id.list_item)
        ConstraintLayout listItem;
        @BindView(R.id.profile_picture) ImageView profileImage;
        @BindView(R.id.staff_user_id) TextView staffUserID;
        @BindView(R.id.name) TextView staffName;
        @BindView(R.id.phone) TextView phone;
//        @BindView(R.id.distance) TextView distance;



        public ViewHolderTripRequest(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {



//                    TripRequest tripRequest = (TripRequest) dataset.get(getLayoutPosition());



//                    if(selectedVehicleTypes.containsKey(
//                            vehicleType.getVehicleTypeID()
//                    ))
//                    {
//                        selectedVehicleTypes.remove(vehicleType.getVehicleTypeID());
//
//                    }else
//                    {
//
//                        selectedVehicleTypes.put(vehicleType.getVehicleTypeID(),vehicleType);
//                    }



                    notificationReceiver.notifyTripRequestSelected();
                    notifyItemChanged(getLayoutPosition());



                    return notificationReceiver.listItemLongClick(v,
                            (User) dataset.get(getLayoutPosition()),
                            getLayoutPosition()
                    );
                }
            });
        }





        @OnClick(R.id.list_item)
        void listItemLongClick()
        {

            notificationReceiver.listItemClick((User) dataset.get(getLayoutPosition()),
                    getLayoutPosition()
            );

        }





        @OnClick(R.id.phone)
        void phoneClick()
        {
            dialPhoneNumber(phone.getText().toString());
        }

        public void dialPhoneNumber(String phoneNumber) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }




    }// ViewHolder Class declaration ends






    class ViewHolderHeader extends RecyclerView.ViewHolder{

        @BindView(R.id.header)
        TextView header;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }// ViewHolder Class declaration ends





    interface NotificationsFromAdapter
    {
        // method for notifying the list object to request sub category

//        void currentItemClick(Item item, int position);
//        void itemUpdateClick(ItemSubmission itemSubmission, int position);
//        void imageUpdatedClick(ItemImage itemImage, int position);

        void notifyTripRequestSelected();
        void listItemClick(User user, int position);
        boolean listItemLongClick(View view, User user, int position);
    }



    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



}