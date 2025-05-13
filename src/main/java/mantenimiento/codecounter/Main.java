package mantenimiento.codecounter;

import java.util.Scanner;
import mantenimiento.codecounter.models.ProgramBuilder;

public class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    ProgramBuilder.buildProgram(requestFolderPath(scanner));

    System.out.println("\nPresiona Enter para salir...");
    scanner.nextLine();
    scanner.close();
  }

  private static String requestFolderPath(Scanner scanner) {
    System.out.println("Ingresa la ruta de la carpeta del proyecto:");
    return scanner.nextLine();
  }
}
