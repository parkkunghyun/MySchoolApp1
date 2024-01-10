package org.techtown.myschoolapp1.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.techtown.myschoolapp1.R;

public class FragSettings extends Fragment {
    private View view;
    private String TAG = "설정 프래그먼트";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        view = inflater.inflate(R.layout.frag_settings, container, false);

        return view;
    }

}
