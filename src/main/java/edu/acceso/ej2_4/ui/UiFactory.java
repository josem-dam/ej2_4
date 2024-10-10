package edu.acceso.ej2_4.ui;

import java.util.Arrays;
import java.util.Map;

import edu.acceso.ej2_4.ui.auto.AutoUi;
import edu.acceso.ej2_4.ui.texto.TextUi;

/**
 * Implementa el patrón Factory para seleccionar la clase apropiada
 * que genera la interfaz de usuario.
 */
public class UiFactory {

    /**
     * Lista de interfaces de usuario disponibles.
     */
    public final static String[] uis = new String[] {"auto", "text"};

    private Map<String, String> opciones;

    /**
     * Constructor de la clase
     * @param opciones Opciones de usuario que modifican el comportamiento del programa.
     */
    public UiFactory(Map<String, String> opciones) {
        setOpciones(opciones);
    }

    private void setOpciones(Map<String, String> opciones) {
        String ui = opciones.get("ui");
        opciones.put("ui", ui.toLowerCase());

        switch(opciones.get("ui")) {
            case "auto":
            case "text":
                break;
            default:
                if(Arrays.stream(uis).anyMatch(u -> u.toLowerCase().equals(opciones.get("ui")))) {
                    throw new UnsupportedOperationException(ui + ": Interfaz de usuario no soportada.");
                }
                else {
                    throw new IllegalArgumentException(ui + ": Interfaz de usuario desconocida.");
                }
        }

        this.opciones = opciones;
    }

    /**
     * Crea una interfaz de usuario.
     * @return La interfaz de usuario.
     */
    public Ui crearInterfaz() {
        switch(opciones.get("ui")) {
            case "auto":
                return new AutoUi(opciones);
            case "text":
                return new TextUi(opciones);
            default:
                assert false: "Interfaz de usuario imposible";
                return null;
        }
    }
    
}
