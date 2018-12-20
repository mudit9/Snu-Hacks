package mu.snuhacks.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
        for(int j = 0; j<3; j++){
            String textToHighlight = "Chicken";
            String textToHighlight2 = "Paneer";
            // Construct the formatted text
            String replacedWith = "<b><font color= #13c000>" + textToHighlight + "</font></b>";
            String replacedWith2 = "<b><font color= #13c000>" + textToHighlight + "</font></b>";

            // Get the text from TextView
            String originalString = Menu_full.get(j);

            // Replace the specified text/word with formatted text/word
            String modifiedString1 = originalString.replaceAll(textToHighlight,replacedWith);
            String modifiedString = modifiedString1.replaceAll(textToHighlight2,replacedWith2);

            Log.d("Tag",modifiedString);
            Menu_full.set(j,modifiedString);
        }
        if (i == 1){
            holder.meal.setText("Breakfast");
            holder.menu.setText(Html.fromHtml(Menu_full.get(i-1)));
        }
        if (i == 2){
            holder.meal.setText("Lunch");
            holder.menu.setText(Html.fromHtml(Menu_full.get(i-1)));

        }
        if (i == 3){
            holder.meal.setText("Dinner");
            holder.menu.setText(Html.fromHtml(Menu_full.get(i-1)));

        }
    }


    @Override
    public int getItemCount() {
        return 4;
    }
}
