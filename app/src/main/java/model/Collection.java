package model;

import java.util.ArrayList;
import java.util.Random;

public class Collection {
    private ArrayList<Item> m_items;
    private Random m_random;

    public Collection() {
        m_items = new ArrayList<Item>();
        m_random = new Random();
    }

    public void addItem(Item item) {
        m_items.add(item);
    }

    public int size() {
        return m_items.size();
    }

    public Item item(int index) {
        return m_items.get(index);
    }

    public class PickResult {
        public int index;
        public Item item;
    }

    public PickResult pick() {
        PickResult pick = new PickResult();
        pick.index = m_random.nextInt(size());
        pick.item = item(pick.index);

        return pick;
    }
}
