package com.falvojr.nd818.p2.view.base;

import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment<T extends BaseActivity> extends Fragment {

    @SuppressWarnings("unchecked")
    protected T getBaseActivity() {
        return (T) getActivity();
    }

    protected void showProgress(View content, View progress) {
        content.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
    }

    protected void hideProgress(View content, View progress) {
        content.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }
}
