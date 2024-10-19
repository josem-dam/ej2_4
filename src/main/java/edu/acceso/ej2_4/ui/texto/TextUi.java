package edu.acceso.ej2_4.ui.texto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import edu.acceso.ej2_4.Estudiante;
import edu.acceso.ej2_4.Estudios;
import edu.acceso.ej2_4.backend.Backend;
import edu.acceso.ej2_4.backend.BackendFactory;
import edu.acceso.ej2_4.ui.Ui;

/**
 * Interfaz de texto para la aplicación.
 */
public class TextUi implements Ui {

    private static Scanner sc = new Scanner(System.in);
    private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    private ArrayList<Estudiante> estudiantes;
    private BackendFactory factory;
    private Backend backend;
    private Path ruta;
    private Map<String, String> opciones;

    /**
     * Constructor de la clase.
     * @param opciones Opciones de usuario que modifican el comportamiento del programa
     */
    public TextUi(Map<String, String> opciones) {
        this.opciones = opciones;
        estudiantes = new ArrayList<>();
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

    private static boolean confirmar(String mensaje) {
        while(true) {
            System.out.println(mensaje + " (s/n)");
            String respuesta = sc.nextLine();
            switch(respuesta.toLowerCase()) {
                case "s":
                case "si":
                case "sí":
                case "y":
                case "yes":
                    return true;
                case "n":
                case "no":
                    return false;
                default:
                    break;
            }
        }
    }

    /**
     * Arranca la interfaz.
     */
    @Override
    public void start() {

        String[] menuPrincipal = {"Matricular estudiante", "Eliminar estudiante", "Ver estudiantes", "Guardar en disco", "Recuperar de disco", "Borrar de memoria", "Salir"};

        String formato = opciones.getOrDefault("formato", null);
        if(formato == null) {
            String[] formatos = Arrays.stream(BackendFactory.Formato.values()).map(f -> f.name()).toArray(String[]::new);
            int respFormato = preguntarOpcion(formatos, "¿En qué formato quiere almacenar la información");
            formato = formatos[respFormato];
        }

        ruta = opciones.containsKey("file")?Path.of(opciones.get("file")):Ui.generarRuta(Estudiante.class, formato);
        factory = new BackendFactory(formato);
        backend = factory.crearBackend(ruta);
        
        while(true) {
            int opcion = preguntarOpcion(menuPrincipal, "\n¿Qué desea hacer?");
            switch(opcion) {
                case 0:
                    Estudiante estudiante = preguntarEstudiante();
                    estudiantes.add(estudiante);
                    System.out.printf("Creado el estudiante %s.\n", estudiante);
                    break;
                case 1:
                    eliminarEstudiante();
                    break;
                case 2:
                    mostrarEstudiantes();
                    break;
                case 3:
                    int registros = guardar();
                    System.out.printf("%d registro(s) almacenado(s).\n", registros);
                    break;
                case 4:
                    leer();
                    break;
                case 5:
                    borrarTodo();
                    System.out.printf("El próximo matriculado será el %d.\n", Estudiante.getSiguienteMatricula());
                    break;
                case 6:
                    System.out.println("¡¡¡Adiós!!!");
                    System.exit(0);
                    break;
                default:
                    assert false: "Opción imposible";
                    System.exit(3);
            }
        }
    }

    /**
     * Realiza las preguntas para obtener información sobre un estudiante.
     * @return El objeto Estudiante generado.
     */
    private Estudiante preguntarEstudiante() {
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
     * Elimina un estudiante de la lista.
     */
    private void eliminarEstudiante() {
        if(estudiantes.size() == 0) {
            System.err.println("ERROR. No hay estudiantes en memoria.");
            return;
        }

        String[] borrandos = estudiantes.stream().map(Estudiante::toString).toArray(String[]::new);
        int opcion = preguntarOpcion(borrandos, "Indique qué estudiante desea borrar:");
        Estudiante borrado =  estudiantes.remove(opcion);
        System.out.printf("Borrado el estudiante %d.\n", borrado.getMatricula());
    }

    /**
     * Muestra la lista de estudiantes en memoria.
     */
    private void mostrarEstudiantes() {
        if(estudiantes.size() > 0 ) {
            System.out.println("Estudiantes en memoria:");
            estudiantes.forEach(System.out::println);
        }
        else {
            System.out.println("No hay estudiantes en memoria.");
        }
        System.out.printf("El próximo matriculado será el %d.\n", Estudiante.getSiguienteMatricula());
    }

    /**
     * Guardar estudiantes en disco.
     * @return El número de registros (estudiantes) guardados.
     */
    private int guardar() {
        if(Files.exists(ruta)) {
            boolean seguir = confirmar("El archivo existe, ¿quiere sobreescribirlo?");
            if(!seguir) return 0;
        }

        try {
            return backend.save(estudiantes.toArray(Estudiante[]::new));
        }
        catch(IOException err) {
            System.err.printf("ERROR. No puede guardarse la información: %s.\n", err.getMessage());
            return 0;
        }
    }

    /**
     * Leer estudiantes de disco.
     */
    private void leer() {
        if(!Files.exists(ruta)) {
            System.err.println("ERROR. No existe el archivo %s. ¿Ha guardado alguna vez estudiantes?\n");
            return;
        }

        Estudiante[] estudiantesLeidos = null;
        // La lectura modifica automáticamente el próximo matriculado, así que
        // debemos guardarlo por si al final desechamos la lectura ya hecha.
        long proximoMatriculado = Estudiante.getSiguienteMatricula();

        try {
            estudiantesLeidos = backend.read(factory.getFormato().getTipoEstudiante());
        }
        catch(IOException err) {
            System.err.printf("ERROR. No puede leerse el archivo: %s.\n", err.getMessage());
            return;
        }

        boolean hay = estudiantes.size() > 0;
        boolean iguales = hay?Arrays.equals(estudiantes.toArray(Estudiante[]::new), estudiantesLeidos):false;
        boolean reemplazar = !hay;

        if(iguales) {
            System.out.println("Los estudiantes ya estaban en memoria. No se hace nada.");
            reemplazar = false;
        }
        else if(hay) {
            reemplazar = confirmar("En memoria tiene otros estudiantes, ¿quiere reemplazarlos por los de disco?");
        }

        if(reemplazar) {
            proximoMatriculado = Estudiante.getSiguienteMatricula();
            borrarTodo();
            estudiantes.addAll(0, Arrays.asList(estudiantesLeidos));
        }
        else {
            System.out.println("Continuamos con los estudiantes cargados en memoria.");
        }

        new Estudiante().cargarDatos(proximoMatriculado, null, null, null, null);
        System.out.printf("El próximo matriculado será el %d.\n", Estudiante.getSiguienteMatricula());
    }

    /**
     * Borrar los estudiantes que hay en memoria.
     */
    private void borrarTodo() {
        estudiantes.clear();
        new Estudiante().cargarDatos(1L, null, null, null, null);
    }
}