package org.zephyrsoft.sdb;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.*;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import org.pushingpixels.trident.*;
import org.pushingpixels.trident.Timeline.*;
import org.zephyrsoft.sdb.dnd.*;
import org.zephyrsoft.sdb.structure.*;
import org.zephyrsoft.util.*;

/**
 * GUI f�r die Lieder-Pr�sentation auf dem Beamer
 *
 * @author   Mathis Dirksen-Thedens
 */

public class BeamerGUI extends JFrame {

	private boolean debug = true;
	
	public GUI parent = null;
	
	protected BeamerView beamerview = null;
	
	protected Timeline scrollTimeline = null;
	
	public boolean closing = false;
	
	Font titelfont = null;
	Font textfont = null;
	Font translatefont = null;
	Font copyrightfont = null;
	
	String logoname = null;
	
	// DND
	private DGListener dglistener;
	private DSListener dslistener;
	private DropTarget droptarget;
	private DTListener dtlistener;
	
	public Structure structure = null;
	private String filename = ""; //$NON-NLS-1$
	
	JMenuBar menubar = null;
	JMenu menu_file = null;
	JMenu menu_window = null;
	JMenuItem menuitem_file_new = null;
	JMenuItem menuitem_file_open = null;
	JMenuItem menuitem_file_save = null;
	JMenuItem menuitem_file_saveas = null;
	JMenuItem menuitem_file_sorttitle = null;
	JMenuItem menuitem_file_sortbegin = null;
	JMenuItem menuitem_window_exit = null;
	JPanel contentPane = null;
	BoxLayout boxLayout = null;
	JList list = null;
	JLabel actualSongTitel = null;
	JLabel actualSongRealTitel = null;
	JLabel actualSongSprache = null;
	JLabel actualSongFolie = null;
	JLabel selSongTitle = null;
	JLabel selSongRealTitle = null;
	JLabel selSongLanguage = null;
	int aktuelleFolie = 0;
	JButton upButton = null;
	JButton downButton = null;
	JButton deleteButton = null;
	JButton goButton = null;
	JButton nextButton = null;
	JButton previousButton = null;
	JButton emptyButton = null;
	JButton logoButton = null;
	JButton nextFoilButton = null;
	JButton previousFoilButton = null;
	JComboBox goFoilBox = null;
	JButton goFoilButton = null;
	JComboBox startFoilBox = null;
	JButton startFoilButton = null;
	JButton hidePresentationButton = null;
	JScrollBar presentationScrollBar = null;
	JPanel reur_innen = null;
	JPanel reur_aussen = null;
	GridBagConstraints reur_innen_constraints = null;
	GridBagLayout reur_innen_gridbag = null;
	
