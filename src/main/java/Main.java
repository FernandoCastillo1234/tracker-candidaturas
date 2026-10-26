import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    private static final String ARCHIVO_DATOS = "datos_candidaturas.txt";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: Faltan argumentos. Operaciones disponibles: listar, añadir, ver, borrar, importar");
            return;
        }

        String operacion = args[0].toLowerCase();

        try {
            switch (operacion) {
                case "importar":
                    importarCSV(args);
                    break;
                case "listar":
                case "añadir":
                case "ver":
                case "borrar":
                    System.out.println("Comando " + operacion + " en desarrollo por el equipo.");
                    break;
                default:
                    System.out.println("Error: Operación no reconocida. Use: listar, añadir, ver, borrar, importar");
            }
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    public static void importarCSV(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Error: Falta el nombre del archivo. Uso: importar <archivo.csv>");
            return;
        }

        Path archivoImportar = Paths.get(args[1]);
        if (!Files.exists(archivoImportar)) {
            System.out.println("Error: El archivo '" + args[1] + "' no existe.");
            return;
        }

        List<String> lineasNuevas = Files.readAllLines(archivoImportar);
        boolean esPrimerArchivo = !Files.exists(Paths.get(ARCHIVO_DATOS));

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ARCHIVO_DATOS),
                esPrimerArchivo ? StandardOpenOption.CREATE : StandardOpenOption.APPEND)) {

            for (int i = 0; i < lineasNuevas.size(); i++) {
                if (!esPrimerArchivo && i == 0 && lineasNuevas.get(i).startsWith("id;")) {
                    continue;
                }
                writer.write(lineasNuevas.get(i));
                writer.newLine();
            }
        }
        System.out.println("Datos importados correctamente desde " + args[1]);
    }

    public static String obtenerParametro(String[] args, String nombreParametro) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equalsIgnoreCase(nombreParametro)) {
                return args[i + 1];
            }
        }
        return null;
    }
}