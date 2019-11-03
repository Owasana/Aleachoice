package model;

public class Pion extends Position {
    private Object user;

    public Pion(int x, int y, Object user) {
        super(x, y);
        this.user = user;
    }

    public Object getUser() {
        return user;
    }
}

