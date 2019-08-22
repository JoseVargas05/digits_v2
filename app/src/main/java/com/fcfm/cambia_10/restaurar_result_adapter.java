package com.fcfm.cambia_10;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class restaurar_result_adapter extends BaseAdapter {

    private Context mContext;
    private List<restaurar_result> mrest_result_List;

    public restaurar_result_adapter(Context mContext, List<restaurar_result> mrest_result_list) {
        this.mContext = mContext;
        mrest_result_List = mrest_result_list;
    }


    @Override
    public int getCount() {
        return mrest_result_List.size();
    }

    @Override
    public Object getItem(int i) {
        return mrest_result_List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if(view == null){
            view = View.inflate(mContext, R.layout.item_list_result_3, null);

            holder = new ViewHolder();

            holder.tvName = view.findViewById(R.id.nombre);
            holder.tvNumber = view.findViewById(R.id.numero);
            holder.tvEstado = view.findViewById(R.id.estado);
            holder.tvCheck = view.findViewById(R.id.checkb);
            holder.tvCheck.setSelected(false);
            //Save contact id to tag
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        restaurar_result result = mrest_result_List.get(i);

        //Set text for TextView
        holder.tvName.setText(mrest_result_List.get(i).getNombre());
        holder.tvNumber.setText(mrest_result_List.get(i).getNumero());
        holder.tvEstado.setText(mrest_result_List.get(i).getEstado());

        if (result.isSelected())
            holder.tvCheck.setChecked(true);
        else
            holder.tvCheck.setChecked(false);


        return view;
    }

    public void UpdateRecords(List<restaurar_result> mrest_result_List){
        this.mrest_result_List = mrest_result_List;

        notifyDataSetChanged();
    }

    public void remove(restaurar_result result_restaurar) {
        this.mrest_result_List.remove(result_restaurar);
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tvName;
        TextView tvNumber;
        TextView tvEstado;
        CheckBox tvCheck;
    }

}
