package e.alexsalasanleandro.gestiomaterialgires;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class ComandListAdapter extends ArrayAdapter<Itemcomandprop> {
    public ComandListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super( context, resource, objects );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View result = convertView;
       if (result==null){
           LayoutInflater inflater = LayoutInflater.from( getContext() );
           result = inflater.inflate( R.layout.itemedit,null );
       }
       CheckBox item_comand = (CheckBox) result.findViewById( R.id.check_item );
       EditText numberitem = (EditText) result.findViewById( R.id.txt_numberitem );
       Itemcomandprop item = getItem( position );
       item_comand.setText( item.getText() );
       item_comand.setChecked( item.isChecked() );
       int number = 1;
       //numberitem.setText( "2" );
       return result;
    }
}
