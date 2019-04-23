package com.strongblackcoffee.mandelbrot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.invoke.MethodHandles;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class ColorEditor extends JDialog implements ColorProvider {
    static final String SIMPLE_NAME = MethodHandles.lookup().lookupClass().getSimpleName();
    static final Logger LOGGER = LogManager.getLogger(SIMPLE_NAME);
    
    ColorEditor(Frame owner) {
        super(owner, "Colors", false);
        this.setContentPane(constructContentPane());
        this.setMinimumSize(new Dimension(100,100));
        this.pack();
    }
    
    Container constructContentPane() {
        final JPanel panel = new JPanel(new BorderLayout());

        JPanel colorPane= new JPanel() {
            protected void paintComponent(Graphics g) {
                paintColorPane(g);
            }
        };
        panel.add(colorPane,BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JButton dismissButton = new JButton("hide");
        dismissButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                ColorEditor.this.setVisible(false);
            }
        });
        controls.add(dismissButton);
        
        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }
    
    Color[] fadein = new Color[] {
        // ff - 0c = f3, f3 - 0c = e7 
      new Color(0xFFFFFF),
      new Color(0xF3F3F3),
      new Color(0xE7E7E7),
      new Color(0xDBDBDB),
      new Color(0xCFCFCF),
      new Color(0xC3C3C3),
      new Color(0xB7B7B7),
      new Color(0xABABAB),
    };
    
    Color[] colors = new Color[] {
        // Reds
        new Color(0xFFEFEF),
        new Color(0xFFE3E3),
        new Color(0xFFD7D7),
        new Color(0xFFCBCB),
        new Color(0xFFBFBF),
        new Color(0xFFB3B3),
        new Color(0xFFA7A7),
        new Color(0xFF9B9B),
        new Color(0xFF8F8F),
        new Color(0xFF8383),
        
        // Greens
        new Color(0xEFFFEF),
        new Color(0xDFFFDF),
        new Color(0xCFFECF),
        new Color(0xBFFFBF),
        new Color(0xAFFFAF),
        new Color(0x9FFF9F),
        new Color(0x8FFF8F),
        new Color(0x7FFF7F),
        
        new Color(0xEFEFFF),
        new Color(0xE3E3FF),
        new Color(0xD7D7FF),
        new Color(0xCBCBFF),
        new Color(0xBFBFFF),
        new Color(0xB3B3FF),
        new Color(0xA7A7FF),
        new Color(0x9B9BFF),
        new Color(0x8F8FFF),
        new Color(0x8383FF),
        
    };
    
    int[] colorMap;
    
    void paintColorPane(Graphics g) {
        LOGGER.info("paintColorPane: clip="+g.getClip()+", clipBounds="+g.getClipBounds());

        for (int i=0; i < fadein.length; ++i) {
            g.setColor(fadein[i]);
            g.fillRect((32*i),0,32,32);
        }
        
    }
    
    private void constructColorMap(int m) {
        colorMap = new int[m];
        int i=0;
        for (Color c: fadein) {
            if (i >= m) return;
            colorMap[i] = c.getRGB();
            ++i;
        }
        for (int scale=1; true; ++scale) {
            for (Color c: colors) {
                for (int j=0; j < scale; ++j) {
                    if (i >= m) return;
                    colorMap[i] = c.getRGB();
                    ++i;
                }
            }
        }
    }
    
    @Override
    public int getColor(int n, int maxColors) {
        
        if (n == 0) return 0;
        --n;
        
        if (colorMap == null || colorMap.length != maxColors) {
            constructColorMap(maxColors);
        }
        
        return colorMap[n];
        
    }
    
    
    
}
