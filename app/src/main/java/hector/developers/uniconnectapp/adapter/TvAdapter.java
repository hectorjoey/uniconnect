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
import hector.developers.uniconnectapp.detail.TvDetailsActivity;
import hector.developers.uniconnectapp.model.Tv;


public class TvAdapter extends RecyclerView.Adapter<TvAdapter.ViewHolder> {

    Context context;
    List<Tv> tvList;

    public TvAdapter(List<Tv> tvList, Context context) {
        this.tvList = tvList;
        this.context = context;
    }

    @NonNull
    @Override
    public TvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tvlist, parent, false);
        return new TvAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TvAdapter.ViewHolder holder, int position) {
        Tv tv = tvList.get(position);
        holder.teleFreeType.setText(tv.getFee_type());
        holder.teleName.setText(tv.getName());
        holder.teleAmount.setText(tv.getAmount());
        Glide.with(context)
                .load(tv.getImgUrl())
                .into(holder.teleImgVImgUrl);
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale));
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E8EAF6"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#BBDEFB"));
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TvDetailsActivity.class);
            intent.putExtra("key", tv);
            intent.putExtra("id", tv.getId());
            intent.putExtra("operator", tv.getOperator());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tvList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView teleAmount;
        private final TextView teleName;
        private final TextView teleFreeType;
        private final ImageView teleImgVImgUrl;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);


            teleAmount = itemView.findViewById(R.id.teleAmount);
            teleFreeType = itemView.findViewById(R.id.teleFreeType);
            teleName = itemView.findViewById(R.id.teleName);
            teleImgVImgUrl = itemView.findViewById(R.id.teleImgUrl);
            cardView = itemView.findViewById(R.id.cardView);
        }

    }
}
