package xyz.hanks.hsqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanks on 16/5/6.
 */
public class TableListFragment extends Fragment {

    private static final String DB_PATH = "db_path";
    private ListView listView;
    private ArrayAdapter adapter;
    private List<String> tableList = new ArrayList<>();


    public static TableListFragment newInstance(String dbFilePath) {

        Bundle args = new Bundle();
        args.putString(DB_PATH,dbFilePath);
        TableListFragment fragment = new TableListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tablelist,container,false);
        return view;
    }


    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = (ListView) getView().findViewById(R.id.listView);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, tableList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        getData();
    }

    private void getData() {
        String dbPath = getArguments().getString(DB_PATH);
        if(TextUtils.isEmpty(dbPath)) return;
        getTableDetail(dbPath);

    }



    public void getTableDetail(String dbFilePath) {
        File file = new File(dbFilePath);
        if(!file.exists()){
            Toast.makeText(getActivity(),"文件不存在"+dbFilePath,Toast.LENGTH_SHORT).show();
            return;
        }
        SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);

        Cursor cursor2 = openDatabase.query("sqlite_master", null, "type in('table','view')", null, null, null, "name");

        cursor2.moveToFirst();
        while (!cursor2.isAfterLast()) {

            String type = cursor2.getString(0);
            String tableName = cursor2.getString(1);
            String tableSql = cursor2.getString(4);
            Log.e("........", cursor2.getString(1) + "," + cursor2.getString(4) + "," + cursor2.getString(0));
            tableList.add(tableName);
            cursor2.moveToNext();
        }
        if (!(cursor2 == null || cursor2.isClosed())) {
            try {
                cursor2.close();
            } catch (Exception e5) {
            }
        }
        if (openDatabase != null && openDatabase.isOpen()) {
            try {
                openDatabase.close();
            } catch (Exception e6) {
            }
        }
    }

}
