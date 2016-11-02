package com.uci.android101.sup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by somarkoe on 11/2/16.
 */

public class SupFragment extends Fragment {

    private Button mSelectFriend;
    private Button mSendButton;
    private TextView mLastSupSent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sup, container, false);

        mSelectFriend = (Button) v.findViewById(R.id.select_friend);
        mSendButton = (Button) v.findViewById(R.id.send_sup);
        mLastSupSent = (TextView) v.findViewById(R.id.last_sup_sent);

        return v;
    }
}
