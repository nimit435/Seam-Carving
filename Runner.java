
import edu.princeton.cs.algs4.Picture;

import java.util.Scanner;

public class Runner {

    static Scanner scanner = new Scanner(System.in);


    public static String mode(String input)
    {
        input = input.toLowerCase();

        if ( input.equals("h") )
        {
            return "height";
        }

        if (input.equals("w") )
        {
            return "width";
        }

        throw new IllegalArgumentException("Invalid argument, choose either " +
                "height or width");
    }


    public static void resizeWidth(SeamCarver seamCarver)
    {
        System.out.println("Enter new width:");
        int input = Integer.parseInt(scanner.nextLine());

        // Resize image to the user-inputted width.
        Picture pic = seamCarver.resizeTo("width", input);

        // Display the image.
        pic.show();
    }


    public static void resizeHeight(SeamCarver seamCarver)
    {
        System.out.println("Enter new height:");
        int input = Integer.parseInt(scanner.nextLine());

        // Resize image to the user-inputted width.
        Picture pic = seamCarver.resizeTo("height", input);

        // Display the image.
        pic.show();
    }


    public static void main(String[] args)
    {
        System.out.println("Choose one:");
        String input = scanner.nextLine();
        String mode = mode(input);

        System.out.println();

        System.out.println("Enter the the image to resize:");
        input = scanner.nextLine();
        Picture inputImg = new Picture(input);
        SeamCarver seamCarver = new SeamCarver(inputImg);

        if (mode.equals("width")) {
            resizeWidth(seamCarver);
        }

        if (mode.equals("height")) {
            resizeHeight(seamCarver);
        }

    }
}
