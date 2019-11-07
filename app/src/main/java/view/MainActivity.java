package view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.aleachoice.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.Collection;
import model.Item;

public class MainActivity extends AppCompatActivity implements BasicItemFragment.OnFragmentInteractionListener {
    private Collection collection;
    private Button go_button;

    private final static String CHOICE_STORAGE = "choice.data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // On essaie de recupérer les données de la dernière activité (e.g changement d'orientation du téléphone).
        /*if (savedInstanceState != null && savedInstanceState.containsKey("collection")) {
            collection = (Collection)savedInstanceState.getSerializable("collection");
        }*/
        try {
            FileInputStream in = this.openFileInput(CHOICE_STORAGE);
            ObjectInputStream br = new ObjectInputStream(in);
            collection = (Collection)br.readObject();
        } catch (Exception e) {
            // Sinon on crée une collection avec trois éléments.
            collection = new Collection();

            Item item1 = new Item("Pizza");
            Item item2 = new Item("Burger");
            Item item3 = new Item("Tacos");

            collection.addItem(item1);
            collection.addItem(item2);
            collection.addItem(item3);
        }

        if (savedInstanceState == null) {
            switchAdd();
        }

        go_button = findViewById(R.id.go_button);
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("collection", collection);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //savedInstanceState.putSerializable("collection", collection);
        // TODO json
        try {
            FileOutputStream out = this.openFileOutput(CHOICE_STORAGE, MODE_PRIVATE);
            ObjectOutputStream br = new ObjectOutputStream(out);
            br.writeObject(collection);
        }
        catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            super.onDestroy();
            FileOutputStream out = this.openFileOutput(CHOICE_STORAGE, MODE_PRIVATE);
            ObjectOutputStream br = new ObjectOutputStream(out);
            br.writeObject(collection);
        }
        catch (Exception e) {

        }
    }

    // Changement vers le fragment de suppression
    @Override
    public void switchDelete() {
        DeleteItemFragment fragment = DeleteItemFragment.newInstance(collection);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    // Changement vers le fragment d'ajout
    @Override
    public void switchAdd() {
        AddItemFragment fragment = AddItemFragment.newInstance(collection);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
