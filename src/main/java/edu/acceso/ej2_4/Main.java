package edu.acceso.ej2_4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.acceso.ej2_4.backend.BackendFactory;
import edu.acceso.ej2_4.ui.UiFactory;

/**
 * Programa principal.
 */
public class Main {

    /**
     * Obtiene los argumentos de la línea de comandos.
     * @param args Los argumentos suministrados por el usuario.
     * @return Un mapa con los valores de las opciones.
     */
    private static Map<String, String> leerArgumentos(String[] args) {
        Options options = new Options();
        Option interfaz = Option.builder("i")
                                .longOpt("ui")
                                .argName("ui")
                                .hasArg()
                                .desc("Establece la interfaz de usuario")
                                .build();
        options.addOption(interfaz);
        Option backend = Option.builder("b")
                               .longOpt("backend")
                               .argName("backend")
                               .hasArg()
                               .desc("Define el formato de almacenamiento")
                               .build();
        options.addOption(backend);
        Option archivo = Option.builder("f")
                               .longOpt("file")
                               .argName("file")
                               .hasArg()
                               .desc("Ruta del archivo de almacenamiento")
                               .build();
        options.addOption(archivo);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter helper = new HelpFormatter();
        Map<String,String> opciones = new HashMap<>();

        try {
            CommandLine cmd = parser.parse(options, args);

            if(cmd.hasOption("backend")) {
                String formato = cmd.getOptionValue("backend");
                if(BackendFactory.Formato.getFormato(formato) == null) {
                    throw new ParseException(formato + ": Formato desconocido");
                }
                else {
                    opciones.put("formato", formato);
                }
            }

            if(cmd.hasOption("ui")) {
                String interf = cmd.getOptionValue("ui");
                if(!Arrays.stream(UiFactory.uis).anyMatch(ui ->  ui.toLowerCase().equals(interf.toLowerCase()))) {
                    throw new ParseException(interf + ": Inerfaz de usuario desconocida");
                }
                opciones.put("ui", interf);
            }

            if(cmd.hasOption("file")) {
                opciones.put("file", cmd.getOptionValue("file"));
            }

        }
        catch(ParseException err) {
            err.printStackTrace();
            System.exit(2);
        }

        return opciones;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> opciones = leerArgumentos(args);

        // Interfaz por defecto: automática y formato csv.
        if(!opciones.containsKey("ui")) opciones.put("ui", "auto");
        if(!opciones.containsKey("formato") && opciones.get("ui") == "auto") {
            opciones.put("formato", "yaml");
        }

        UiFactory uiFactory = new UiFactory(opciones);
        uiFactory.crearInterfaz().start();
    }
}