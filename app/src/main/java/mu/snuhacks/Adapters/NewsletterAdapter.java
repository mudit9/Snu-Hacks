package mu.snuhacks.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mu.snuhacks.R;
import mu.snuhacks.firstActivity;

public class NewsletterAdapter extends RecyclerView.Adapter<NewsletterAdapter.ViewHolder> {
    private final String TAG = NewsletterAdapter.class.getSimpleName();

    private ArrayList<firstActivity.NewsletterData> newsletterData = new ArrayList<firstActivity.NewsletterData>();

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
            if(newsletterData.get(i).getContent().equals(data.getContent()) &&
                    newsletterData.get(i).getHeading().equals(data.getContent())){
                flag = i;
            }
        }
        if(flag != -1){
            newsletterData.remove(flag);
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

        public ViewHolder(View view){
            super(view);
            heading = (TextView) view.findViewById(R.id.meal);
            content = (TextView) view.findViewById(R.id.menu);
        }

        public void bind(firstActivity.NewsletterData data){
            heading.setText(data.getHeading());
            content.setText(data.getContent());
        }
    }

}
