package mantenimiento.codecounter;

import java.util.Scanner;
import mantenimiento.codecounter.models.ProgramBuilder;

public class Main {

  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);

    ProgramBuilder.buildProgram(requestFolderPath(s));

    System.out.println("\nPresiona Enter para salir...");
    s.nextLine();
    s.close();
  }

  private static String requestFolderPath(Scanner scan) {
    System.out.println("Ingresa la ruta de la carpeta del proyecto:");
    return scan.nextLine();
  }
}