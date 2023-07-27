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
import hector.developers.uniconnectapp.list.AirtimeListActivity;
import hector.developers.uniconnectapp.model.AirtimeOperator;

public class AirtimeOperatorAdapter extends RecyclerView.Adapter<AirtimeOperatorAdapter.ViewHolder> {

    Context context;
    List<AirtimeOperator> airtimeOperatorList;

    public AirtimeOperatorAdapter(List<AirtimeOperator> airtimeOperatorList, Context context) {
        this.airtimeOperatorList = airtimeOperatorList;
        this.context = context;
    }

    @NonNull
    @Override
    public AirtimeOperatorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.airtimeoperatorslist, parent, false);
        return new AirtimeOperatorAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AirtimeOperatorAdapter.ViewHolder holder, int position) {
        AirtimeOperator airtimeOperator = airtimeOperatorList.get(position);
        holder.tvDesc.setText(airtimeOperator.getDesc());
        holder.tvName.setText(airtimeOperator.getName());
        holder.tvSector.setText(airtimeOperator.getSector());
        Glide.with(context)
                .load(airtimeOperator.getImgUrl())
                .into(holder.imgVImgUrl);
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale));
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E8EAF6"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#BBDEFB"));
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AirtimeListActivity.class);
            intent.putExtra("key", airtimeOperator);
            intent.putExtra("id", airtimeOperator.getId());
            intent.putExtra("desc", airtimeOperator.getDesc());
            intent.putExtra("sector", airtimeOperator.getSector());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return airtimeOperatorList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDesc;
        private final TextView tvName;
        private final TextView tvSector;
        private final ImageView imgVImgUrl;


        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDesc = itemView.findViewById(R.id.tvAirtimeDesc);
            tvName = itemView.findViewById(R.id.tvAirtimeName);
            tvSector = itemView.findViewById(R.id.tvAirtimeSector);
            imgVImgUrl = itemView.findViewById(R.id.airtimeImageUrl);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}