package xyz.hanks.hsqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanks on 16/5/6.
 */
public class TableDetailFragment extends Fragment {

    private static final String DB_PATH = "db_path";
    private static final String TABLE_NAME = "table_name";
    private static final String TABLE_COLUMN_LIST = "table_column_list";
    List<String[]> datas = new ArrayList<>();
    private GridView gridView;
    private ArrayList<String> columnList;

    public static TableDetailFragment newInstance(String dbPath, String tableName, ArrayList<String> columnList) {
        Bundle args = new Bundle();
        args.putString(DB_PATH, dbPath);
        args.putString(TABLE_NAME, tableName);
        args.putStringArrayList(TABLE_COLUMN_LIST, columnList);
        TableDetailFragment fragment = new TableDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabledetaile, container, false);
        return view;
    }


    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        gridView = (GridView) getView().findViewById(R.id.gridView);


        columnList = getArguments().getStringArrayList(TABLE_COLUMN_LIST);
        gridView.setColumnWidth(100); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(columnList.size()); // 设置列数量=列表集合数

        gridView.setAdapter(new GridViewAdapter());
        getData();

    }

    private void getData() {
        String dbPath = getArguments().getString(DB_PATH);
        String tableName = getArguments().getString(TABLE_NAME);
        if (TextUtils.isEmpty(dbPath) || TextUtils.isEmpty(tableName)) return;


        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbPath, null, 0);
        String[] columns = columnList.toArray(new String[]{});
        Cursor cursor = database.query(tableName, columns, null, null, null, null, null, "50");
        while (cursor.moveToNext()) {

            String[] row = new String[columns.length];
            for (int i = 0; i < columns.length; i++) {
                row[i] = cursor.getString(cursor.getColumnIndex(columns[i]));
            }
            datas.add(row);
        }

    }


    /**
     * GirdView 数据适配器
     */
    public class GridViewAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return datas.size()*columnList.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.grid_item, null);
            }

            TextView text = (TextView) convertView.findViewById(R.id.text);
            text.setText(datas.get(position/columnList.size())[position%columnList.size()]);
            return convertView;
        }
    }


}
