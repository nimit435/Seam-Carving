
import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.util.HashMap;


public class SeamCarver {

    private Picture pic;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        pic = new Picture(picture);
        width = pic.width();
        height = pic.height();
    }


    public Picture picture() {
        return pic;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IndexOutOfBoundsException();

        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
        {
            return Math.pow(255.0, 2) * 3;
        }

        double dx = 0.0, dy = 0.0;
        Color xl, xr, yu, yd;
        xl = pic.get(x - 1, y);
        xr = pic.get(x + 1, y);
        yu = pic.get(x, y - 1);
        yd = pic.get(x, y + 1);
        dx = Math.pow((xl.getRed() - xr.getRed()), 2) + Math.pow((xl.getGreen() - xr.getGreen()), 2) + Math.pow((xl.getBlue() - xr.getBlue()), 2);
        dy = Math.pow((yu.getRed() - yd.getRed()), 2) + Math.pow((yu.getGreen() - yd.getGreen()), 2) + Math.pow((yu.getBlue() - yd.getBlue()), 2);
        return dx + dy;
    }

    private int[] summon(String mode, HashMap<String,String> lastNode, String end) {
        int size;
        if (mode.equals("h"))
            size = width();
        else if (mode.equals("v"))
            size = height();
        else
            throw new IllegalArgumentException();

        int[] res = new int[size];
        String cur = end;

        for(int i=size-1; i>=0; i--) {
            res[i] = getInt(mode, cur);
            cur = lastNode.get(cur);
        }
        // path represents the seam as a 1D array of the coordinates in the seam.
        //y-coordinates are stored if the seam traverses horizontally.
        //x-coordinates are stored if the seam traverses vertically.
        return res;
    }

    private String getStr(int col, int row) {
        return col + " " + row;
    }

    private int getInt(String mode, String str) {
        if (mode.equals("v"))
            return Integer.parseInt(str.split(" ")[0]);
        else if (mode.equals("h"))
            return Integer.parseInt(str.split(" ")[1]);
        else
            throw new IllegalArgumentException();
    }

    // Loops through indices to find horizontal seam.
    public int[] findHorizontalSeam() {
        String mode = "h";
        HashMap<String, String> lastNode = new HashMap<String, String>();
        HashMap<String, Double> minEnergy = new HashMap<String, Double>();
        double cost = Double.MAX_VALUE;
        //cur represents the current pixel.
        //next represents a potential pixel to connect cur to.
        String cur, next, end = null;

        for (int col = 0; col < width() - 1; col++)
            for (int row = 0; row < height(); row++) {

                cur = getStr(col, row);
                if (col == 0) {
                    lastNode.put(cur, null);
                    minEnergy.put(cur, energy(col, row));
                }
                for (int i = row - 1; i <= row + 1; i++)
                    if (i >= 0 && i < height()) {
                        next = getStr(col + 1, i);
                        double newEng = energy(col + 1, i) + minEnergy.get(cur);
                        //If we don't have a next edge yet, add one. Or, if this edge
                        // is better than the one we have, use it.
                        if (minEnergy.get(next) == null || newEng < minEnergy.get(next)) {

                            lastNode.put(next, cur);
                            minEnergy.put(next, newEng);

                            //End at the second to last column, because 'next' inolves
                            // the next column.
                            if (col + 1 == width() - 1 && newEng < cost) {
                                cost = newEng;
                                end = next;
                            }
                        }
                    }
            }
        return summon(mode, lastNode, end);
    }

    // Loops through indices to find vertical seam.
    public int[] findVerticalSeam() {
        //See comments in findHorizontalSeam() for equivalent explanations.
        String mode = "v";
        HashMap<String, String> lastNode = new HashMap<String, String>();
        HashMap<String, Double> minEnergy = new HashMap<String, Double>();
        double cost = Double.MAX_VALUE;
        String cur, next, end = null;

        for (int row = 0; row < height() - 1; row++)
            for (int col = 0; col < width(); col++) {

                cur = getStr(col, row);
                if (row == 0) {
                    lastNode.put(cur, null);
                    minEnergy.put(cur, energy(col, row));
                }
                for (int k = col - 1; k <= col + 1; k++)
                    if (k >= 0 && k < width()) {
                        next = getStr(k, row + 1);
                        double newEng = energy(k, row + 1) + minEnergy.get(cur);
                        if (minEnergy.get(next) == null || newEng < minEnergy.get(next)) {

                            lastNode.put(next, cur);
                            minEnergy.put(next, newEng);
                            if (row + 1 == height() - 1 && newEng < cost) {
                                cost = newEng;
                                end = next;
                            }
                        }
                    }
            }
        return summon(mode, lastNode, end);
    }

    private boolean isNotValidSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                return true;
            }
        }
        return false;
    }

    // Removes horizontal seam from picture.
    public void removeHorizontalSeam(int[] seam) {
        if (width() <= 1 || height() <= 1 || seam.length >width() || isNotValidSeam(seam)){
            throw new IllegalArgumentException();
        }

        Picture newPic = new Picture(width(), height() - 1);

        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height() - 1; row++) {

                if (row < seam[col])
                    newPic.set(col, row, pic.get(col, row));
                else
                    newPic.set(col, row, pic.get(col, row + 1));

            }

        height--;
        pic = new Picture(newPic);
    }

    // Removes vertical seam from picture.
    public void removeVerticalSeam(int[] seam) {
        if (width() <= 1 || height() <= 1 || seam.length > height() || isNotValidSeam(seam)){
            throw new IllegalArgumentException();
        }

        Picture newPic = new Picture(width() - 1, height());

        for (int row = 0; row < height(); row++)
            for (int col = 0; col < width() - 1; col++) {

                if (col < seam[row])
                    newPic.set(col, row, pic.get(col, row));
                else
                    newPic.set(col, row, pic.get(col + 1, row));

            }

        width--;
        pic = newPic;
    }

    // Resizes the picture to a specified width or height.
    public Picture resizeTo(String mode, int dimension)
    {
        // Resize the width; remove vertical seams.
        if (mode.equals("width")) {
            while (this.width() > dimension) {
                int[] seam = this.findVerticalSeam();
                this.removeVerticalSeam(seam);
            }
            System.out.println("Sort");
        }

        // Resize the height; remove horizontal seams.
        else if (mode.equals("height")) {
            while (this.height() > dimension) {
                int[] seam = this.findHorizontalSeam();
                this.removeHorizontalSeam(seam);
            }
            System.out.println("Sort");
        }

        else throw new IllegalArgumentException();

        // Return the resized image.
        return this.picture();
    }

}
