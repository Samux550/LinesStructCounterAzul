package mantenimiento.codecounter.exceptions;

import mantenimiento.codecounter.constants.ReasonInvalidFormat;

public class InvalidFormatException extends Exception {
  private String fileName = "";

  public InvalidFormatException(ReasonInvalidFormat error, String lineOfCode) {
    super(error.toString() + "\nLinea: " + lineOfCode);
  }

  public InvalidFormatException(String message) {
    super(message);
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileName() {
    return this.fileName;
  }
}
