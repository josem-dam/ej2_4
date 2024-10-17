package edu.acceso.ej2_4;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Stream;

import edu.acceso.ej2_4.backend.Backend;

public class EstudiantePersistente {

    /**
     * Wrapper para almacenar estudiantes. Básicamente añade al array de estudiantes un estudiante
     * inicial ficticio que contiene el siguiente número de matrícula.
     * @param backend El objeto que permite escribir en archivo
     * @param estudiantes El array de estudiantes
     */
    public static void save(Backend backend, Estudiante[] estudiantes) throws IOException {
        if(estudiantes.length == 0) return;

        try {
            Estudiante falso = estudiantes[0].getClass().getDeclaredConstructor().newInstance();
            falso.cargarDatos(Estudiante.getSiguienteMatricula(), null, null, null, null);

            backend.save(Stream.concat(Arrays.stream(new Estudiante[] {falso}), Arrays.stream(estudiantes)).toArray(Estudiante[]::new));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            return;
        }
    }

    /**
     * Wrapper para leer estudiantes de archivo. Básicamente elimina el primer estudiante, que en realidad
     * es un estudiante ficticio que se usa para almacenar el siguiente número de matrícula.
     * @param backend El objeto que permite escribir en archivo
     * @param estudiantes El array de estudiantes
     */
    public static Estudiante[] read(Backend backend, Class<? extends Estudiante> tipo) throws IOException {
        Estudiante[] estudiantesLeidos = backend.read(tipo);

        // Usamos el primer estudiante para restutir el valor de siguienteMatricula
        Estudiante falso = estudiantesLeidos[0];
        new Estudiante().cargarDatos(falso.getMatricula(), null, null, null, null);

        return Arrays.copyOfRange(estudiantesLeidos, 1, estudiantesLeidos.length);
    }
}
