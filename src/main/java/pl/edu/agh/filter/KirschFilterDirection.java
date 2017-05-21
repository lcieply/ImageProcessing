package pl.edu.agh.filter;

/**
 * Created by Lukasz on 21.05.2017.
 */
public enum KirschFilterDirection {
    W(new int[][]{{-3, -3, 5}, {-3, 0, 5}, {-3, -3, 5}}),
    SE(new int[][]{{-3, 5, 5}, {-3, 0, 5}, {-3, -3, -3}}),
    N(new int[][]{{5, 5, 5}, {-3, 0, -3}, {-3, -3, -3}}),
    NE(new int[][]{{5, 5, -3}, {5, 0, -3}, {-3, -3, -3}}),
    E(new int[][]{{5, -3, -3}, {5, 0, -3}, {5, -3, -3}}),
    NW(new int[][]{{-3, -3, -3}, {5, 0, -3}, {5, 5, 5}}),
    S(new int[][]{{-3, -3, -3}, {-3, 0, -3}, {5, 5, 5}}),
    SW(new int[][]{{-3, -3, -3}, {-3, 0, 5}, {-3, 5, 5}}),
    ALL(new int[][]{});
    private int[][] mask;
    KirschFilterDirection(int[][] mask) {
        this.mask = mask;

    }
    public int[][] getMask() {
        return mask;
    }

    public void setMask(int[][] mask) {
        this.mask = mask;
    }
}
