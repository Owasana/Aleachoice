package model;

import android.graphics.Color;

import java.util.Random;

public class Item {
    private String m_name;
    private Color m_color;

    private static Color randomColor() {
        Random random = new Random();
        return Color.valueOf(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
    }

    public Item(String name, Color color) {
        m_name = name;
        m_color = color;
    }

    public Item(String name) {
        this(name, randomColor());
    }

    String name() {
        return m_name;
    }

    Color color() {
        return m_color;
    }
}
