package edu.acceso.ej2_4.ui.auto;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

import edu.acceso.ej2_4.Estudiante;
import edu.acceso.ej2_4.Estudios;
import edu.acceso.ej2_4.backend.Backend;
import edu.acceso.ej2_4.backend.BackendFactory;
import edu.acceso.ej2_4.backend.EstudiantePersistente;
import edu.acceso.ej2_4.ui.Ui;

/**
 * Interfaz que ahorra introducir datos. Sirve, simplemente, para hacer pruebas
 * sin tener que perder tiempo tecleando.
 */
public class AutoUi implements Ui {

    private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private Map<String, String> opciones;

    /**
     * Constructor de la clase.
     * @param opciones Opciones de usuario que modifican el comportamiento del programa
     */
    public AutoUi(Map<String, String> opciones) {
        this.opciones = opciones;
    }

    @Override
    public void start() {

        Estudiante[] estudiantes = null;

        String formato = opciones.get("formato");
        if(formato == null) throw new RuntimeException("No hay definido formato de almacenamiento");

        Path ruta = Backend.generarRuta(opciones.getOrDefault("file", null), formato.toLowerCase());
        BackendFactory factory = new BackendFactory(formato);
        
        try {
            estudiantes = new Estudiante[] {
                factory.crearEstudiante().cargarDatos(
                    "Pedro",
                    "García Gavilán",
                    df.parse("20/12/2012"),
                    Estudios.SECUNDARIA
                ),
                factory.crearEstudiante().cargarDatos(
                    "María Asunción",
                    "Menéndez Valera",
                    df.parse("20/09/2009"),
                    Estudios.SECUNDARIA
                )
            };
        }
        catch(ParseException err) {
            err.printStackTrace();
            System.exit(1);
        }

        Backend backend = factory.crearBackend(ruta);

        try {
            System.out.println("Guardamos los estudiantes en un archivo...");
            EstudiantePersistente.save(backend, estudiantes);

            System.out.println("Y ahora los recuperamos para comparar");
            Estudiante[] estudiantesLeidos = EstudiantePersistente.read(backend, factory.getFormato().getTipoEstudiante());

            System.out.printf("Lista original: %s\n", Arrays.toString(estudiantes));
            System.out.printf("Lista recuperada: %s\n", Arrays.toString(estudiantesLeidos));
            System.out.printf("¿Son iguales ambas listas? %b\n", Arrays.equals(estudiantes, estudiantesLeidos));
        }
        catch(IOException err) {
            err.printStackTrace();
        }

    }
}
