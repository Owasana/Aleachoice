package model;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Random;


public class Collection extends Observable implements Serializable {
    private ArrayList<Item> m_items;
    private Random m_random;

    public Collection() {
        m_items = new ArrayList<>();
        m_random = new Random();
    }

    public void addItem(Item item) {
        m_items.add(item);
        setChanged();
        notifyObservers();
    }

    public int size() {
        return m_items.size();
    }

    public Item item(int index) {
        return m_items.get(index);
    }

    public void remove(int index) {
        m_items.remove(index);
        setChanged();
        notifyObservers();
    }

    public ArrayList<Item> shuffled() {
        ArrayList<Item> list = (ArrayList<Item>) m_items.clone();
        Collections.shuffle(list);
        return list;
    }

    /**
     * Copie du contenu d'une collection sur une autre
     */
    public void copy(Collection other) {
        m_items = (ArrayList<Item>) other.m_items.clone();
    }

    public void save(FileOutputStream outfile) {
        JSONObject object = new JSONObject();
        JSONArray liste = new JSONArray();

        try {
            // Construction de la liste
            for (Item item : m_items) {
                JSONObject itemJson = new JSONObject();
                itemJson.put("name", item.name());
                itemJson.put("color", item.color());
                liste.put(itemJson);
            }

            object.put("items", liste);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(object);
        DataOutputStream stream = new DataOutputStream(outfile);
        try {
            stream.writeUTF(object.toString());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public Collection read(FileInputStream inFile) {
        try {
            DataInputStream stream = new DataInputStream(inFile);
            JSONObject object = new JSONObject(stream.readUTF());

            JSONArray liste = (JSONArray)object.get("items");

            Collection collection = new Collection();
            for (int i = 0; i < liste.length(); ++i) {
                JSONObject itemJson = (JSONObject)liste.get(i);
                Item item = new Item(itemJson.getString("name"), itemJson.getInt("color"));
                collection.addItem(item);
            }

            return collection;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
