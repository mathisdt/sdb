package org.zephyrsoft.sdb;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

import org.zephyrsoft.sdb.structure.*;
import org.zephyrsoft.util.*;

import com.lowagie.text.*;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;

/**
 * Druck-Voransicht für die Lieder-Datenbank
 * @author Mathis Dirksen-Thedens
 */

public class PrintView extends JFrame {

	//public static int ANZAHL_KORREKTUR_LEERZEICHEN = 10;
	
	private boolean debug = false;
	
	JToolBar toolbar = null;
	JButton button_print = null;
	JButton button_pdf = null;
	JButton button_allpdf = null;
	JButton button_close = null;
	GUI parent = null;
	
	JPanel contentPane = null;
	BorderLayout borderLayout = null;
	JScrollPane scrollPane = null;
	JPanel back = null;
	
	Song mysong = null;
	java.awt.Font mytitelfont = null;
	java.awt.Font mytextfont = null;
	java.awt.Font mytranslatefont = null;
	java.awt.Font mycopyrightfont = null;
	boolean myprintaccords = true;
	
	public PrintView(GUI gui, Song song, java.awt.Font titelfont, java.awt.Font textfont, java.awt.Font translatefont, java.awt.Font copyrightfont, boolean printAccords, boolean printing, int abstandLinks, int abstandOben) {
		super(Messages.getString("PrintView.0") + song.getTitel()); //$NON-NLS-1$
		
		parent = gui;
		mysong = song;
		mytitelfont = titelfont;
		mytextfont = textfont;
		mytranslatefont = translatefont;
		mycopyrightfont = copyrightfont;
		myprintaccords = printAccords;
		
		// Aussehen
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		if ( lookAndFeel != null ) {
			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (Exception e) {
				System.out.println("Look&Feel konnte nicht gesetzt werden."); //$NON-NLS-1$
			}
		}
		setIconImage((new ImageIcon("res/icon.gif")).getImage()); //$NON-NLS-1$
		
		// GUI-Elemente definieren
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		button_print = new JButton(Messages.getString("PrintView.3")); //$NON-NLS-1$
		button_pdf = new JButton(Messages.getString("PrintView.4")); //$NON-NLS-1$
		button_allpdf = new JButton(Messages.getString("PrintView.1")); //$NON-NLS-1$
		button_close = new JButton(Messages.getString("PrintView.5")); //$NON-NLS-1$
		toolbar.add(button_print);
		toolbar.add(button_pdf);
		toolbar.add(button_allpdf);
		toolbar.add(button_close);
		contentPane = new JPanel();
		borderLayout = new BorderLayout();
		contentPane.setLayout(borderLayout);
		this.setContentPane(contentPane);
		contentPane.add(toolbar, BorderLayout.NORTH);
		scrollPane = new JScrollPane();
		back = new JPanel();
		back.setBackground(Color.white);
		back.setLayout(null);
		
		// Text draufschreiben:
		JTextArea titel = new JTextArea(song.getTitel()); // + StringTools.repeat(" ", ANZAHL_KORREKTUR_LEERZEICHEN)); //$NON-NLS-1$
		titel.setEditable(false);
		JTextPane text = new JTextPane();
		if ( printAccords ) {
			text.setText(song.getTextAndAccordsInFont(textfont, printing));
		} else {
			text.setText(song.getOnlyText());
		}
		
		// Text nach reg. Ausdruck "[.+]" durchsuchen
		Vector vonalle = new Vector();
		Vector bisalle = new Vector();
		String text1 = text.getText();
		int von = text1.indexOf("["); //$NON-NLS-1$
		while (von >= 0) {
		    int vorher = 0; // zählt Leerzeichen vorher
		    while (text1.substring(von-vorher-1,von-vorher).equals(" ")) { //$NON-NLS-1$
			    vorher++;
			}
		    text1 = text1.substring(0, von) + text1.substring(von+1);
		    vonalle.addElement(new Integer(von-vorher));
		    int bis = text1.indexOf("]", von); //$NON-NLS-1$
		    if (bis == -1) {
		        bis = text1.substring(von).length();
		        text1 += "]"; //$NON-NLS-1$
		    }
		    int nachher = 0; // zählt Leerzeichen nachher
			while (text1.substring(bis+nachher+1,bis+nachher+2).equals(" ")) { //$NON-NLS-1$
			    nachher++;
			}
			text1 = text1.substring(0, bis) + text1.substring(bis+1);
		    bisalle.addElement(new Integer(bis+nachher));
		    von = text1.indexOf("["); //$NON-NLS-1$
		}
		text.setText(text1 + "\n\n\n" + song.getCopyright()); //$NON-NLS-1$
		text.setFont(textfont);
		
		StyledDocument doc = text.getStyledDocument();
		Style defaultstyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		
		Style titlestyle = doc.addStyle("title", defaultstyle); //$NON-NLS-1$
		StyleConstants.setItalic(titlestyle, titelfont.isItalic());
		StyleConstants.setBold(titlestyle, titelfont.isBold());
		StyleConstants.setFontFamily(titlestyle, titelfont.getFamily());
		StyleConstants.setFontSize(titlestyle, titelfont.getSize());
		
		Style textstyle = doc.addStyle("text", defaultstyle); //$NON-NLS-1$
		StyleConstants.setItalic(textstyle, textfont.isItalic());
		StyleConstants.setBold(textstyle, textfont.isBold());
		StyleConstants.setFontFamily(textstyle, textfont.getFamily());
		StyleConstants.setFontSize(textstyle, textfont.getSize());
		
		Style translatestyle = doc.addStyle("translate", defaultstyle); //$NON-NLS-1$
		StyleConstants.setItalic(translatestyle, translatefont.isItalic());
		StyleConstants.setBold(translatestyle, translatefont.isBold());
		StyleConstants.setFontFamily(translatestyle, translatefont.getFamily());
		StyleConstants.setFontSize(translatestyle, translatefont.getSize());
		
		Style copyrightstyle = doc.addStyle("copyright", defaultstyle); //$NON-NLS-1$
		StyleConstants.setItalic(copyrightstyle, copyrightfont.isItalic());
		StyleConstants.setBold(copyrightstyle, copyrightfont.isBold());
		StyleConstants.setFontFamily(copyrightstyle, copyrightfont.getFamily());
		StyleConstants.setFontSize(copyrightstyle, copyrightfont.getSize());
		
		// Text-Style zuweisen
		doc.setCharacterAttributes(0, text1.length(), doc.getStyle("text"), false); //$NON-NLS-1$
		
		// Translate-Style zuweisen
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
		    doc.setCharacterAttributes(von1, bis1-von1, doc.getStyle("translate"), false); //$NON-NLS-1$
		}
		
