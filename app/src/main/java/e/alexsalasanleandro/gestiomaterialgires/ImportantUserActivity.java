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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ImportantUserActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final int EDIT_NAME = 3;
    private static final int SEARCH = 2;
    private static final int COMANDA =4;
    private AdaptComandes adapter;
    private ArrayList<comanda> list_return;
    private AdaptComandes adapterpre;
    private ArrayList<comanda> list_prep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_user);
        //Icono en el action bar:
        getSupportActionBar().setDisplayShowHomeEnabled( true );
        getSupportActionBar().setIcon( R.mipmap.ic_launcher );

        //Conectar amb firebase
        refreshListPrep();
        refreshListRet();

       /* db.collection( "Comandas" ).whereEqualTo( "entrega",true ).addSnapshotListener( this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e!=null){
                    Log.e( "GestioMaterialGIRES","Firestore Error: "+e.toString() );
                    return;
                }
                list_return.clear();
                for (DocumentSnapshot doc: documentSnapshots){
                    comanda c = new comanda( doc.getString( "name" ),doc.getString( "usuari" ),doc.getString( "data" ) );
                    c.setId( doc.getId() );
                    list_return.add( c );
                }
                adapter.notifyDataSetChanged();
            }
        } );*/

        list_return = new ArrayList<comanda>();
        //list_return.add(new comanda( "Forat Mico" ));
        //list_return.add(new comanda( "Escalada Gelida" ));
        //list_return.add(new comanda( "Guara" ));
        //list_return.add(new comanda( "Picos de Europa" ));


        list_prep = new ArrayList<comanda>();
        //list_prep.add(new comanda( "Escalada Vallirana" ));
        //list_prep.add(new comanda( "Matinal GIRES" ));
        //list_prep.add(new comanda( "Escalada Margalef" ));

        ListView List_prep = findViewById(R.id.list_preperar);
        ListView list_ret = findViewById(R.id.list_retornar);
        adapterpre = new AdaptComandes(this,R.layout.listcomanda,list_prep);
        adapter = new AdaptComandes(this,R.layout.listcomanda,list_return);
        list_ret.setAdapter(adapter);
        List_prep.setAdapter(adapterpre);
        list_ret.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText( ImportantUserActivity.this,String.format( list_return.get(i).name ),Toast.LENGTH_SHORT ).show();
                goToComnandaItems(list_return.get(i),true);


            }
        } );
        List_prep.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText( ImportantUserActivity.this,String.format( list_prep.get(i).name ),Toast.LENGTH_SHORT ).show();
                goToComnandaItems(list_prep.get(i),false);
            }
        } );


    }

    public void newcommand(View view) {
        final AlertDialog.Builder novacomanda = new AlertDialog.Builder(this);
        novacomanda.setMessage("Introduir nom de la comanda"); //posar - ho com a recurs que es pugui traduir
        novacomanda.setTitle("Comanda");
        final EditText namecomanda = new EditText(this);
        novacomanda.setView(namecomanda);


        novacomanda.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name =namecomanda.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(ImportantUserActivity.this,"Posa un nom", Toast.LENGTH_SHORT).show();

                }else {
                //list_prep.add(new comanda( name ));
                novacomanda(name);}
            }
        });
        novacomanda.setNegativeButton("Cancel·lar",null);
        novacomanda.create();
        novacomanda.show();
    }

    public void novacomanda (String nomcomanda){
        Intent intent = new Intent(this,EditCommand.class); //El·liminar espais si en posa
        intent.putExtra("name",nomcomanda);
        startActivityForResult(intent,EDIT_NAME);


    }

    //Retornem a l' activitat importantUserActivity quan ja hem acabat d' editar la comanda i anem on tenim totes les comandes.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case EDIT_NAME:
                if (resultCode==RESULT_OK){
                    String fecha = data.getStringExtra( "fecha" );
                    Toast.makeText(ImportantUserActivity.this,"Comanda creada", Toast.LENGTH_SHORT).show(); //Posar com a recurs
                    refreshListPrep();
                    //list_prep.get( list_prep.size()-1 ).setData( fecha );
                }
            case COMANDA:
                if ((resultCode==RESULT_OK)){
                    refreshListPrep();
                    refreshListRet();

                }
                break;


            default:
                super.onActivityResult( requestCode, resultCode, data );
        }

    }

    public void viewMaterial(View view) {
        Intent intent = new Intent(this,ListMaterial.class);
        startActivityForResult(intent,SEARCH);

    }
    public void goToComnandaItems (comanda C, boolean entrega){
        Intent intent = new Intent(this,Viewitemscomanda.class); //S' ha de canviar la pantalla a la que volem que vagi
        intent.putExtra("id", C.getId());
        intent.putExtra("name",C.getName());
        intent.putExtra("data",C.getData());
        intent.putExtra("usuari",C.getUsuari());
        intent.putExtra("entrega",entrega);
        startActivityForResult(intent,COMANDA);
    }
    public void refreshListPrep (){

        db.collection("Comandas").whereEqualTo("entrega", false).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("GestioMaterialGIRES", "Firestore Error: " + e.toString());
                    return;
                }
                list_prep.clear();
                for (DocumentSnapshot doc : documentSnapshots) {
                    comanda cprep = new comanda( doc.getString( "name" ),doc.getString( "usuari" ),doc.getString( "data" ) );
                    cprep.setId(doc.getId());
                    //comanda c = doc.toObject(comanda.class);
                    //list_prep.add(c);
                    list_prep.add(cprep);
                }
                adapterpre.notifyDataSetChanged();
            }
        });

    }

    public void refreshListRet (){
        db.collection( "Comandas" ).whereEqualTo( "entrega",true ).addSnapshotListener( this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e!=null){
                    Log.e( "GestioMaterialGIRES","Firestore Error: "+e.toString() );
                    return;
                }
                list_return.clear();
                for (DocumentSnapshot doc: documentSnapshots){
                    comanda c = new comanda( doc.getString( "name" ),doc.getString( "usuari" ),doc.getString( "data" ) );
                    c.setId( doc.getId() );
                    list_return.add( c );
                }
                adapter.notifyDataSetChanged();
            }
        } );
    }
}
