package org.zephyrsoft.sdb;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.swing.text.rtf.*;

import org.zephyrsoft.sdb.dnd.*;
import org.zephyrsoft.sdb.structure.*;
import org.zephyrsoft.util.*;

/**
 * GUI für die Lieder-Datenbank
 * @author   Mathis Dirksen-Thedens
 */

public class GUI extends JFrame {

	public static final String VERSION = "1.15"; //$NON-NLS-1$
	public static final String DATE = "22.03.2006"; //$NON-NLS-1$
	
	public static final String SEPARATOR = "###"; //$NON-NLS-1$
	
	// falls FALSE, wird bei ungespeicherter DB nachgefragt:
	private boolean debug = false;
	
	private Options options = null;
	private static String options_filename = "SDB.options"; //$NON-NLS-1$
	
	public KeyListener globalKeyListener = null;
	
	// für den Ausdruck definiert:
	Font titelfont = new Font("Arial", Font.BOLD, 22); //$NON-NLS-1$
	Font textfont = new Font("Arial", Font.PLAIN, 16); //$NON-NLS-1$
	Font translatefont = new Font("Arial", Font.ITALIC, 14); //$NON-NLS-1$
	Font copyrightfont = new Font("Arial", Font.PLAIN, 8); //$NON-NLS-1$
	
	/** Description of the Field */
	protected BeamerView beamerview = null;
	/** Description of the Field */
	protected BeamerGUI beamergui = null;
	
