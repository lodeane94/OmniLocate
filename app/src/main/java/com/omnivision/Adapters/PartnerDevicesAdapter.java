package com.omnivision.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omnivision.core.PartnerDevice;
import com.tablayoutexample.lkelly.omnivision.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lkelly on 3/18/2017.
 */

public class PartnerDevicesAdapter extends ArrayAdapter<PartnerDevice> {

    int resource;
    List<PartnerDevice> partnerDevices = new ArrayList<PartnerDevice>();
    Context context;
    //initializing adapter
    public PartnerDevicesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<PartnerDevice> items) {
        super(context, resource, items);
        this.resource = resource;
        this.context = context;
        this.partnerDevices = items;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        View partnerDeviceView = view;
        PartnerDevice partnerDevice = getItem(position);//getting current partner device object

        //inflate the view
        if(view == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            partnerDeviceView = inflater.inflate(resource, parent, false);
        }else{
            partnerDeviceView = view;
        }
        //setting widget to view
        TextView partnerDeviceNumTv = (TextView) partnerDeviceView.findViewById(R.id.partner_device_num_tv);

        /*assigning partner device number from partner device class to
          the widget above to allow the usage of the array adapter on custom objects
         */
        partnerDeviceNumTv.setText(partnerDevice.getPartnerDeviceNum());

        return partnerDeviceView;
    }
}
