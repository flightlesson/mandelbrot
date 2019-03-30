package com.strongblackcoffee.mandelbrot;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.math3.complex.Complex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class MandelbrotStatisticsPanel extends JPanel {
    static final Logger LOGGER = LogManager.getLogger();
    
    public interface Callback {
        public void maxIterationsChanged(int newMaxIterations);
    }
    
    public MandelbrotStatisticsPanel(final Callback callback) {
        super();
        this.add(configureTextField(centerXField,"center x",11, null));
        this.add(configureTextField(centerYField,"center y", 9, null));
        this.add(configureTextField(deltaField,  "delta", 9, null));
        this.add(configureTextField(maxIterField,"max iterations", 5, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                String changedToString = ((JTextField)e.getSource()).getText();
                LOGGER.debug("Max iterations changed: " + changedToString);
                try {
                    int newMaxIterations = Integer.parseInt(changedToString);
                    callback.maxIterationsChanged(newMaxIterations);
                } catch (NumberFormatException ex) {
                    LOGGER.warn(changedToString + ": " + ex.getLocalizedMessage());
                }
            }
        }));
    }
    
    private final JTextField centerXField = new JTextField();
    private final JTextField centerYField = new JTextField();
    private final JTextField deltaField = new JTextField();
    private final JTextField maxIterField = new JTextField();
    
    final JComponent configureTextField(JTextField textField, String textFieldLabel, int width, ActionListener actionListener) {
        textField.setEditable(actionListener != null);
        if (actionListener != null) {
            textField.addActionListener(actionListener);
        }
        textField.setFont(textField.getFont().deriveFont(11.0f));
        textField.setText("    ");
        textField.setColumns(width);
        JComponent panel = new Box(BoxLayout.Y_AXIS);
        JLabel label = new JLabel(textFieldLabel);
        label.setFont(label.getFont().deriveFont(9.0f));
        label.setLabelFor(textField);
        panel.add(label);
        panel.add(textField);
        return panel;
    }
    
    public void setMaxIterations(int maxIterations) {
        this.maxIterField.setText(String.valueOf(maxIterations));
    }
    
    public void setCenter(Complex point) {
        this.centerXField.setText(String.format("%g", point.getReal()));
        this.centerYField.setText(String.format("%g", point.getImaginary()));
    }
    
    public void setDelta(double delta) {
        this.deltaField.setText(String.format("%g",delta));
    }
    
}
