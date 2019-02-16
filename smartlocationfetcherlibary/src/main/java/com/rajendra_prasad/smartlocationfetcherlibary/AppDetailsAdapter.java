package com.rajendra_prasad.smartlocationfetcherlibary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class AppDetailsAdapter extends RecyclerView.Adapter<AppDetailsAdapter.ViewHolder> {
    private static final String TAG = "SmartLocationDetection";
    private Context mContext;
    private ArrayList<AppDetailsModel> appsList = new ArrayList<>();
    private AppDetailsFragment appDetailsFragment;

    public AppDetailsAdapter(Context mContext, ArrayList<AppDetailsModel> appsList, AppDetailsFragment appDetailsFragment) {
        this.mContext = mContext;
        this.appsList = appsList;
        this.appDetailsFragment = appDetailsFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.app_details_row_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.txv_package_name.setText("( " + appsList.get(i).getPackagename() + " )");

        viewHolder.txv_app_name.setText(appsList.get(i).getAppname());
        viewHolder.txv_package_name.setText("( " + appsList.get(i).getPackagename() + " )");
        viewHolder.imv_app_icon.setImageDrawable(appsList.get(i).getIcon());


        viewHolder.imv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    // to delete/ uninstall the app
                    Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:" + appsList.get(i).getPackagename()));
                    mContext.startActivity(intent);
                    appDetailsFragment.dismiss();

                } catch (Exception e) {
                    Log.d(TAG, "onClick: " + e);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txv_app_name, txv_package_name;
        private ImageView imv_app_icon, imv_close;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txv_app_name = itemView.findViewById(R.id.txv_app_name);
            txv_package_name = itemView.findViewById(R.id.txv_package_name);
            imv_app_icon = itemView.findViewById(R.id.imv_app_icon);
            imv_close = itemView.findViewById(R.id.imv_close);
        }
    }


}
