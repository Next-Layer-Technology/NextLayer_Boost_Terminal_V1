package com.sis.clighteningboost.Adapter

import com.sis.clighteningboost.Models.REST.RoutingNode
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.sis.clighteningboost.R
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.TextView
import java.util.ArrayList

class MerchantItemAdapter     // RecyclerView recyclerView;
    (private val itemsArrayList: ArrayList<RoutingNode>) :
    RecyclerView.Adapter<MerchantItemAdapter.ViewHolder>() {
    //For the Purpose of update or notifyDataSetChanged
    fun updateList(itemList: ArrayList<RoutingNode>?) {
        itemsArrayList.clear()
        itemsArrayList.addAll(itemList!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            layoutInflater.inflate(R.layout.routingnoderow, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemsArrayList[position]
        holder.setIsRecyclable(false)
        holder.ip.text = currentItem.ip
        holder.port.text = currentItem.port
        holder.username.text = currentItem.username
        holder.password.text = currentItem.password
        holder.customRowLayout.setOnClickListener {
            // Toast.makeText(view.getContext(),"click on item: "+currentItem.getName(),Toast.LENGTH_LONG).show();
        }
    }

    override fun getItemCount(): Int {
        return itemsArrayList.size
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        var customRowLayout: ConstraintLayout
        var ip: TextView
        var port: TextView
        var username: TextView
        var password: TextView

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        init {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            customRowLayout = itemView.findViewById<View>(R.id.linearLayout2) as ConstraintLayout
            ip = itemView.findViewById<View>(R.id.ip) as TextView
            port = itemView.findViewById<View>(R.id.port) as TextView
            username = itemView.findViewById<View>(R.id.userName) as TextView
            password = itemView.findViewById<View>(R.id.userPass) as TextView
        }
    }
}