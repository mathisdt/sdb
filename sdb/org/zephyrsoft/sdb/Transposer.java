package org.zephyrsoft.sdb;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.zephyrsoft.sdb.structure.*;

/**
 * GUI für das Transponieren eines Liedes der Lieder-Datenbank.<br>
 * Akkordformat: A7/F oder A7|9 oder A7.9 (der Slash "/" nur bei Bassnote).
 * @author Mathis Dirksen-Thedens
 */

public class Transposer extends JFrame {
	
	private GUI gui = null;
	private Structure structure = null;
	private int songid = -1;
	private Song song = null;
	
	JPanel contentPane = null;
	BoxLayout layout = null;
	JComboBox combobox = null;
	JButton ok = null;
	
	public Transposer(int songid, Structure structure, GUI gui) {
		super(Messages.getString("Transposer.0")); //$NON-NLS-1$
		
		this.songid = songid;
		this.structure = structure;
		this.gui = gui;
		
		this.song = structure.getSongByID(songid);
		
		// Aussehen
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		if ( lookAndFeel != null ) {
			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch(Exception e) {
				System.out.println(Messages.getString("Transposer.1")); //$NON-NLS-1$
			}
		}
		setIconImage((new ImageIcon("res/icon.gif")).getImage()); //$NON-NLS-1$
		
		// GUI-Elemente definieren
		contentPane = new JPanel();
		layout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
		String[] daten = { "+11", "+10", "+9", "+8", "+7", "+6", "+5", "+4", "+3", "+2", "+1", "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9", "-10", "-11" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$
		combobox = new JComboBox(daten);
		combobox.setEditable(false);
		combobox.setSelectedItem("+1"); //$NON-NLS-1$
		ok = new JButton(Messages.getString("Transposer.26")); //$NON-NLS-1$
		
		// GUI formen
	    this.setContentPane(contentPane);
	    contentPane.setLayout(layout);
	    contentPane.add(new JLabel(Messages.getString("Transposer.27"))); //$NON-NLS-1$
		contentPane.add(new JLabel(Messages.getString("Transposer.28"))); //$NON-NLS-1$
		contentPane.add(new JLabel(Messages.getString("Transposer.29"))); //$NON-NLS-1$
		contentPane.add(new JLabel(Messages.getString("Transposer.30"))); //$NON-NLS-1$
	    contentPane.add(combobox);
	    contentPane.add(ok);
	    this.setLocale(java.util.Locale.getDefault());
        pack();
        setSize(new Dimension(300, 150));
        setLocation(new Point(350, 200));
		
		// EVENT HANDLER START =======================================
	    ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submit();
			}
	    });
	    this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
	    });
		
		// EVENT HANDLER ENDE ========================================
		
        show();
	}
	
    // EVENT HANLDER HILFSMETHODEN:
    private final void submit() {
    	String str = (String)combobox.getSelectedItem();
    	if ( str.startsWith("+") ) { //$NON-NLS-1$
    		str = str.substring(1);
    	}
    	transposeSong(song, Integer.parseInt(str));
    	exit();
    }
	private final void exit() {
		gui.transposed();
		dispose();
	}
	
	private void transposeSong(Song song1, int halbtoene) {
		System.out.println(song1.getTitel() + Messages.getString("Transposer.32") + halbtoene + Messages.getString("Transposer.33")); //$NON-NLS-1$ //$NON-NLS-2$
		
		
	}
	
}
