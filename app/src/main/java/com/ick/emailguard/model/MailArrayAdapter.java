package com.ick.emailguard.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ick.emailguard.R;

/**
 * Created by Edo Firmansyah on 01/12/2017.
 */

public class MailArrayAdapter extends ArrayAdapter<String>{

    private final Context context;
    private final String[] ID;
    private final String[] from;
    private final String[] subject;
    private final String[] content;
    private final String[] attachment;
    private final String[] date;


    public MailArrayAdapter(Context context, String[] ID, String[] from,
                            String[] subject, String[] content,String[] attachment, String[] date) {
        super(context, R.layout.lv_mail);
        this.context = context;
        this.ID = ID;
        this.from = from;
        this.subject = subject;
        this.content = content;
        this.attachment = attachment;
        this.date = date;
    }

//    public static void setIsScrolling(boolean isScrolling) {
//        MailArrayAdapter.isScrolling = isScrolling;
//    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.lv_mail, parent, false);

        TextView txtID = (TextView) rowView.findViewById(R.id.id);
        TextView txtFrom = (TextView) rowView.findViewById(R.id.from);
        TextView txtSubject = (TextView) rowView.findViewById(R.id.subject);
        TextView txtContent = (TextView) rowView.findViewById(R.id.content);
        TextView txtAttachment = (TextView) rowView.findViewById(R.id.attachment);
        TextView txtDate = (TextView) rowView.findViewById(R.id.date);;
        txtID.setText(ID[position]);
        txtFrom.setText(from[position]);
        txtSubject.setText(subject[position]);
        txtContent.setText(content[position]);
        txtAttachment.setText(attachment[position]);
        txtDate.setText(date[position]);

        return rowView;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return from.length;
    }

    public String getID(int position) {
        return ID[position];
    }
}
