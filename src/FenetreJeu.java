import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class FenetreJeu extends JFrame implements WindowListener {
    // Attributs
    //======================================================================
    FenetreMenu fenetreMenu;
    protected PanelJeu mainPanel;

    // Constructeur
    //======================================================================
    public FenetreJeu(FenetreMenu fenetreMenu, boolean playMusic, int indice){
        super("Vise le trou");

        this.fenetreMenu = fenetreMenu;

        mainPanel = new PanelJeu(indice);
        mainPanel.setFocusable(true); // Sinon la KeyListener interface ne marche pas
        add(mainPanel);
        if(!playMusic) mainPanel.musique.clip.stop();

        addWindowListener(this);
        setSize(mainPanel.background.getWidth(null),mainPanel.background.getHeight(null));
        setLocation(350, 75);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    /**
     * Invoked the first time a window is made visible.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * Invoked when the user attempts to close the window
     * from the window's system menu.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowClosing(WindowEvent e) {

    }

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowClosed(WindowEvent e) {

        //Arrêt de la musique et de l'animation de la fenêtre jeu
        mainPanel.musique.clip.stop();
        mainPanel.gameLoopTimer.stop();

        //Rendre visible la fenêtre menu, relancer la musique et les animations
        fenetreMenu.setVisible(true);
        fenetreMenu.mainPanel.musique.clip.setMicrosecondPosition(0); // Recommence la musique au début
        fenetreMenu.mainPanel.musique.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Invoked when a window is changed from a normal to a
     * minimized state. For many platforms, a minimized window
     * is displayed as the icon specified in the window's
     * iconImage property.
     *
     * @param e the event to be processed
     * @see Frame#setIconImage
     */
    @Override
    public void windowIconified(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a minimized
     * to a normal state.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    /**
     * Invoked when the Window is set to be the active Window. Only a Frame or
     * a Dialog can be the active Window. The native windowing system may
     * denote the active Window or its children with special decorations, such
     * as a highlighted title bar. The active Window is always either the
     * focused Window, or the first Frame or Dialog that is an owner of the
     * focused Window.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowActivated(WindowEvent e) {

    }

    /**
     * Invoked when a Window is no longer the active Window. Only a Frame or a
     * Dialog can be the active Window. The native windowing system may denote
     * the active Window or its children with special decorations, such as a
     * highlighted title bar. The active Window is always either the focused
     * Window, or the first Frame or Dialog that is an owner of the focused
     * Window.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}