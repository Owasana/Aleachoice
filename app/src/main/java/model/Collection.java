package model;

import java.io.Serializable;
import java.util.ArrayList;
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

    public static class PickResult implements Serializable {
        public int index;
        public Item item;
    }

    public PickResult pick() {
        PickResult pick = new PickResult();
        pick.index = m_random.nextInt(size());
        pick.item = item(pick.index);

        return pick;
    }

    /** Copie du contenu d'une collection sur une autre
     */
    public void move(Collection other) {
        m_items = (ArrayList<Item>) other.m_items.clone();
    }
}
