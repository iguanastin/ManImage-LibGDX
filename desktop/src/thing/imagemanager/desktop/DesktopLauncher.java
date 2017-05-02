package thing.imagemanager.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.badlogic.gdx.graphics.Color;
import java.awt.Toolkit;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import thing.imagemanager.GameMain;

public class DesktopLauncher {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SetupFrame().setVisible(true);
            }
        });
    }

    public static void launchManImage(String sourceFolderPath) {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.backgroundFPS = 0;
        config.foregroundFPS = 60;
        config.fullscreen = false;
        config.initialBackgroundColor = Color.BLACK;
        config.resizable = true;
        config.title = "ManImage - Image Management Application";
        config.vSyncEnabled = true;
        config.width = width - 400;
        config.height = height - 200;
        LwjglFrame frame = new LwjglFrame(new GameMain(sourceFolderPath), config);
        frame.setExtendedState(LwjglFrame.MAXIMIZED_BOTH);
    }
}
