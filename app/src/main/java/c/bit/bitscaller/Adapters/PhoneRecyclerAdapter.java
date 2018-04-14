package c.bit.bitscaller.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import c.bit.bitscaller.Pojo;
import c.bit.bitscaller.R;

public class PhoneRecyclerAdapter extends RecyclerView.Adapter<PhoneRecyclerAdapter.PhoneViewHolder> {

    private ArrayList<Pojo> listPhone;
    Context context;

    public PhoneRecyclerAdapter(ArrayList<Pojo> listPhone, Context context) {
        this.listPhone = listPhone;
        this.context = context;

    }


    public class PhoneViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView number;
        public TextView lati;
        public TextView longi;
        public TextView time;
        public TextView date;

        public PhoneViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            number = view.findViewById(R.id.number);
            lati = view.findViewById(R.id.lati);
            longi = view.findViewById(R.id.longi);
            time = view.findViewById(R.id.time);
            date = view.findViewById(R.id.date);
            Log.e("Error 4", "Reached Here  4");
        }
    }

    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new PhoneViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhoneViewHolder holder, int position) {
        holder.name.setText(listPhone.get(position).getName());
        holder.number.setText(listPhone.get(position).getNumber());
        holder.lati.setText("" + listPhone.get(position).getLati());
        holder.longi.setText("" + listPhone.get(position).getLongi());
        holder.time.setText(listPhone.get(position).getTime());
        Log.e("Error5 ", "Reached Here 5");
        holder.date.setText(listPhone.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return listPhone.size();
    }
}
