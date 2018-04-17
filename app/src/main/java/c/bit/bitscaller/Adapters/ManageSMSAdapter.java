package c.bit.bitscaller.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import c.bit.bitscaller.DatabaseHelper;
import c.bit.bitscaller.Pojo;
import c.bit.bitscaller.R;

public class ManageSMSAdapter extends RecyclerView.Adapter<ManageSMSAdapter.ManageSMSViewHolder> {

    private ArrayList<Pojo> listmanagesms;
    Context context;

    public ManageSMSAdapter(ArrayList<Pojo> listmanagesms, Context context) {
        this.listmanagesms = listmanagesms;
        this.context = context;
    }

    public static class ManageSMSViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        ArrayList<Pojo> managedList;
        Context ctx;
        DatabaseHelper dbHelper;

        public TextView name;
        public TextView number;
        public TextView bsms;

        public ManageSMSViewHolder(View view, Context ctx, ArrayList<Pojo> ledList) {
            super(view);
            this.ctx = ctx;
            this.managedList = ledList;
            dbHelper = new DatabaseHelper(ctx);
            view.setOnLongClickListener(this);
            name = view.findViewById(R.id.name_manage);
            number = view.findViewById(R.id.number_manage);
            bsms = view.findViewById(R.id.managestatus);
        }

        @Override
        public boolean onLongClick(View view) {
            //Do what you have to do here on Long Click
            int position = getAdapterPosition();
            Pojo apojo = this.managedList.get(position);
            if (apojo.getBsms() == 0) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
                dialogBuilder.setTitle("Block Messages");
                dialogBuilder.setMessage("Are You Sure You Want To Block Messages From Number: " + apojo.getNumber());
                dialogBuilder.setPositiveButton("Block", (dialog, which) -> {
                    dbHelper.blocksms(apojo.getNumber());
                    dialog.dismiss();
                    Activity activity = (Activity) ctx;
                    activity.recreate();
                });
                dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });
                dialogBuilder.create().show();
                return true;
            } else if (apojo.getBsms() == 1) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
                dialogBuilder.setTitle("Unblock Messages");
                dialogBuilder.setMessage("Are You Sure You Want To Unblock Messages From Number: " + apojo.getNumber());
                dialogBuilder.setPositiveButton("Unblock", (dialog, which) -> {
                    dbHelper.unblocksms(apojo.getNumber());
                    dialog.dismiss();
                    Activity activity = (Activity) ctx;
                    activity.recreate();
                });
                dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });
                dialogBuilder.create().show();
                return true;
            }
            return false;
        }
    }

    @Override
    public ManageSMSAdapter.ManageSMSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_manage, parent, false);
        return new ManageSMSAdapter.ManageSMSViewHolder(itemView, context, listmanagesms);
    }

    @Override
    public void onBindViewHolder(final ManageSMSAdapter.ManageSMSViewHolder holder, int position) {
        holder.name.setText(listmanagesms.get(position).getName());
        holder.number.setText(listmanagesms.get(position).getNumber());
        int stat = listmanagesms.get(position).getBsms();
        if (stat == 0)
            holder.bsms.setText("Not Blocked");
        else
            holder.bsms.setText("Blocked");
    }

    @Override
    public int getItemCount() {
        return listmanagesms.size();
    }
}
