package core;

public class Room {
    public int wid;
    public int len;
    public int x;
    public int y;
    public int left;
    public int right;
    public int top;
    public int bottom;


    public Room(int w, int l, int x, int y) {
        wid = w;
        len = l;
        this.x = x;
        this.y = y;
        left = x - wid / 2 - 1; // -1 是指的墙的那1米
        right = x + wid /  2 + 1;
        top = y + len / 2 + 1;
        bottom = y - len / 2 - 1;
    }

    public boolean superposition(Room anotherRoom) {


        int anotherLeft = anotherRoom.x - anotherRoom.wid / 2 - 1;
        int anotherRight = anotherRoom.x + anotherRoom.wid / 2 + 1;
        int anotherTop = anotherRoom.y - anotherRoom.len / 2 - 1;
        int anotherBottom = anotherRoom.y + anotherRoom.len / 2 + 1;

        return !(right < anotherLeft || left > anotherRight || top < anotherTop || top > anotherBottom);
    }

    public Point center() {
        return new Point(x, y);
    }


}
