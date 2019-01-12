package e.alexsalasanleandro.gestiomaterialgires;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditCommand extends AppCompatActivity {

    private static final String USUARI = "usuari";
    private static final String DATA = "data";
    private static final String ENTREGA = "entrega";
    private static final String NOM = "name";
    private static final String NOMBRE = "nombre";
    private static final String CANTIDAD = "cantidad";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int EDIT_NAME = 1;
    private ComandListAdapter adapter;
    private ArrayList<Itemcomandprop> listmaterial;

    String nomcom = "Nom per defecte";
    TextView Titulcomanda;
    String noumaterial;
    ListView list_material;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_command);

        Toast.makeText( EditCommand.this, "Clica al botó + per afegir material",Toast.LENGTH_SHORT).show();  //FALTARIA FER RECURS
        Intent intent = getIntent();
        if (intent != null){
            nomcom = intent.getStringExtra("name");

        }
        Titulcomanda = findViewById(R.id.lbl_namecommand);
        Titulcomanda.setText("Nom de la comanda: "+nomcom);
        list_material = findViewById( R.id.list_material );
        listmaterial = new ArrayList<>(  );
        adapter = new ComandListAdapter( this,R.layout.itemllistmaterial,listmaterial );

        list_material.setAdapter( adapter );

        list_material.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                listmaterial.get( pos ).toggleChecked();
                adapter.notifyDataSetChanged();
            }
        } );



    }

    public void addItem(View view) {
        Intent intent = new Intent( this,ListMaterial.class );
        startActivityForResult( intent,EDIT_NAME );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case EDIT_NAME:
                if (resultCode==RESULT_OK){
                    ArrayList<String> material = new ArrayList<>(  );
                    material = data.getStringArrayListExtra( "material" );
                    int i = 0;
                    while (i<material.size()){
                        listmaterial.add( new Itemcomandprop( material.get( i ) ) );
                        i++;

                    }
                    //noumaterial = data.getStringExtra( "material" );
                    //listmaterial.add(new Itemcomandprop( noumaterial ));

                    adapter.notifyDataSetChanged();
                    list_material.smoothScrollToPosition( listmaterial.size()-1 );
                }
        default:
                super.onActivityResult( requestCode, resultCode, data );
    }}

    public void deleteItems(View view) {
        //El·liminar elements de la llista. El·limina tots aquesll elements que tinguin un checkbox = true.
        int i = 0;
        while (i<listmaterial.size()){
            if (listmaterial.get( i ).isChecked()){
                listmaterial.remove( i );
            } else {
                i++;
            }
        }
        adapter.notifyDataSetChanged();

    }

    public void confirmComand(View view) {
        //Confirmar la comanda.
        //Mostrar calendari per escollir la data
        final Calendar calendar = Calendar.getInstance();
        int dia = calendar.get( Calendar.DAY_OF_MONTH);
        int mes = calendar.get( Calendar.MONTH );
        int ano = calendar.get( Calendar.YEAR );
        DatePickerDialog datePickerDialog = new DatePickerDialog( EditCommand.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                guardaComanda(year, month, dayOfMonth);
            }
        },ano,mes,dia );
        datePickerDialog.show();

    }

    private void guardaComanda(int year, int month, int dayOfMonth) {
        String fecha = dayOfMonth+"/"+(month+1)+"/"+year;
        Intent data = new Intent(  );
        data.putExtra( "fecha",fecha );
        setResult( RESULT_OK,data );

        WriteBatch batch = db.batch();

        DocumentReference comRef = db.collection( "Comandas" ).document( nomcom );

        Map<String,Object> comand = new HashMap<>(  );
        comand.put( USUARI,"paco" );
        comand.put( DATA,fecha );
        comand.put( ENTREGA,false );
        comand.put( NOM,nomcom );
        batch.set(comRef, comand);

        for (int d = 0;d < listmaterial.size(); d++) {
            Map<String,Object> items = new HashMap<>(  );
            items.put( NOMBRE, listmaterial.get( d ).getText() );
            items.put( CANTIDAD, listmaterial.get( d ).getNumlloguer() );
            batch.set(comRef.collection("items").document(), items);
        }

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditCommand.this,"Error!",Toast.LENGTH_SHORT).show();
                Log.d("ERROR",e.toString());
            }
        }); //FALTA POSAR AIXO PERQUE SINO DONA UN ERROR AL TORNAR A ENTRAR


    }

    @Override
    public void onBackPressed(){

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro que deseas ir para atrás?");
        builder.setTitle("Atención");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditCommand.super.onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
}
