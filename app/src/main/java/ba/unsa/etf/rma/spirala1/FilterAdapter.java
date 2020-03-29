package ba.unsa.etf.rma.spirala1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FilterAdapter extends ArrayAdapter<FilterItem> {
    public FilterAdapter(Context context, ArrayList<FilterItem> filterlist){
        super(context,0,filterlist);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position,View convertView,ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_layout_spinner,parent,false);
        }
        ImageView imageViewFlag = convertView.findViewById(R.id.slika);
        TextView textViewName = convertView.findViewById(R.id.SpinnerItem);

        FilterItem trenutniItem = getItem(position);

        if(trenutniItem != null) {
            imageViewFlag.setImageResource(trenutniItem.getImg());
            textViewName.setText(trenutniItem.getFilterItem());
        }
        return convertView;
    }

}
