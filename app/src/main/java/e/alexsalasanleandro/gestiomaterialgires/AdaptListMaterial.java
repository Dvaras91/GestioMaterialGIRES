package e.alexsalasanleandro.gestiomaterialgires;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class AdaptListMaterial extends ArrayAdapter<Itemcomandprop> {
    public AdaptListMaterial(@NonNull Context context, int resource, @NonNull List objects) {
        super( context, resource, objects );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View result = convertView;
        if (result==null){
            LayoutInflater inflater =LayoutInflater.from( getContext() );
            result = inflater.inflate( R.layout.itemllistmaterial,null );

        }
        Button btn_add = result.findViewById(R.id.btn_add);
        Button btn_res = result.findViewById(R.id.btn_rest);
        CheckBox item_comand = (CheckBox) result.findViewById( R.id.check_item );
        final TextView item_quantitat = (TextView) result.findViewById( R.id.txt_quantitat );
        final Itemcomandprop item = getItem( position );
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getNumlloguer()>=item.getNumtotal()){

                } else {
                    item.setNumlloguer(item.getNumlloguer()+1);
                    item_quantitat.setText(item.getNumlloguer()+"/"+item.getNumtotal());
                }
            }
        });
        btn_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getNumlloguer()<=0){

                } else {
                    item.setNumlloguer(item.getNumlloguer()-1);
                    item_quantitat.setText(item.getNumlloguer()+"/"+item.getNumtotal());
                }
            }
        });
        item_comand.setText( item.getText() );
        item_comand.setChecked( item.isChecked() );
        item_quantitat.setText( item.getNumlloguer()+"/"+item.getNumtotal() );
        return result;
    }
}
