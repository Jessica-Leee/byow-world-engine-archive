package core;

import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
public class World {
    public static final int WIDTH = 40;
    public static final int LENGTH = 40;
    private static final int MIN_ROOM_SIZE = 2;
    private static final int MAX_ROOM_SIZE = 6;
    private static final int MAX_ROOMS = 13;
    private static final int FOV_RADIUS = 5;
    public TETile[][] world;
    public Random r;
    public List<Room> rooms;
    private int avatarX;
    private int avatarY;
    private long seed;
    private boolean isFOVEnabled = false;
    private Set<Point> visibleTiles;

    public World(long seed) {
        world = new TETile[WIDTH][LENGTH];
        r = new Random(seed);
        rooms = new ArrayList<>();
        visibleTiles = new HashSet<>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < LENGTH; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public void produceWholeWorld() {
        produceRooms();
        produceHallways();
        addWalls();
    }

    public void produceRooms() {
        int loops = 0;
        while (rooms.size() < MAX_ROOMS && loops < 1000) {
            int wid = r.nextInt(MAX_ROOM_SIZE - MIN_ROOM_SIZE + 1) + MIN_ROOM_SIZE;
            int len = r.nextInt(MAX_ROOM_SIZE - MIN_ROOM_SIZE + 1) + MIN_ROOM_SIZE;

            int x = r.nextInt(WIDTH - 2) + 1;
            int y = r.nextInt(LENGTH - 2) + 1;

            Room room = new Room(wid, len, x, y);

            if (roomAvilHelper(room)) {
                this.addRoom(room);
            }
            loops++;
        }
    }

    public boolean roomAvilHelper(Room room) {
        if (room.left < 0 || room.bottom < 0 || room.left + room.wid > WIDTH - 2 || room.bottom + room.len > LENGTH - 2) {
            return false;
        }
        for (Room otherRoom : rooms) {
            if (room.superposition(otherRoom)) {
                return false;
            }
        }
        return true;

    }

    public void addRoom(Room room) {
        for (int x = room.left+1; x < room.right-1; x++) {
            for (int y = room.bottom+1; y < room.top-1; y++) {
                world[x][y] = Tileset.FLOOR;
            }
        }
        rooms.add(room);
    }

    private void produceHallways() {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Point start = rooms.get(i).center();
            Point end = rooms.get(i + 1).center();
            addHallway(start, end,r);
        }
    }
    public void addHallway(Point start, Point end, Random random) {
        int x = start.getX();
        int y = start.getY();
        boolean horizontalFirst = RandomUtils.bernoulli(random);
        if (horizontalFirst) {
            while (x != end.getX()) {
                world[x][y] = Tileset.FLOOR;
                x += Integer.compare(end.getX(), x);
            }
            while (y != end.getY()) {
                world[x][y] = Tileset.FLOOR;
                y += Integer.compare(end.getY(), y);
            }
        } else {
            while (y != end.getY()) {
                world[x][y] = Tileset.FLOOR;
                y += Integer.compare(end.getY(), y);
            }
            while (x != end.getX()) {
                world[x][y] = Tileset.FLOOR;
                x += Integer.compare(end.getX(), x);
            }
        }
        world[x][y] = Tileset.FLOOR;
    }

    public void addWalls(){
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < LENGTH; y++) {
                if (world[x][y] == Tileset.FLOOR) {
                    for (int z = -1; z <= 1; z++) {
                        for (int m = -1; m <= 1; m++) {
                            int nx = x + z;
                            int ny = y + m;
                            if (isInBounds(nx, ny) && world[nx][ny] == Tileset.NOTHING) {
                                world[nx][ny] = Tileset.WALL;
                            }
                        }
                    }
                }
            }
        }
    }
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < LENGTH;
    }


    public void moveAvatar(char dir) {
        world[avatarX][avatarY] = Tileset.FLOOR;

        int movedX = avatarX;
        int movedY = avatarY;

        if (dir == 'w' || dir == 'W') {
            movedY += 1;
        } else if (dir == 'a' || dir == 'A') {
            movedX -= 1;
        } else if (dir == 's' || dir == 'S') {
            movedY -= 1;
        } else if (dir == 'd' || dir == 'D') {
            movedX += 1;
        } else if (dir == 'f' || dir == 'F') {
            isFOVEnabled = !isFOVEnabled;
            calculateVisibleTiles();
            world[avatarX][avatarY] = Tileset.AVATAR;
            return;
        }

        if (isInBounds(movedX, movedY) && world[movedX][movedY] == Tileset.FLOOR) {
            avatarX = movedX;
            avatarY = movedY;
        }

        world[avatarX][avatarY] = Tileset.AVATAR;
        calculateVisibleTiles();
    }

    public TETile getTileForRendering(int x, int y) {
        if (!isFOVEnabled) {
            return world[x][y];
        }
        if (visibleTiles.isEmpty()) {
            calculateVisibleTiles();
        }
        if (calculateDistance(avatarX, avatarY, x, y) <= FOV_RADIUS) {
            return world[x][y];
        }
        return Tileset.NOTHING;
    }
    public void produceAvatar(){
        for (int x = 0; x < WIDTH ; x++) {
            for (int y = 0; y < LENGTH; y++) {
                if (world[x][y] == Tileset.FLOOR) {
                    avatarX = x;
                    avatarY = y;
                    world[avatarX][avatarY] = Tileset.AVATAR;
                    initializeVisibleTiles(); // Add this line
                    return;
                }
            }
        }
    }
    public long getSeed(){
        return seed;
    }

    public int getAvatarX() {
            return avatarX;
        }

        public int getAvatarY() {
            return avatarY;
        }
        public void setAvatarPosition(int x, int y) {
            this.avatarX = x;
            this.avatarY = y;
            world[avatarX][avatarY] = Tileset.AVATAR;
    }
    public void initializeVisibleTiles() {
        calculateVisibleTiles();
    }
    public void calculateVisibleTiles() {
        visibleTiles.clear();

        if (!isFOVEnabled) {
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < LENGTH; y++) {
                    if (world[x][y] != Tileset.NOTHING) {
                        visibleTiles.add(new Point(x, y));
                    }
                }
            }
            return;
        }

        for (int dx = -FOV_RADIUS; dx <= FOV_RADIUS; dx++) {
            for (int dy = -FOV_RADIUS; dy <= FOV_RADIUS; dy++) {
                int checkX = avatarX + dx;
                int checkY = avatarY + dy;

                // Check if the point is within the world bounds
                if (!isInBounds(checkX, checkY)) {
                    continue;
                }

                double distance = calculateDistance(avatarX, avatarY, checkX, checkY);
                if (distance <= FOV_RADIUS) {
                    visibleTiles.add(new Point(checkX, checkY));
                }
            }
        }
    }


    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}

//I use claude.ai for task 6 primary task for assistance. Here are my prompts:
//How can I modify the World class to calculate a radius of visible tiles around the avatar?
//What algorithm would work best for determining line of sight in a tile-based game?
//How do I ensure walls block line of sight but don't completely prevent seeing nearby tiles?
// What's a good approach to calculating which tiles should be visible when the avatar moves?
