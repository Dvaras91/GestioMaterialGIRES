package e.alexsalasanleandro.gestiomaterialgires;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Viewitemscomanda extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AdaptListMaterial adapter;
    private ArrayList<Itemcomandprop> list_items;
    String id;
    ListView List_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewitemscomanda);
        Intent intent = getIntent();
        if (intent!=null){
            id = intent.getStringExtra("id");
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
}
