package e.alexsalasanleandro.gestiomaterialgires;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Viewitemscomanda extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AdaptListMaterial adapter;
    private ArrayList<Itemcomandprop> list_items;
    String id,name,usuari,data;
    private static final String ENTREGA = "entrega";
    private static final String NAME = "name";
    private static final String USUARI = "usuari";
    private static final String DATA = "data";
    ListView List_items;

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
                    list_items.add(new Itemcomandprop(doc.getString("nombre")));
                }
                adapter.notifyDataSetChanged();
            }
        });

        list_items = new ArrayList<Itemcomandprop>();
        List_items = findViewById(R.id.list_itemcom);
        adapter = new AdaptListMaterial(this,R.layout.activity_viewitemscomanda,list_items);
        List_items.setAdapter(adapter);
    }

    public void returnComanda(View view) {
        DocumentReference comRef = db.collection( "Comandas" ).document( id );
        Map<String,Object> modificar = new HashMap<>();
        modificar.put(ENTREGA,true);
        modificar.put(DATA,data);
        modificar.put(NAME,name);
        modificar.put(USUARI,usuari);
        comRef.set(modificar).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent data = new Intent();
                setResult(RESULT_OK,data);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("GestioMaterialGires","Error Firestore: "+e.toString());
            }
        });


    }

    public void returnImportantUser(View view) {
        finish();
    }
}
