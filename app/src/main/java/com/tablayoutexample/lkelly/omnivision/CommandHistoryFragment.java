package com.tablayoutexample.lkelly.omnivision;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.omnivision.Adapters.CmdHistoryRecyclerViewAdapter;
import com.omnivision.core.CommandHistory;
import com.omnivision.core.Constants;
import com.omnivision.core.DaoSession;
import com.omnivision.core.Phone;
import com.omnivision.dao.CommandHistoryDaoImpl;
import com.omnivision.dao.ICommandHistoryDao;
import com.omnivision.utilities.SessionManager;

import java.util.HashMap;
import java.util.List;

public class CommandHistoryFragment extends Fragment {

    private String TAG = CommandHistoryFragment.class.getSimpleName();
    public static long ARG_NAVIGATION_POSITION;
    public static String ARG_PARTNER_DEVICE_NUMBER;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private HashMap<String,String> userDetails;
    private DaoSession daoSession;
    private SessionManager sessionManager;

    private ICommandHistoryDao commandHistoryDao;
    private Context context;

    private TextView partnerDeviceJumbotron;

    public CommandHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                context = getContext();
                daoSession = OmniLocateApplication.getSession(context);//initializing dao session
                //initializing session manager
                sessionManager = new SessionManager(context);
                //setting user details
                userDetails = sessionManager.getUserDetails();
                commandHistoryDao = new CommandHistoryDaoImpl(daoSession);
            }catch (Exception ex){
                String errMessage = "Error occurred while initializing";
                Log.e(TAG,errMessage + "\n"+ex.getLocalizedMessage());
                Toast.makeText(context,errMessage,Toast.LENGTH_LONG);
                ex.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        List<CommandHistory> commandHistoriesByIssuer;
        try {
            // Inflate the layout for this fragment
            rootView = inflater.inflate(R.layout.fragment_selected_partner_device, container, false);

            if(ARG_NAVIGATION_POSITION == Constants.Navigation.NavigationPositions.PARTNER_DEVICES){
                commandHistoriesByIssuer = commandHistoryDao.getAllByCmdIssuer(ARG_PARTNER_DEVICE_NUMBER);

                //display jumbotron highligting the partner device's number and alias
                partnerDeviceJumbotron = (TextView) rootView.findViewById(R.id.partner_device_jumbotron);
                partnerDeviceJumbotron.setText(ARG_PARTNER_DEVICE_NUMBER);
                partnerDeviceJumbotron.setVisibility(View.VISIBLE);
            }else{
                Phone phone = OmniLocateApplication.getPhoneInstance();
                commandHistoriesByIssuer = commandHistoryDao.findAllByOwnerId(phone.getId());
            }

            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cmd_history_rv);
            mLayoutManager = new LinearLayoutManager(context);
            mAdapter = new CmdHistoryRecyclerViewAdapter(commandHistoriesByIssuer);

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        } catch (Exception ex) {
            String errMessage = "Error occurred while initializing";
            Log.e(TAG, errMessage + "\n" + ex.getLocalizedMessage());
            Toast.makeText(context, errMessage, Toast.LENGTH_LONG);
            ex.printStackTrace();
        }

        return rootView;
    }
}
