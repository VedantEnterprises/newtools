package xyz.hanks.hsqlite;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    List<App> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        ListView listView = (ListView) findViewById(R.id.listView);
        AppAdapter adapter = new AppAdapter();
        listView.setAdapter(adapter);


        PackageManager pm = getPackageManager();
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
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fm_container, DbListFragment.newInstance(appDataDir))
                        .addToBackStack("dbList")
                        .commit();
            }
        });
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("首页");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
            ic_logo.setImageDrawable(app.applicationInfo.loadIcon(getPackageManager()));
            tv_appname.setText(app.applicationInfo.loadLabel(getPackageManager()));
            tv_packagename.setText(app.applicationInfo.packageName);
            return convertView;
        }
    }
}
