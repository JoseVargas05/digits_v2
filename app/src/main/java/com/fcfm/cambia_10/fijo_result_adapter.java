package com.fcfm.cambia_10;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class fijo_result_adapter extends BaseAdapter {
    private ViewHolder holder = null;
    private Context mContext;
    private List<fijo_result> mfijo_result_List;

    public fijo_result_adapter(Context mContext, List<fijo_result> mfijo_result_List) {
        this.mContext = mContext;
        this.mfijo_result_List = mfijo_result_List;
    }

    @Override
    public int getCount() {
        return mfijo_result_List.size();
    }

    @Override
    public Object getItem(int i) {
        return mfijo_result_List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        if(view == null){
            view = View.inflate(mContext, R.layout.item_list_result_2, null);

            holder = new ViewHolder();

            holder.tvName = view.findViewById(R.id.nombre);
            holder.tvAdd = view.findViewById(R.id.agregar);
            holder.tvNumber = view.findViewById(R.id.numero);
            holder.tvEstado = view.findViewById(R.id.estado);
            holder.tvCheck = view.findViewById(R.id.checkb);
            holder.tvCheck.setSelected(false);
            //Save contact id to tag
            //Save contact id to tag
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        fijo_result result = mfijo_result_List.get(i);

        //Set text for TextView
        holder.tvName.setText(mfijo_result_List.get(i).getNombre());
        holder.tvAdd.setText(mfijo_result_List.get(i).getAgregar());
        holder.tvNumber.setText(mfijo_result_List.get(i).getNumero());
        holder.tvEstado.setText(mfijo_result_List.get(i).getEstado());

        if (result.isSelected())
            holder.tvCheck.setChecked(true);
        else
            holder.tvCheck.setChecked(false);


        return view;
    }
    public void UpdateRecords(List<fijo_result> mfijo_result_List){
        this.mfijo_result_List = mfijo_result_List;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tvName;
        TextView tvAdd;
        TextView tvNumber;
        TextView tvEstado;
        CheckBox tvCheck;
    }
}
