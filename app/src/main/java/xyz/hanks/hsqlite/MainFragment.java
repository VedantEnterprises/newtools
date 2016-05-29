package xyz.hanks.hsqlite;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanks on 16/5/29.
 */
public class MainFragment extends Fragment {

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    List<App> data = new ArrayList<>();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView) view.findViewById(R.id.listView);
        AppAdapter adapter = new AppAdapter();
        listView.setAdapter(adapter);


        PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);

        for (ApplicationInfo app : apps) {
            //checks for flags; if flagged, check if updated system app
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
                // installedApps.add(app);
                //it's a system app, not interested
            } else {
                //Discard this one
                //in this case, it should be a user-installed app
                App tmp = new App();
                tmp.applicationInfo = app;
                data.add(tmp);
            }
        }

        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String appDataDir = "/data/data/" + data.get(position).applicationInfo.packageName;
                getFragmentManager().beginTransaction()
                        .replace(R.id.fm_container, DbListFragment.newInstance(appDataDir))
                        .addToBackStack("dbList")
                        .commit();
            }
        });
    }

    class App {
        ApplicationInfo applicationInfo;
        String dataDir;
    }

    class AppAdapter extends BaseAdapter {

        @Override public int getCount() {
            return data.size();
        }

        @Override public Object getItem(int position) {
            return null;
        }

        @Override public long getItemId(int position) {
            return 0;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_app, parent, false);
            }

            ImageView ic_logo = (ImageView) convertView.findViewById(R.id.ic_logo);
            TextView tv_appname = (TextView) convertView.findViewById(R.id.tv_appname);
            TextView tv_packagename = (TextView) convertView.findViewById(R.id.tv_packagename);

            App app = data.get(position);
            ic_logo.setImageDrawable(app.applicationInfo.loadIcon(getActivity().getPackageManager()));
            tv_appname.setText(app.applicationInfo.loadLabel(getActivity().getPackageManager()));
            tv_packagename.setText(app.applicationInfo.packageName);
            return convertView;
        }
    }
}
