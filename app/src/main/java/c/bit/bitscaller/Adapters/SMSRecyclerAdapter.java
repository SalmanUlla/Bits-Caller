package c.bit.bitscaller.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import c.bit.bitscaller.Pojo;
import c.bit.bitscaller.R;

public class SMSRecyclerAdapter extends RecyclerView.Adapter<SMSRecyclerAdapter.SMSViewHolder> {

    private ArrayList<Pojo> listPhone;
    Context context;

    public SMSRecyclerAdapter(ArrayList<Pojo> listPhone, Context context) {
        this.listPhone = listPhone;
        this.context = context;

    }


    public class SMSViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView number;
        public TextView body;
        public TextView time;
        public TextView date;

        public SMSViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.namesms);
            number = view.findViewById(R.id.numbersms);
            body = view.findViewById(R.id.smsbody);
            time = view.findViewById(R.id.timesms);
            date = view.findViewById(R.id.datesms);
        }
    }

    @Override
    public SMSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_sms, parent, false);
        return new SMSViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SMSViewHolder holder, int position) {
        holder.name.setText(listPhone.get(position).getName());
        holder.number.setText(listPhone.get(position).getNumber());
        holder.body.setText(listPhone.get(position).getBody());
        holder.time.setText(listPhone.get(position).getTime());
        holder.date.setText(listPhone.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return listPhone.size();
    }
}
