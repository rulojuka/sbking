package br.com.sbk.sbking.gui.frames;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_COLOR;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;  
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.gui.constants.FrameConstants;
import br.com.sbk.sbking.gui.main.ClientApplicationState;
import br.com.sbk.sbking.gui.painters.Painter;
import br.com.sbk.sbking.networking.client.SBKingClient;


@SuppressWarnings("serial")
public abstract class NetworkClientScreen extends JFrame {

	final static Logger logger = LogManager.getLogger(NetworkClientScreen.class);

	protected boolean connectedToServer = false;
	protected SBKingClient sbKingClient;

	protected ExecutorService pool;

	public NetworkClientScreen() {
		super();
		initializeFrame();
		initializeContentPane(this);
		pool = Executors.newFixedThreadPool(4);
	}

	private void initializeFrame() {
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(FrameConstants.TABLE_WIDTH, FrameConstants.TABLE_HEIGHT);

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

	public String getIpFromServer(String server) {
		Map<String, String> nameToHostname = new HashMap<String, String>();
		nameToHostname.put("Local", "127.0.0.1");
		nameToHostname.put("Alejandro", "144.126.251.69");
		nameToHostname.put("Perez", "161.35.251.100");
		nameToHostname.put("Taís", "144.126.251.87");
		String hostname = nameToHostname.get(server);
		if (hostname != null) {
			return hostname;
		} else {
			return "127.0.0.1";
		}
	}

	protected void paintPainter(Painter painter) {
		this.getContentPane().removeAll();
		painter.paint(this.getContentPane());
		ClientApplicationState.setGUIHasChanged(false);
	}
}
