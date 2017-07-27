package com.falvojr.nd818.p2.view.base;

import android.support.v4.app.Fragment;

public class BaseFragment<T extends BaseActivity> extends Fragment {

    @SuppressWarnings("unchecked")
    protected T getBaseActivity() {
        return (T) getActivity();
    }
}
