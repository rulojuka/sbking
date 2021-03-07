package br.com.sbk.sbking.gui.frames;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_COLOR;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.gui.constants.FrameConstants;
import br.com.sbk.sbking.networking.client.SBKingClient;

@SuppressWarnings("serial")
public abstract class NetworkClientScreen extends JFrame {

	final static Logger logger = LogManager.getLogger(NetworkClientScreen.class);

	protected boolean connectedToServer = false;
	protected SBKingClient sbKingClient;

	protected ExecutorService pool;

	public NetworkClientScreen() {
		super();
		initializeJFrame();
		initializeContentPane();
		pool = Executors.newFixedThreadPool(4);
	}

	private void initializeJFrame() {
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(TABLE_WIDTH, TABLE_HEIGHT);

		this.setApplicationIcon();
	}

	private void initializeContentPane() {
		getContentPane().setLayout(null);
		getContentPane().setBackground(TABLE_COLOR);
		NetworkClientScreen screen = this;
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				// Recompute frame constants.
				FrameConstants.computeConstants(screen.getWidth(), screen.getHeight());

				// Connecting screen has no sbKingClient to store the GUI invalidation flag.
				if (sbKingClient != null) {
					// Invalidating the client's GUI flag provekes a Pane repaint on the main loop.
					sbKingClient.setGUIHasChanged(true);
				}
			}
		});
	}

	public abstract void run();

	public void connectToServer(String nickname, String hostname) {
		hostname = hostname.trim();
		if (isValidIP(hostname)) {
			this.sbKingClient = new SBKingClient(nickname, hostname);
			this.connectedToServer = true;
			pool.execute(this.sbKingClient);
		} else {
			logger.error("Invalid IP");
		}
	}

	private boolean isValidIP(String ipAddr) {
		Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
		Matcher mtch = ptn.matcher(ipAddr);
		return mtch.find();
	}

	private void setApplicationIcon() {
		String imagePath = "/images/logo/logo.jpg";
		URL logo_url = getClass().getResource(imagePath);

		// Prevents an application crash in case image is non-existent.
		if (logo_url != null) {
			ImageIcon img = new ImageIcon(logo_url);
			this.setIconImage(img.getImage());
		}
	}
}
