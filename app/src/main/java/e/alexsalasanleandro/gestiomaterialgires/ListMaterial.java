package e.alexsalasanleandro.gestiomaterialgires;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListMaterial extends AppCompatActivity {

    private AdaptListMaterial adapter;
    private ArrayList<Itemcomandprop> list_item;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_list_material );
        Intent intent = getIntent();
        list_item = new ArrayList<>(  );
        db.collection( "Catalogo" ).addSnapshotListener( this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null){
                    Log.e( "GestioMaterialGIRES","Firestore Error: "+e.toString() );
                    return;
                }
                list_item.clear();
                for (DocumentSnapshot doc: documentSnapshots){
                    list_item.add( new Itemcomandprop( doc.getString( "nombre" ), doc.getDouble( "cantidad" ).intValue()));
                }
                adapter.notifyDataSetChanged();
            }
        } );
        //list_item.add( new Itemcomandprop( "CD TITO EL BAMBINO" ) );
        //list_item.add( new Itemcomandprop( "Arnes" ) );
        //list_item.add( new Itemcomandprop( "Casc" ) );
        //list_item.add( new Itemcomandprop( "Raquetes" ,false,1,10) );
        //list_item.add( new Itemcomandprop( "Corda 1/2" ,false,1,5) );
        //list_item.add( new Itemcomandprop( "Neopreno" ,false,1,4) );
        //list_item.add( new Itemcomandprop( "Bagues" ,false,1,10) );
        //list_item.add( new Itemcomandprop( "Cordino" ,false,1,15) );
        //list_item.add( new Itemcomandprop( "Corda 40m estàtica" ,false,1,3) );
        //list_item.add( new Itemcomandprop( "Dissipador" ,false,1,6) );
        //list_item.add( new Itemcomandprop( "STOP" ,false,1,6) );
        //list_item.add( new Itemcomandprop( "Crashpad" ,false,1,6) );
        //list_item.add( new Itemcomandprop( "Llibre ressenyes" ,false,1,6) );

        final ListView list_material = findViewById( R.id.list_material );
        adapter = new AdaptListMaterial( this,R.layout.itemllistmaterial,list_item );
        list_material.setAdapter( adapter );

        list_material.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list_item.get( i ).isChecked()==false) {
                    //Posar builder per dir el nombre d' items que volem
                    /*final AlertDialog.Builder quantity = new AlertDialog.Builder( ListMaterial.this );
                    quantity.setMessage( "Quantitat que voleu llogar:" );
                    final Button incrementar = new Button( ListMaterial.this );
                    final Button decrementar = new Button( ListMaterial.this );
                    decrementar.setText( "-" );
                    incrementar.setText( "+" );
                    quantity.setView( incrementar );
                    quantity.setView( decrementar );
                    quantity.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    } );
                    quantity.setNegativeButton( "Cancel·lar",null );
                    list_item.get( i ).toggleChecked();
                    quantity.create();
                    quantity.show();*/
                    Toast.makeText( ListMaterial.this, String.format( list_item.get( i ).getText() ), Toast.LENGTH_SHORT ).show();
                }
                list_item.get( i ).toggleChecked();
                adapter.notifyDataSetChanged();
            }
        } );

    }

    public void confirmItems(View view) {
        //Enviem tots aquells items que estiguin marcats en el checkbox per així incluir-los en la nostra comanda
        int i = 0;
        ArrayList<String> material;
        material = new ArrayList<>(  );

        Intent data = new Intent(  );

        while (i<list_item.size()){
            if (list_item.get( i ).isChecked()){
                material.add( list_item.get( i ).getText() );
            }
                i++;

        }

        data.putExtra( "material", material);
        setResult( RESULT_OK,data );
        finish();
    }
}
