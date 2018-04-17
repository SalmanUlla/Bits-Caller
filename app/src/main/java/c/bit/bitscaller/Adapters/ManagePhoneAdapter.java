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
import c.bit.bitscaller.managephonelist;

public class ManagePhoneAdapter extends RecyclerView.Adapter<ManagePhoneAdapter.ManagePhoneViewHolder> {

    private ArrayList<Pojo> listmanagePhone;
    Context context;

    public ManagePhoneAdapter(ArrayList<Pojo> listmanagePhone, Context context) {
        this.listmanagePhone = listmanagePhone;
        this.context = context;
    }

    public static class ManagePhoneViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        ArrayList<Pojo> managedList;
        Context ctx;
        DatabaseHelper dbHelper;

        public TextView name;
        public TextView number;
        public TextView bphone;

        public ManagePhoneViewHolder(View view, Context ctx, ArrayList<Pojo> ledList) {
            super(view);
            this.ctx = ctx;
            this.managedList = ledList;
            dbHelper = new DatabaseHelper(ctx);
            view.setOnLongClickListener(this);
            name = view.findViewById(R.id.name_manage);
            number = view.findViewById(R.id.number_manage);
            bphone = view.findViewById(R.id.managestatus);
        }

        @Override
        public boolean onLongClick(View view) {
            //Do what you have to do here on Long Click
            int position = getAdapterPosition();
            Pojo apojo = this.managedList.get(position);
            if (apojo.getBphone() == 0) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
                dialogBuilder.setTitle("Block Calls");
                dialogBuilder.setMessage("Are You Sure You Want To Block Calls From Number: " + apojo.getNumber());
                dialogBuilder.setPositiveButton("Block", (dialog, which) -> {
                    dbHelper.blockphone(apojo.getNumber());
                    dialog.dismiss();
                    Activity activity = (Activity) ctx;
                    activity.recreate();
                });
                dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });
                dialogBuilder.create().show();
                return true;
            } else if (apojo.getBphone() == 1) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
                dialogBuilder.setTitle("Unblock Calls");
                dialogBuilder.setMessage("Are You Sure You Want To Unblock Calls From Number: " + apojo.getNumber());
                dialogBuilder.setPositiveButton("Unblock", (dialog, which) -> {
                    dbHelper.unblockphone(apojo.getNumber());
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
    public ManagePhoneAdapter.ManagePhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_manage, parent, false);
        return new ManagePhoneAdapter.ManagePhoneViewHolder(itemView, context, listmanagePhone);
    }

    @Override
    public void onBindViewHolder(final ManagePhoneAdapter.ManagePhoneViewHolder holder, int position) {
        holder.name.setText(listmanagePhone.get(position).getName());
        holder.number.setText(listmanagePhone.get(position).getNumber());
        int stat = listmanagePhone.get(position).getBphone();
        if (stat == 0)
            holder.bphone.setText("Not Blocked");
        else
            holder.bphone.setText("Blocked");
    }

    @Override
    public int getItemCount() {
        return listmanagePhone.size();
    }
}
