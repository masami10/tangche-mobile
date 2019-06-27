package com.example.tangchemobile;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.tangchemobile.services.config;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private Switch swScannerContinueMode;
    private EditText txtRfidPort;
    private Spinner spRfidPower;
    private Spinner spRfidSensitive;
    private EditText txtRushUrl;

    private Button btnSave;

    public SettingFragment() {
        // Required empty public constructor
    }

    private void initView(View v) {
        this.swScannerContinueMode = v.findViewById(R.id.continuescan);
        this.txtRfidPort = v.findViewById(R.id.txtPort);
        this.spRfidPower = v.findViewById(R.id.spPower);
        this.spRfidSensitive = v.findViewById(R.id.spSense);
        this.txtRushUrl = v.findViewById(R.id.txtRushUrl);
        this.btnSave = v.findViewById(R.id.btnSave);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void loadConfig() {
        Map<String, String> cfg = config.Load(this.getContext());
        for (Map.Entry<String, String> entry : cfg.entrySet()) {
            switch (entry.getKey()) {
                case config.SCANNER_CONTINUE_MODE:
                    this.swScannerContinueMode.setChecked(Boolean.parseBoolean(entry.getValue()));
                    break;

                case config.RFID_PORT:
                    this.txtRfidPort.setText(entry.getValue());
                    break;

                case config.RFID_POWER_INDEX:
                    this.spRfidPower.setSelection(Integer.parseInt(entry.getValue()));
                    break;

                case config.RFID_SENSITIVE_INDEX:
                    this.spRfidSensitive.setSelection(Integer.parseInt(entry.getValue()));
                    break;

                case config.RUSH_URL:
                    this.txtRushUrl.setText(entry.getValue());
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        this.initView(v);

        String[] powers = {"26dbm", "24dbm", "20dbm", "18.5dbm", "17dbm", "15.5dbm", "14dbm", "12.5dbm"};
        ArrayAdapter<String> powersAdapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, powers);
        powersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spRfidPower.setAdapter(powersAdapter);

        String[] sensitives = {"高", "中", "低", "非常低"};
        ArrayAdapter<String> senAdapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, sensitives);
        senAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spRfidSensitive.setAdapter(senAdapter);

        // 载入配置
        this.loadConfig();

        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 保存配置

                Map<String, String> cfg = new HashMap<>();
                cfg.put(config.SCANNER_CONTINUE_MODE, String.valueOf(swScannerContinueMode.isChecked()));
                cfg.put(config.RFID_PORT, txtRfidPort.getText().toString());
                cfg.put(config.RFID_POWER_INDEX, String.valueOf(spRfidPower.getSelectedItemPosition()));
                cfg.put(config.RFID_SENSITIVE_INDEX, String.valueOf(spRfidSensitive.getSelectedItemPosition()));
                cfg.put(config.RUSH_URL, txtRushUrl.getText().toString());

                if (config.Save(view.getContext(), cfg)) {
                    Toast.makeText(view.getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(final Context context) {
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
