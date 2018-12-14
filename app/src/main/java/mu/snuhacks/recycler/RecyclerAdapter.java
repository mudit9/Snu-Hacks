package mu.snuhacks.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mu.snuhacks.R;


/**
 * Created by florentchampigny on 07/08/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    protected static final int TYPE_HEADER = 0;
    protected static final int TYPE_CELL = 1;
    public ArrayList<String> Menu_full;
    public RecyclerAdapter(ArrayList<String> menu_full){
        Menu_full = menu_full;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0: return TYPE_HEADER;
            default: return TYPE_CELL;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView meal, menu;

        public MyViewHolder(View view) {
            super(view);
            meal = (TextView) view.findViewById(R.id.meal);
            menu = (TextView) view.findViewById(R.id.menu);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view;
        switch (type){
            case TYPE_HEADER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hvp_header_placeholder,viewGroup,false);
                break;
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_card,viewGroup,false);
                break;
        }
        return new MyViewHolder(view) {};
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Log.d("afa",""+i);
        if (i == 1){
            holder.meal.setText("Breakfast");
            holder.menu.setText(Menu_full.get(i-1));
        }
        if (i == 2){
            holder.meal.setText("Lunch");
            holder.menu.setText(Menu_full.get(i-1));

        }
        if (i == 3){
            holder.meal.setText("Dinner");
            holder.menu.setText(Menu_full.get(i-1));

        }
    }


    @Override
    public int getItemCount() {
        return 4;
    }
}
