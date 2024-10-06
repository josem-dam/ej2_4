package edu.acceso.ej2_4;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.stream.Stream;

import edu.acceso.ej2_4.backend.Backend;
import edu.acceso.ej2_4.backend.Factory;

/**
 * Interfaz de texto para la aplicación.
 */
public class InterfazTexto {

    private static Factory factory;
    private static Scanner sc = new Scanner(System.in);
    private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Crea un menú para escoger una entre varias opciones.
     * @param opciones El texto para cada una de las opciones.
     * @param pregunta La pregunta que se quiere realizar.
     * @return El índice de la opción seleccionada.
     */
    private static int preguntarOpcion(String[] opciones, String pregunta) {
        int respuesta = -1;

        System.out.println(pregunta);
        for(int i=0; i<opciones.length; i++) {
            System.out.printf("[%d] %s\n", i+1, opciones[i]);
        }
        do {
            System.out.print("Escoja una opción: ");
            respuesta = sc.nextInt();
        } while(respuesta<1 || respuesta>opciones.length);
        return respuesta - 1;

    }

    /**
     * Permite seleccionar una de las opciones de un tipo Enum.
     * 
     * <pre>
     *  Estudios previos = preguntarEnum(Estudios.class, "¿Qué estudios previos tiene?");
     * </pre>
     * 
     * @param <E> La instancia del Enum seleccionada.
     * @param enumClass La clase del Enum.
     * @param pregunta La pregunta que se formula.
     * @return
     */
    private static <E extends Enum<E>> E preguntarEnum(Class<E> enumClass, String pregunta) {
        E[] opciones = enumClass.getEnumConstants();
        int resp = preguntarOpcion(Arrays.stream(opciones).map(o -> o.name()).toArray(String[]::new), pregunta);
        return opciones[resp];
    }

    /**
     * Realiza las preguntas para obtener información sobre un estudiante.
     * @return El objeto Estudiante generado.
     */
    private static Estudiante preguntarEstudiante() {
        sc.nextLine();
        System.out.print("Nombre: ");
        String nombre  = sc.nextLine();
        System.out.print("Apellidos: ");
        String apellidos  = sc.nextLine();
        Date nacimiento = null;
        while(nacimiento == null) {
            System.out.print("Nacimiento (dd/MM/yyyy): ");
            try {
                nacimiento = df.parse(sc.nextLine());
            }
            catch(ParseException err) {
                System.err.println("Fecha inválida. Inténtelo de nuevo.");
            }
        }
        Estudios previos = preguntarEnum(Estudios.class, "¿Qué estudios previos posee?");

        return factory.crearEstudiante().cargarDatos(
            nombre,
            apellidos,
            nacimiento,
            previos
        );
    }

    /**
     * Genera la ruta de almacenamiento añadiendo la extensión.
     * @param ruta Ruta original.
     * @param formato Formato de almacenamiento que sirve para definir la extensión.
     * @return La ruta con la extensión correcto.
     */
    static Path calcularRuta(Path ruta, String formato) {
        return ruta.resolveSibling(ruta.getFileName().toString() + "." + formato.toLowerCase());
    }

    /**
     * Lanza la interfaz de interacción con el usuario.
     * @param ruta Ruta sin extensión del archivo que se usará para almacenar datos.
     */
    public static void start(Path ruta) {
        String formato[] = new String[] {"JSON", "CSV", "object"};
        int respFormato = preguntarOpcion(formato, "¿En qué formato quiere almacenar la información");
        factory = new Factory(formato[respFormato]);

        // Añadimos a la ruta la extensión.
        ruta = calcularRuta(ruta, formato[respFormato]);

        System.out.print("Indique el número de estudiantes que desea registrar: ");
        int cantidad = sc.nextInt();

        Estudiante[] estudiantes =  Stream.generate(()-> {
            System.out.printf("--Estudiante--\n");
            return preguntarEstudiante();
        }).limit(cantidad).toArray(Estudiante[]::new);

        Backend backend = factory.crearBackend(ruta);
        try {
            System.out.println("Guardamos los estudiantes en un archivo...");
            backend.save(estudiantes);
            System.out.println("Y ahora pulse Intro para recuperarlos");
            sc.nextLine();
            Estudiante[] estudiantesLeidos = (Estudiante[]) backend.read();
            System.out.printf("Lista original: %s\n", Arrays.toString(estudiantes));
            System.out.printf("Lista recuperada: %s\n", Arrays.toString(estudiantesLeidos));
            System.out.printf("¿Son iguales ambas listas? %b\n", Arrays.equals(estudiantes, estudiantesLeidos));
        }
        catch(IOException err) {
            err.printStackTrace();
        }
    }
}