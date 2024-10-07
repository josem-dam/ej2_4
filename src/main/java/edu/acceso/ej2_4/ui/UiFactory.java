package edu.acceso.ej2_4.ui;

import java.util.Arrays;

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

    private String ui;

    /**
     * Constructor de la clase
     * @param ui Interfaz de usuario que se quiere usar.
     */
    public UiFactory(String ui) {
        setUi(ui);
    }

    private void setUi(String ui) {
        this.ui = ui.toLowerCase();
        switch(this.ui) {
            case "auto":
            case "text":
                break;
            default:
                if(Arrays.stream(uis).anyMatch(u -> u.toLowerCase().equals(this.ui))) {
                    throw new UnsupportedOperationException(ui + ": Interfaz de usuario no soportada.");
                }
                else {
                    throw new IllegalArgumentException(ui + ": Interfaz de usuario desconocida.");
                }
        }
    }

    /**
     * Crea una interfaz de usuario.
     * @return La interfaz de usuario.
     */
    public Ui crearInterfaz() {
        switch(ui) {
            case "auto":
                return new AutoUi();
            case "text":
                return new TextUi();
            default:
                assert false: "Interfaz de usuario imposible";
                return null;
        }
    }
    
}
