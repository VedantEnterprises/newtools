package xyz.hanks.hsqlite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by hanks on 16/5/6.
 */
public class TableDetailFragment extends Fragment {

    private static final String DB_PATH = "db_path";
    private static final String TABLE_NAME = "table_name";
    private GridView gridView;

    public static TableDetailFragment newInstance(String dbPath, String tableName) {
        Bundle args = new Bundle();
        args.putString(DB_PATH,dbPath);
        args.putString(TABLE_NAME,tableName);
        TableDetailFragment fragment = new TableDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabledetaile,container,false);
        return view;
    }


    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        gridView = (GridView) getView().findViewById(R.id.gridView);

    }
}
