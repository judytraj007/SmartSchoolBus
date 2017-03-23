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

import Model.Student;

/**
 * Created by Judy T Raj on 08-Mar-17.
 */

public class CustomListAdapter extends ArrayAdapter <Student>{
    private ArrayList<Student> students;
    Activity context;
    LayoutInflater inflater;
    public CustomListAdapter(Activity context, ArrayList students) {
        super(context,0, students);
        this.context = context;
        this.students=students;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater=context.getLayoutInflater();
        Student s=getItem(position);
        View listViewItem = inflater.inflate(R.layout.list_item, null, true);
        TextView textViewId = (TextView) listViewItem.findViewById(R.id.listId);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.listName);
        TextView textViewClass = (TextView) listViewItem.findViewById(R.id.listCls);
        TextView textViewPresent = (TextView) listViewItem.findViewById(R.id.listPresent);

        textViewId.setText(s.getId());
        textViewName.setText(s.getName());
        textViewClass.setText(s.getClassNo());
        if(s.getStatus()==0)
            textViewPresent.setText("No");
        else
            textViewPresent.setText("Yes");

        return listViewItem;
    }
}
