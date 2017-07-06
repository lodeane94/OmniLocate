package com.omnivision.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omnivision.core.CommandHistory;
import com.tablayoutexample.lkelly.omnivision.R;

import java.util.List;

/**
 * Created by Lodeane on 4/7/2017.
 */

public class CmdHistoryRecyclerViewAdapter extends RecyclerView.Adapter<CmdHistoryRecyclerViewAdapter.DataObjectHolder> {
    private static String TAG = CmdHistoryRecyclerViewAdapter.class.getSimpleName();
    private List<CommandHistory> mDataset;
    private static CmdHistoryRecyclerClickListener cmdHistoryRecyclerClickListener;

    public CmdHistoryRecyclerViewAdapter(List<CommandHistory> dataSet) {
        mDataset = dataSet;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cmd_history,parent,false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.cmdName.setText(mDataset.get(position).getCmd());
        holder.dateIssued.setText(mDataset.get(position).getDateIssued());
        holder.issuedBy.setText(mDataset.get(position).getIssuedBy());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setOnItemClickListener(CmdHistoryRecyclerClickListener cmdHistoryRecyclerClickListener) {
        this.cmdHistoryRecyclerClickListener = cmdHistoryRecyclerClickListener;
    }

    public interface CmdHistoryRecyclerClickListener {
        public void onItemClick(int position, View v);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder{

        private TextView cmdName;
        private TextView dateIssued;
        private TextView issuedBy;

        public DataObjectHolder(View itemView) {
            super(itemView);
            cmdName = (TextView) itemView.findViewById(R.id.cmd_name_tv);
            dateIssued = (TextView) itemView.findViewById(R.id.cmd_date_issued_tv);
            issuedBy = (TextView) itemView.findViewById(R.id.cmd_issued_by_tv);
            Log.i(TAG,"Adding listener");
        }
    }
}
