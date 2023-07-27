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

import hector.developers.uniconnectapp.R;
import hector.developers.uniconnectapp.detail.PowerDetailsActivity;
import hector.developers.uniconnectapp.model.Power;


public class PowerAdapter extends RecyclerView.Adapter<PowerAdapter.ViewHolder> {

    Context context;
    List<Power> powerList;

    public PowerAdapter(List<Power> powerList, Context context) {
        this.powerList = powerList;
        this.context = context;
    }

    @NonNull
    @Override
    public PowerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.powerlist, parent, false);
        return new PowerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PowerAdapter.ViewHolder holder, int position) {
        Power power = powerList.get(position);
        holder.tvDesc.setText(power.getDesc());
        holder.tvName.setText(power.getName());
        holder.tvSector.setText(power.getSector());
        Glide.with(context)
                .load(power.getImgUrl())
                .into(holder.imgVImgUrl);
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale));
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E8EAF6"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#BBDEFB"));
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PowerDetailsActivity.class);
            intent.putExtra("key", power);
            intent.putExtra("id", power.getId());
            intent.putExtra("sector", power.getSector());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return powerList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDesc;
        private final TextView tvName;
        private final TextView tvSector;
        private final ImageView imgVImgUrl;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvName = itemView.findViewById(R.id.tvName);
            tvSector = itemView.findViewById(R.id.tvSector);
            imgVImgUrl = itemView.findViewById(R.id.imageUrl);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}