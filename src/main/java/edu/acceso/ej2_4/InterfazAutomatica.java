package edu.acceso.ej2_4;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import edu.acceso.ej2_4.backend.Backend;
import edu.acceso.ej2_4.backend.Factory;

public class InterfazAutomatica {

    private static Factory factory;
    private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public static void start(Path ruta) {

        String formato = "csv";

        factory = new Factory(formato);
        ruta = InterfazTexto.calcularRuta(ruta, formato);
        Estudiante[] estudiantes = new Estudiante[] {
            factory.crearEstudiante(),
            factory.crearEstudiante()
        };

        try {
            estudiantes[0].cargarDatos(
                "Pedro",
                "García Gavilán",
                df.parse("20/12/2012"),
                Estudios.SECUNDARIA
            );

            estudiantes[1].cargarDatos(
                "María Asunción",
                "Menéndez Valera",
                df.parse("20/09/2009"),
                Estudios.SECUNDARIA
            );
        }
        catch(ParseException err) {
            err.printStackTrace();
            System.exit(1);
        }

        Backend backend = factory.crearBackend(ruta);

        try {
            System.out.println("Guardamos los estudiantes en un archivo...");
            backend.save(estudiantes);
            System.out.println("Y ahora los recuperamos para comparar");
            Estudiante[] estudiantesLeidos = (Estudiante[]) backend.load();
            System.out.printf("Lista original: %s\n", Arrays.toString(estudiantes));
            System.out.printf("Lista recuperada: %s\n", Arrays.toString(estudiantesLeidos));
            System.out.printf("¿Son iguales ambas listas? %b\n", Arrays.equals(estudiantes, estudiantesLeidos));
        }
        catch(IOException err) {
            err.printStackTrace();
        }

    }
}
