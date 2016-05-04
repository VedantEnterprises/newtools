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
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
                String filePath = "/data/data/" + data.get(position)+"/databases";
                Toast.makeText(view.getContext(),filePath,Toast.LENGTH_SHORT).show();
                Log.e("..............",filePath);
                try {
                    Process processe = Runtime.getRuntime().exec("su");
                    OutputStream outputStream = processe.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.writeBytes("chmod -R 777 " + filePath + "\n");
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    int value = processe.waitFor();
                    if (value == 0) {
                        Log.e("", "success");
                        
                    }


                    File files = new File(filePath);

                    for (File file : files.listFiles()) {
                        Log.e("..............",file.getAbsolutePath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    class App {
        ApplicationInfo applicationInfo;
        String dataDir;
    }
}
