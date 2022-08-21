import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class MovableObjectNotFittableException extends Exception {

    MovableObjectNotFittableException(String exceptionMessage) {
        super(exceptionMessage);
    }

}

class ObjectCanNotBeMovedException extends Exception {

    ObjectCanNotBeMovedException(int x, int y) {
        super(String.format("Point (%d,%d) is out of bounds", x, y));
    }
}

interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();

    int getCurrentYPosition();

    TYPE getType();

    boolean canBeFitted(int xMax, int yMax);

    String exceptionMessage();

}

class MovablePoint implements Movable {

    int x;
    int y;
    int xSpeed;
    int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (y + ySpeed > MovablesCollection.Y_MAX)
            throw new ObjectCanNotBeMovedException(x, y + ySpeed);
        y += ySpeed;

    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (y - ySpeed < 0)
            throw new ObjectCanNotBeMovedException(x, y - ySpeed);
        y -= ySpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (x - xSpeed < 0)
            throw new ObjectCanNotBeMovedException(x - xSpeed, y);
        x -= xSpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (x + xSpeed > MovablesCollection.X_MAX)
            throw new ObjectCanNotBeMovedException(x + xSpeed, y);
        x += xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public TYPE getType() {
        return TYPE.POINT;
    }

    @Override
    public boolean canBeFitted(int xMax, int yMax) {
        return (x >= 0&&x <= xMax&&y >= 0 && y <= yMax);
    }

    @Override
    public String exceptionMessage() {
        return String.format("Movable point with coordinates (%d,%d) can not be fitted into the collection", x, y);
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)", x, y);
    }
}

class MovableCircle implements Movable {

    int radius;
    MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        center.moveDown();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        center.moveLeft();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        center.moveRight();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }

    @Override
    public boolean canBeFitted(int xMax, int yMax) {
        return ((center.x - radius) >= 0 && (center.x + radius) <= xMax && (center.y + radius) <= yMax && (center.y - radius) >= 0);
    }

    @Override
    public String exceptionMessage() {
        return String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection", center.x, center.y, radius);
    }

    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d) and radius %d", center.x, center.y, radius);
    }
}

class MovablesCollection {
    List<Movable> movableList;
    static int X_MAX;
    static int Y_MAX;

    MovablesCollection(int x_MAX, int y_MAX) {
        movableList = new ArrayList<>();
        X_MAX = x_MAX;
        Y_MAX = y_MAX;
    }

    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {

        int x = m.getCurrentXPosition();
        int y = m.getCurrentYPosition();

        if (!m.canBeFitted(X_MAX, Y_MAX)) {
            throw new MovableObjectNotFittableException(m.exceptionMessage());
        }

        movableList.add(m);
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {

        movableList.stream()
                .filter(movable -> movable.getType().equals(type))
                .forEach(movable -> {
                    try {
                        switch (direction) {
                            case UP:
                                movable.moveUp();
                                break;
                            case DOWN:
                                movable.moveDown();
                                break;
                            case LEFT:
                                movable.moveLeft();
                                break;
                            case RIGHT:
                                movable.moveRight();
                                break;
                        }
                    } catch (ObjectCanNotBeMovedException e) {
                        System.out.println(e.getMessage());
                    }
                });

    }

    public static int getxMax() {
        return X_MAX;
    }

    public static void setxMax(int xMax) {
        X_MAX = xMax;
    }

    public static int getyMax() {
        return Y_MAX;
    }

    public static void setyMax(int yMax) {
        Y_MAX = yMax;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Collection of movable objects with size %d:\n", movableList.size()));

        movableList.forEach(movable -> sb.append(movable.toString()).append("\n"));
        return sb.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);
            try {
                if (Integer.parseInt(parts[0]) == 0) { //point
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } else { //circle
                    int radius = Integer.parseInt(parts[5]);
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                }
            } catch (MovableObjectNotFittableException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());
        
        


    }


}
