package com.skywavestudios.prefactor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Iman on 4/17/2017.
 */

public class History_Adaptor extends RecyclerView.Adapter<History_Adaptor.HistoryViewHolder> {
    final private RowClickListener rowclicklistener;
    final private Cursor mCurser;
    private String[] _Jsons;
    private Long[] _Ids;
    private HistoryActivity mHistory;

    public History_Adaptor(RowClickListener rListener, Cursor cursor, HistoryActivity history) {
        rowclicklistener = rListener;
        mCurser = cursor;
        _Jsons = new String[mCurser.getCount()];
        _Ids = new Long[mCurser.getCount()];
        mHistory = history;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.history_row, parent, false);
        HistoryViewHolder viewHolder = new HistoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        if (!mCurser.moveToPosition(position))
            return;
        holder.bind(position);
//        long id = mCurser.getLong(mCurser.getColumnIndex(FactorsContract.FactorsEntry._ID));
//        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCurser.getCount();
    }

    public interface RowClickListener {
        void onRowclick(View view, Long id, String json);
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView history_row_factorno;
        TextView history_row_customerno;
        TextView history_row_date;
        ImageButton history_row_delete;

        public HistoryViewHolder(final View itemView) {
            super(itemView);
            history_row_factorno = (TextView) itemView.findViewById(R.id.history_row_factorno);
            history_row_customerno = (TextView) itemView.findViewById(R.id.history_row_customername);
            history_row_date = (TextView) itemView.findViewById(R.id.history_row_factordate);
            history_row_delete = (ImageButton) itemView.findViewById(R.id.history_row_delete);
            history_row_delete.setOnClickListener(this);
            history_row_factorno.setOnClickListener(this);
            history_row_customerno.setOnClickListener(this);
            history_row_date.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String json = _Jsons[position];
            Long id = _Ids[position];
            rowclicklistener.onRowclick(v, id, json);
        }

        void bind(int position) {
            _Jsons[position] = mCurser.getString(mCurser.getColumnIndex(FactorsContract.FactorsEntry.COLUMN_JSON));
            _Ids[position] = mCurser.getLong(mCurser.getColumnIndex(FactorsContract.FactorsEntry._ID));
            history_row_date.setText(mCurser.getString(mCurser.getColumnIndex(FactorsContract.FactorsEntry.COLUMN_DATE)));
            history_row_factorno.setText(String.valueOf(mCurser.getInt(mCurser.getColumnIndex(FactorsContract.FactorsEntry.COLUMN_FACTORNO))));
            history_row_customerno.setText(mCurser.getString(mCurser.getColumnIndex(FactorsContract.FactorsEntry.COLUMN_CUSTOMER)));
        }


    }

}
