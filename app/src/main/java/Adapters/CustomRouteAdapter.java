package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.judy.smartschoolbus.R;

import java.util.ArrayList;

import Model.Stop;

/**
 * Created by Judy T Raj on 09-Mar-17.
 */

public class CustomRouteAdapter extends ArrayAdapter<Stop> {
    ArrayList<Stop> stops;
    Activity context;
    LayoutInflater inflater;
    public CustomRouteAdapter(Activity context,ArrayList<Stop> stops) {
        super(context,0,stops);
        this.stops=stops;
        this.context=context;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater=context.getLayoutInflater();
        Stop s=getItem(position);
        View listViewItem = inflater.inflate(R.layout.list_item_route, null, true);
        TextView textViewId = (TextView) listViewItem.findViewById(R.id.SerialNo);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.stopName);
        TextView textViewAm = (TextView) listViewItem.findViewById(R.id.amTime);
        TextView textViewPm = (TextView) listViewItem.findViewById(R.id.pmTime);

        textViewId.setText(s.getsNo());
        textViewName.setText(s.getStopName());
        textViewAm.setText(s.getAmTime());
        textViewPm.setText(s.getPmTime());
        return listViewItem;
    }
}
