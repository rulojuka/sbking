package br.com.sbk.sbking.gui.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.gui.constants.FrameConstants;

public class ClientApplicationState {

    final static Logger logger = LogManager.getLogger(ClientApplicationState.class);
    private static boolean guiHasChanged = false;
    private static boolean guiScaleValid = true;


    public static void setGUIHasChanged(boolean changed) {
        guiHasChanged = changed;
    }

    public static boolean getGUIHasChanged() {
        return guiHasChanged;
    }

    public static void invalidateGUIScale() {
        guiScaleValid = false;
    }

    public static void checkWindowResize(int newWidth, int newHeight) {
        if (!guiScaleValid) {
            resizeWindow(newWidth, newHeight);  
        }

        guiScaleValid = true;
    }

    public static void startAppState() {
        logger.info("Initializing Frame Constants");
        
        FrameConstants.initFrameConstants();

        // AssetLoader must be initialized after FrameConstants.
        AssetLoader.initAssetLoader();
    }

    public static void resizeWindow(int width, int height) {
        logger.info("Resizing Window");

        FrameConstants.computeConstants(width, height);
        
        AssetLoader.invalidateAssetLoaderCache();

        ClientApplicationState.setGUIHasChanged(true);
    }

}
