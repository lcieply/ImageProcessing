package pl.edu.agh.binary;

/**
 * Created by Kamil Jureczka on 2017-05-17.
 */
public enum BinaryColor {
    BLACK(0), WHITE(255);

    private int value;

    BinaryColor(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
