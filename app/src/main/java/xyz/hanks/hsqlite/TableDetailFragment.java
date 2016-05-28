package xyz.hanks.hsqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
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
    List<String[]> data = new ArrayList<>();
    private GridView gridView;
    private ArrayList<String> columnList;
    private ListView listView;
    private GridViewAdapter adapter;
    private float[] lengths;
    private int limit = 50;
    private int page = 0;
    private TextView tv_page;

    public static TableDetailFragment newInstance(String dbPath, String tableName, ArrayList<String> columnList) {
        Bundle args = new Bundle();
        args.putString(DB_PATH, dbPath);
        args.putString(TABLE_NAME, tableName);
        args.putStringArrayList(TABLE_COLUMN_LIST, columnList);
        TableDetailFragment fragment = new TableDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 处理listview嵌套listview，子listview不完全显示问题
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter

    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabledetaile, container, false);
        return view;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        //        gridView = (GridView) getView().findViewById(R.id.gridView);

        listView = (ListView) getView().findViewById(R.id.listView);
        View bt_next = getView().findViewById(R.id.bt_next);
        View bt_pre = getView().findViewById(R.id.bt_pre);
        tv_page = (TextView) getView().findViewById(R.id.tv_page);
        updatePageNumber();
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                page++;
                getData();
            }
        });

        bt_pre.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                page--;
                if(page<0) page = 0;
                getData();
            }
        });

        columnList = getArguments().getStringArrayList(TABLE_COLUMN_LIST);
        //        gridView.setColumnWidth(100); // 设置列表项宽
        //        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        //        gridView.setStretchMode(GridView.NO_STRETCH);
        //        gridView.setNumColumns(columnList.size()); // 设置列数量=列表集合数

        //        gridView.setAdapter(new GridViewAdapter());

        adapter = new GridViewAdapter();
        listView.setAdapter(adapter);
        getData();

    }

    private void updatePageNumber() {
        tv_page.setText("第" + (page+1)+"页");
    }

    private View getRowView() {

        RowView rowView = new RowView(getContext());

        rowView.setTextLengthArray(lengths);
        return rowView;
    }

    public int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void getData() {
        String dbPath = getArguments().getString(DB_PATH);
        String tableName = getArguments().getString(TABLE_NAME);
        if (TextUtils.isEmpty(dbPath) || TextUtils.isEmpty(tableName)) return;


        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbPath, null, 0);
        String[] columns = columnList.toArray(new String[]{});
        Cursor cursor = database.query(tableName, columns, null, null, null, null, null, (page * limit)+" , "+limit );
        data.clear();
        updatePageNumber();
        while (cursor.moveToNext()) {

            String[] row = new String[columns.length];
            for (int i = 0; i < columns.length; i++) {
                row[i] = cursor.getString(cursor.getColumnIndex(columns[i]));
            }
            data.add(row);
        }
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(listView);
        if (!(cursor == null || cursor.isClosed())) {
            try {
                cursor.close();
            } catch (Exception e5) {
            }
        }
        if (database != null && database.isOpen()) {
            try {
                database.close();
            } catch (Exception e6) {
            }
        }

        // 计算 width

        String[] firstRow = null;
        if(data.size()>0){
             firstRow = data.get(0);
        }

        lengths = new float[columnList.size()];
        int char_width = dp2px(6);
        for (int i = 0; i < columnList.size(); i++) {

            int width = (columnList.get(i).length() * char_width);
            if(firstRow!=null) {
                if (firstRow[i]==null) firstRow[i] = " ";
                
                if (firstRow[i].length() * char_width > dp2px(200)) {
                    width = dp2px(200);
                } else {
                    width = Math.max(width,firstRow[i].length() * char_width);
                }
            }
            lengths[i] = Math.max(dp2px(50),width) ;
        }
    }

    /**
     * GirdView 数据适配器
     */
    public class GridViewAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return data.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getRowView();
            }


            if (position == 0) {
                convertView.setBackgroundColor(Color.parseColor("#a1a1a1"));
                if (convertView instanceof RowView) {
                    ((RowView) convertView).setTextArray(columnList.toArray(new String[]{}));
                }
            } else {
                convertView.setBackgroundColor(Color.parseColor("#e1e1e1"));
                String[] strings = data.get(position - 1);
                if (convertView instanceof RowView) {
                    ((RowView) convertView).setTextArray(strings);
                }
            }

            return convertView;
        }
    }


}
