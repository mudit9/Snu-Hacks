package mu.snuhacks.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mu.snuhacks.R;

public class ListViewAdapter extends ArrayAdapter<String> {
    public ListViewAdapter(Context context, ArrayList<String> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
         String user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_component, parent, false);
        }
        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Junction-regular.otf");
        TextView details = (TextView) convertView.findViewById(R.id.text41);
        details.setTypeface(custom_font);
        Log.d("listview",convertView.toString()) ;
        details.setText(user);
        return convertView;
    }
}