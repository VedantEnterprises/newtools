package xyz.hanks.hsqlite;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);


        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);

        List<App> installedApps = new ArrayList<>();

        for (ApplicationInfo app : apps) {
            //checks for flags; if flagged, check if updated system app
            //            if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
            //                installedApps.add(app);
            //                //it's a system app, not interested
            //            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
            //                //Discard this one
            //                //in this case, it should be a user-installed app
            //            } else {
            //                installedApps.add(app);
            //            }

            App tmp = new App();
            tmp.applicationInfo = app;


            Log.e("", app.packageName);

            data.add(app.packageName);


        }


        adapter.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String appDataDir = "/data/data/" + data.get(position);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fm_container, DbListFragment.newInstance(appDataDir))
                        .addToBackStack("dbList")
                        .commit();
            }
        });
    }


    class App {
        ApplicationInfo applicationInfo;
        String dataDir;
    }
}
