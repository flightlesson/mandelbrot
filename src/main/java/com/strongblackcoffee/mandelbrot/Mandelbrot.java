package com.strongblackcoffee.mandelbrot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import javax.swing.Box;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The application frame and menus.
 */
public class Mandelbrot extends JFrame {
    static final String SIMPLE_NAME = MethodHandles.lookup().lookupClass().getSimpleName();
    static final Logger LOGGER = LogManager.getLogger(SIMPLE_NAME);
    
    static private final String USAGE = "$app [options[]";
    static private final String HEADER = "Mandelbrot and Julia set viewer.\nOptions are:";
    static private final String FOOTER = "\n"
            +"\nProvides views into the Mandelbrot and Julia sets."
            +"\n"
            +"\nFor example, these lead to interesting movies"
            +"\n  -c 0.39728754255254+0.13503122419329i -z 0.99 -m 5000"
            +"\n";
    static private final Options OPTIONS;
    static {
        OPTIONS = new Options();
        OPTIONS.addOption("h","help",false,"Print this message.");
        OPTIONS.addOption("c","center",true,"Initial center point. [(-0.2,0)]");
        OPTIONS.addOption("z","zoom-factor",true,"Initial zoom factor. [1.2]");
        OPTIONS.addOption("m","movie-mode",true,"Movie mode, specifies the number of frames to create. [1]");
        
        // Add application specific options here.
    }
    
    static public void main(String[] args) {
        try {
            CommandLine cmdline = (new DefaultParser()).parse(OPTIONS,args);
            if (cmdline.hasOption("help")) {
                (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
                System.exit(1);
            }
            
            BigComplex center = new BigComplex(BigDecimal.valueOf(-0.2),BigDecimal.ZERO);
            if (cmdline.hasOption("center")) {
                try {
                    //center = ComplexFormat.getInstance().parse(cmdline.getOptionValue("center"));
                    String[] s = cmdline.getOptionValue("center").split(",");
                    center = new BigComplex(new BigDecimal(s[0]),new BigDecimal(s[1]));
                } catch (Exception ex) {
                    LOGGER.warn("-center " + cmdline.getOptionValue("center") + ": " + ex.getLocalizedMessage());
                }
            }
            
            double zoomFactor = 0.8; // 0.99;
            if (cmdline.hasOption("zoom-factor")) {
                try {
                    zoomFactor = Double.parseDouble(cmdline.getOptionValue("zoom-factor"));
                } catch (NumberFormatException ex) {
                    LOGGER.warn("-zoom-factor "+ cmdline.getOptionValue("zoom-factor") + ": " + ex.getLocalizedMessage());
                }
            }
            
            if (cmdline.hasOption("movie-mode")) {
                
            }
            
            
        
            Mandelbrot application = new Mandelbrot(center, zoomFactor);
        } catch (ParseException ex) {
            // can't use logger; it's not configured
            System.err.println(ex.getMessage());
            (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
        }
    }
    
    public Mandelbrot(BigComplex center, double zoomFactor) {
        super("Mandelbrot");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setJMenuBar(constructMenuBar());
        this.setMinimumSize(new Dimension(600,600));
        this.colorEditor = new ColorEditor(this);
        // WAS {
        //this.statisticsPane = new MandelbrotStatisticsPanel();
        //this.mandelbrotPane = new MandelbrotPane((ColorProvider)colorEditor, this.statisticsPane);
        // } IS {
        this.mandelbrotPane = new MandelbrotPane((ColorProvider) colorEditor, center, 0.009, 100, zoomFactor);
        this.statisticsPane = this.mandelbrotPane.getStatisticsPane();
        // }
        LOGGER.info("got mandelbrotPane");
        JPanel pane = new JPanel(new BorderLayout());
        pane.add(this.mandelbrotPane, BorderLayout.CENTER);
        pane.add(statisticsPane, BorderLayout.SOUTH);
        this.setContentPane(pane);
        LOGGER.info("Mandelbrot pack()");
        pack();
        LOGGER.info("Mandelbrot constructing setVisible()");
        setVisible(true);
    }
    
    private final ColorEditor colorEditor;
    private final MandelbrotStatisticsPanel statisticsPane;
    private final MandelbrotPane mandelbrotPane;
    
    final JMenuBar constructMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu;
        JMenuItem menuItem;
        
        menu = new JMenu("File");

        menuItem = new JMenuItem("Quit");
        menuItem.setAccelerator(KeyStroke.getKeyStroke('Q',InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(menuItem);

        menuBar.add(menu);
        
        menu = new JMenu("View");

        menuItem = new JMenuItem("Show Color Editor");
        menuItem.setEnabled(false);
        menuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                LOGGER.info("Show Color Editor");
                Mandelbrot.this.colorEditor.setVisible(true);
            }
        });
        menu.add(menuItem);

        // The distance histogram shows how many pixels are at
        // each distance, or number of iterations, from the set.
        menuItem = new JMenuItem("Show Distance Histogram");
        menuItem.setEnabled(false);
        menuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                LOGGER.info("Show Distance Histogram");
            }
        });
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Zoom in");
        menuItem.setAccelerator(KeyStroke.getKeyStroke('Z',InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                LOGGER.info("Zoom in");
                mandelbrotPane.zoomIn();
            }
        });
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Zoom out");
        menuItem.setAccelerator(KeyStroke.getKeyStroke('Z',InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                LOGGER.info("Zoom out");
                mandelbrotPane.zoomOut();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Reset");
        menuItem.setAccelerator(KeyStroke.getKeyStroke('R',InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                mandelbrotPane.reset();
            }
        });
        menu.add(menuItem);
        
        menuBar.add(menu);
        
        menuBar.add(Box.createHorizontalGlue());
        
        menu = new JMenu("Help");
        menuItem = new JMenuItem("About", KeyEvent.VK_A);
        menuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                showAboutPopup();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        
        return menuBar;
    }
    
    void showAboutPopup() {
        LOGGER.debug("Help => About was clicked");
        String aboutMessage;
        try {
            aboutMessage = IOUtils.resourceToString("about.html", StandardCharsets.US_ASCII, getClass().getClassLoader());
        } catch (IOException ex) {
            LOGGER.warn(ex.getLocalizedMessage());
            aboutMessage = ex.getLocalizedMessage();
        }
        JEditorPane pane = new JEditorPane("text/html", aboutMessage);
        pane.setEditable(false);
        pane.setEnabled(true);
        pane.setBackground(this.getContentPane().getBackground());
        JOptionPane.showMessageDialog(this.rootPane, pane, "about this application", JOptionPane.INFORMATION_MESSAGE);
    }
}
