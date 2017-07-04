package com.tablayoutexample.lkelly.omnivision;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.omnivision.Adapters.CmdHistoryRecyclerViewAdapter;
import com.omnivision.core.CommandHistory;
import com.omnivision.core.DaoSession;
import com.omnivision.core.PartnerDevice;
import com.omnivision.dao.CommandHistoryDaoImpl;
import com.omnivision.dao.ICommandHistoryDao;
import com.omnivision.dao.IPartnerDeviceDao;
import com.omnivision.dao.PartnerDeviceDaoImpl;
import com.omnivision.Adapters.PartnerDevicesAdapter;
import com.omnivision.utilities.SessionManager;

import java.util.HashMap;
import java.util.List;

public class SelectedPartnerDeviceFragment extends Fragment {

    private String TAG = SelectedPartnerDeviceFragment.class.getSimpleName();
   // public static long ARG_PARTNER_DEVICE_ID;
    public static String ARG_PARTNER_DEVICE_NUMBER;
   // private PartnerDevice partnerDevice;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private HashMap<String,String> userDetails;
    private DaoSession daoSession;
    private SessionManager sessionManager;

    private ICommandHistoryDao commandHistoryDao;
    private Context context;

    private OnFragmentInteractionListener mListener;

    public SelectedPartnerDeviceFragment() {
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
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<CommandHistory> commandHistoriesByIssuer = commandHistoryDao.getAllByCmdIssuer(ARG_PARTNER_DEVICE_NUMBER);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_selected_partner_device, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cmd_history_rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CmdHistoryRecyclerViewAdapter(commandHistoriesByIssuer);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
