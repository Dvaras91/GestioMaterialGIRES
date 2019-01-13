package e.alexsalasanleandro.gestiomaterialgires;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Viewitemscomanda extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ComandListAdapter adapter;
    private ArrayList<Itemcomandprop> list_items;
    String id,name,usuari,data;
    boolean entrega;
    private static final String ENTREGA = "entrega";
    private static final String NAME = "name";
    private static final String USUARI = "usuari";
    private static final String DATA = "data";
    private static final String NOMBRE = "nombre";
    private static final String CANTIDAD = "cantidad";
    private static final String PRECIOITEM = "precio";
    ListView List_items;
    TextView Infocomanda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewitemscomanda);
        Intent intent = getIntent();
        if (intent!=null){
            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
            usuari = intent.getStringExtra("usuari");
            data = intent.getStringExtra("data");
            entrega = intent.getBooleanExtra("entrega",false);

        }
        Button btn_entrega = findViewById(R.id.btn_retornada);
        if (entrega){
            btn_entrega.setText("RETORNAR");
        }else {
            btn_entrega.setText("ENTREGADA");

        }
        // NO FUNCIONA:
        //db.collection("Comandas").whereEqualTo("name",nomcom).addSnapshotListener()
        db.collection("Comandas").document(id).collection("items").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e!=null){
                    Log.e("GestioMaterialGIRES","Firestore Errror: "+e.toString());
                    return;
                }
                list_items.clear();
                for (DocumentSnapshot doc: documentSnapshots){
                    Itemcomandprop item = new Itemcomandprop(doc.getString("nombre"));
                    item.setId(doc.getId());
                    list_items.add(item);

                }
                adapter.notifyDataSetChanged();
            }
        });

        list_items = new ArrayList<Itemcomandprop>();
        List_items = findViewById(R.id.list_itemcom);
        Infocomanda = findViewById(R.id.lbl_namecomanda);
        Infocomanda.setText(name+"/ Uusari: "+usuari);
        adapter = new ComandListAdapter(this,R.layout.activity_viewitemscomanda,list_items);
        List_items.setAdapter(adapter);
        List_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list_items.get(position).toggleChecked();
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void returnComanda(View view) {
        DocumentReference comRef = db.collection( "Comandas" ).document( id );
        WriteBatch batch = db.batch();
        if (!entrega){ //Posar la comanda com a entregada. Nomès puja els elements marcats

            Map<String,Object> modificar = new HashMap<>();
            modificar.put(ENTREGA,true);
            modificar.put(DATA,data);
            modificar.put(NAME,name);
            modificar.put(USUARI,usuari);
            batch.set(comRef,modificar);
            //DocumentReference Itemref = comRef.collection("items").document();
            //Map<String,Object> items = new HashMap<>();
            for (int d = 0;d<list_items.size();d++){
                if (!list_items.get(d).isChecked()){
                    DocumentReference Itemref = comRef.collection("items").document(list_items.get(d).getId());
                /*Map<String,Object> items = new HashMap<>();
                items.put(NOMBRE,list_items.get(d).getText());
                items.put(CANTIDAD,list_items.get(d).getNumtotal());
                items.put(PRECIOITEM,list_items.get(d).getPrecio());
                batch.set(comRef.collection("items").document(), items);*/
                    batch.delete(Itemref);
                }
            }
            batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent data = new Intent();
                    setResult(RESULT_OK,data);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Viewitemscomanda.this,"Error!",Toast.LENGTH_SHORT).show();
                    Log.d("ERROR",e.toString());
                }
            });
        }
        else { //L' usuari a retornat la comanda, el·liminar-la en cas que es retornint tots els elements/modificar - la si falta algun element per retornar
            int count = 0;
            for (int d= 0;d<list_items.size();d++){ //El·liminar de la comanda objectes marcats
                if (list_items.get(d).isChecked()){
                    DocumentReference Itemref = comRef.collection("items").document(list_items.get(d).getId());
                    batch.delete(Itemref);
                    count = count + 1;
                }

            }
            if (count==list_items.size()){ //Si tots els objectes són entregats, el·liminem la comanda
                batch.delete(comRef);
            }
            batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent data = new Intent();
                    setResult(RESULT_OK,data);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Viewitemscomanda.this,"Error!",Toast.LENGTH_SHORT).show();
                    Log.d("ERROR",e.toString());
                }
            });
        }

    }






    public void returnImportantUser(View view) {
        finish();
    }
}
