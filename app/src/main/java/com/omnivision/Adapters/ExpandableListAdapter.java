package com.omnivision.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.omnivision.core.Constants;
import com.tablayoutexample.lkelly.omnivision.PrepaidCreditFragment;
import com.tablayoutexample.lkelly.omnivision.R;
import java.util.List;
import java.util.Map;

/**
 * Created by Lodeane on 6/5/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String,Map<String,String>> creditCollections;
    private List<String> credits;

    public ExpandableListAdapter(Activity context, List<String> credits, Map<String,Map<String,String>> creditCollections){
        this.context = context;
        this.creditCollections = creditCollections;
        this.credits = credits;
    }

    @Override
    public int getGroupCount() {
        return credits.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //returning a children count of one (1) because we only want our child view to be loaded once
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return credits.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return creditCollections.get(credits.get(childPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String credit = (String)getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_lv_parent,null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.parent_tv);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(credit);
        LinearLayout expandableLvParentLL = (LinearLayout) convertView.findViewById(R.id.expandable_lv_parent_ll);
        //setting selected item background color
        if(isExpanded){
            expandableLvParentLL.setBackgroundResource(R.color.colorPrimaryDark);
            item.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            expandableLvParentLL.setBackgroundResource(android.R.color.transparent);
            item.setTextColor(context.getResources().getColor(R.color.text_item_selected));
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        if(convertView == null){
            convertView = inflater.inflate(R.layout.expandable_lv_child,null);
        }

        //initializing button that will delete the selected credit
        //Button deleteBtn = (Button) convertView.findViewById(R.id.delete_credit_btn);
        TextView addedByTv = (TextView) convertView.findViewById(R.id.added_by_result_tv);
        TextView isCreditUsedTv = (TextView) convertView.findViewById(R.id.is_credit_used_result_tv);
        TextView dateAddedTv = (TextView) convertView.findViewById(R.id.date_added_result_tv);

        Map<String,String> child = creditCollections.get(credits.get(groupPosition));

        /*deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.Theme_AppCompat_Light_Dialog);
                builder.setMessage("Do you want to remove ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        creditCollections.remove(credits.get(groupPosition));
                        credits.remove(groupPosition);
                        notifyDataSetChanged();
                        Toast.makeText(context,"Credit removed successfully",Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("No",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });*/

        addedByTv.setText(child.get(Constants.PrepaidCredit.ADDED_BY));
        isCreditUsedTv.setText(child.get(Constants.PrepaidCredit.IS_USED));
        dateAddedTv.setText(child.get(Constants.PrepaidCredit.DATE_ADDED));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