		// Copyright-Style zuweisen
		doc.setCharacterAttributes(text1.length(), text1.length() + 3 + song.getCopyright().length(), doc.getStyle("copyright"), false); //$NON-NLS-1$
		
		text.setEditable(false);
		text.setOpaque(false);
		text.setCaretPosition(0);
//		JTextArea copyright = new JTextArea(song.getCopyright() + StringTools.repeat(" ", ANZAHL_KORREKTUR_LEERZEICHEN)); //$NON-NLS-1$
//		copyright.setEditable(false);
		
		titel.setFont(titelfont);
//		copyright.setFont(copyrightfont);
		
		if ( abstandLinks < 1 ) {
			abstandLinks = 1;
		}
		if ( abstandOben < 1 ) {
			abstandOben = 1;
		}
		Point punkt = new Point(abstandLinks, abstandOben);
		int abstand = 30;
		
		back.add(titel);
		titel.setSize(titel.getPreferredSize());
		titel.setLocation(punkt);
		punkt.y += titel.getHeight() + abstand;
		back.add(text);
		text.setSize(text.getPreferredSize());
		text.setLocation(punkt);
		punkt.y += text.getHeight() + abstand;
//		back.add(copyright);
//		copyright.setSize(copyright.getPreferredSize());
//		copyright.setLocation(punkt);
		
		// GUI formen
		this.setLocale(java.util.Locale.getDefault());
		
		scrollPane.getViewport().add(back, null);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		pack();
		setSize(new Dimension(640, 480));
		
		// jetzt die Größe von "back" setzen, passend zu allen darauf liegenden Components
		//                        Breite, Höhe
		Dimension size = new Dimension(1, 1);
		Component[] components = back.getComponents();
		for ( int i = 0; i < components.length; i++ ) {
			int component_max_x = components[i].getX() + components[i].getWidth();
			int component_max_y = components[i].getY() + components[i].getHeight();
			if ( component_max_x > size.getWidth() ) {
				size.width = component_max_x;
			}
			if ( component_max_y > size.getHeight() ) {
				size.height = component_max_y;
			}
		}
		size.height += 10;
		size.width += 10;
		back.setPreferredSize(size);
		
		setLocation(new Point(200, 150));
		
