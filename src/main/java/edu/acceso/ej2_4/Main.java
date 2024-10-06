package edu.acceso.ej2_4;

import java.nio.file.Path;

/**
 * Programa principal.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Path archivo = Path.of(System.getProperty("java.io.tmpdir"), "estudiantes");
        //InterfazTexto.start(archivo);
        InterfazAutomatica.start(archivo);
    }
}