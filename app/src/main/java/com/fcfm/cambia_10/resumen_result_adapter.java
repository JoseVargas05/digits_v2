package com.fcfm.cambia_10;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class resumen_result_adapter extends BaseAdapter {
    private Context mContext;
    private List<resumen_result> mresu_result_List;

    public resumen_result_adapter(Context mContext, List<resumen_result> mresu_result_List) {
        this.mContext = mContext;
        this.mresu_result_List = mresu_result_List;
    }

    @Override
    public int getCount() {
        return mresu_result_List.size();
    }

    @Override
    public Object getItem(int i) {
        return mresu_result_List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if(view == null){
            view = View.inflate(mContext, R.layout.item_list_result_4, null);

            holder = new ViewHolder();

            holder.tvName = view.findViewById(R.id.nombre);
            holder.tvNumber = view.findViewById(R.id.numero);

            //Save contact id to tag
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        //Set text for TextView
        holder.tvName.setText(mresu_result_List.get(i).getNombre());
        holder.tvNumber.setText(mresu_result_List.get(i).getNumero());

        return view;
    }

    public void UpdateRecords(List<resumen_result> mresu_result_List){
        this.mresu_result_List = mresu_result_List;

        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tvName;
        TextView tvNumber;
    }

}
