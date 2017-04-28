package radek.tesar.ab.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import radek.tesar.ab.R;

/**
 * Created by tesar on 28.04.2017.
 */

public abstract class BaseFragment extends Fragment {
    View content;
    View offline;
    View emptyList;
    View loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(mainView(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        content = view.findViewById(R.id.content);
        offline = view.findViewById(R.id.offline);
        emptyList = view.findViewById(R.id.empty);
        loading = view.findViewById(R.id.progressBar);
        showLoading();
        handleContent(view);

    }

    /**
     *Handle view
     * @param view
     */
    public abstract void handleContent(View view);

    /**
     * ger int of layout
     * @return
     */
    public abstract int mainView();


    public void showContent(){
        content.setVisibility(View.VISIBLE);
        offline.setVisibility(View.GONE);
        emptyList.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }

    public void showoffline(){
        content.setVisibility(View.GONE);
        offline.setVisibility(View.VISIBLE);
        emptyList.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }

    public void showEmptyList(){
        content.setVisibility(View.GONE);
        offline.setVisibility(View.GONE);
        emptyList.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    public void showLoading(){
        content.setVisibility(View.GONE);
        offline.setVisibility(View.GONE);
        emptyList.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }
}
