package com.omnivision.utilities;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

/**
 * Created by Lodeane on 17/5/2017.
 */

public class NavigationListViewAdapter extends ArrayAdapter {
    public NavigationListViewAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }
}
