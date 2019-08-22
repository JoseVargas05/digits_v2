package com.fcfm.cambia_10;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

public class contact_result_adapter extends BaseAdapter {


    private Context mContext;
    private List<contact_result> mContact_result_List;

    public contact_result_adapter(Context mContext, List<contact_result> mContact_result) {
        this.mContext = mContext;
        this.mContact_result_List = mContact_result;
    }

    @Override
    public int getCount() {
        return mContact_result_List.size();
    }

    @Override
    public Object getItem(int i) {
        return mContact_result_List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if(view == null){
            view = View.inflate(mContext, R.layout.item_list_result, null);

            holder = new ViewHolder();

            holder.tvName = view.findViewById(R.id.nombre);
            holder.tvNacio = view.findViewById(R.id.nacional);
            holder.tvQuitar = view.findViewById(R.id.quitar);
            holder.tvNumber = view.findViewById(R.id.numero);
            holder.tvTipo = view.findViewById(R.id.tipo);
            holder.tvCheck = view.findViewById(R.id.checkb);
            holder.tvCheck.setSelected(false);
            //Save contact id to tag
            //Save contact id to tag
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        contact_result result = mContact_result_List.get(i);

        //Set text for TextView
        holder.tvName.setText(mContact_result_List.get(i).getNombre());
        holder.tvNacio.setText(mContact_result_List.get(i).getNacional());
        holder.tvQuitar.setText(mContact_result_List.get(i).getAquitar());
        holder.tvNumber.setText(mContact_result_List.get(i).getNumero());
        holder.tvTipo.setText(mContact_result_List.get(i).getTipo());

        if (result.isSelected())
            holder.tvCheck.setChecked(true);
        else
            holder.tvCheck.setChecked(false);


        return view;
    }

    public void UpdateRecords(List<contact_result> mContact_result_List){
        this.mContact_result_List = mContact_result_List;

        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tvName;
        TextView tvNacio;
        TextView tvQuitar;
        TextView tvNumber;
        TextView tvTipo;
        CheckBox tvCheck;
    }
}
