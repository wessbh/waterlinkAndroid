package com.fourwhys.waterlink.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourwhys.waterlink.Entities.Facture;
import com.fourwhys.waterlink.R;

import java.util.List;

public class FactureListAdapter extends RecyclerView.Adapter<FactureListAdapter.MyViewHolder> {

    private List<Facture> facturesList;
    private Context mContext;
    private int selected_position = 0;


    public FactureListAdapter(Context mContext, List<Facture> annoncesList) {
        this.facturesList = annoncesList;
        mContext = this.mContext;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView periode, status;
        ImageView img_status;

        private MyViewHolder(View view) {
            super(view);
            periode = view.findViewById(R.id.periode_value);
            status = view.findViewById(R.id.status);
            img_status =(ImageView) view.findViewById(R.id.img_status);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Facture facture = facturesList.get(position);
        holder.periode.setText(facture.getPeriode());
        holder.status.setText(facture.getStatus());
        if(facture.getStatus().equals("1")){
            holder.img_status.setBackgroundResource(R.drawable.ic_correct);
        }
        else
            holder.img_status.setBackgroundResource(R.drawable.ic_wrong);
        

    }

    @Override
    public int getItemCount() {
        return facturesList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


}


