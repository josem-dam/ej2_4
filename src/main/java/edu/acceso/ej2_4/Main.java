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
import edu.acceso.ej2_4.ui.Ui;
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
                                .longOpt("interface")
                                .argName("interface")
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
                if(Arrays.stream(BackendFactory.formatos).anyMatch(f ->  f.toLowerCase().equals(formato.toLowerCase()))) {
                    throw new ParseException(formato + ": Formato desconocido");
                }
                else {
                    opciones.put("formato", formato);
                }
            }

            if(cmd.hasOption("interface")) {
                String interf = cmd.getOptionValue("interface");
                if(!Arrays.stream(UiFactory.uis).anyMatch(ui ->  ui.toLowerCase().equals(interf.toLowerCase()))) {
                    throw new ParseException(interf + ": Inerfaz de usuario desconocida");
                }
                opciones.put("interface", interf);
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

        System.out.println(BackendFactory.formatos);

        // Interfaz por defecto: la automática
        UiFactory uiFactory = new UiFactory(opciones.getOrDefault("interface", "auto"));
        Ui ui = uiFactory.crearInterfaz();
        ui.start(opciones);
    }
}