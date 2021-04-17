package br.com.sbk.sbking.gui.frames;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_COLOR;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

import br.com.sbk.sbking.gui.constants.FrameConstants;
import br.com.sbk.sbking.gui.main.ClientApplicationState;
import br.com.sbk.sbking.gui.painters.Painter;

@SuppressWarnings("serial")
public class SBKingClientJFrame extends JFrame {

    public SBKingClientJFrame() {
        super();
        initializeFrame();
        initializeContentPane(this);
    }

    private void initializeFrame() {
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FrameConstants.tableWidth, FrameConstants.tableHeight);

        this.setApplicationIcon();
    }

    private void initializeContentPane(SBKingClientJFrame screen) {
        getContentPane().setLayout(null);
        getContentPane().setBackground(TABLE_COLOR);

        Timer resizeDebounceTimer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ClientApplicationState.checkWindowResize(screen.getWidth(), screen.getHeight());
            }
        });
        resizeDebounceTimer.setRepeats(false);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                ClientApplicationState.invalidateGUIScale();
                resizeDebounceTimer.restart();
            }
        });
    }

    private void setApplicationIcon() {
        String imagePath = "/images/logo/logo.jpg";
        URL logoUrl = getClass().getResource(imagePath);

        // Prevents an application crash in case image is non-existent.
        if (logoUrl != null) {
            ImageIcon img = new ImageIcon(logoUrl);
            this.setIconImage(img.getImage());
        }
    }

    public void paintPainter(Painter painter) {
        this.getContentPane().removeAll();
        painter.paint(this.getContentPane());
        ClientApplicationState.setGUIHasChanged(false);
    }

}
