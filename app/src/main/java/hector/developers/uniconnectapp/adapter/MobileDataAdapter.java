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
import hector.developers.uniconnectapp.detail.DataDetailActivity;
import hector.developers.uniconnectapp.model.MobileData;

public class MobileDataAdapter extends RecyclerView.Adapter<MobileDataAdapter.ViewHolder> {

    Context context;
    List<MobileData> mobileDataList;

    public MobileDataAdapter(List<MobileData> mobileDataList, Context context) {
        this.mobileDataList = mobileDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mobiledatalist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MobileData mobileData = mobileDataList.get(position);
        holder.tvName.setText(mobileData.getName());
        holder.tvDataValue.setText(mobileData.getData_value());
        holder.tvAmount.setText(mobileData.getAmount());
        holder.tvExpiry.setText(mobileData.getData_expiry());
        Glide.with(context)
                .load(mobileData.getImgUrl())
                .into(holder.imgVimgUrl);

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale));
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E8EAF6"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#BBDEFB"));
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DataDetailActivity.class);
            intent.putExtra("key", mobileData);
            intent.putExtra("operator_id", mobileData.getOperator());
            intent.putExtra("product_id", mobileData.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mobileDataList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvDataValue;
        private final TextView tvExpiry;
        private final TextView tvAmount;
        private final ImageView imgVimgUrl;

        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
            tvDataValue = itemView.findViewById(R.id.tvDataValue);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            imgVimgUrl = itemView.findViewById(R.id.imgUrl);


            cardView = itemView.findViewById(R.id.cardView);
        }

    }
}
