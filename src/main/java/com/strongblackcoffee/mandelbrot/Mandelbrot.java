package com.strongblackcoffee.mandelbrot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The application frame and menus.
 */
public class Mandelbrot extends JFrame {
    static final Logger LOGGER = LogManager.getLogger();
    
    static private final String USAGE = "replace this with stuff that will appear after 'usage:'";
    static private final String HEADER = "Replace this with a brief summary describing what the application does.\nOptions are:";
    static private final String FOOTER = "\n"
            +"Replace this with a longer description of the application "
            +"that answers 'what does this do?' and 'why should I use it?'.\n"
            +"\n"
            +"I like to use a blank line to separate paragraphs.\n"
            +"\n";
    static private final Options OPTIONS;
    static {
        OPTIONS = new Options();
        OPTIONS.addOption("h","help",false,"Print this message.");
        OPTIONS.addOption(null,"longopt",false,"Example of an option that only has a long name.");
        // Add application specific options here.
    }
    
    static public void main(String[] args) {
        try {
            CommandLine cmdline = (new DefaultParser()).parse(OPTIONS,args);
            if (cmdline.hasOption("help")) {
                (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
                System.exit(1);
            }
        
            Mandelbrot application = new Mandelbrot();
        } catch (ParseException ex) {
            // can't use logger; it's not configured
            System.err.println(ex.getMessage());
            (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
        }
    }
    
    public Mandelbrot() {
        super("Mandelbrot");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setJMenuBar(constructMenuBar());
        this.setMinimumSize(new Dimension(600,600));
        this.colorEditor = new ColorEditor(this);
        this.statisticsPane = new MandelbrotStatisticsPanel();
        this.mandelbrotPane = new MandelbrotPane((ColorProvider)colorEditor, (MandelbrotStatisticsCollector) this.statisticsPane, this.stretchingIsEnabled);
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
    
    /**
     * When the aspect ratio is 1to1, the view's deltaX and deltaY must be the same.
     */
    private boolean stretchingIsEnabled;

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

        this.stretchingIsEnabled = false;
        menuItem = new JMenuItem("Enable Stretching");
        menuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                JMenuItem item = (JMenuItem) e.getSource();
                LOGGER.info("Toggle Aspect Ratio,  stretchingIsEnabled="+Mandelbrot.this.stretchingIsEnabled
                            +", text="+item.getText());
                if (Mandelbrot.this.stretchingIsEnabled) {
                    Mandelbrot.this.stretchingIsEnabled = false;
                    Mandelbrot.this.mandelbrotPane.setAllowStretching(false);
                    item.setText("Enable Stretching");
                } else {
                    Mandelbrot.this.stretchingIsEnabled = true;
                    Mandelbrot.this.mandelbrotPane.setAllowStretching(true);
                    item.setText("Maintain Perspective");
                }
            }
        });
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Show Color Editor");
        menuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                LOGGER.info("Show Color Editor");
                Mandelbrot.this.colorEditor.setVisible(true);
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Show Distance Histogram");
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
            }
        });
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Zoom out");
        menuItem.setAccelerator(KeyStroke.getKeyStroke('Z',InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                LOGGER.info("Zoom out");
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Reset");
        menuItem.setAccelerator(KeyStroke.getKeyStroke('R',InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                mandelbrotPane.resetClipping();
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
