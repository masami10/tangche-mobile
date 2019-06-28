package com.example.tangchemobile;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tangchemobile.services.config;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OperationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OperationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OperationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OperationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OperationFragment newInstance(String param1, String param2) {
        OperationFragment fragment = new OperationFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_operation, container, false);

        this.initView(v);

        final MainActivity activity = (MainActivity)v.getContext();

        if (!init) {
            this.InitDevice(activity);
            init = true;
        }

        // 点击扫码按钮
        this.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean scan = pressScan();
                if (scan) {
                    btnScan.setText("停止");
                    if(!activity.GetScanner().Start()) {
                        Toast.makeText(activity, "无法开始扫码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    btnScan.setText("开始扫码");
                    if(!activity.GetScanner().Stop()) {
                        Toast.makeText(activity, "无法停止扫码", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void InitDevice(MainActivity ctx) {
        Map<String, String> cfg = config.Load(ctx);

        // 加载扫码
        ctx.GetScanner().Open();
        ctx.GetScanner().SetContinueMode(Boolean.parseBoolean(cfg.get(config.SCANNER_CONTINUE_MODE)));

        IntentFilter filter = new IntentFilter();
        filter.addAction("scan.rcv.message");
        try {
            ctx.unregisterReceiver(this.GetReceiver());
        } catch (Exception e){}

        ctx.registerReceiver(this.GetReceiver(), filter);

        // 加载rfid
        ctx.Rfid().SetPower(Integer.parseInt(cfg.get(config.RFID_POWER_INDEX)));
        ctx.Rfid().SetSensitive(Integer.parseInt(cfg.get(config.RFID_SENSITIVE_INDEX)));
        if (!ctx.Rfid().Open(cfg.get(config.RFID_PORT))) {
            Toast.makeText(ctx, "初始化RFID失败", Toast.LENGTH_SHORT).show();
        }

        // 加载rush
        ctx.Rush().SetUrl(cfg.get(config.RUSH_URL));
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public BroadcastReceiver GetReceiver() {
        return this.scanReceiver;
    }

    private BroadcastReceiver scanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            String barcodeStr = new String(barocode, 0, barocodelen);

            Toast.makeText(context.getApplicationContext(), barcodeStr, Toast.LENGTH_SHORT).show();
            txtSrc.setText("扫码");
            txtValue.setText(barcodeStr);

            final MainActivity activity = (MainActivity)context.getApplicationContext();
            if (!activity.GetScanner().IsContinueMode()) {
                pressScan();
            }

            // 推送rush
            activity.Rush().Put("scanner", barcodeStr);
        }
    };

    private void initView(View v) {
        this.btnScan = v.findViewById(R.id.btnScan);
        this.txtSrc = v.findViewById(R.id.txtSrc);
        this.txtValue = v.findViewById(R.id.txtValue);
    }

    private boolean pressScan() {
        scanStart = !scanStart;
        return scanStart;
    }

    public void SetInfo(String src, String value) {
        this.txtSrc.setText(src);
        this.txtValue.setText(value);
    }

    private static boolean init = false;
    private static boolean scanStart = false;

    private Button btnScan;
    private TextView txtSrc;
    private TextView txtValue;


}
