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
import hector.developers.uniconnectapp.list.TvListActivity;
import hector.developers.uniconnectapp.model.UniOperators;;

public class UniOperatorsAdapter extends RecyclerView.Adapter<UniOperatorsAdapter.ViewHolder> {

    Context context;
    List<UniOperators> operatorsList;

    public UniOperatorsAdapter(List<UniOperators> operatorsList, Context context) {
        this.operatorsList = operatorsList;
        this.context = context;
    }
    @NonNull
    @Override
    public UniOperatorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.unioperatorslist, parent, false);
        return new UniOperatorsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UniOperatorsAdapter.ViewHolder holder, int position) {
        UniOperators operators = operatorsList.get(position);
        holder.tvDesc.setText(operators.getDesc());
        holder.tvName.setText(operators.getName());
        holder.tvSector.setText(operators.getSector());
        Glide.with(context)
                .load(operators.getImgUrl())
                .into(holder.imgVImgUrl);
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale));
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E8EAF6"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#BBDEFB"));
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TvListActivity.class);
            intent.putExtra("key", operators);
            intent.putExtra("id", operators.getId());
            intent.putExtra("desc", operators.getDesc());
            intent.putExtra("sector", operators.getSector());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return operatorsList.size();

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

        }  }
    }