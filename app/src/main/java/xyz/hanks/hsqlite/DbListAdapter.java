package xyz.hanks.hsqlite;

import android.content.Context;

import java.util.List;

import xyz.hanks.hsqlite.base.BaseViewHolder;
import xyz.hanks.hsqlite.base.CommonAdapter;

/**
 * Created by hanks on 16/6/4.
 */
public class DbListAdapter extends CommonAdapter<String> {

    public DbListAdapter(Context context, List<String> dbNameList) {
        super(context, R.layout.list_item_db_list, dbNameList);
    }

    @Override protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_dbname,item);
    }
}
