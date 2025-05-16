package mantenimiento.codecounter;

import java.util.Scanner;
import mantenimiento.codecounter.models.ProgramBuilder;

public class Main {

  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);

    ProgramBuilder.buildProgram(requestFolderPath(s));
  }

  private static String requestFolderPath(Scanner scan) {
    System.out.println();
    System.out.println("Ingrese la ruta o las rutas de las carpetas del proyecto:");
    System.out.println(" - Para analizar un solo proyecto, introduzca UNA sola ruta.");
    System.out.println(" - Para comparar dos proyectos, introduzca DOS rutas separadas por un espacio.");
    System.out.println(" * Importante: Asegúrese de que las carpetas en las rutas NO contengan espacios.");
    System.out.println();
    System.out.print("Ruta(s): ");
    return scan.nextLine();
}
}