package com.feicui.fragmentnews.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feicui.fragmentnews.R;

/**
 * Created by Administrator on 2016/11/16 0016.
 */

public class SocialFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_social_fragment,null);
        return view;
    }
}