	private Structure structure = null;
	private String filename = ""; //$NON-NLS-1$
	private int previouslySelectedIndex = -1;
	private Song prevSong = null;
	String[] table_column_names = {Messages.getString("GUI.8"), Messages.getString("GUI.9"), Messages.getString("GUI.10"), "ID"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	private String titelFilter = ""; //$NON-NLS-1$
	private String textFilter = ""; //$NON-NLS-1$
	// für store und load:
	private boolean saved = true;
	
	// für das Speichern der .sdb Datei:
	private boolean dbaltered = false;
	
	// DND
	private DGListener dglistener;
	private DSListener dslistener;
	private DragSource dragsource;
	
	JMenuBar menubar = null;
	JMenu menu_file = null;
	JMenuItem menuitem_file_new = null;
	JMenuItem menuitem_file_open = null;
	JMenuItem menuitem_file_save = null;
	JMenuItem menuitem_file_saveas = null;
	JMenuItem menuitem_file_options = null;
	JMenuItem menuitem_file_version = null;
	JMenuItem menuitem_file_exit = null;
	JMenu menu_view = null;
	JMenuItem menuitem_view_beamer = null;
	JMenuItem menuitem_view_print = null;
	JMenuItem menuitem_view_beamerpres = null;
	JMenu menu_song = null;
	JMenuItem menuitem_song_new = null;
	JMenuItem menuitem_song_newfromclipboard = null;
	JMenuItem menuitem_song_newfromRTF = null;
	JMenuItem menuitem_song_newfromRTFdir = null;
	JMenuItem menuitem_song_delete = null;
	JMenuItem menuitem_song_copy = null;
	JMenuItem menuitem_song_transpose = null;
	JMenu menu_lang = null;
	JMenuItem menuitem_lang_de = null;
	JMenuItem menuitem_lang_en = null;
	JPanel contentPane = null;
	BorderLayout borderLayout = null;
	JTabbedPane tabbedPane = null;
	JPanel allgemein_panel = null;
	JPanel musik_panel = null;
	JPanel rest_panel = null;
	JSplitPane jSplitPane1 = null;
	JScrollPane scrollPane1 = null;
	JScrollPane scrollPane2 = null;
	JScrollPane scrollPane3 = null;
	JTextPane text = null;
	protected JTable table = null;
	DefaultTableModel tablemodel = null;
	ListSelectionModel tableselectionmodel = null;
	JTextField titel = null;
	JComboBox sprache = null;
	JTextField tonart = null;
	JTextArea copyright = null;
	JTextField bemerkungen = null;
	JPanel such_panel = null;
	JTextField suche_text = null;
	JTextField suche_titel = null;
	
	
	/**
	 * Constructor for the GUI object
	 *
	 * @param arg  Description of the Parameter
	 */
	public GUI(String arg) {
		super(Messages.getString("GUI.14")); //$NON-NLS-1$
		
		// Aussehen
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		if (lookAndFeel != null) {
			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (Exception e) {
				System.out.println("Look&Feel konnte nicht gesetzt werden."); //$NON-NLS-1$
			}
		}
		setIconImage((new ImageIcon("res/icon.gif")).getImage()); //$NON-NLS-1$
		
		// Menü definieren
		menubar = new JMenuBar();
		menu_file = new JMenu(Messages.getString("GUI.17")); //$NON-NLS-1$
		menuitem_file_new = new JMenuItem(Messages.getString("GUI.18")); //$NON-NLS-1$
		menuitem_file_open = new JMenuItem(Messages.getString("GUI.19")); //$NON-NLS-1$
		menuitem_file_save = new JMenuItem(Messages.getString("GUI.20")); //$NON-NLS-1$
		menuitem_file_saveas = new JMenuItem(Messages.getString("GUI.21")); //$NON-NLS-1$
		menuitem_file_options = new JMenuItem(Messages.getString("GUI.22")); //$NON-NLS-1$
		menuitem_file_version = new JMenuItem(Messages.getString("GUI.23") + VERSION); //$NON-NLS-1$
		menuitem_file_exit = new JMenuItem(Messages.getString("GUI.24")); //$NON-NLS-1$
		menu_song = new JMenu(Messages.getString("GUI.25")); //$NON-NLS-1$
		menuitem_song_new = new JMenuItem(Messages.getString("GUI.26")); //$NON-NLS-1$
		menuitem_song_newfromclipboard = new JMenuItem(Messages.getString("GUI.27")); //$NON-NLS-1$
		menuitem_song_newfromRTF = new JMenuItem(Messages.getString("GUI.11")); //$NON-NLS-1$
		menuitem_song_newfromRTFdir = new JMenuItem(Messages.getString("GUI.15")); //$NON-NLS-1$
		menuitem_song_delete = new JMenuItem(Messages.getString("GUI.28")); //$NON-NLS-1$
		menuitem_song_copy = new JMenuItem(Messages.getString("GUI.29")); //$NON-NLS-1$
		menuitem_song_transpose = new JMenuItem(Messages.getString("GUI.30")); //$NON-NLS-1$
		menu_view = new JMenu(Messages.getString("GUI.31")); //$NON-NLS-1$
		menuitem_view_print = new JMenuItem(Messages.getString("GUI.32")); //$NON-NLS-1$
		menuitem_view_beamer = new JMenuItem(Messages.getString("GUI.33")); //$NON-NLS-1$
		menuitem_view_beamerpres = new JMenuItem(Messages.getString("GUI.34")); //$NON-NLS-1$
		menu_lang = new JMenu(Messages.getString("GUI.3")); //$NON-NLS-1$
		menuitem_lang_de = new JMenuItem(Messages.getString("GUI.4")); //$NON-NLS-1$
		menuitem_lang_en = new JMenuItem(Messages.getString("GUI.5")); //$NON-NLS-1$
		
		// Menü zusammenbauen
		menubar.add(menu_file);
		menubar.add(menu_song);
		menubar.add(menu_view);
		menubar.add(Box.createHorizontalGlue());
		menubar.add(menu_lang);
		menu_file.add(menuitem_file_new);
		menu_file.add(menuitem_file_open);
		menu_file.addSeparator();
		menu_file.add(menuitem_file_save);
		menu_file.add(menuitem_file_saveas);
		menu_file.addSeparator();
		menu_file.add(menuitem_file_options);
		menu_file.addSeparator();
		menu_file.add(menuitem_file_version);
		menu_file.addSeparator();
		menu_file.add(menuitem_file_exit);
		menu_song.add(menuitem_song_new);
		menu_song.add(menuitem_song_newfromclipboard);
		menu_song.add(menuitem_song_newfromRTF);
		menu_song.add(menuitem_song_newfromRTFdir);
		menu_song.add(menuitem_song_copy);
		//menu_song.add(menuitem_song_transpose);
		menu_song.add(menuitem_song_delete);
		menu_view.add(menuitem_view_beamer);
		menu_view.add(menuitem_view_print);
		menu_view.addSeparator();
		menu_view.add(menuitem_view_beamerpres);
		menu_lang.add(menuitem_lang_de);
		menu_lang.add(menuitem_lang_en);
		
		// nichts standardmäßig tun, nur exit() darf das Fenster schließen!
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		// GUI-Elemente definieren
		contentPane = new JPanel();
		borderLayout = new BorderLayout();
		tabbedPane = new JTabbedPane();
		jSplitPane1 = new JSplitPane();
		scrollPane1 = new JScrollPane();
		scrollPane2 = new JScrollPane();
		scrollPane3 = new JScrollPane();
		scrollPane3.setPreferredSize(new Dimension(scrollPane3.getPreferredSize().width, 25));
		tablemodel =
			new DefaultTableModel() {
				public boolean isCellEditable(int rowIndex, int colIndex) {
					return false;
				}
			};
		table = new JTable(tablemodel);
		tablemodel.setColumnIdentifiers(table_column_names);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setShowGrid(false);
		
		// DND
		dglistener = new DGListener(this);
		dslistener = new DSListener(this);
		dragsource = DragSource.getDefaultDragSource();
		dragsource.createDefaultDragGestureRecognizer(
			table,
			DnDConstants.ACTION_COPY,
			dglistener);
		    
		tableselectionmodel = table.getSelectionModel();
		text = new JTextPane();
		text.setOpaque(false);
		
		StyledDocument doc = new DefaultStyledDocument() {
		    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		        super.insertString(offset,str,a);
		        rescan(this);
		    }
		    public void remove(int offset, int length) throws BadLocationException {
		        super.remove(offset, length);
		        rescan(this);
		    }
		};
		text.setStyledDocument(doc);
		Style defaultstyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		
		// definiere die Styles
		
		Style textstyle = doc.addStyle("text", defaultstyle); //$NON-NLS-1$
		StyleConstants.setItalic(textstyle, false);
		StyleConstants.setBold(textstyle, false);
		StyleConstants.setForeground(textstyle, Color.BLACK);
		StyleConstants.setBackground(textstyle, Color.WHITE);
		
		Style titlestyle = doc.addStyle("chord", defaultstyle); //$NON-NLS-1$
		StyleConstants.setItalic(titlestyle, true);
		StyleConstants.setBold(titlestyle, false);
		StyleConstants.setForeground(titlestyle, Color.BLACK);
		StyleConstants.setBackground(titlestyle, Color.LIGHT_GRAY);
		
		Style translatestyle = doc.addStyle("translate", defaultstyle); //$NON-NLS-1$
		StyleConstants.setItalic(translatestyle, false);
		StyleConstants.setBold(translatestyle, true);
		StyleConstants.setForeground(translatestyle, Color.WHITE);
		StyleConstants.setBackground(translatestyle, new Color(0,0,200));
		
		Style dividestyle = doc.addStyle("divide", defaultstyle); //$NON-NLS-1$
		StyleConstants.setItalic(dividestyle, false);
		StyleConstants.setBold(dividestyle, true);
		StyleConstants.setForeground(dividestyle, Color.WHITE);
		StyleConstants.setBackground(dividestyle, Color.BLACK);
		
		// setze General-Style
		doc.setParagraphAttributes(0, 1, doc.getStyle("text"), false); //$NON-NLS-1$
		
		titel = new JTextField();
		String[] sprachen = {Messages.getString("GUI.35"), Messages.getString("GUI.36"), Messages.getString("GUI.37")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sprache = new JComboBox(sprachen);
		tonart = new JTextField();
		copyright = new JTextArea();
		bemerkungen = new JTextField();
		such_panel = new JPanel();
		such_panel.setLayout(new GridLayout(2, 2));
		such_panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		suche_text = new JTextField();
		suche_titel = new JTextField();
		such_panel.add(new JLabel(Messages.getString("GUI.38"), SwingConstants.LEFT)); //$NON-NLS-1$
		such_panel.add(new JLabel(Messages.getString("GUI.39"), SwingConstants.LEFT)); //$NON-NLS-1$
		such_panel.add(suche_text);
		such_panel.add(suche_titel);
		
		// GUI formen
		this.setContentPane(contentPane);
		contentPane.setLayout(borderLayout);
		this.setLocale(java.util.Locale.getDefault());
		this.setJMenuBar(menubar);
		text.setFont(new java.awt.Font("Monospaced", 0, 14)); //$NON-NLS-1$
		scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane3.getViewport().add(copyright, null);
		JPanel untenPanel = new JPanel(new BorderLayout());
		untenPanel.add(such_panel, BorderLayout.CENTER);
		contentPane.add(untenPanel, BorderLayout.SOUTH);
		contentPane.add(jSplitPane1, BorderLayout.CENTER);
		scrollPane1.getViewport().add(text, null);
		allgemein_panel = new JPanel();
		musik_panel = new JPanel();
		rest_panel = new JPanel();
		allgemein_panel.setLayout(new GridLayout(4, 1));
		musik_panel.setLayout(new GridLayout(4, 1));
		rest_panel.setLayout(new GridLayout(4, 1));
		allgemein_panel.add(new JLabel(Messages.getString("GUI.41"), SwingConstants.LEFT)); //$NON-NLS-1$
		allgemein_panel.add(titel);
		allgemein_panel.add(new JLabel(Messages.getString("GUI.42"), SwingConstants.LEFT)); //$NON-NLS-1$
		allgemein_panel.add(sprache);
		musik_panel.add(new JLabel(Messages.getString("GUI.43"), SwingConstants.LEFT)); //$NON-NLS-1$
		musik_panel.add(tonart);
		rest_panel.add(new JLabel(Messages.getString("GUI.44"), SwingConstants.LEFT)); //$NON-NLS-1$
		rest_panel.add(scrollPane3);
		rest_panel.add(new JLabel(Messages.getString("GUI.45"), SwingConstants.LEFT)); //$NON-NLS-1$
		rest_panel.add(bemerkungen);
		tabbedPane.add(allgemein_panel, Messages.getString("GUI.46")); //$NON-NLS-1$
		tabbedPane.add(musik_panel, Messages.getString("GUI.47")); //$NON-NLS-1$
		tabbedPane.add(rest_panel, Messages.getString("GUI.48")); //$NON-NLS-1$
		JPanel linksPanel = new JPanel();
		linksPanel.setLayout(new BoxLayout(linksPanel, BoxLayout.Y_AXIS));
		linksPanel.add(new JLabel(Messages.getString("GUI.49"))); //$NON-NLS-1$
		linksPanel.add(scrollPane2);
		JPanel rechtsobenPanel = new JPanel();
		JPanel rechtsPanel = new JPanel(new BorderLayout());
		rechtsobenPanel.setLayout(new BoxLayout(rechtsobenPanel, BoxLayout.Y_AXIS));
		rechtsobenPanel.add(new JLabel(Messages.getString("GUI.50"))); //$NON-NLS-1$
		rechtsobenPanel.add(scrollPane1);
		rechtsPanel.add(rechtsobenPanel, BorderLayout.CENTER);
		rechtsPanel.add(tabbedPane, BorderLayout.SOUTH);
		
		jSplitPane1.add(linksPanel, JSplitPane.LEFT);
		jSplitPane1.add(rechtsPanel, JSplitPane.RIGHT);
		scrollPane2.getViewport().add(table, null);
		pack();
		setFieldsEditable(false);
		
		// Optionen laden
		try {
			options = loadOptions();
		} catch (Exception ex) {
			options = getDefaultOptions();
		}
		
		// EVENT HANDLER START =======================================
		this.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					exit();
				}
			}
		);
		globalKeyListener =
			new KeyListener() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_F13) {
						// Scroll Up
						if (beamergui != null) {
							beamergui.scroll_up();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_F14) {
						// Scroll Down
						if (beamergui != null) {
							beamergui.scroll_down();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_F16) {
						// prev. Song
						if (beamergui != null) {
							beamergui.do_prev();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_F17) {
						// Start Displaying
						if (beamergui != null) {
							beamergui.showActualSong();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_F18) {
						// next Song
						if (beamergui != null) {
							beamergui.do_next();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_F19) {
						// blank Display
						if (beamergui != null) {
							beamergui.do_empty();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_F21) {
						// show Pres. Control Window
						do_showbeamercontrol();
					} else if (e.getKeyCode() == KeyEvent.VK_F22) {
						// show DB Window
						getThis().toFront();
					}
				}
			    
				public void keyTyped(KeyEvent e) {
					// nix
				}
			    
				public void keyReleased(KeyEvent e) {
					// nix
				}
			};
		
		KeyListener myKeyListener =
			new KeyListener() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_S && e.getModifiers() == InputEvent.CTRL_MASK) {
						save();
					} else if (e.getKeyCode() == KeyEvent.VK_F && e.getModifiers() == InputEvent.CTRL_MASK) {
						suche_text.requestFocus();
						suche_text.setSelectionStart(0);
						suche_text.setSelectionEnd(suche_text.getText().length());
					} else if (e.getKeyCode() == KeyEvent.VK_L && e.getModifiers() == InputEvent.CTRL_MASK) {
						table.requestFocus();
					}
				}
			    
				public void keyTyped(KeyEvent e) {
					// nix
				}
			    
				public void keyReleased(KeyEvent e) {
					// nix
				}
			}
			;
		this.addKeyListener(myKeyListener);
		contentPane.addKeyListener(myKeyListener);
		tabbedPane.addKeyListener(myKeyListener);
		allgemein_panel.addKeyListener(myKeyListener);
		musik_panel.addKeyListener(myKeyListener);
		rest_panel.addKeyListener(myKeyListener);
		jSplitPane1.addKeyListener(myKeyListener);
		scrollPane1.addKeyListener(myKeyListener);
		scrollPane2.addKeyListener(myKeyListener);
		scrollPane3.addKeyListener(myKeyListener);
		table.addKeyListener(myKeyListener);
		text.addKeyListener(myKeyListener);
		titel.addKeyListener(myKeyListener);
		sprache.addKeyListener(myKeyListener);
		tonart.addKeyListener(myKeyListener);
		copyright.addKeyListener(myKeyListener);
		bemerkungen.addKeyListener(myKeyListener);
		suche_text.addKeyListener(myKeyListener);
		suche_titel.addKeyListener(myKeyListener);
		
		KeyListener sucheTextKeyListener =
			new KeyListener() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == e.VK_ENTER) {
						// Enter: Filter anwenden
						suche_titel.setText(""); //$NON-NLS-1$
						titelFilter = ""; //$NON-NLS-1$
						filterText(suche_text.getText());
					} else if (e.getKeyCode() == e.VK_ESCAPE) {
						// Esc: Filter aufheben & Feld löschen
						suche_text.setText(""); //$NON-NLS-1$
						filterText(""); //$NON-NLS-1$
					}
				}
			    
				public void keyTyped(KeyEvent e) {
					// nix
				}
			    
				public void keyReleased(KeyEvent e) {
					// nix
				}
			}
			;
		suche_text.addKeyListener(sucheTextKeyListener);
		suche_text.addFocusListener(
			new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					suche_text.setText(textFilter);
				}
			}
		);
		
		KeyListener sucheTitelKeyListener =
			new KeyListener() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == e.VK_ENTER) {
						// Enter: Filter anwenden
						suche_text.setText(""); //$NON-NLS-1$
						textFilter = ""; //$NON-NLS-1$
						filterTitel(suche_titel.getText());
					} else if (e.getKeyCode() == e.VK_ESCAPE) {
						// Esc: Filter aufheben & Feld löschen
						suche_titel.setText(""); //$NON-NLS-1$
						filterTitel(""); //$NON-NLS-1$
					}
				}
			    
				public void keyTyped(KeyEvent e) {
					// nix
				}
			    
				public void keyReleased(KeyEvent e) {
					// nix
				}
			}
			;
		suche_titel.addKeyListener(sucheTitelKeyListener);
		suche_titel.addFocusListener(
			new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					suche_titel.setText(titelFilter);
				}
			}
		);
		KeyListener tableKeyListener =
			new KeyListener() {
		    
				private long lastKeyTime = 0; // Zeitpunkt!
				private String tosearch = ""; //$NON-NLS-1$
			    
				public void keyPressed(KeyEvent e) {
					//System.out.println("keyPressed: " + e.getKeyCode());
					if (e.getKeyCode() == KeyEvent.VK_D && e.getModifiers() == InputEvent.CTRL_MASK) {
						songdelete();
					} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						// Song in die Liste
						showActualSongPresentation();
						// Markierung festhalten:
                        e.consume();
					}
//					else if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
//						// Get the current time
//						long curTime = System.currentTimeMillis();
//						if ( curTime - lastKeyTime > 1200 ) {
//							// falls Zeitdifferenz > 1,2s: neu suchen
//							tosearch = String.valueOf(e.getKeyChar());
//						} else {
//							// sonst: als Folgebuchstaben suchen
//							tosearch += String.valueOf(e.getKeyChar());
//						}
//						// Zeit neu setzen
//						lastKeyTime = curTime;
//						// jetzt: suchen & erste Übereinstimmung markieren.
//						// falls keine Übereinst. => nichts tun.
//						for ( int i = 0; i < tablemodel.getRowCount(); i++ ) {
//							Object thisone = tablemodel.getValueAt(i, 0);
//							if ( thisone != null && thisone.toString().toLowerCase().startsWith(tosearch.toLowerCase()) ) {
//								// selektieren
//								tableselectionmodel.setSelectionInterval(i, i);
//								break;
//							}
//						}
//					}
				}
			    
				public void keyTyped(KeyEvent e) {
					if (e.getModifiers()==0) {
						String addtosearch = String.valueOf(e.getKeyChar());
						//System.out.println("keyTyped: " + addtosearch);
						// Get the current time
						long curTime = System.currentTimeMillis();
						if ( curTime - lastKeyTime > 1200 ) {
							// falls Zeitdifferenz > 1,2s: neu suchen
							tosearch = addtosearch;
						} else {
							// sonst: als Folgebuchstaben suchen
							tosearch += addtosearch;
						}
						// Zeit neu setzen
						lastKeyTime = curTime;
						// jetzt: suchen & erste Übereinstimmung markieren.
						// falls keine Übereinst. => nichts tun.
						for ( int i = 0; i < tablemodel.getRowCount(); i++ ) {
							Object thisone = tablemodel.getValueAt(i, 0);
							if ( thisone != null && thisone.toString().toLowerCase().startsWith(tosearch.toLowerCase()) ) {
								// selektieren
								tableselectionmodel.setSelectionInterval(i, i);
								break;
							}
						}
					}
				}
			    
				public void keyReleased(KeyEvent e) {
					// nix
				}
			}
			;
		table.addKeyListener(tableKeyListener);
		table.addMouseListener(
			new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
						showActualSongPresentation();
					}
				}
			}
		);
		
		menuitem_file_new.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					n_e_w();
				}
			}
		);
		menuitem_file_open.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					open();
				}
			}
		);
		menuitem_file_save.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					save();
				}
			}
		);
		menuitem_file_saveas.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveas();
				}
			}
		);
		menuitem_file_options.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showOptionsDialog();
				}
			}
		);
		menuitem_file_version.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(GUI.this, "<html><b><u>Song Database " + VERSION + "</u></b><br><br>" + Messages.getString("GUI.1") + "<font size=\"+1\"><b>" + Messages.getString("GUI.0") + "</b></font></html>", "Version " + VERSION + Messages.getString("GUI.2") + DATE, JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				}
			}
		);
		menuitem_file_exit.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exit();
				}
			}
		);
		menuitem_song_new.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					songnew();
				}
			}
		);
		menuitem_song_newfromRTF.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					songnewfromRTF();
				}
			}
		);
		menuitem_song_newfromRTFdir.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					songsnewfromRTFdir();
				}
			}
		);
		menuitem_song_newfromclipboard.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					songnewfromclipboard();
				}
			}
		);
		menuitem_song_copy.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					songcopy();
				}
			}
		);
		menuitem_song_transpose.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					songtranspose();
				}
			}
		);
		menuitem_song_delete.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					songdelete();
				}
			}
		);
		menuitem_view_beamer.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showActualSongPresentation();
				}
			}
		);
		menuitem_view_print.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (previouslySelectedIndex != -1) {
						// store UND load, weil sonst beim nächsten Listenklick die von
						// store gesetzten leeren String nochmals gespeichert würden!
						store(previouslySelectedIndex);
						load(previouslySelectedIndex);
						try {
							new PrintView(structure.getSongByID(Integer.parseInt((String)table.getModel().getValueAt(table.getSelectedRow(), 3))), titelfont, textfont, translatefont, copyrightfont, true, false, 40, 40);
						} catch (Exception ex) {
							System.out.println("CAUGHT:"); //$NON-NLS-1$
							ex.printStackTrace();
						}
					}
				}
			}
		);
		menuitem_view_beamerpres.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_showbeamercontrol();
				}
			}
		);
		menuitem_lang_de.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Messages.setLanguage("de"); //$NON-NLS-1$
					JOptionPane.showMessageDialog(getThis(), Messages.getString("GUI.6"), Messages.getString("GUI.7"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		);
		menuitem_lang_en.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Messages.setLanguage("en"); //$NON-NLS-1$
					JOptionPane.showMessageDialog(getThis(), Messages.getString("GUI.6"), Messages.getString("GUI.7"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		);
		tableselectionmodel.addListSelectionListener(
			new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (previouslySelectedIndex > -1) {
						store(previouslySelectedIndex);
					}
					previouslySelectedIndex = table.getSelectedRow();
					load(previouslySelectedIndex);
				}
			}
		);
		
		// EVENT HANDLER ENDE ========================================
		
		// Struktur herstellen
		this.structure = new Structure(this);
		
		// Datei args[0] reinladen, falls es diese gibt:
		try {
			File file = new File(arg);
			structure.loadFromFile(file);
			filename = arg;
		} catch (Exception ex) {}
		
		reloadModel(false);
		
		jSplitPane1.setDividerLocation(295);
		
		// Titel größer, Sprache und Tonart kleiner machen
		TableColumn column = table.getColumnModel().getColumn(0);
		column.setPreferredWidth(180);
		column = table.getColumnModel().getColumn(1);
		column.setPreferredWidth(50);
		column = table.getColumnModel().getColumn(2);
		column.setPreferredWidth(40);
		
		// alle Components mit globalKeyListener verbinden:
		registerGKL(getContentPane());
		
		setVisible(true);
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle r2 = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration().getBounds();
        Dimension d2 = r2.getSize();
        Point p2 = r2.getLocation();
