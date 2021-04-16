package br.com.sbk.sbking.gui.frames;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_COLOR;
import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

import br.com.sbk.sbking.gui.constants.FrameConstants;
import br.com.sbk.sbking.gui.main.ClientApplicationState;
import br.com.sbk.sbking.gui.painters.ConnectToServerPainter;
import br.com.sbk.sbking.gui.painters.Painter;
import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.client.SBKingClientFactory;
import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;

@SuppressWarnings("serial")
public abstract class NetworkClientScreen extends JFrame {

    protected boolean connectedToServer = false;
    protected SBKingClient sbKingClient;
    private NetworkingProperties networkingProperties;

    public NetworkClientScreen() {
        super();
        initializeFrame();
        initializeContentPane(this);
        this.networkingProperties = new NetworkingProperties(new FileProperties(), new SystemProperties());
    }

    private void initializeFrame() {
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FrameConstants.tableWidth, FrameConstants.tableHeight);

        this.setApplicationIcon();
    }

    private void initializeContentPane(NetworkClientScreen screen) {
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

    public abstract void run();

    public void connectToServer(String nickname, String hostname) {
        hostname = hostname.trim();
        if (isValidIP(hostname)) {
            int port = networkingProperties.getPort();
            this.sbKingClient = SBKingClientFactory.createWithKryonetConnection(nickname, hostname, port);
            this.connectedToServer = true;
        } else {
            LOGGER.error("Invalid IP");
        }
    }

    private boolean isValidIP(String ipAddr) {
        Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher mtch = ptn.matcher(ipAddr);
        return mtch.find();
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

    public String getIpFromServer() {
        return this.networkingProperties.getHost();
    }

    protected void paintPainter(Painter painter) {
        this.getContentPane().removeAll();
        painter.paint(this.getContentPane());
        ClientApplicationState.setGUIHasChanged(false);
    }

    protected void paintConnectToServerScreen() {
        Painter connectToServerPainter = new ConnectToServerPainter(this);
        this.paintPainter(connectToServerPainter);
    }
}
