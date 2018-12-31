package mu.snuhacks.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import mu.snuhacks.R;
import mu.snuhacks.firstActivity;

public class NewsletterAdapter extends RecyclerView.Adapter<NewsletterAdapter.ViewHolder> {
    private final String TAG = NewsletterAdapter.class.getSimpleName();

    private NewsletterAdapterInterface newsletterAdapterInterface;

    private ArrayList<firstActivity.NewsletterData> newsletterData = new ArrayList<firstActivity.NewsletterData>();

    public NewsletterAdapter(NewsletterAdapterInterface newsletterAdapterInterface){
        this.newsletterAdapterInterface = newsletterAdapterInterface;
    }

    public interface NewsletterAdapterInterface {
        void onEdit(firstActivity.NewsletterData newsletter);
        void onDelete(firstActivity.NewsletterData newsletter);
        void sendNotification(firstActivity.NewsletterData newsletter);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(newsletterData.size() > 0) {
            holder.bind(newsletterData.get(position));
        }
    }

    public void remove(firstActivity.NewsletterData data){
        int flag = -1;
        for(int i=0;i<newsletterData.size();i++){
            if(newsletterData.get(i).getKey().equals(data.getKey())){
                flag = i;
            }
        }
        if(flag != -1){
            newsletterData.remove(flag);
            notifyDataSetChanged();
        }
    }

    public void changeData(firstActivity.NewsletterData data) {
        int flag = -1;
        for (int i = 0; i < newsletterData.size(); i++) {
            if (newsletterData.get(i).getKey().equals(data.getKey())) {
                flag = i;
            }
        }
        if(flag !=-1){
            newsletterData.get(flag).setHeading(data.getHeading());
            newsletterData.get(flag).setContent(data.getContent());
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return newsletterData.size();
    }

    public void add(firstActivity.NewsletterData newsletter){
        newsletterData.add(newsletter);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final String TAG = ViewHolder.class.getSimpleName();

        private TextView heading;
        private TextView content;
        private ImageButton editButton;
        private ImageButton deleteButton;
        private ImageButton sendNotificationButton;
        private CardView card;

        public ViewHolder(View view){
            super(view);
            heading = (TextView) view.findViewById(R.id.meal);
            heading.setPadding(0,0,0,5);
            content = (TextView) view.findViewById(R.id.menu);
            card = (CardView) view.findViewById(R.id.card);
            if(newsletterAdapterInterface != null){
                editButton = (ImageButton) view.findViewById(R.id.edit_image_button);
                deleteButton = (ImageButton) view.findViewById(R.id.delete_image_button);
                sendNotificationButton = (ImageButton) view.findViewById(R.id.send_notification_image_button);
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                sendNotificationButton.setVisibility(View.VISIBLE);
            }
        }

        public void bind(final firstActivity.NewsletterData data){
            heading.setText(data.getHeading());
            content.setText(data.getContent());
            //card.setCardBackgroundColor(Color.parseColor("#fff4e6"));

            if(newsletterAdapterInterface != null){
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newsletterAdapterInterface.onEdit(data);
                    }
                });
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newsletterAdapterInterface.onDelete(data);
                    }
                });
                sendNotificationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newsletterAdapterInterface.sendNotification(data);
                    }
                });
            }
        }
    }

}