/*
		try {
			setSize(new Dimension((int)d2.getWidth(), (int)d2.getHeight()-75));
			setLocation(new Point(0, 0));
		} catch(Exception ex) {
			setExtendedState(MAXIMIZED_BOTH);
			System.out.println("Maximizing window anyway.");
		}
*/
        if (d2.getWidth()>1500) {
            setSize(new Dimension(800, (int)d2.getHeight()-75));
			setLocation(new Point(0, 0));
        } else {
            setExtendedState(MAXIMIZED_BOTH);
        }
	}
	
	public void registerGKL(Component comp) {
		if (comp instanceof Container) {
			comp.addKeyListener(globalKeyListener);
			for (int i = 0; i < ((Container)comp).getComponentCount(); i++) {
				registerGKL(((Container)comp).getComponent(i));
			}
		} else {
			comp.addKeyListener(globalKeyListener);
		}
	}
	
	public void do_showbeamercontrol() {
		if (beamergui == null) {
			beamergui = new BeamerGUI(getThis());
		} else {
			beamergui.toFront();
		}
	}
	
	private void songnewfromRTF() {
	    // jetzt Dateiauswahl anzeigen:
		JFileChooser pChooser = new JFileChooser();
		pChooser.setDialogTitle(Messages.getString("GUI.12")); //$NON-NLS-1$
		org.zephyrsoft.util.CustomFileFilter filter = new org.zephyrsoft.util.CustomFileFilter(".rtf", Messages.getString("GUI.13")); //$NON-NLS-1$ //$NON-NLS-2$
		pChooser.addChoosableFileFilter(filter);
		int iValue = pChooser.showOpenDialog(getThis());
		
		if (iValue == JFileChooser.APPROVE_OPTION) {
			File fFile = new File(pChooser.getSelectedFile().toString());
			if (fFile.exists()) {
				// Datei importieren
			    RTFEditorKit rtf = new RTFEditorKit();
				JEditorPane editor = new JEditorPane();
				editor.setEditorKit(rtf);
				try {
					FileInputStream fi = new FileInputStream(fFile);
					rtf.read(fi, editor.getDocument(), 0);
				} catch(FileNotFoundException e) {
					System.out.println("File not found"); //$NON-NLS-1$
				} catch(IOException e) {
					System.out.println("I/O error"); //$NON-NLS-1$
				} catch(BadLocationException e) {
				    System.out.println("bad location"); //$NON-NLS-1$
				}
				
				if (getSelectedSong() != null) {
					store(previouslySelectedIndex);
				}
				Song song = structure.insertNewSong();
				
				try {
				    song.setText(editor.getDocument().getText(0, editor.getDocument().getLength()-1));
				} catch(BadLocationException e) {
				    System.out.println("bad location"); //$NON-NLS-1$
				}
				
				reloadModel(false);
				setSelectedSong(song);
				load(table.getSelectedRow());
				// Songtitel markieren zum überschreiben
				tabbedPane.setSelectedIndex(0);
				titel.setSelectionStart(0);
				titel.setSelectionEnd(titel.getText().length());
				titel.requestFocus();
				dbaltered = true;				
			    
			} else {
				JOptionPane.showMessageDialog(this,
											  Messages.getString("GUI.82"), //$NON-NLS-1$
											  Messages.getString("GUI.83"), //$NON-NLS-1$
											  JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void songsnewfromRTFdir() {
	    // jetzt Dateiauswahl anzeigen:
		JFileChooser pChooser = new JFileChooser();
		pChooser.setDialogTitle(Messages.getString("GUI.15")); //$NON-NLS-1$
		org.zephyrsoft.util.CustomFileFilter filter = new org.zephyrsoft.util.CustomFileFilter(".rtf", ""); //$NON-NLS-1$ //$NON-NLS-2$
		pChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int iValue = pChooser.showOpenDialog(getThis());
		
		if (iValue == JFileChooser.APPROVE_OPTION) {
			File fFile = new File(pChooser.getSelectedFile().toString());
			if (fFile.exists() && fFile.isDirectory()) {
				// Dateien darin importieren
			    Song song = null;
			    File[] dir = fFile.listFiles(filter);
			    
			    for (int i = 0; i < dir.length; i++) {
			        File oneFile = dir[i];
				    RTFEditorKit rtf = new RTFEditorKit();
					JEditorPane editor = new JEditorPane();
					editor.setEditorKit(rtf);
					try {
						FileInputStream fi = new FileInputStream(oneFile);
						rtf.read(fi, editor.getDocument(), 0);
					} catch(FileNotFoundException e) {
						System.out.println("File not found: " + oneFile.getAbsolutePath()); //$NON-NLS-1$
					} catch(IOException e) {
						System.out.println("I/O error: " + oneFile.getAbsolutePath()); //$NON-NLS-1$
					} catch(BadLocationException e) {
					    System.out.println("bad location: " + oneFile.getAbsolutePath()); //$NON-NLS-1$
					}
					
					if (getSelectedSong() != null) {
						store(previouslySelectedIndex);
					}
					song = structure.insertNewSong();
					
					try {
					    song.setText(editor.getDocument().getText(0, editor.getDocument().getLength()-1));
					} catch(BadLocationException e) {
					    System.out.println("bad location"); //$NON-NLS-1$
					}
				}
				
			    reloadModel(false);
				setSelectedSong(song);
				load(table.getSelectedRow());
				// Songtitel markieren zum überschreiben
				tabbedPane.setSelectedIndex(0);
				titel.setSelectionStart(0);
				titel.setSelectionEnd(titel.getText().length());
				titel.requestFocus();
				dbaltered = true;				
			    
			} else {
				JOptionPane.showMessageDialog(this,
											  Messages.getString("GUI.82"), //$NON-NLS-1$
											  Messages.getString("GUI.83"), //$NON-NLS-1$
											  JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public final void rescan(StyledDocument doc) {
	    String text1 = text.getText() + "\n"; //$NON-NLS-1$
	    // Style TEXT zuweisen
		doc.setCharacterAttributes(0, text1.length(), doc.getStyle("text"), false); //$NON-NLS-1$
		// CHORD Entsprechungen suchen
	    Vector vonalle = new Vector();
		Vector bisalle = new Vector();
		int von = 0;
		int bis = text1.indexOf("\n", 1); //$NON-NLS-1$
	    while (bis >= 0) {
		    if (bis > von && !Song.isTextLine(text1.substring(von, bis)) && !Song.isTranslateLine(text1.substring(von, bis))) {
			    vonalle.addElement(new Integer(von));
			    bisalle.addElement(new Integer(bis+1));
		    }
		    von = bis+1;
		    bis = text1.indexOf("\n", von); //$NON-NLS-1$
		}
		// Style CHORD zuweisen
		for (int i = 0; i < vonalle.size(); i++) {
		    int von1 = ((Integer)vonalle.elementAt(i)).intValue();
		    int bis1 = ((Integer)bisalle.elementAt(i)).intValue();
		    try {
			    if (text.getText(bis1,1).equals("\n")) { //$NON-NLS-1$
			        bis1++;
			    }
		    } catch(Exception ex) {
		        ex.printStackTrace();
		    }
		    doc.setCharacterAttributes(von1, bis1-von1, doc.getStyle("chord"), true); //$NON-NLS-1$
		}
		// TRANSLATE Entsprechungen suchen
	    vonalle = new Vector();
		bisalle = new Vector();
		von = text1.indexOf("["); //$NON-NLS-1$
		while (von >= 0) {
		    int vorher = 0; // zählt Zeichen vorher
		    try {
			    while (!text1.substring(von-vorher-1,von-vorher).equals("\n")) { //$NON-NLS-1$
				    vorher++;
				}
		    } catch(Exception ex) {}
		    vonalle.addElement(new Integer(von-vorher));
		    bis = text1.indexOf("]", von+1); //$NON-NLS-1$
		    if (bis == -1) {
		        bis = text1.length();
		    }
		    int nachher = 0; // zählt Zeichen nachher
		    try {
				while (!text1.substring(bis+nachher+1,bis+nachher+2).equals("\n")) { //$NON-NLS-1$
				    nachher++;
				}
		    } catch(Exception ex) {}
			bisalle.addElement(new Integer(bis+nachher+1));
		    von = text1.indexOf("[", bis+nachher+1); //$NON-NLS-1$
		}
		// Style TRANSLATE zuweisen
		for (int i = 0; i < vonalle.size(); i++) {
		    int von1 = ((Integer)vonalle.elementAt(i)).intValue();
		    int bis1 = ((Integer)bisalle.elementAt(i)).intValue();
		    try {
			    if (text.getText(bis1,1).equals("\n")) { //$NON-NLS-1$
			        bis1++;
			    }
		    } catch(Exception ex) {
		        //ex.printStackTrace();
		    }
		    doc.setCharacterAttributes(von1, bis1-von1, doc.getStyle("translate"), true); //$NON-NLS-1$
		}
		// DIVIDE Entsprechungen suchen
	    vonalle = new Vector();
		bisalle = new Vector();
		von = text1.indexOf("\n###"); //$NON-NLS-1$
		while (von >= 0) {
		    von++;
		    // keine Zeichen vorher erlaubt
		    vonalle.addElement(new Integer(von));
		    bis = text1.indexOf("\n", von+1); //$NON-NLS-1$
		    bisalle.addElement(new Integer(bis+1));
		    von = text1.indexOf("\n###", bis+1); //$NON-NLS-1$
		}
		// Style DIVIDE zuweisen
		for (int i = 0; i < vonalle.size(); i++) {
		    int von1 = ((Integer)vonalle.elementAt(i)).intValue();
		    int bis1 = ((Integer)bisalle.elementAt(i)).intValue();
		    try {
			    if (text.getText(bis1,1).equals("\n")) { //$NON-NLS-1$
			        bis1++;
			    }
		    } catch(Exception ex) {
		        ex.printStackTrace();
		    }
		    doc.setCharacterAttributes(von1, bis1-von1, doc.getStyle("divide"), true); //$NON-NLS-1$
		}
	}
	
	
	
	// EVENT HANLDER HILFSMETHODEN:
	
	private final void showOptionsDialog() {
		new OptionsGUI(this);
	}
	private final void showActualSongPresentation() {
		// BeamerGUI öffnen/benutzen, Song dort anfügen und BeamerGUI i.d.Vordergrund
		if (beamergui == null) {
			beamergui = new BeamerGUI(getThis());
		} else {
			beamergui.toFront();
		}
		try {
		    beamergui.addSong(structure.getSongByID(Integer.parseInt((String)table.getModel().getValueAt(table.getSelectedRow(), 3))));
		} catch(ArrayIndexOutOfBoundsException ex) {}
	}
	
	/**
	 * Description of the Method
	 *
	 * @param str  Description of the Parameter
	 */
	private final void filterText(String str) {
		try {
			textFilter = str;
			store(previouslySelectedIndex);
			reloadModel(true);
			table.setRowSelectionInterval(0, 0);
			table.removeRowSelectionInterval(0, table.getRowCount() - 1);
			previouslySelectedIndex = -1;
		} catch (Exception ex) {}
	}
	
	/**
	 * Description of the Method
	 *
	 * @param str  Description of the Parameter
	 */
	private final void filterTitel(String str) {
		try {
			titelFilter = str;
			store(previouslySelectedIndex);
			reloadModel(true);
			table.setRowSelectionInterval(0, 0);
			table.removeRowSelectionInterval(0, table.getRowCount() - 1);
			previouslySelectedIndex = -1;
		} catch (Exception ex) {}
	}
	
	/** Description of the Method */
	private final void exit() {
		if (getSelectedSong() != null) {
			store(previouslySelectedIndex);
		}
		
		int answer = JOptionPane.NO_OPTION;
		if (!debug && dbaltered) {
			answer = JOptionPane.showConfirmDialog(this, Messages.getString("GUI.65"), Messages.getString("GUI.66"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			if (answer == JOptionPane.YES_OPTION) {
				save();
			}
		}
		
		answer = JOptionPane.YES_OPTION;
		if (beamergui != null && beamergui.beamerview != null) {
			try {
				answer = JOptionPane.showConfirmDialog(this, Messages.getString("GUI.67"), Messages.getString("GUI.68"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (answer == JOptionPane.YES_OPTION ) {
			System.exit(0);
		}
	}
	
	/**
	 * Description of the Method
	 *
	 * @param index  Description of the Parameter
	 */
	protected void load(int index) {
		if (index > -1 && saved) {
			try {
				// markierte Zeile hinscrollen
				table.scrollRectToVisible(table.getCellRect(table.getSelectedRow(), 0, true));
				// reinladen
				//System.out.println("load  " + index);
				Song song = structure.getSongByID(Integer.parseInt((String)table.getModel().getValueAt(index, 3)));
				titel.setText(song.getTitel());
				sprache.setSelectedItem(song.getSprache());
				tonart.setText(song.getTonart());
				copyright.setText(song.getCopyright());
				bemerkungen.setText(song.getBemerkungen());
				text.setText(song.getText());
				setFieldsEditable(true);
				copyright.setSelectionStart(0);
				copyright.setSelectionEnd(0);
				text.setSelectionStart(0);
				text.setSelectionEnd(0);
				saved = false;
			} catch (Exception ex) {
				System.err.println("ERROR IN LOAD:"); //$NON-NLS-1$
				ex.printStackTrace();
				setFieldsEditable(false);
			}
		}
	}
	
	/** Jetzt die Variante dass man einen Song übergibt (z.B. prevSong) */
	protected void load(Song song) {
		if (song != null && saved) {
			try {
				setSelectedSong(song);
				load(table.getSelectedRow());
			} catch (Exception ex) {
				System.err.println("ERROR IN LOAD:"); //$NON-NLS-1$
				ex.printStackTrace();
				setFieldsEditable(false);
			}
		}
	}
	
	
	/**
	 * Description of the Method
	 *
	 * @param index  Description of the Parameter
	 */
	protected void store(int index) {
		if (index > -1 && !saved) {
			try {
				//System.out.println("store " + index);
				Song song = structure.getSongByID(Integer.parseInt((String)table.getModel().getValueAt(index, 3)));
				
				// hat sich etwas geändert?
				if (!(song.getTitel().equals(titel.getText()) &&
						song.getSprache().equals((String)sprache.getSelectedItem()) &&
						song.getTonart().equals(tonart.getText()) &&
						song.getCopyright().equals(copyright.getText()) &&
						song.getBemerkungen().equals(bemerkungen.getText()) &&
						song.getText().equals(text.getText()))) {
					// JA!
					dbaltered = true;
				}
				
				song.setTitel(new String(titel.getText()));
				song.setSprache(new String((String)sprache.getSelectedItem()));
				song.setTonart(new String(tonart.getText()));
				song.setCopyright(new String(copyright.getText()));
				song.setBemerkungen(new String(bemerkungen.getText()));
				song.setText(new String(text.getText()));
				prevSong = song;
				setFieldsEditable(false);
				saved = true;
			} catch (ArrayIndexOutOfBoundsException ex) {
				// das letzte Lied in der Liste wurde gelöscht!
				//System.out.println("Alles OK.");
			}
			catch (Exception ex) {
				System.err.println("ERROR IN STORE:"); //$NON-NLS-1$
				ex.printStackTrace();
				setFieldsEditable(false);
			}
		}
	}
	
	/**
	 * Sets the fieldsEditable attribute of the GUI object
	 *
	 * @param value  The new fieldsEditable value
	 */
	protected void setFieldsEditable(boolean value) {
		text.setEnabled(value);
		titel.setEditable(value);
		sprache.setEnabled(value);
		tonart.setEditable(value);
		copyright.setEnabled(value);
		bemerkungen.setEditable(value);
		text.setBackground(titel.getBackground());
		copyright.setBackground(titel.getBackground());
		if (!value) {
			text.setText(""); //$NON-NLS-1$
			titel.setText(""); //$NON-NLS-1$
			sprache.setSelectedItem(Messages.getString("GUI.74")); //$NON-NLS-1$
			tonart.setText(""); //$NON-NLS-1$
			copyright.setText(""); //$NON-NLS-1$
			bemerkungen.setText(""); //$NON-NLS-1$
		}
	}
	
	/** Description of the Method */
	protected void n_e_w() {
		this.structure = new Structure(this);
		reloadModel(false);
		setFieldsEditable(false);
		filename = ""; //$NON-NLS-1$
		saved = true;
	}
	
	/** Description of the Method */
	protected void open() {
		// jetzt Dateiauswahl anzeigen:
		JFileChooser pChooser = new JFileChooser();
		pChooser.setDialogTitle(Messages.getString("GUI.79")); //$NON-NLS-1$
		org.zephyrsoft.util.CustomFileFilter filter = new org.zephyrsoft.util.CustomFileFilter(".sdb", Messages.getString("GUI.81")); //$NON-NLS-1$ //$NON-NLS-2$
		pChooser.addChoosableFileFilter(filter);
		int iValue = pChooser.showOpenDialog(getThis());
		
		if (iValue == JFileChooser.APPROVE_OPTION) {
			File fFile = new File(pChooser.getSelectedFile().toString());
			if (fFile.exists()) {
				n_e_w();
				structure.loadFromFile(fFile);
				filename = fFile.getAbsolutePath();
				reloadModel(false);
				setFieldsEditable(false);
				saved = true;
			} else {
				JOptionPane.showMessageDialog(this,
											  Messages.getString("GUI.82"), //$NON-NLS-1$
											  Messages.getString("GUI.83"), //$NON-NLS-1$
											  JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/** Description of the Method */
	protected void save() {
		if (filename.equals("")) { //$NON-NLS-1$
			// noch nicht gespeichert
			saveas();
		} else {
			boolean wasSelected = false;
			int whatSelected = 0;
			if (previouslySelectedIndex > -1) {
				wasSelected = true;
				whatSelected = previouslySelectedIndex;
			}
			int songid = -1;
			if (wasSelected) {
				store(previouslySelectedIndex);
				songid = Integer.parseInt((String)table.getModel().getValueAt(previouslySelectedIndex, 3));
				table.clearSelection();
			}
			structure.saveToFile(new File(filename));
			dbaltered = false;
			reloadModel(wasSelected);
			if (wasSelected) {
				// wieder selektieren
				load(prevSong);
			}
		}
	}
	
	/** Description of the Method */
	protected void saveas() {
		// jetzt Dateiauswahl anzeigen:
		JFileChooser pChooser = new JFileChooser();
		pChooser.setDialogTitle(Messages.getString("GUI.85")); //$NON-NLS-1$
		org.zephyrsoft.util.CustomFileFilter filter = new org.zephyrsoft.util.CustomFileFilter(".sdb", Messages.getString("GUI.87")); //$NON-NLS-1$ //$NON-NLS-2$
		pChooser.addChoosableFileFilter(filter);
		int iValue = pChooser.showSaveDialog(this);
		
		if (iValue == JFileChooser.APPROVE_OPTION) {
			File fFile = new File(pChooser.getSelectedFile().toString());
			try {
				Song song = getSelectedSong();
				if (song != null) {
					store(previouslySelectedIndex);
				}
				table.clearSelection();
				structure.saveToFile(fFile);
				dbaltered = false;
				reloadModel(true);
				setSelectedSong(song);
				load(table.getSelectedRow());
				filename = fFile.getAbsolutePath();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this,
											  Messages.getString("GUI.88"), //$NON-NLS-1$
											  Messages.getString("GUI.89"), //$NON-NLS-1$
											  JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/** Description of the Method */
	protected void songnew() {
		if (getSelectedSong() != null) {
			store(previouslySelectedIndex);
		}
		Song song = structure.insertNewSong();
		reloadModel(false);
		setSelectedSong(song);
		load(table.getSelectedRow());
		// Songtitel markieren zum überschreiben
		tabbedPane.setSelectedIndex(0);
		titel.setSelectionStart(0);
		titel.setSelectionEnd(titel.getText().length());
		titel.requestFocus();
		dbaltered = true;
	}
	
	/** Description of the Method */
	protected void songnewfromclipboard() {
		// Song neu, Text aus der Zwischenablage
		if (getSelectedSong() != null) {
			store(previouslySelectedIndex);
		}
		Song song = structure.insertNewSong();
		
		// Zwischenablagen-Kunststück:
		song.importTextFromClipboard(this);
		
		reloadModel(false);
		setSelectedSong(song);
		load(table.getSelectedRow());
		// Songtitel markieren zum überschreiben
		tabbedPane.setSelectedIndex(0);
		titel.setSelectionStart(0);
		titel.setSelectionEnd(titel.getText().length());
		titel.requestFocus();
		dbaltered = true;
	}
	
	/** Description of the Method */
	protected void songdelete() {
		// markierten Song löschen
		Song song = getSelectedSong();
		if (song != null) {
			structure.deleteSong(song);
			saved = true;
			reloadModel(false);
			setFieldsEditable(false);
			dbaltered = true;
		}
	}
	
	/** Description of the Method */
	protected void songcopy() {
		// markierten Song duplizieren
		Song song = getSelectedSong();
		if (song != null) {
			Song newsong = structure.copySong(song);
			reloadModel(false);
			setSelectedSong(newsong);
			load(table.getSelectedRow());
			dbaltered = true;
		}
	}
	
	/** Description of the Method */
	protected void songtranspose() {
		// markierten Song transponieren
		Song song = getSelectedSong();
		if (song != null) {
			new Transposer(song.getID(), structure, getThis());
		}
	}
	
	/** Description of the Method */
	public void transposed() {
		// Daten neu laden
		Song song = getSelectedSong();
		reloadModel(true);
		if (song != null) {
			setSelectedSong(song);
			load(table.getSelectedRow());
			dbaltered = true;
		}
	}
	
	/**
	 * Gets the this attribute of the GUI object
	 *
	 * @return   The this value
	 */
	protected GUI getThis() {
		return this;
	}
	
	/**
	 * Description of the Method
	 *
	 * @param selectAgain  Description of the Parameter
	 */
	private void reloadModel(boolean selectAgain) {
		Song sel = getSelectedSong();
		int selected_index = table.getSelectedRow();
		store(selected_index);
		table.clearSelection();
		
		tablemodel.setDataVector(structure.getTableData(titelFilter, textFilter), table_column_names);
		//System.out.println(Messages.getString("GUI.90") + structure.getSongCount()); //$NON-NLS-1$
		// ID verstecken
		TableColumn column = table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex("ID")); //$NON-NLS-1$
		TableCellRenderer renderer = null;
		Component renderercomp = null;
		column.setResizable(false);
		column.setMaxWidth(0);
		column.setMinWidth(0);
		column.setPreferredWidth(0);
		column.setWidth(0);
		
		// Disable autoCreateColumnsFromModel otherwise all the
		// column customizations and adjustments will be lost
		// when the model data is sorted
		table.setAutoCreateColumnsFromModel(false);
		
		sortAllRowsBy(tablemodel, 0, true);
		
		try {
			if (selectAgain) {
				// wieder selektieren
				setSelectedSong(sel);
			}
		} catch (Exception ex) {
			System.out.println("CAUGHT:"); //$NON-NLS-1$
			ex.printStackTrace();
		}
	}
	
	// Regardless of sort order (ascending or descending),
	// null values always appear last.
	// colIndex specifies a column in model.
	/**
	 * Description of the Method
	 *
	 * @param model      Description of the Parameter
	 * @param colIndex   Description of the Parameter
	 * @param ascending  Description of the Parameter
	 */
	public void sortAllRowsBy(DefaultTableModel model, int colIndex, boolean ascending) {
		Vector data = model.getDataVector();
		Collections.sort(data, new ColumnSorter(colIndex, ascending));
		model.fireTableStructureChanged();
	}
	
	/**
	 * Gets the selectedSong attribute of the GUI object
	 *
	 * @return   The selectedSong value
	 */
	public Song getSelectedSong() {
		try {
			return structure.getSongByID(Integer.parseInt((String)table.getModel().getValueAt(table.getSelectedRow(), 3)));
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Sets the selectedSong attribute of the GUI object
	 *
	 * @param song  The new selectedSong value
	 */
	private void setSelectedSong(Song song) {
		if (song != null) {
			int id = song.getID();
			for (int i = 0; i < tablemodel.getRowCount(); i++) {
				if (tablemodel.getValueAt(i, 3) != null && ((String)tablemodel.getValueAt(i, 3)).equals(String.valueOf(id))) {
					table.setRowSelectionInterval(i, i);
					previouslySelectedIndex = i;
				}
			}
		} else {
			previouslySelectedIndex = -1;
		}
	}
	
	public void setBeamerGuiToNull() {
		this.beamergui = null;
	}
	
	public void beamerGuiToFront() {
		if (beamergui != null) {
			beamergui.toFront();
		} else {
			beamergui = new BeamerGUI(this);
			beamergui.toFront();
		}
	}
	
	public void showPresentation(String file_name) {
		showPresentation();
		beamergui.loadFile(file_name);
	}
	
	public void showPresentation() {
		beamergui = new BeamerGUI(this);
		//this.setState(Frame.ICONIFIED);
	}
	
	public DSListener getDSListener() {
		return dslistener;
	}
	
	public Options getDefaultOptions() {
		Options ret = new Options();
		ret.put("fgco", java.awt.Color.black); //$NON-NLS-1$
		ret.put("bgco", java.awt.Color.white); //$NON-NLS-1$
		ret.put("tifo", new java.awt.Font("Arial", Font.BOLD, 22)); //$NON-NLS-1$ //$NON-NLS-2$
		ret.put("tefo", new java.awt.Font("Arial", Font.PLAIN, 18)); //$NON-NLS-1$ //$NON-NLS-2$
		ret.put("trfo", new java.awt.Font("Arial", Font.ITALIC, 14)); //$NON-NLS-1$ //$NON-NLS-2$
		ret.put("cofo", new java.awt.Font("Arial", Font.PLAIN, 10)); //$NON-NLS-1$ //$NON-NLS-2$
		ret.put("sple", new Integer(90)); //$NON-NLS-1$
		ret.put("spup", new Integer(90)); //$NON-NLS-1$
		ret.put("spdo", new Integer(30)); //$NON-NLS-1$
		ret.put("sptt", new Integer(30)); //$NON-NLS-1$
		ret.put("sptc", new Integer(30)); //$NON-NLS-1$
		ret.put("bst", new Boolean(true)); //$NON-NLS-1$
		ret.put("zt", new Boolean(true)); //$NON-NLS-1$
		ret.put("mf", new Boolean(false)); //$NON-NLS-1$
		return ret;
	}
	
	public Options loadOptions() throws Exception {
		// von Datei einlesen; DefaultOptions falls Datei nicht vorhanden
		try {
			FileInputStream fis = new FileInputStream(options_filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Options in = (Options)ois.readObject();
			ois.close();
			fis.close();
			// Runtime-Objekt überschreiben
			options = (Options)in.clone();
			// zurückgeben
			return in;
		} catch (FileNotFoundException ex) {
			return getDefaultOptions();
		}
	}
	
	public void saveOptions(Options tosave) throws Exception {
		// Runtime-Objekt überschreiben
		boolean vorher = ((Boolean)options.get("bst")).booleanValue(); //$NON-NLS-1$
		options = (Options)tosave.clone();
		boolean nachher = ((Boolean)options.get("bst")).booleanValue(); //$NON-NLS-1$
		// in Datei schreiben
		FileOutputStream fos = new FileOutputStream(options_filename);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(tosave);
		oos.close();
		fos.close();
		// Song-Liste reloaden
		reloadModel(true);
		if (beamergui != null) {
			beamergui.reloadModel(true);
		}
	}
	
	public Options getOptions() {
		return (Options)options.clone();
		//return options;
	}
}
