package edu.acceso.ej2_4.ui.texto;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import edu.acceso.ej2_4.Estudiante;
import edu.acceso.ej2_4.Estudios;
import edu.acceso.ej2_4.backend.Backend;
import edu.acceso.ej2_4.backend.BackendFactory;
import edu.acceso.ej2_4.ui.Ui;

/**
 * Interfaz de texto para la aplicación.
 */
public class TextUi implements Ui {

    private static BackendFactory factory;
    private static Scanner sc = new Scanner(System.in);
    private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private Map<String, String> opciones;

    /**
     * Constructor de la clase.
     * @param opciones Opciones de usuario que modifican el comportamiento del programa
     */
    public TextUi(Map<String, String> opciones) {
        this.opciones = opciones;
    }

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
     * Arranca la interfaz.
     */
    @Override
    public void start() {

        String formato = opciones.getOrDefault("formato", null);
        if(formato == null) {
            String[] formatos = Arrays.stream(BackendFactory.Formato.values()).map(f -> f.name()).toArray(String[]::new);
            int respFormato = preguntarOpcion(formatos, "¿En qué formato quiere almacenar la información");
            formato = formatos[respFormato];
        }

        Path ruta = opciones.containsKey("file")?Path.of(opciones.get("file")):Ui.generarRuta(Estudiante.class, formato);
        factory = new BackendFactory(formato);
        
        System.out.print("Indique el número de estudiantes que desea registrar: ");
        int cantidad = sc.nextInt();

        Estudiante[] estudiantes =  Stream.generate(()-> {
            System.out.printf("--Estudiante--\n");
            return preguntarEstudiante();
        }).limit(cantidad).toArray(Estudiante[]::new);

        Backend backend = factory.crearBackend(ruta);
        try {
            System.out.println("Guardamos los estudiantes en un archivo...");
            int num = backend.save(estudiantes);
            System.out.printf("%d registro(s) almacenado(s).\n", num);
            System.out.println("Y ahora pulse Intro para recuperarlos");
            sc.nextLine();
            Estudiante[] estudiantesLeidos = backend.read(factory.getFormato().getTipoEstudiante());
            System.out.printf("Lista original: %s\n", Arrays.toString(estudiantes));
            System.out.printf("Lista recuperada: %s\n", Arrays.toString(estudiantesLeidos));
            System.out.printf("¿Son iguales ambas listas? %b\n", Arrays.equals(estudiantes, estudiantesLeidos));
        }
        catch(IOException err) {
            err.printStackTrace();
        }
    }
}