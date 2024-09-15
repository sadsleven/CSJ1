/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

/**
 *
 * @author abrah
 */
import javax.swing.JDialog;
import javax.swing.JTextArea;

public class SaidaFrame extends JDialog {
    public JTextArea texto;

    public SaidaFrame() {
        // Define the dimensions of the window
        setBounds(100, 100, 400, 300);

        // Set the modal status to true
        setModal(false);

        // Create a JTextArea and add it to the window
        texto = new JTextArea();
        add(texto);
    }
}
