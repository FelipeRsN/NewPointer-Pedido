package newpointer.com.br.newpointerpedido.CustomAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import newpointer.com.br.newpointerpedido.Model.GroupAcomp_ListView_Model;
import newpointer.com.br.newpointerpedido.R;

/**
 * Created by FelipeRsN on 7/6/16.
 */
public class AcompanhamentosCustomAdapter extends BaseAdapter {
    Activity activity;
    Context context;

    protected List<GroupAcomp_ListView_Model> listCars;
    LayoutInflater inflater;

    public AcompanhamentosCustomAdapter(Activity activity, Context context, List<GroupAcomp_ListView_Model> listCars) {
        this.activity = activity;
        this.listCars = listCars;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setSelectedItem(int position, boolean isSelected){
        listCars.get(position).setSelected(isSelected);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listCars.size();
    }

    @Override
    public Object getItem(int i) {
        return listCars.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listCars.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = this.inflater.inflate(R.layout.layout_acompanhamento, viewGroup, false);

            holder.rb = (RadioButton) view.findViewById(R.id.rb_acomp_desc);

            holder.title = (TextView) view.findViewById(R.id.tv_acomp_title);

            holder.cb = (CheckBox) view.findViewById(R.id.cb_acomp_desc);

            holder.v = (View) view.findViewById(R.id.view_acomp_v1);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final GroupAcomp_ListView_Model item = listCars.get(i);

        if(item.getMode() == 1){
            holder.title.setText(item.getDesc());
            holder.title.setVisibility(View.VISIBLE);
            holder.v.setVisibility(View.VISIBLE);
            holder.cb.setVisibility(View.INVISIBLE);
            holder.rb.setVisibility(View.INVISIBLE);
        }else{
            holder.title.setVisibility(View.INVISIBLE);
            holder.v.setVisibility(View.INVISIBLE);
            holder.cb.setVisibility(View.VISIBLE);
            holder.cb.setText(item.getDesc());
            holder.rb.setVisibility(View.INVISIBLE);
            holder.cb.setChecked(item.isSelected());
        }
        return view;
    }

    private class ViewHolder {
        CheckBox cb;
        TextView title;
        View v;
        RadioButton rb;
    }
}
