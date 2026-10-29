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
                    listarCandidaturas(args);
                    break;
                case "ver":
                    verCandidatura(args);
                    break;
                case "añadir":
                    añadirCandidatura(args);
                    break;
                case "borrar":
                    borrarCandidatura(args);
                    break;
                default:
                    System.out.println("Error: Operación no reconocida. Use: listar, añadir, ver, borrar, importar");
            }
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    public static void listarCandidaturas(String[] args) throws IOException {
        String estadoFiltro = obtenerParametro(args, "--estado");
        String empresaFiltro = obtenerParametro(args, "--empresa");

        if (!Files.exists(Paths.get(ARCHIVO_DATOS))) {
            System.out.println("Error: No hay datos guardados. Ejecuta primero 'importar candidaturas.csv'.");
            return;
        }

        List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_DATOS));
        System.out.println("--- LISTADO DE CANDIDATURAS ---");
        int encontradas = 0;

        for (String linea : lineas) {
            if (linea.startsWith("id;") || linea.trim().isEmpty()) continue;
            String[] partes = linea.split(";");
            if (partes.length < 4) continue;
            String empresa = partes[2];
            String estado = partes[3];

            boolean coincideEstado = (estadoFiltro == null || estado.equalsIgnoreCase(estadoFiltro));
            boolean coincideEmpresa = (empresaFiltro == null || empresa.equalsIgnoreCase(empresaFiltro));

            if (coincideEstado && coincideEmpresa) {
                System.out.println(String.format("ID: %s | Puesto: %s | Empresa: %s | Estado: %s",
                        partes[0], partes[1], partes[2], partes[3]));
                encontradas++;
            }
        }

        if (encontradas == 0) {
            System.out.println("No se encontraron candidaturas.");
        }
    }

    public static void añadirCandidatura(String[] args) throws IOException {
        String titulo = obtenerParametro(args, "--titulo");
        String empresa = obtenerParametro(args, "--empresa");
        String estado = obtenerParametro(args, "--estado");
        String fecha = obtenerParametro(args, "--fecha");

        if (titulo == null || empresa == null || estado == null || fecha == null) {
            System.out.println("Error: Faltan parámetros requeridos.");
            System.out.println("Uso: añadir --titulo X --empresa Y --estado Z --fecha dd/MM/yyyy");
            return;
        }

        int nuevoId = 1;
        if (Files.exists(Paths.get(ARCHIVO_DATOS))) {
            List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_DATOS));
            for (String linea : lineas) {
                if (linea.startsWith("id;") || linea.trim().isEmpty()) continue;
                String[] partes = linea.split(";");
                if (partes.length > 0) {
                    try {
                        int idActual = Integer.parseInt(partes[0]);
                        if (idActual >= nuevoId) nuevoId = idActual + 1;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } else {
            Files.writeString(Paths.get(ARCHIVO_DATOS), "id;titulo;empresa;estado;fechaPostulacion;enlace;notas\n");
        }

        String nuevaLinea = String.format("%d;%s;%s;%s;%s;;", nuevoId, titulo, empresa, estado, fecha);
        Files.writeString(Paths.get(ARCHIVO_DATOS), nuevaLinea + System.lineSeparator(), StandardOpenOption.APPEND);
        System.out.println("Candidatura registrada con éxito. ID asignado: " + nuevoId);
    }

    public static void verCandidatura(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Error: Falta el ID. Uso: ver <id>");
            return;
        }
        String idBuscado = args[1];
        if (!Files.exists(Paths.get(ARCHIVO_DATOS))) {
            System.out.println("Error: Archivo de datos no encontrado.");
            return;
        }

        List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_DATOS));
        for (String linea : lineas) {
            if (linea.startsWith(idBuscado + ";")) {
                String[] p = linea.split(";", -1);
                System.out.println("--- DETALLE DE CANDIDATURA ID " + p[0] + " ---");
                System.out.println("Título: " + p[1]);
                System.out.println("Empresa: " + p[2]);
                System.out.println("Estado: " + p[3]);
                System.out.println("Fecha Postulación: " + p[4]);
                System.out.println("Enlace: " + (p.length > 5 && !p[5].isEmpty() ? p[5] : "N/A"));
                System.out.println("Notas: " + (p.length > 6 && !p[6].isEmpty() ? p[6] : "N/A"));
                return;
            }
        }
        System.out.println("Error: ID inexistente.");
    }

    public static void borrarCandidatura(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Error: Falta el ID a borrar. Uso: borrar <id>");
            return;
        }

        String idBorrar = args[1];
        if (!Files.exists(Paths.get(ARCHIVO_DATOS))) {
            System.out.println("Error: Archivo de datos no encontrado.");
            return;
        }

        List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_DATOS));
        List<String> lineasRestantes = new ArrayList<>();
        boolean encontrado = false;

        for (String linea : lineas) {
            if (linea.startsWith(idBorrar + ";")) {
                encontrado = true;
            } else {
                lineasRestantes.add(linea);
            }
        }

        if (encontrado) {
            Files.write(Paths.get(ARCHIVO_DATOS), lineasRestantes);
            System.out.println("Candidatura con ID " + idBorrar + " eliminada.");
        } else {
            System.out.println("Error: ID " + idBorrar + " inexistente.");
        }
    }

    public static void importarCSV(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Error: Falta el nombre del archivo. Uso: importar <archivo.csv>");
            return;
        }

        Path archivoImportar = Paths.get(args[1]);
        if (!Files.exists(archivoImportar)) {
            System.out.println("Error: El archivo no existe.");
            return;
        }

        List<String> lineasNuevas = Files.readAllLines(archivoImportar);
        boolean esPrimerArchivo = !Files.exists(Paths.get(ARCHIVO_DATOS));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ARCHIVO_DATOS),
                esPrimerArchivo ? StandardOpenOption.CREATE : StandardOpenOption.APPEND)) {
            for (int i = 0; i < lineasNuevas.size(); i++) {
                if (!esPrimerArchivo && i == 0 && lineasNuevas.get(i).startsWith("id;")) continue;
                writer.write(lineasNuevas.get(i));
                writer.newLine();
            }
        }
        System.out.println("Datos importados correctamente.");
    }

    public static String obtenerParametro(String[] args, String nombreParametro) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equalsIgnoreCase(nombreParametro)) return args[i + 1];
        }
        return null;
    }
}