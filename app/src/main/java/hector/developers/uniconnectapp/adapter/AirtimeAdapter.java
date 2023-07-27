package hector.developers.uniconnectapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
;
import hector.developers.uniconnectapp.R;
import hector.developers.uniconnectapp.detail.AirtimeDetailActivity;
import hector.developers.uniconnectapp.model.Airtime;

public class AirtimeAdapter extends RecyclerView.Adapter<AirtimeAdapter.ViewHolder> {

    Context context;
    List<Airtime> airtimeList;

    public AirtimeAdapter(List<Airtime> airtimeList, Context context) {
        this.airtimeList = airtimeList;
        this.context = context;
    }

    @NonNull
    @Override
    public AirtimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.airlist, parent, false);
        return new AirtimeAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AirtimeAdapter.ViewHolder holder, int position) {
        Airtime airtime = airtimeList.get(position);
        holder.airFeeType.setText(airtime.getFee_type());
        holder.airName.setText(airtime.getName());
        holder.airMax.setText(airtime.getMaximum_fee());
        holder.minAmount.setText(airtime.getMinimum_fee());
        Glide.with(context)
                .load(airtime.getImgUrl())
                .into(holder.airImgUrl);
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale));
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E8EAF6"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#BBDEFB"));
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AirtimeDetailActivity.class);
            intent.putExtra("key", airtime);
            intent.putExtra("id", airtime.getId());
            intent.putExtra("operator", airtime.getOperator());
            intent.putExtra("category", airtime.getCategory());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return airtimeList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView airMax;
        private final TextView airName;
        private final TextView airFeeType;
        private final ImageView airImgUrl;
        private final TextView minAmount;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            airMax = itemView.findViewById(R.id.airMax);
            minAmount = itemView.findViewById(R.id.minAmount);
            airFeeType = itemView.findViewById(R.id.airFeeType);
            airName = itemView.findViewById(R.id.airName);
            airImgUrl = itemView.findViewById(R.id.airImgUrl);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}