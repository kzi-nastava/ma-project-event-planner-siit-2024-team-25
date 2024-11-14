package com.team25.event.planner.product_service.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.R;
import com.team25.event.planner.product_service.fragments.ServiceAddForm;
import com.team25.event.planner.product_service.model.Service;

import java.util.ArrayList;

public class ServiceListAdapter extends ArrayAdapter<Service> {
    private ArrayList<Service> aServices;
    private FragmentActivity fragmentActivity;

    private NavController navController;

    public ServiceListAdapter(Context context, ArrayList<Service> services, FragmentActivity fragmentActivity){
        super(context, R.layout.service_card, services);
        aServices = services;
        this.fragmentActivity = fragmentActivity;
        navController = Navigation.findNavController(fragmentActivity, R.id.nav_host_fragment);
    }

    ///Returns number of elements list
    @Override
    public int getCount() {
        return aServices.size();
    }

    ///Return elem from position
    @Nullable
    @Override
    public Service getItem(int position) {
        return aServices.get(position);
    }

    ///Position is good enough id when we talk about adapters
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Service service = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_card,parent,false);
        }

        LinearLayout productCard = convertView.findViewById(R.id.service_card_item);
        ImageView imageView = convertView.findViewById(R.id.imageViewService);
        TextView productTitle = convertView.findViewById(R.id.nameService);
        TextView productDescription = convertView.findViewById(R.id.descriptionService);
        ImageButton editButton = convertView.findViewById(R.id.editButton);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// Define the fragment you want to open

                navController.navigate(R.id.action_ownerHomePage_to_serviceAddForm);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("New service")
                        .setMessage("Are you sure you want to delete service:"+service.getName())
                        .setPositiveButton("Yes", (dialog, which) -> {
                            Toast.makeText(getContext(), "Deleted: " + service.getName(), Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Code to execute when the "No" button is clicked
                            dialog.dismiss(); // Close the dialog
                        })


                        .create();

                alertDialog.show();
            }
        });

        if(service != null){
            imageView.setImageResource(R.drawable.profile_icon);
            productTitle.setText(service.getName());
            productDescription.setText(service.getDescription());
            productCard.setOnClickListener(v -> {
                Log.i("ShopApp", "Clicked: " + service.getName() + ", id: " +
                        service.getId());
                Toast.makeText(getContext(), "Clicked: " + service.getName()  +
                        ", id: " + service.getId(), Toast.LENGTH_SHORT).show();
            });
        }

        return  convertView;
    }
}
