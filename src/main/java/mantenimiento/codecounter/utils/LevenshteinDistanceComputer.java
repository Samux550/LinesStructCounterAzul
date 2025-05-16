package mantenimiento.codecounter.utils;

import java.util.Arrays;

public class LevenshteinDistanceComputer {
    /**
     * Calcula la distancia de Levenshtein entre dos cadenas de caracteres.
     *
     * @param source la primera cadena
     * @param target la segunda cadena
     * @return el número mínimo de operaciones necesarias para transformar {@code x}
     *         en {@code y}
     */
    public static int calculate(String source, String target) {
        int[][] dp = new int[source.length() + 1][target.length() + 1];

        for (int i = 0; i <= source.length(); i++) {
            for (int j = 0; j <= target.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(dp[i - 1][j - 1]
                            + costOfSubstitution(source.charAt(i - 1), target.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[source.length()][target.length()];
    }

    /**
     * Devuelve el costo de sustituir un carácter por otro.
     * Si los caracteres son iguales, el costo es 0. Si son diferentes, el costo es 1.
     *
     * @param sourceChar el primer carácter
     * @param targetChar el segundo carácter
     * @return 0 si {@code a} y {@code b} son iguales, 1 en caso contrario
     */
    private static int costOfSubstitution(char sourceChar, char targetChar) {
        return sourceChar == targetChar ? 0 : 1;
    }

    /**
     * Devuelve el valor mínimo entre un conjunto de enteros.
     *
     * @param numbers los números a comparar
     * @return el menor valor entre {@code numbers}
     */
    private static int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }
}