	public BeamerGUI(GUI parent) {
		super(Messages.getString("BeamerGUI.4")); //$NON-NLS-1$
		
		structure = new Structure(parent);
		this.parent = parent;
		
		titelfont = (Font)parent.getOptions().get("tifo"); //$NON-NLS-1$
		textfont = (Font)parent.getOptions().get("tefo"); //$NON-NLS-1$
		translatefont = (Font)parent.getOptions().get("trfo"); //$NON-NLS-1$
		copyrightfont = (Font)parent.getOptions().get("cofo"); //$NON-NLS-1$
		
		// Aussehen
		LookAndFeelUtil.setMyLookAndFeel();
		setIconImage(LookAndFeelUtil.getIcon());
		
		// Men� definieren
		menubar = new JMenuBar();
		menu_file = new JMenu(Messages.getString("BeamerGUI.7")); //$NON-NLS-1$
		menu_window = new JMenu(Messages.getString("BeamerGUI.8")); //$NON-NLS-1$
		menuitem_file_new = new JMenuItem(Messages.getString("BeamerGUI.9")); //$NON-NLS-1$
		menuitem_file_open = new JMenuItem(Messages.getString("BeamerGUI.10")); //$NON-NLS-1$
		menuitem_file_save = new JMenuItem(Messages.getString("BeamerGUI.11")); //$NON-NLS-1$
		menuitem_file_saveas = new JMenuItem(Messages.getString("BeamerGUI.12")); //$NON-NLS-1$
		menuitem_file_sorttitle = new JMenuItem(Messages.getString("BeamerGUI.3")); //$NON-NLS-1$
		menuitem_file_sortbegin = new JMenuItem(Messages.getString("BeamerGUI.2")); //$NON-NLS-1$
		menuitem_window_exit = new JMenuItem(Messages.getString("BeamerGUI.13")); //$NON-NLS-1$
		
		// Men� zusammenbauen
		menubar.add(menu_file);
		menubar.add(menu_window);
		menu_file.add(menuitem_file_sorttitle);
		menu_file.add(menuitem_file_sortbegin);
		menu_file.addSeparator();
		menu_file.add(menuitem_file_new);
		menu_file.add(menuitem_file_open);
		menu_file.add(menuitem_file_save);
		menu_file.add(menuitem_file_saveas);
		menu_window.add(menuitem_window_exit);
		
		// GUI-Elemente definieren
		contentPane = new JPanel();
		boxLayout = new BoxLayout(contentPane, BoxLayout.X_AXIS);
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// DND zur Liste hinzuf�gen
		this.dtlistener = new DTListener(this);
		
		// component, ops, listener, accepting
		this.droptarget = new DropTarget(list,
										 DnDConstants.ACTION_COPY,
										 this.dtlistener,
										 true);
		                                 
		actualSongTitel = new JLabel(" "); //$NON-NLS-1$
		actualSongRealTitel = new JLabel(" "); //$NON-NLS-1$
		actualSongSprache = new JLabel(" "); //$NON-NLS-1$
		actualSongFolie = new JLabel(" "); //$NON-NLS-1$
		selSongTitle = new JLabel(" "); //$NON-NLS-1$
		selSongRealTitle = new JLabel(" "); //$NON-NLS-1$
		selSongLanguage = new JLabel(" "); //$NON-NLS-1$
		upButton = new JButton(Messages.getString("BeamerGUI.18")); //$NON-NLS-1$
		downButton = new JButton(Messages.getString("BeamerGUI.19")); //$NON-NLS-1$
		deleteButton = new JButton(Messages.getString("BeamerGUI.20")); //$NON-NLS-1$
		goButton = new JButton(Messages.getString("BeamerGUI.21")); //$NON-NLS-1$
		nextButton = new JButton(Messages.getString("BeamerGUI.22")); //$NON-NLS-1$
		previousButton = new JButton(Messages.getString("BeamerGUI.23")); //$NON-NLS-1$
		nextFoilButton = new JButton(Messages.getString("BeamerGUI.24")); //$NON-NLS-1$
		previousFoilButton = new JButton(Messages.getString("BeamerGUI.25")); //$NON-NLS-1$
		goFoilButton = new JButton(Messages.getString("BeamerGUI.26")); //$NON-NLS-1$
		goFoilBox = new JComboBox();
		goFoilBox.setEditable(false);
		startFoilButton = new JButton(Messages.getString("BeamerGUI.27")); //$NON-NLS-1$
		startFoilBox = new JComboBox();
		startFoilBox.setEditable(false);
		emptyButton = new JButton(Messages.getString("BeamerGUI.28")); //$NON-NLS-1$
		logoButton = new JButton(Messages.getString("BeamerGUI.1")); //$NON-NLS-1$
		hidePresentationButton = new JButton(Messages.getString("BeamerGUI.29")); //$NON-NLS-1$
		
		if (!((Boolean)parent.getOptions().get("mf")).booleanValue()) { //$NON-NLS-1$
			nextFoilButton.setEnabled(false);
			previousFoilButton.setEnabled(false);
			goFoilBox.setEnabled(false);
			goFoilButton.setEnabled(false);
			startFoilBox.setEnabled(false);
			startFoilButton.setEnabled(false);
		}
		
		// GUI formen
		this.setContentPane(contentPane);
		contentPane.setLayout(boxLayout);
		JSplitPane pane = new JSplitPane();
		contentPane.add(pane);
		this.setLocale(java.util.Locale.getDefault());
		this.setJMenuBar(menubar);
		
		// links:
		JSplitPane li = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JPanel lio = new JPanel();
		BoxLayout blay = new BoxLayout(lio, BoxLayout.X_AXIS);
		lio.setLayout(blay);
		lio.add(new JScrollPane(list));
		li.setTopComponent(lio);
		pane.setLeftComponent(li);
		JPanel liu = new JPanel();
		BoxLayout liulay = new BoxLayout(liu, BoxLayout.Y_AXIS);
		liu.setLayout(liulay);
		liu.add(selSongTitle);
		liu.add(selSongRealTitle);
		liu.add(selSongLanguage);
		li.setBottomComponent(liu);
		li.setDividerLocation(480);
		
		// rechts:
		JPanel re = new JPanel();
		re.setLayout(new BoxLayout(re, BoxLayout.Y_AXIS));
		JPanel rego = new JPanel();
		rego.setLayout(new BoxLayout(rego, BoxLayout.Y_AXIS));
		JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		temp.add(upButton);
		rego.add(temp);
		temp = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		temp.add(downButton);
		rego.add(temp);
		temp = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		temp.add(deleteButton);
		rego.add(temp);
		re.add(rego);
		JPanel reo = new JPanel(new BorderLayout());
		JPanel reo_innen = new JPanel();
		reo_innen.setLayout(new BoxLayout(reo_innen, BoxLayout.Y_AXIS));
		reo_innen.add(actualSongTitel);
		reo_innen.add(actualSongRealTitel);
		reo_innen.add(actualSongSprache);
		reo_innen.add(actualSongFolie);
		reo_innen.setBorder(new TitledBorder(Messages.getString("BeamerGUI.30"))); //$NON-NLS-1$
		reo.add(reo_innen, BorderLayout.CENTER);
		re.add(reo);
		JPanel reu = new JPanel();
		reu.setLayout(new BoxLayout(reu, BoxLayout.X_AXIS));
		JPanel reul = new JPanel();
		reul.setLayout(new BoxLayout(reul, BoxLayout.Y_AXIS));
		reul.add(goButton);
		JPanel foils2 = new JPanel(new FlowLayout());
		foils2.add(startFoilBox);
		foils2.add(startFoilButton);
		reul.add(foils2);
		reul.add(logoButton);
		reul.add(emptyButton);
		reul.add(new JLabel(" ")); //$NON-NLS-1$
		reul.add(previousButton);
		reul.add(nextButton);
		reul.add(new JLabel(" ")); //$NON-NLS-1$
		reul.add(previousFoilButton);
		reul.add(nextFoilButton);
		JPanel foils = new JPanel(new FlowLayout());
		foils.add(goFoilBox);
		foils.add(goFoilButton);
		reul.add(foils);
		reul.add(new JLabel("")); //$NON-NLS-1$
		reul.add(hidePresentationButton);
		JPanel reur = new JPanel(new BorderLayout());
		reur_innen_gridbag = new GridBagLayout();
		reur_innen = new JPanel(reur_innen_gridbag);
		reur_innen_constraints = new GridBagConstraints();
		reur_innen_constraints.fill = GridBagConstraints.VERTICAL;
		reur_innen_constraints.weighty = 1.0;
		reur.add(reur_innen, BorderLayout.CENTER);
		reur_aussen = new JPanel();
		reur_aussen.setLayout(new BoxLayout(reur_aussen, BoxLayout.Y_AXIS));
		reur.add(reur_aussen, BorderLayout.EAST);
		reu.add(reul);
		reu.add(reur);
		re.add(reu);
		pane.setRightComponent(re);
		pane.setDividerLocation(200);
		pack();
		setSize(new Dimension(600, 590));
		setLocation(new Point(350, 100));
		
		// nichts standardm��ig tun, nur exit() darf das Fenster schlie�en!
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		// EVENT HANDLER START =======================================
		
		KeyListener showKeyListener =
			new KeyListener() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						showActualSong();
						nextButton.requestFocus();
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
		list.addKeyListener(showKeyListener);
		list.addMouseListener(
			new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
						showActualSong();
						nextButton.requestFocus();
					}
				}
			}
		);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				fillStartFoilList();
				if (list.getSelectedValue() != null) {
					Song newsong = (Song)list.getSelectedValue();
					String title = newsong.getTitel();
					String realtitle = getNormalTitleByID(newsong.getID());
					if (StringTools.replace(StringTools.replace(title, ".", ""), ",", "").equalsIgnoreCase(StringTools.replace(StringTools.replace(realtitle, ".", ""), ",", ""))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
						selSongTitle.setText(StringTools.cutdown(title, 25));
						selSongRealTitle.setText(" "); //$NON-NLS-1$
					} else {
						selSongTitle.setText(StringTools.cutdown(title, 25));
						selSongRealTitle.setText(Messages.getString("BeamerGUI.70") + StringTools.cutdown(realtitle, 25) + ")"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					selSongLanguage.setText("(" + newsong.getSprache() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					selSongTitle.setText(""); //$NON-NLS-1$
					selSongRealTitle.setText(""); //$NON-NLS-1$
					selSongLanguage.setText(""); //$NON-NLS-1$
				}
			}
		});
		
		KeyListener myKeyListener =
			new KeyListener() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_S) {
					    showActualSong();
					} else if (e.getKeyCode() == KeyEvent.VK_B || e.getKeyCode() == KeyEvent.VK_H) {
					    try {
							int selected_was_list = list.getSelectedIndex();
							int selected_was = -1;
							if (beamerview != null) {
								selected_was = structure.findSong(beamerview.getActualSong());
							}
							list.removeSelectionInterval(0, list.getModel().getSize() - 1);
							showActualSong();
							if (selected_was != -1 && selected_was+1 < structure.getSongCount()) {
								list.setSelectedIndex(selected_was+1);
							} else if (selected_was != -1) {
								list.setSelectedIndex(selected_was);
							} else {
								list.setSelectedIndex(selected_was_list);
							}
							goButton.requestFocus();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_L) {
						list.requestFocus();
					} else if (e.getKeyCode() == KeyEvent.VK_N) {
					    if (beamerview != null && beamerview.getActualSong() != null && beamerview.getActualSong().getID() != -2) {
							try {
								int newIndex = structure.findSong(beamerview.getActualSong()) + 1;
								if (newIndex < list.getModel().getSize() && newIndex >= 0) {
									list.setSelectedIndex(newIndex);
									showActualSong();
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else {
							// einfach nur die Markierung verschieben
							int to_select = list.getSelectedIndex()+1;
							if (to_select<structure.getSongCount()) {
								list.setSelectedIndex(to_select);
							}
						}
					} else if (e.getKeyCode() == KeyEvent.VK_P) {
					    do_prev();
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
		list.addKeyListener(myKeyListener);
		upButton.addKeyListener(myKeyListener);
		downButton.addKeyListener(myKeyListener);
		deleteButton.addKeyListener(myKeyListener);
		goButton.addKeyListener(myKeyListener);
		nextButton.addKeyListener(myKeyListener);
		previousButton.addKeyListener(myKeyListener);
		nextFoilButton.addKeyListener(myKeyListener);
		previousFoilButton.addKeyListener(myKeyListener);
		goFoilButton.addKeyListener(myKeyListener);
		goFoilBox.addKeyListener(myKeyListener);
		startFoilButton.addKeyListener(myKeyListener);
		startFoilBox.addKeyListener(myKeyListener);
		emptyButton.addKeyListener(myKeyListener);
		logoButton.addKeyListener(myKeyListener);
		hidePresentationButton.addKeyListener(myKeyListener);
		
		upButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_up();
				}
			}
		);
		downButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_down();
				}
			}
		);
		deleteButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_delete();
				}
			}
		);
		goButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showActualSong();
					nextButton.requestFocus();
				}
			}
		);
		nextButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_next();
				}
			}
		);
		previousButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_prev();
				}
			}
		);
		nextFoilButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (beamerview != null && beamerview.getActualSong() != null && beamerview.getActualSong().getID() != -2) {
						try {
							aktuelleFolie = beamerview.nextFoil();
							if (((Boolean)getOptions().get("mf")).booleanValue()) { //$NON-NLS-1$
								actualSongFolie.setText(Messages.getString("BeamerGUI.34") + (aktuelleFolie+1) + Messages.getString("BeamerGUI.35") + beamerview.getActualSong().getFoilCount()); //$NON-NLS-1$ //$NON-NLS-2$
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		);
		goFoilButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (beamerview != null && beamerview.getActualSong() != null && beamerview.getActualSong().getID() != -2) {
						try {
							if (beamerview.goToFoil(goFoilBox.getSelectedIndex())) {
								aktuelleFolie = goFoilBox.getSelectedIndex();
							}
							if (((Boolean)getOptions().get("mf")).booleanValue()) { //$NON-NLS-1$
								actualSongFolie.setText(Messages.getString("BeamerGUI.37") + (aktuelleFolie+1) + Messages.getString("BeamerGUI.38") + beamerview.getActualSong().getFoilCount()); //$NON-NLS-1$ //$NON-NLS-2$
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		);
		startFoilButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				    showActualSong_Foil(startFoilBox.getSelectedIndex());
				}
			}
		);
		previousFoilButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (beamerview != null && beamerview.getActualSong() != null && beamerview.getActualSong().getID() != -2) {
						try {
							aktuelleFolie = beamerview.previousFoil();
							if (((Boolean)getOptions().get("mf")).booleanValue()) { //$NON-NLS-1$
								actualSongFolie.setText(Messages.getString("BeamerGUI.40") + (aktuelleFolie+1) + Messages.getString("BeamerGUI.41") + beamerview.getActualSong().getFoilCount()); //$NON-NLS-1$ //$NON-NLS-2$
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		);
		hidePresentationButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
					    if (beamerview!=null && hidePresentationButton.getText().equals(Messages.getString("BeamerGUI.0"))) { //$NON-NLS-1$
					        hidePresentationButton.setText(Messages.getString("BeamerGUI.107")); //$NON-NLS-1$
					        beamerview.hide();
					    } else if (beamerview!=null) {
					        hidePresentationButton.setText(Messages.getString("BeamerGUI.108")); //$NON-NLS-1$
					        beamerview.show();
					    }
						
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		);
		emptyButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_empty();
				}
			}
		);
		logoButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_empty();
					do_showlogo();
				}
			}
		);
		this.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					exit();
				}
			}
		);
		menuitem_file_new.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					newlist();
				}
			}
		);
		menuitem_file_open.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openlist();
				}
			}
		);
		menuitem_file_save.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					savelist();
				}
			}
		);
		menuitem_file_saveas.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					savelistas();
				}
			}
		);
		menuitem_file_sorttitle.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sortlist(true);
				}
			}
		);
		menuitem_file_sortbegin.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sortlist(false);
				}
			}
		);
		menuitem_window_exit.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exit();
				}
			}
		);
		// EVENT HANDLER ENDE ========================================
		
		// alles f�r globalKeyListener registrieren:
		parent.registerGKL(getContentPane());
		
		setVisible(true);
	}
	
	public void updateJumpButtons() {
		reur_aussen.removeAll();
		if (list.getSelectedValue() != null) {
			java.util.List[] lists = beamerview.getTextWithPositions();
			for (int i = 0; i < lists[0].size(); i++) {
				String txt = (String)lists[0].get(i);
				final Integer pos = (Integer)lists[1].get(i);
				// create new button in reur_aussen
				JButton butt = new JButton();
				butt.setText(txt.substring(0, 10) + "..."); //$NON-NLS-1$
				butt.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (scrollTimeline!=null && scrollTimeline.getState()==TimelineState.PLAYING_FORWARD) {
							scrollTimeline.cancel();
						}
						scrollTimeline = new Timeline(beamerview.scrollPane.getViewport());
						scrollTimeline.addPropertyToInterpolateTo("viewPosition", new Point(0, pos.intValue())); //$NON-NLS-1$
						scrollTimeline.setDuration(1200);
						scrollTimeline.play();
					}
				});
				reur_aussen.add(butt);
			}
		}
	}
	
	public void sortlist(boolean byTitle) {
		// Liste alphabetisch sortieren nach Titel wenn afterTitle==true,
		// sonst nach Liedanfang
		if (byTitle) {
			structure.sortByTitle();
		} else {
			structure.sortByBegin();
		}
		reloadModel(true);
	}
	
	public void scroll_up() {
		if (beamerview != null) {
			beamerview.scroll_up();
		}
	}
	
	public void scroll_down() {
		if (beamerview != null) {
			beamerview.scroll_down();
		}
	}
	
	public void do_empty() {
		try {
			int selected_was_list = list.getSelectedIndex();
			int selected_was = -1;
			if (beamerview != null && beamerview.getActualSong()!=null) {
				selected_was = structure.findSong(beamerview.getActualSong());
			}
			list.removeSelectionInterval(0, list.getModel().getSize() - 1);
			showActualSong();
			if (selected_was != -1 && selected_was+1 < structure.getSongCount()) {
				list.setSelectedIndex(selected_was+1);
			} else if (selected_was != -1) {
				list.setSelectedIndex(selected_was);
			} else {
				list.setSelectedIndex(selected_was_list);
			}
			goButton.requestFocus();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void do_showlogo() {
		try {
			showLogoImage();
			goButton.requestFocus();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void do_next() {
		if (beamerview != null && beamerview.getActualSong() != null && beamerview.getActualSong().getID() != -2) {
			try {
				int newIndex = structure.findSong(beamerview.getActualSong()) + 1;
				if (newIndex < list.getModel().getSize() && newIndex >= 0) {
					list.setSelectedIndex(newIndex);
					showActualSong();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			// einfach nur die Markierung verschieben
			int to_select = list.getSelectedIndex()+1;
			if (to_select<structure.getSongCount()) {
				list.setSelectedIndex(to_select);
			}
		}
	}
	
	public void do_prev() {
		if (beamerview != null && beamerview.getActualSong() != null && beamerview.getActualSong().getID() != -2) {
			try {
				int newIndex = structure.findSong(beamerview.getActualSong()) - 1;
				if (newIndex < list.getModel().getSize() && newIndex >= 0) {
					list.setSelectedIndex(newIndex);
					showActualSong();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			// einfach nur die Markierung verschieben
			int to_select = list.getSelectedIndex()-1;
			if (to_select>=0) {
				list.setSelectedIndex(to_select);
			}
		}
	}
	
	public void reloadModel(boolean reselect) {
		// structure neu darstellen
		int index = list.getSelectedIndex();
		list.setListData(structure.getSongs(((Boolean)parent.getOptions().get("bst")).booleanValue())); //$NON-NLS-1$
		if (reselect) {
			list.setSelectedIndex(index);
		}
		// neue Texte in die Labels
		if (beamerview != null) {
			Song song = beamerview.getActualSong();
			boolean bst = ((Boolean)parent.getOptions().get("bst")).booleanValue(); //$NON-NLS-1$
			if (song != null) {
				String title = structure.getTitleOf(song, bst);
				String realtitle = getNormalTitleByID(song.getID());
				//System.out.println(title + " - " + realtitle);
				if (title.equals(realtitle)) {
					actualSongTitel.setText(title);
					actualSongRealTitel.setText(" "); //$NON-NLS-1$
				} else {
					actualSongTitel.setText(title);
					actualSongRealTitel.setText(Messages.getString("BeamerGUI.45") + realtitle + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				actualSongTitel.setText(" "); //$NON-NLS-1$
				actualSongRealTitel.setText(" "); //$NON-NLS-1$
			}
		}
	}
	
	protected void do_up() {
		if (list.getSelectedValue() != null ) {
			int newindex = structure.moveUp(list.getSelectedIndex());
			list.setListData(structure.getSongs(((Boolean)parent.getOptions().get("bst")).booleanValue())); //$NON-NLS-1$
			list.setSelectedIndex(newindex);
		}
	}
	
	public String getNormalTitle(int index) {
		Song song = structure.songAt(index);
		if (song != null) {
			return song.getTitel();
		} else {
			return ""; //$NON-NLS-1$
		}
	}
	
	public String getNormalTitleByID(int id) {
		Song song = structure.getSongByID(id);
		try {
			return song.getTitel();
		} catch(Exception ex) {
			return ""; //$NON-NLS-1$
		}
	}
	
	protected void do_down() {
		if (list.getSelectedValue() != null ) {
			int newindex = structure.moveDown(list.getSelectedIndex());
			list.setListData(structure.getSongs(((Boolean)parent.getOptions().get("bst")).booleanValue())); //$NON-NLS-1$
			list.setSelectedIndex(newindex);
		}
	}
	
	protected void do_delete() {
		if (list.getSelectedValue() != null ) {
			int prevSel = list.getSelectedIndex();
			structure.delete(list.getSelectedIndex());
			list.setListData(structure.getSongs(((Boolean)parent.getOptions().get("bst")).booleanValue())); //$NON-NLS-1$
			try {
				list.getSelectionModel().setSelectionInterval(prevSel, prevSel);
			} catch (Exception ex) {
				list.getSelectionModel().setSelectionInterval(prevSel-1, prevSel-1);
			}
		}
	}
	
	private void fillFoilList(Song song) {
		if (song.getID() == -2 || !((Boolean)parent.getOptions().get("mf")).booleanValue()) { //$NON-NLS-1$
			// leerer Song oder keine Folien erlaubt
			goFoilBox.removeAllItems();
			goFoilBox.setEnabled(false);
			goFoilButton.setEnabled(false);
			nextFoilButton.setEnabled(false);
			previousFoilButton.setEnabled(false);
		} else {
			goFoilBox.removeAllItems();
			goFoilBox.setEnabled(true);
			goFoilButton.setEnabled(true);
			nextFoilButton.setEnabled(true);
			previousFoilButton.setEnabled(true);
			for (int i = 0; i < song.getFoilCount(); i++) {
				goFoilBox.addItem(Messages.getString("BeamerGUI.55") + (i+1) + ": " + ( song.getFoilText(i).length() <= 13 ? song.getFoilText(i) : song.getFoilText(i).substring(0, 13)) + "..."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}
			goFoilBox.setSelectedIndex(0);
		}
	}
	
	private void fillStartFoilList() {
		Song song = (Song)list.getSelectedValue();
		if (song != null) {
			if (song.getID() == -2 || !((Boolean)parent.getOptions().get("mf")).booleanValue()) { //$NON-NLS-1$
				// leerer Song oder keine Folien erlaubt
				startFoilBox.removeAllItems();
				startFoilBox.setEnabled(false);
				startFoilButton.setEnabled(false);
			} else {
				startFoilBox.removeAllItems();
				startFoilBox.setEnabled(true);
				startFoilButton.setEnabled(true);
				for (int i = 0; i < song.getFoilCount(); i++) {
					startFoilBox.addItem(Messages.getString("BeamerGUI.55") + (i+1) + ": " + ( song.getFoilText(i).length()<=13 ? song.getFoilText(i) : song.getFoilText(i).substring(0, 13)) + "..."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				}
				startFoilBox.setSelectedIndex(0);
			}
		}
	}
	
	public ImagePanel getLogoForBack() {
		return parent.getLogoForBack();
	}
	
	// EVENT HANLDER HILFSMETHODEN:
	public final void showActualSong() {
		showActualSong_Foil(0);
	}
	private final void showLogoImage() {
		titelfont = (Font)getOptions().get("tifo"); //$NON-NLS-1$
		textfont = (Font)getOptions().get("tefo"); //$NON-NLS-1$
		translatefont = (Font)getOptions().get("trfo"); //$NON-NLS-1$
		copyrightfont = (Font)getOptions().get("cofo"); //$NON-NLS-1$
		int units = 10;
		logoname = (String)getOptions().get("logo"); //$NON-NLS-1$
		
		if (getLogoForBack()!=null && beamerview == null) {
			try {
				// neuen BV mit selektiertem Lied auf den letzten verf�gbaren Bildschirm legen
				GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice[] devices = env.getScreenDevices();
				GraphicsDevice device = devices[devices.length - 1];
				Dimension dim = new Dimension(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());
				GraphicsConfiguration gc = device.getDefaultConfiguration();
				
				beamerview = new BeamerView(null, titelfont, textfont, translatefont, copyrightfont, getLogoForBack(), false, getThis(), gc, BeamerView.FOIL_NUMBER_TO_SHOW_LOGO);
				getThis().toFront();
				getThis().requestFocus();
			} catch (Exception ex) {
				System.out.println("CAUGHT:"); //$NON-NLS-1$
				ex.printStackTrace();
			}
		} else if (getLogoForBack()!=null && beamerview != null) {
			// vorhandenen BV mit neuem Lied f�llen
			beamerview.showSong(null, titelfont, textfont, translatefont, copyrightfont, false, BeamerView.FOIL_NUMBER_TO_SHOW_LOGO);
		} else {
			// Fehler anzeigen
			JOptionPane.showMessageDialog(getThis(), Messages.getString("BeamerGUI.6"), Messages.getString("BeamerGUI.14"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	private final void showActualSong_Foil(final int foil) {
	    titelfont = (Font)getOptions().get("tifo"); //$NON-NLS-1$
		textfont = (Font)getOptions().get("tefo"); //$NON-NLS-1$
		translatefont = (Font)getOptions().get("trfo"); //$NON-NLS-1$
		copyrightfont = (Font)getOptions().get("cofo"); //$NON-NLS-1$
		int units = 10;
		logoname = (String)getOptions().get("logo"); //$NON-NLS-1$
		
		if (beamerview == null && list.getSelectedValue() != null) {
			try {
				// neuen BV mit selektiertem Lied auf den letzten verf�gbaren Bildschirm legen
				GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice[] devices = env.getScreenDevices();
				GraphicsDevice device = devices[devices.length - 1];
				Dimension dim = new Dimension(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());
				GraphicsConfiguration gc = device.getDefaultConfiguration();
				Song newsong = (Song)list.getSelectedValue();
				fillFoilList(newsong);
				aktuelleFolie = foil;
				if (((Boolean)parent.getOptions().get("mf")).booleanValue()) { //$NON-NLS-1$
					actualSongFolie.setText(Messages.getString("BeamerGUI.59") + (aktuelleFolie+1) + Messages.getString("BeamerGUI.60") + newsong.getFoilCount()); //$NON-NLS-1$ //$NON-NLS-2$
				}
				
				String title = newsong.getTitel();
				String realtitle = getNormalTitleByID(newsong.getID());
				if (StringTools.replace(StringTools.replace(title, ".", ""), ",", "").equalsIgnoreCase(StringTools.replace(StringTools.replace(realtitle, ".", ""), ",", ""))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
					actualSongTitel.setText(title);
					actualSongRealTitel.setText(" "); //$NON-NLS-1$
				} else {
					actualSongTitel.setText(title);
					actualSongRealTitel.setText(Messages.getString("BeamerGUI.70") + realtitle + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				
				actualSongSprache.setText("(" + newsong.getSprache() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				beamerview = new BeamerView(newsong, titelfont, textfont, translatefont, copyrightfont, getLogoForBack(), false, getThis(), gc, foil);
				// ScrollBar �bernehmen
				try {
					reur_innen.removeAll();
					for (int i = 0; i < reur_innen.getMouseWheelListeners().length; i++) {
					    reur_innen.removeMouseWheelListener(reur_innen.getMouseWheelListeners()[i]);
					}
					for (int i = 0; i < presentationScrollBar.getMouseWheelListeners().length; i++) {
					    presentationScrollBar.removeMouseWheelListener(presentationScrollBar.getMouseWheelListeners()[i]);
					}
				} catch (Exception ex) {}
				presentationScrollBar = beamerview.getScrollPane().getVerticalScrollBar();
				presentationScrollBar.setUnitIncrement(units);
				reur_innen_gridbag.setConstraints(presentationScrollBar, (GridBagConstraints)reur_innen_constraints.clone());
				reur_innen.add(presentationScrollBar);
				reur_innen.validate();
				for (int i = 0; i < beamerview.getScrollPane().getMouseWheelListeners().length; i++) {
				    reur_innen.addMouseWheelListener(beamerview.getScrollPane().getMouseWheelListeners()[i]);
				    presentationScrollBar.addMouseWheelListener(beamerview.getScrollPane().getMouseWheelListeners()[i]);
				}
				getThis().toFront();
				getThis().requestFocus();
			} catch (Exception ex) {
				System.out.println("CAUGHT:"); //$NON-NLS-1$
				ex.printStackTrace();
			}
		} else if (beamerview != null && list.getSelectedValue() != null) {
			// vorhandenen BV mit neuem Lied f�llen
		    Song newsong = (Song)list.getSelectedValue();
			fillFoilList(newsong);
			aktuelleFolie = foil;
			
			String title = newsong.getTitel();
			String realtitle = getNormalTitleByID(newsong.getID());
			if (title.equalsIgnoreCase(realtitle)) {
				actualSongTitel.setText(title);
				actualSongRealTitel.setText(" "); //$NON-NLS-1$
			} else {
				actualSongTitel.setText(title);
				actualSongRealTitel.setText(Messages.getString("BeamerGUI.76") + realtitle + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			actualSongSprache.setText("(" + newsong.getSprache() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			if (((Boolean)parent.getOptions().get("mf")).booleanValue()) { //$NON-NLS-1$
				actualSongFolie.setText(Messages.getString("BeamerGUI.81") + (aktuelleFolie+1) + Messages.getString("BeamerGUI.82") + newsong.getFoilCount()); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
			    actualSongFolie.setText(" "); //$NON-NLS-1$
			}
			beamerview.showSong(newsong, titelfont, textfont, translatefont, copyrightfont, false, foil);
			// ScrollBar �bernehmen
			try {
				reur_innen.removeAll();
				for (int i = 0; i < reur_innen.getMouseWheelListeners().length; i++) {
				    reur_innen.removeMouseWheelListener(reur_innen.getMouseWheelListeners()[i]);
				}
				for (int i = 0; i < presentationScrollBar.getMouseWheelListeners().length; i++) {
				    presentationScrollBar.removeMouseWheelListener(presentationScrollBar.getMouseWheelListeners()[i]);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			presentationScrollBar = beamerview.getScrollPane().getVerticalScrollBar();
			presentationScrollBar.setUnitIncrement(units);
			reur_innen_gridbag.setConstraints(presentationScrollBar, reur_innen_constraints);
			reur_innen.add(presentationScrollBar);
			for (int i = 0; i < beamerview.getScrollPane().getMouseWheelListeners().length; i++) {
			    reur_innen.addMouseWheelListener(beamerview.getScrollPane().getMouseWheelListeners()[i]);
			    presentationScrollBar.addMouseWheelListener(beamerview.getScrollPane().getMouseWheelListeners()[i]);
			}
			reur_innen.validate();
		} else if (beamerview == null && list.getSelectedValue() == null) {
			try {
				// neuen BV auf den letzten verf�gbaren Bildschirm legen
				GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice[] devices = env.getScreenDevices();
				GraphicsDevice device = devices[devices.length - 1];
				Dimension dim = new Dimension(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());
				GraphicsConfiguration gc = device.getDefaultConfiguration();
				Song newsong = new Song( -2);
				aktuelleFolie = foil;
				fillFoilList(newsong);
				newsong.setTitel(""); //$NON-NLS-1$
				actualSongTitel.setText(" "); //$NON-NLS-1$
				actualSongRealTitel.setText(" "); //$NON-NLS-1$
				actualSongSprache.setText(" "); //$NON-NLS-1$
				actualSongFolie.setText(" "); //$NON-NLS-1$
				beamerview = new BeamerView(newsong, titelfont, textfont, translatefont, copyrightfont, getLogoForBack(), false, getThis(), gc, foil);
				// ScrollBar �bernehmen
				try {
					reur_innen.removeAll();
					for (int i = 0; i < reur_innen.getMouseWheelListeners().length; i++) {
					    reur_innen.removeMouseWheelListener(reur_innen.getMouseWheelListeners()[i]);
					}
					for (int i = 0; i < presentationScrollBar.getMouseWheelListeners().length; i++) {
					    presentationScrollBar.removeMouseWheelListener(presentationScrollBar.getMouseWheelListeners()[i]);
					}
				} catch (Exception ex) {}
				for (int i = 0; i < beamerview.getScrollPane().getMouseWheelListeners().length; i++) {
				    reur_innen.addMouseWheelListener(beamerview.getScrollPane().getMouseWheelListeners()[i]);
				    if (presentationScrollBar!=null && beamerview!=null && beamerview.getScrollPane()!=null) {
				    	presentationScrollBar.addMouseWheelListener(beamerview.getScrollPane().getMouseWheelListeners()[i]);
				    }
				}
				reur_innen.validate();
				getThis().toFront();
				getThis().requestFocus();
			} catch (Exception ex) {
				System.out.println("CAUGHT:"); //$NON-NLS-1$
				ex.printStackTrace();
			}
		} else if (beamerview != null && list.getSelectedValue() == null) {
			// BV l�schen
			Song newsong = new Song( -2);
			aktuelleFolie = foil;
			fillFoilList(newsong);
			newsong.setTitel(""); //$NON-NLS-1$
			actualSongTitel.setText(" "); //$NON-NLS-1$
			actualSongRealTitel.setText(" "); //$NON-NLS-1$
			actualSongSprache.setText(" "); //$NON-NLS-1$
			actualSongFolie.setText(" "); //$NON-NLS-1$
			beamerview.showSong(newsong, titelfont, textfont, translatefont, copyrightfont, false, foil);
			// ScrollBar �bernehmen
			try {
				reur_innen.removeAll();
				for (int i = 0; i < reur_innen.getMouseWheelListeners().length; i++) {
				    reur_innen.removeMouseWheelListener(reur_innen.getMouseWheelListeners()[i]);
				}
				for (int i = 0; i < presentationScrollBar.getMouseWheelListeners().length; i++) {
				    presentationScrollBar.removeMouseWheelListener(presentationScrollBar.getMouseWheelListeners()[i]);
				}
			} catch (Exception ex) {}
			for (int i = 0; i < beamerview.getScrollPane().getMouseWheelListeners().length; i++) {
			    reur_innen.addMouseWheelListener(beamerview.getScrollPane().getMouseWheelListeners()[i]);
			    if (presentationScrollBar!=null && beamerview!=null && beamerview.getScrollPane()!=null) {
			    	presentationScrollBar.addMouseWheelListener(beamerview.getScrollPane().getMouseWheelListeners()[i]);
			    }
			}
			reur_innen.validate();
		}
	}
	
	private final void exit() {
		int answer = JOptionPane.YES_OPTION;
		if (beamerview != null) {
			try {
				answer = JOptionPane.showConfirmDialog(this, Messages.getString("BeamerGUI.92"), Messages.getString("BeamerGUI.93"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (answer == JOptionPane.YES_OPTION ) {
			parent.setBeamerGuiToNull();
			if (beamerview != null) {
				closing = true;
				beamerview.exit();
			}
			this.setVisible(false);
			this.dispose();
		}
	}
	
	protected void newlist() {
		this.structure = new Structure(parent);
		list.setListData(structure.getSongs(((Boolean)parent.getOptions().get("bst")).booleanValue())); //$NON-NLS-1$
	}
	
	protected void openlist() {
		// jetzt Dateiauswahl anzeigen:
		JFileChooser pChooser = new JFileChooser();
		pChooser.setDialogTitle(Messages.getString("BeamerGUI.95")); //$NON-NLS-1$
		org.zephyrsoft.util.CustomFileFilter filter = new org.zephyrsoft.util.CustomFileFilter(Messages.getString("BeamerGUI.97"), new String[] {".spr"}); //$NON-NLS-1$ //$NON-NLS-2$
		pChooser.addChoosableFileFilter(filter);
		int iValue = pChooser.showOpenDialog(this);
		
		if (iValue == JFileChooser.APPROVE_OPTION) {
			loadFile(pChooser.getSelectedFile().toString());
		}
	}
	
	public void loadFile(String file_name) {
		File fFile = new File(file_name);
		if (fFile.exists()) {
			structure.loadFromFile(fFile);
			list.setListData(structure.getSongs(((Boolean)parent.getOptions().get("bst")).booleanValue())); //$NON-NLS-1$
			filename = fFile.getAbsolutePath();
		} else {
			JOptionPane.showMessageDialog(this,
										  Messages.getString("BeamerGUI.99"), //$NON-NLS-1$
										  Messages.getString("BeamerGUI.100"), //$NON-NLS-1$
										  JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void savelist() {
		if (filename.equals("")) { //$NON-NLS-1$
			// noch nicht gespeichert
			savelistas();
		} else {
			structure.saveToFile(new File(filename));
		}
	}
	
	protected void savelistas() {
		// jetzt Dateiauswahl anzeigen:
		JFileChooser pChooser = new JFileChooser();
		pChooser.setDialogTitle(Messages.getString("BeamerGUI.102")); //$NON-NLS-1$
		org.zephyrsoft.util.CustomFileFilter filter = new org.zephyrsoft.util.CustomFileFilter(Messages.getString("BeamerGUI.104"), new String[] {".spr"}); //$NON-NLS-1$ //$NON-NLS-2$
		pChooser.addChoosableFileFilter(filter);
		int iValue = pChooser.showSaveDialog(this);
		
		if (iValue == JFileChooser.APPROVE_OPTION) {
			File fFile = new File(pChooser.getSelectedFile().toString());
			try {
				structure.saveToFile(fFile);
				filename = fFile.getAbsolutePath();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this,
											  Messages.getString("BeamerGUI.105"), //$NON-NLS-1$
											  Messages.getString("BeamerGUI.106"), //$NON-NLS-1$
											  JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public BeamerGUI getThis() {
		return this;
	}
	
	public void setBeamerViewToNull() {
		beamerview = null;
	}
	
	public Song getSelectedSong() {
		return (Song)list.getSelectedValue();
	}
	
	public DSListener getDSListener() {
		return dslistener;
	}
	
	public void addSong(Song song) {
		// Song aufnehmen und ans Ende stellen, auch wenn schon vorhanden
		try {
			structure.addSong(song);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		// Liste neu laden
		list.setListData(structure.getSongs(((Boolean)parent.getOptions().get("bst")).booleanValue())); //$NON-NLS-1$
		// den letzten Song markieren
		list.setSelectedIndex(structure.getSongCount()-1);
		list.requestFocus();
	}
	
	public Options getOptions() {
		return parent.getOptions();
	}
}
