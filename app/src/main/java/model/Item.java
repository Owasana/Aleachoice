package model;

import android.graphics.Color;

import java.io.Serializable;
import java.util.Random;

public class Item implements Serializable {
    private String m_name;
    private int m_color;

    private static int randomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    public Item(String name, int color) {
        m_name = name;
        m_color = color;
    }

    public Item(String name) {
        this(name, randomColor());
    }

    public String name() {
        return m_name;
    }

    public int color() {
        return m_color;
    }
}
