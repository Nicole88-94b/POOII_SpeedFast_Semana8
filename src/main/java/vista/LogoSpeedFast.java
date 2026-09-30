package vista;

import  javax.swing.*;
import  java.awt.*;

/**
 * Prepara el logotipo utilizado en la ventana principal.
 */
public class LogoSpeedFast {
    /**
     * Carga la imagen de recursos y la ajusta al tamaño definido para la interfaz.
     *
     * @return etiqueta que contiene el logotipo escalado
     */
    public JLabel crearLabelSpeedFast() {
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/vista/SpeedFast_logo.png"));

        Image imagen = iconoOriginal.getImage();
        Image imagenEscalada = imagen.getScaledInstance(220, 140, Image.SCALE_SMOOTH);

        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

        return new JLabel(iconoEscalado);
    }
}
