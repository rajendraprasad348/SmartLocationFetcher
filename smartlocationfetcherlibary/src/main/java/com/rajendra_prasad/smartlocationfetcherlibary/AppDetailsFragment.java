package com.rajendra_prasad.smartlocationfetcherlibary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import static com.rajendra_prasad.smartlocationfetcherlibary.SmartLocationFetcher.AppDetailsList;

public class AppDetailsFragment extends DialogFragment {
    private TextView mTxv_Title;
    private RecyclerView rv;
    private AppDetailsAdapter appDetailsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_app_details, container);

        mTxv_Title = rootView.findViewById(R.id.txv_title);
        mTxv_Title.setText("Uninstall (" + AppDetailsList.size() + ") Application");

        //RECYCER
        rv = (RecyclerView) rootView.findViewById(R.id.recycler_view_app_details);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //ADAPTER
        appDetailsAdapter = new AppDetailsAdapter(this.getActivity(), AppDetailsList, AppDetailsFragment.this);
        rv.setAdapter(appDetailsAdapter);


        this.getDialog().setTitle("Uninstall (" + AppDetailsList.size() + ") Application");


        return rootView;
    }


}