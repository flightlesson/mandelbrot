package com.strongblackcoffee.mandelbrot;

import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class MandelbrotStatisticsPanel extends JPanel implements MandelbrotStatisticsCollector {
    static final Logger LOGGER = LogManager.getLogger();
    
    public MandelbrotStatisticsPanel() {
        super();
        this.add(configureTextField(imageSizeField,"image size",11, false));
        this.add(configureTextField(deltaXField,"delta x", 9, false));
        this.add(configureTextField(deltaYField,"delta y", 9, false));
        this.add(configureTextField(currentPointField,"point", 20, false));
        this.add(configureTextField(currentPointDistanceField, "point's distance", 5, false));
        this.add(configureTextField(maxIterationsField,"max iterations", 5, true));
        recordImageSize(0,0);
    }
    
    private final JTextField imageSizeField = new JTextField();
    private final JTextField deltaXField = new JTextField();
    private final JTextField deltaYField = new JTextField();
    private final JTextField currentPointField = new JTextField();
    private final JTextField currentPointDistanceField = new JTextField();
    private final JTextField maxIterationsField = new JTextField();
    
    final JComponent configureTextField(JTextField textField, String textFieldLabel, int width, boolean isEditable) {
        textField.setEditable(isEditable);
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
    
    @Override
    public void recordImageSize(int width, int height) {
        //LOGGER.info("recordImageSize width="+width+", height="+height+" => "+s);
        if (width > 0 && height > 0) {
            String s = ""+width+" X "+height;
            imageSizeField.setText(s);
        }
    }
    
    @Override
    public void recordDeltas(double deltaX, double deltaY) {
        //LOGGER.info("recordDeltas");

        deltaXField.setText(String.format("%g",deltaX));
        deltaYField.setText(String.format("%g",deltaY));
    }
    
    @Override
    public void recordCurrentPoint(MandelbrotSet.Cell cell) { // double a, double b, int distance) {
        //LOGGER.info("setCurrentPoint cell="+cell);
        String s = String.format("%.10g",cell.a)+ (cell.b >= 0 ? "+" : "") + String.format("%.10g",cell.b) + "i";
        //LOGGER.info("... currentPointField="+currentPointField+", s="+s);
        currentPointField.setText(s);
        currentPointDistanceField.setText(""+cell.distance);
    }
    
    @Override
    public void recordMaxIterations(int maxIterations) {
        this.maxIterationsField.setText(""+maxIterations);
    }
    
}