		// EVENT HANDLER START =======================================
		button_print.addActionListener(new ActionListener() {
										   public void actionPerformed(ActionEvent e) {
											   printThis();
										   }
									   }
									  );
		button_pdf.addActionListener(new ActionListener() {
										 public void actionPerformed(ActionEvent e) {
											 exportThis();
										 }
									 }
									);
		button_allpdf.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 exportAll();
			 }
		 }
		);
		button_close.addActionListener(new ActionListener() {
										   public void actionPerformed(ActionEvent e) {
											   dispose();
										   }
									   }
									  );
		this.addWindowListener(new WindowAdapter() {
								   public void windowClosing(WindowEvent e) {
									   dispose();
								   }
							   }
							  );
		                      
		// EVENT HANDLER ENDE ========================================
		
		show();
	}
	
	protected void exportAll() {
		int RAND = 60;
		try {
			// Fonts generieren:
			BaseFont font_titel0 = BaseFont.createFont("res/" + mytitelfont.getFamily() + ".ttf", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED); //$NON-NLS-1$ //$NON-NLS-2$
			com.lowagie.text.Font font_titel = new com.lowagie.text.Font(font_titel0, mytitelfont.getSize(), getFontAttributes(mytitelfont));
			BaseFont font_text0 = BaseFont.createFont("res/" + mytextfont.getFamily() + ".ttf", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED); //$NON-NLS-1$ //$NON-NLS-2$
			com.lowagie.text.Font font_text = new com.lowagie.text.Font(font_text0, mytextfont.getSize(), getFontAttributes(mytextfont));
			BaseFont font_translate0 = BaseFont.createFont("res/" + mytranslatefont.getFamily() + ".ttf", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED); //$NON-NLS-1$ //$NON-NLS-2$
			com.lowagie.text.Font font_translate = new com.lowagie.text.Font(font_translate0, mytranslatefont.getSize(), getFontAttributes(mytranslatefont));
			BaseFont font_copyright0 = BaseFont.createFont("res/" + mycopyrightfont.getFamily() + ".ttf", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED); //$NON-NLS-1$ //$NON-NLS-2$
			com.lowagie.text.Font font_copyright = new com.lowagie.text.Font(font_copyright0, mycopyrightfont.getSize(), getFontAttributes(mycopyrightfont));
			
			// creation of the document with a certain size and certain margins
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			// wohin soll geschrieben werden?
			String name = ""; //$NON-NLS-1$
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new CustomFileFilter(".pdf", Messages.getString("PrintView.17"))); //$NON-NLS-1$ //$NON-NLS-2$
			int returnVal = chooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				name = chooser.getSelectedFile().getAbsolutePath();
			}
			if (name == null || name.equals("")) { //$NON-NLS-1$
				name = "allsongs.pdf"; //$NON-NLS-1$
			}
			if (!name.endsWith(".pdf")) { //$NON-NLS-1$
				name += ".pdf"; //$NON-NLS-1$
			}
			File file = new File(name);
			// creation of the different writers
			PdfWriter.getInstance(document, new FileOutputStream(file));
			
			// we open the document for writing
			document.open();
			
			Paragraph par = null;
			String rest = null;
			Structure allsongs1 = parent.getAllSongs();
			allsongs1.sortByTitle();
			Vector allsongs = allsongs1.getSongs();
			par = new Paragraph("", font_titel); //$NON-NLS-1$
			par.add(new Phrase("Verzeichnis")); //$NON-NLS-1$
			document.add(par);
		    par = new Paragraph("", font_text); //$NON-NLS-1$
		    par.add(new Phrase("\n")); //$NON-NLS-1$
			for (int i = 0; i < allsongs.size(); i++) {
				Song asong = (Song)allsongs.elementAt(i);
				par.add(new Phrase(asong.getTitel() + "\n")); //$NON-NLS-1$
			}
			document.add(par);
			document.newPage();
			for (int i = 0; i < allsongs.size(); i++) {
				Song asong = (Song)allsongs.elementAt(i);
				par = new Paragraph(); //$NON-NLS-1$
				par.add(new Chunk(asong.getTitel() + "\n", font_titel));
				document.add(par);
				rest = asong.getTextAndAccordsInFont(mytextfont, true);
				while (rest.length() > 0 && rest.indexOf("[") > -1) { //$NON-NLS-1$
				    int st = rest.indexOf("["); //$NON-NLS-1$
				    int en = rest.indexOf("]", st); //$NON-NLS-1$
				    if (en == -1) {
				        en = rest.length();
				        rest += "]"; //$NON-NLS-1$
				    }
				    String thistext = rest.substring(0, st);
				    String thistranslate = rest.substring(st+1, en);
				    par = new Paragraph(); //$NON-NLS-1$
					par.add(new Chunk(thistext, font_text));
					if (!thistranslate.equals("")) {
						par.add(new Chunk(thistranslate, font_translate));
					}
					par.setSpacingBefore(1f);
					document.add(par);
					rest = rest.substring(en+1);
				}
				par = new Paragraph(); //$NON-NLS-1$
				par.add(new Chunk(rest, font_text));
				document.add(par);
				par = new Paragraph(); //$NON-NLS-1$
				par.add(new Chunk("\n\n" + asong.getCopyright(), font_copyright));
				document.add(par);
				document.newPage();
			}
			
			document.close();
		} catch (Exception ex) {
			System.out.println("CAUGHT:"); //$NON-NLS-1$
			ex.printStackTrace();
		}
	}
	
	protected void exportThis() {
		int RAND = 60;
		try {
			// Fonts generieren:
			BaseFont font_titel0 = BaseFont.createFont("res/" + mytitelfont.getFamily() + ".ttf", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED); //$NON-NLS-1$ //$NON-NLS-2$
			com.lowagie.text.Font font_titel = new com.lowagie.text.Font(font_titel0, mytitelfont.getSize(), getFontAttributes(mytitelfont));
			BaseFont font_text0 = BaseFont.createFont("res/" + mytextfont.getFamily() + ".ttf", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED); //$NON-NLS-1$ //$NON-NLS-2$
			com.lowagie.text.Font font_text = new com.lowagie.text.Font(font_text0, mytextfont.getSize(), getFontAttributes(mytextfont));
			BaseFont font_translate0 = BaseFont.createFont("res/" + mytranslatefont.getFamily() + ".ttf", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED); //$NON-NLS-1$ //$NON-NLS-2$
			com.lowagie.text.Font font_translate = new com.lowagie.text.Font(font_translate0, mytranslatefont.getSize(), getFontAttributes(mytranslatefont));
			BaseFont font_copyright0 = BaseFont.createFont("res/" + mycopyrightfont.getFamily() + ".ttf", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED); //$NON-NLS-1$ //$NON-NLS-2$
			com.lowagie.text.Font font_copyright = new com.lowagie.text.Font(font_copyright0, mycopyrightfont.getSize(), getFontAttributes(mycopyrightfont));
			
			// creation of the document with a certain size and certain margins
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			// wohin soll geschrieben werden?
			String name = ""; //$NON-NLS-1$
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new CustomFileFilter(".pdf", Messages.getString("PrintView.17"))); //$NON-NLS-1$ //$NON-NLS-2$
			int returnVal = chooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				name = chooser.getSelectedFile().getAbsolutePath();
			}
			if (name == null || name.equals("")) { //$NON-NLS-1$
				name = "song.pdf"; //$NON-NLS-1$
			}
			if (!name.endsWith(".pdf")) { //$NON-NLS-1$
				name += ".pdf"; //$NON-NLS-1$
			}
			File file = new File(name);
			// creation of the different writers
			PdfWriter.getInstance(document, new FileOutputStream(file));
			
			// we open the document for writing
			document.open();
			Paragraph par = new Paragraph(); //$NON-NLS-1$
			par.add(new Chunk(mysong.getTitel() + "\n", font_titel));
			document.add(par);
			String rest = mysong.getTextAndAccordsInFont(mytextfont, true);
			while (rest.length() > 0 && rest.indexOf("[") > -1) { //$NON-NLS-1$
			    int st = rest.indexOf("["); //$NON-NLS-1$
			    int en = rest.indexOf("]", st); //$NON-NLS-1$
			    if (en == -1) {
			        en = rest.length();
			        rest += "]"; //$NON-NLS-1$
			    }
			    String thistext = rest.substring(0, st);
			    String thistranslate = rest.substring(st+1, en);
			    par = new Paragraph(); //$NON-NLS-1$
				par.add(new Chunk(thistext, font_text));
				if (!thistranslate.equals("")) {
					par.add(new Chunk(thistranslate, font_translate));
				}
				par.setSpacingBefore(1f);
				document.add(par);
				rest = rest.substring(en+1);
			}
			par = new Paragraph(); //$NON-NLS-1$
			par.add(new Chunk(rest, font_text));
			document.add(par);
			par = new Paragraph(); //$NON-NLS-1$
			par.add(new Chunk("\n\n" + mysong.getCopyright(), font_copyright));
			document.add(par);
			document.close();
		} catch (Exception ex) {
			System.out.println("CAUGHT:"); //$NON-NLS-1$
			ex.printStackTrace();
		}
	}
	
	private int getFontAttributes(java.awt.Font font) {
	    if (font.isBold() && font.isItalic()) {
	        return com.lowagie.text.Font.BOLDITALIC;
	    } else if (font.isBold()) {
	        return com.lowagie.text.Font.BOLD;
	    } else if (font.isItalic()) {
	        return com.lowagie.text.Font.ITALIC;
	    } else {
	        return com.lowagie.text.Font.NORMAL;
	    }
	}
	
	protected void printThis() {
		int RAND = 0;
		try {
			PrintView temp = new PrintView(parent, (Song)mysong.clone(), mytitelfont, mytextfont, mytranslatefont, mycopyrightfont, myprintaccords, true, RAND, RAND);
			PrintUtilities.printComponent(temp.getContent());
			temp.dispose();
		} catch (Exception ex) {
			System.out.println("CAUGHT:"); //$NON-NLS-1$
			ex.printStackTrace();
		}
	}
	
	public JPanel getContent() {
		return back;
	}
}
