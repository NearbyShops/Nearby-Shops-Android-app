package org.nearbyshops.enduserappnew.multimarketfiles;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.aSuperAdminModule.DashboardAdmin.SDSAdminDashboard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;






public class ViewHolderRoleDashboardSDS extends RecyclerView.ViewHolder{



    @BindView(R.id.dashboard_name) TextView dashboardName;
    @BindView(R.id.dashboard_description) TextView dashboardDescription;


    private Context context;
    private Fragment fragment;




    public static ViewHolderRoleDashboardSDS create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_role_dashboard_sds,parent,false);
        return new ViewHolderRoleDashboardSDS(view,context,fragment);
    }





    public ViewHolderRoleDashboardSDS(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;

        bindDashboard();
    }






    @OnClick(R.id.dashboard_by_role)
    public void dashboardClick()
    {

        User user = PrefLoginGlobal.getUser(context);

        if(user==null)
        {
            return;
        }

        if(user.getRole()==User.ROLE_ADMIN_CODE)
        {
            Intent intent = new Intent(context, SDSAdminDashboard.class);
            context.startActivity(intent);
        }
        else if(user.getRole()==User.ROLE_STAFF_CODE)
        {

        }

    }




    public void bindDashboard()
    {
        User user = PrefLoginGlobal.getUser(context);

        if(user==null)
        {
            return;
        }


        if(user.getRole()==User.ROLE_ADMIN_CODE)
        {
            dashboardName.setText("Super Admin Dashboard");
            dashboardDescription.setText("Press here to access the Admin dashboard !");
        }
        else if(user.getRole()==User.ROLE_STAFF_CODE)
        {
            dashboardName.setText("Shop Staff Dashboard");
            dashboardDescription.setText("Press here to access shop dashboard !");
        }

    }




    void listItemClick()
    {
        ((ListItemClick)fragment).listItemClick();
    }




    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }







    public interface ListItemClick
    {
        void listItemClick();
    }



}

