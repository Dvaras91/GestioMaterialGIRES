package e.alexsalasanleandro.gestiomaterialgires;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AdaptComandes extends ArrayAdapter<comanda> {
    public AdaptComandes(@NonNull Context context, int resource, @NonNull List objects) {
        super( context, resource, objects );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View result = convertView;
        if (result == null){
            LayoutInflater inflater = LayoutInflater.from( getContext() );
            result = inflater.inflate( R.layout.listcomanda,null );
        }
        TextView name = result.findViewById( R.id.lbl_name );
        TextView usuari = result.findViewById( R.id.lbl_usuari );
        TextView data = result.findViewById( R.id.lbl_data );
        comanda com = getItem( position );
        name.setText( com.name );
        usuari.setText( com.usuari );
        data.setText( com.data );


        return result;
    }
}
