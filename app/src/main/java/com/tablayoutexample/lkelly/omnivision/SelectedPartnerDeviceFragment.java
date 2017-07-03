package com.tablayoutexample.lkelly.omnivision;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.omnivision.core.DaoSession;
import com.omnivision.core.PartnerDevice;
import com.omnivision.dao.IPartnerDeviceDao;
import com.omnivision.dao.PartnerDeviceDaoImpl;
import com.omnivision.utilities.PartnerDevicesAdapter;
import com.omnivision.utilities.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectedPartnerDeviceFragment extends Fragment {

    long phoneId;
    private String TAG = SelectedPartnerDeviceFragment.class.getSimpleName();
    public static long ARG_PARTNER_DEVICE_ID;
    private ListView partnerDeviceLv;
    private PartnerDevicesAdapter partnerDeviceAdapter;
    private PartnerDevice partnerDevices;

    private HashMap<String,String> userDetails;
    private DaoSession daoSession;
    private SessionManager sessionManager;

    // private IPhoneDao phoneDao;
    private IPartnerDeviceDao partnerDeviceDao;
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
                partnerDeviceDao = new PartnerDeviceDaoImpl(daoSession);
            }catch (Exception ex){
                String errMessage = "No partner devices found for this device";
                Log.e(TAG,errMessage + "\n"+ex.getLocalizedMessage());
                Toast.makeText(context,errMessage,Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selected_partner_device, container, false);
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
