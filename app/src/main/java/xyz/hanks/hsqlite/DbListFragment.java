package xyz.hanks.hsqlite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanks on 16/5/6.
 */
public class DbListFragment extends Fragment {

    private static final String APP_DATA_PATH = "app_data_path";
    private ListView listView;
    private List<String> dbList = new ArrayList<>();
    private List<String> dbNameList = new ArrayList<>();
    private ArrayAdapter adapter;


    public static DbListFragment newInstance(String appDataDir) {
        Bundle args = new Bundle();
        args.putString(APP_DATA_PATH, appDataDir);
        DbListFragment fragment = new DbListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dblist, container, false);
        return view;
    }


    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = (ListView) getView().findViewById(R.id.listView);
        adapter = new ArrayAdapter(getActivity(), R.layout.list_item_db_list, dbNameList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getFragmentManager().beginTransaction().add(R.id.fm_container, TableListFragment.newInstance(dbList.get(position))).addToBackStack("tableList").commit();
            }
        });

        getData();
    }

    private void getData() {
        String appDataPath = getArguments().getString(APP_DATA_PATH);
        if (TextUtils.isEmpty(appDataPath)) return;

        String filePath = appDataPath + "/databases";
        Log.e("..............", filePath);
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

            getFiles(filePath);

            adapter.notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
      * 通过递归得到某一路径下所有的目录及其文件
      */
    private void getFiles(String filePath) {
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getFiles(file.getAbsolutePath());
            } else {
                if (file.getName().endsWith(".db")) {
                    dbList.add(file.getAbsolutePath());
                    dbNameList.add(file.getName());
                }
            }
        }
    }


}
