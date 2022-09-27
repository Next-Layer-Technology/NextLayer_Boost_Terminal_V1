package com.sis.clighteningboost.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sis.clighteningboost.Models.REST.RoutingNode;
import com.sis.clighteningboost.R;

import java.util.ArrayList;

public class MerchantItemAdapter extends RecyclerView.Adapter<MerchantItemAdapter.ViewHolder> {

    private ArrayList<RoutingNode> itemsArrayList;
    // RecyclerView recyclerView;
    public MerchantItemAdapter(ArrayList<RoutingNode> listdata) {
        this.itemsArrayList = listdata;
    }


    //For the Purpose of update or notifyDataSetChanged
    public void updateList(ArrayList<RoutingNode> itemList){
        itemsArrayList.clear();
        this.itemsArrayList.addAll(itemList);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.routingnoderow, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RoutingNode currentItem = itemsArrayList.get(position);
        holder.setIsRecyclable(false);
        holder.ip.setText(currentItem.getIp());
        holder.port.setText(currentItem.getPort());
        holder.username.setText(currentItem.getUsername());
        holder.password.setText(currentItem.getPassword());
        holder.customRowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(view.getContext(),"click on item: "+currentItem.getName(),Toast.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ConstraintLayout customRowLayout;
        public TextView ip;
        public TextView port;
        public TextView username;
        public TextView password;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            customRowLayout=(ConstraintLayout)itemView.findViewById(R.id.linearLayout2);
            ip = (TextView) itemView.findViewById(R.id.ip);
            port = (TextView) itemView.findViewById(R.id.port);
            username=(TextView) itemView.findViewById(R.id.userName);
            password=(TextView) itemView.findViewById(R.id.userPass);
        }
    }

}