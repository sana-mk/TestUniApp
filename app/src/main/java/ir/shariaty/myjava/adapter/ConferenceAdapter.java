package ir.shariaty.myjava.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import ir.shariaty.myjava.R;
import ir.shariaty.myjava.model.Conference;

public class ConferenceAdapter extends RecyclerView.Adapter<ConferenceAdapter.ConferenceViewHolder> {

    private List<Conference> conferenceList = new ArrayList<>();
    private OnFavoriteClickListener onFavoriteClickListener;
    private OnConferenceClickListener onConferenceClickListener;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Conference conference);
    }

    public interface OnConferenceClickListener {
        void onConferenceClick(Conference conference);
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.onFavoriteClickListener = listener;
    }

    public void setOnConferenceClickListener(OnConferenceClickListener listener) {
        this.onConferenceClickListener = listener;
    }


    @NonNull
    @Override
    public ConferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conference, parent, false);
        return new ConferenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConferenceViewHolder holder, int position) {
        holder.bind(conferenceList.get(position));
    }

    @Override
    public int getItemCount() {
        return conferenceList.size();
    }

    public void submitList(List<Conference> list) {
        this.conferenceList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }


    class ConferenceViewHolder extends RecyclerView.ViewHolder {

        ImageView ivFavorite, ivImage;
        TextView tvTitle, tvOrganizer, tvPlace, tvDate, tvTime, tvStatusLabel;
        MaterialButton btnStatus;

        public ConferenceViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFavorite   = itemView.findViewById(R.id.ivFavorite);
            ivImage      = itemView.findViewById(R.id.ivImage);
            tvTitle      = itemView.findViewById(R.id.tvTitle);
            tvOrganizer  = itemView.findViewById(R.id.tvOrganizer);
            tvPlace      = itemView.findViewById(R.id.tvPlace);
            tvDate       = itemView.findViewById(R.id.tvDate);
            tvTime       = itemView.findViewById(R.id.tvTime);
            btnStatus    = itemView.findViewById(R.id.btnStatus);
            tvStatusLabel = itemView.findViewById(R.id.tvStatusLabel);
        }

        void bind(Conference conference) {

            tvTitle.setText(conference.getTitle());
            tvOrganizer.setText(conference.getOrganizer());
            tvPlace.setText(conference.getPlace());
            tvDate.setText(conference.getDate());
            tvTime.setText(conference.getTime());

            Conference.Status status = conference.getStatus();
            btnStatus.setText(Conference.getStatusText(itemView.getContext(), status));
            btnStatus.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(
                            Conference.getStatusColor(itemView.getContext(), status)
                    )
            );

            tvStatusLabel.setVisibility(View.VISIBLE);

            ivImage.setImageResource(conference.getImageResId());

            updateFavoriteIcon(conference);

            ivFavorite.setOnClickListener(v -> {
                if (onFavoriteClickListener != null) {
                    onFavoriteClickListener.onFavoriteClick(conference);
                }
            });

            itemView.setOnClickListener(v -> {
                if (onConferenceClickListener != null) {
                    onConferenceClickListener.onConferenceClick(conference);
                }
            });

            btnStatus.setOnClickListener(v -> {
                if (onConferenceClickListener != null) {
                    onConferenceClickListener.onConferenceClick(conference);
                }
            });
        }

        private void updateFavoriteIcon(Conference conference) {
            if (conference.isFavorite()) {
                ivFavorite.setColorFilter(
                        ContextCompat.getColor(itemView.getContext(), R.color.status_expired)
                );
            } else {
                ivFavorite.setColorFilter(
                        ContextCompat.getColor(itemView.getContext(), R.color.gray)
                );
            }
        }
    }
}
