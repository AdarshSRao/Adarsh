import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Event;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.util.Iterator;
import javax.swing.undo.UndoManager;

import ch.randelshofer.quaqua.jaguar.Quaqua15JaguarLookAndFeel;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import com.jgoodies.looks.plastic.theme.ExperienceGreen;
import com.jgoodies.looks.plastic.theme.ExperienceRoyale;

import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;


public class MyEditor extends JFrame implements DropTargetListener  {
  private static final long serialVersionUID = 1L; 
	
	Vector<JTextPane> areas = new Vector<JTextPane>() ;
	JTextPane contentArea = new JTextPane(syntax_analysis(Color.GREEN))  ;
	JScrollPane scrollPanel = null  ;
	JMenu fileMenu, editMenu, formatMenu, viewMenu, helpMenu  ;
	JMenuItem newMi, openMi, saveMi, saveAsMi, exitMi, undoMi, cutMi, copyMi, pasteMi, findMi;
	JMenuItem replaceMi, gotoMi, selectallMi, fontMi, statusMi, helpMi, aboutMi;
	String fileName = "untitled.txt" ;
	JToolBar toolBar = new JToolBar();
	JTextField searcher = new JTextField(10);
	ActionMap aMap = new ActionMap();
	JTabbedPane tabPane = new JTabbedPane();
	JButton closeTab = new JButton("X");
	//Vector<JTextPane> vec = new Vector<JTextPane>() ;
	String looknfeels[] = {"Nimbus","Mac","Plastic","Synthetica" } ;
	String plastics[] = {"ExperienceBlue","ExperienceGreen","Royale"};
	JComboBox combo = new JComboBox(looknfeels);
	JList list = new JList(plastics);
	DefaultComboBoxModel comnlis = new DefaultComboBoxModel();
	
	public static final int WIDTH = 300;
 	public static final int HEIGHT = 200;

 	private int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }
 	public void LooknFeel(){
 		String str = (String) combo.getSelectedItem();
		switch(str){
		case "Nimbus" :   NimbusLookAndFeel nim = new NimbusLookAndFeel();
						  try {
							  nim.provideErrorFeedback(rootPane);
							  UIManager.setLookAndFeel(nim);
						  } catch (UnsupportedLookAndFeelException e1) {  } 
						  catch(NullPointerException e1){  }
						  SwingUtilities.updateComponentTreeUI(this);
						  break ;
		
		case "Synthetica" : try {
								UIManager.setLookAndFeel(new SyntheticaStandardLookAndFeel());
							} catch (UnsupportedLookAndFeelException e1) {} catch (ParseException e1) {	}
							SwingUtilities.updateComponentTreeUI(this);
							break ;
							
		case "Plastic" 	: 	PlasticLookAndFeel pal = new Plastic3DLookAndFeel();
							try {
								pal.setCurrentTheme(new ExperienceGreen());
								UIManager.setLookAndFeel(pal);
							} catch (UnsupportedLookAndFeelException e1) {}
							SwingUtilities.updateComponentTreeUI(this);
							break;
			
		case "Mac"      : 	try {
								UIManager.setLookAndFeel(new Quaqua15JaguarLookAndFeel());
							} catch (UnsupportedLookAndFeelException e) {}
							SwingUtilities.updateComponentTreeUI(this);
							break ;
			}
		}
 	public void listSelect(){
 		PlasticLookAndFeel laf = new Plastic3DLookAndFeel();
 		String pl = (String) list.getSelectedValue();	
		switch(pl){
			case  "ExperienceBlue" :
					PlasticLookAndFeel.setCurrentTheme(new ExperienceBlue());
			try {
				UIManager.setLookAndFeel(laf);
			} catch (UnsupportedLookAndFeelException e) {}
			SwingUtilities.updateComponentTreeUI(this);
			
			case "ExperienceGreen"  :
				PlasticLookAndFeel.setCurrentTheme(new ExperienceGreen());
			try {
				UIManager.setLookAndFeel(laf);
			} catch (UnsupportedLookAndFeelException e) {}
				SwingUtilities.updateComponentTreeUI(this);
	
			case "ExperienceRoyle" :
				PlasticLookAndFeel.setCurrentTheme(new ExperienceRoyale());
			try {
				UIManager.setLookAndFeel(laf);
			} catch (UnsupportedLookAndFeelException e) {}
			SwingUtilities.updateComponentTreeUI(this);
		}
	}
 	

    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

	public MyEditor()  {
		this("Untitled - MyEditor") ;
		
	}
	public DefaultStyledDocument syntax_analysis(Color color){
		final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, color);
        final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        DefaultStyledDocument doc = new DefaultStyledDocument() {
          
        	public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = contentArea.getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        if (text.substring(wordL, wordR).matches("(\\W)*(private|public|protected)"))
                            setCharacterAttributes(wordL, wordR - wordL, attr, false);
                        else
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = contentArea.getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);

                if (text.substring(before, after).matches("(\\W)*(private|public|protected)")) {
                    setCharacterAttributes(before, after - before, attr, false);
                } else {
                    setCharacterAttributes(before, after - before, attrBlack, false);
                }
            }
        };  
        return doc ;
	}

	public MyEditor(String title)  {
		super(title) ;		
		//vec.add(contentArea);
	   
	    scrollPanel =   new JScrollPane(contentArea) ;
		JMenuBar mb = new JMenuBar()  ;
		fileMenu = new JMenu("File") ;
		mb.add(fileMenu) ;
		newMi = new JMenuItem("   New   ") ;
		newMi.addActionListener(new NewMiAction()) ;
		newMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		fileMenu.add(newMi) ;
		openMi = new JMenuItem("   Open   ") ;
		openMi.addActionListener(new OpenMiAction()) ;
		openMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		fileMenu.add(openMi)  ;
		fileMenu.addSeparator();
		saveMi = new JMenuItem("   Save   ") ;
		saveMi.addActionListener(new SaveMiAction()) ;
		saveMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		fileMenu.add(saveMi)  ;
		saveAsMi = new JMenuItem("   SaveAs   ") ;
		saveAsMi.addActionListener(new SaveAsMiAction()) ;
		saveAsMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK+KeyEvent.SHIFT_MASK));
		fileMenu.add(saveAsMi);
		fileMenu.addSeparator();
		exitMi = new JMenuItem("   Exit    ") ;
		exitMi.addActionListener(new ExitMiAction()) ;
		fileMenu.add(exitMi)  ;
		
		/* Now the edit bar*/
		
		editMenu = new JMenu("   Edit   ") ;
		mb.add(editMenu) ;
		undoMi= new JMenuItem("   Undo   ");
		undoMi.addActionListener(new UndoMiAction())  ;
		undoMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,ActionEvent.CTRL_MASK));
		editMenu.add(undoMi);
		editMenu.addSeparator();
		
		cutMi= new JMenuItem(new DefaultEditorKit.CutAction());
		cutMi.setText("   Cut   ") ;
		cutMi.setMnemonic(KeyEvent.VK_T) ;
		cutMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
		editMenu.add(cutMi);

		copyMi= new JMenuItem(new DefaultEditorKit.CopyAction());
		copyMi.setText("   Copy   ") ;
		copyMi.setMnemonic(KeyEvent.VK_C) ;
		copyMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
		editMenu.add(copyMi);
		
		pasteMi= new JMenuItem(new DefaultEditorKit.PasteAction());
		pasteMi.setText("   Paste   ") ;
		pasteMi.setMnemonic(KeyEvent.VK_P) ;
		pasteMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
		editMenu.add(pasteMi);
		editMenu.addSeparator();
		
		findMi= new JMenuItem("   Find   ");
		findMi.addActionListener(new FindMiAction())  ;
		findMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,ActionEvent.CTRL_MASK));
		editMenu.add(findMi);
		
		replaceMi= new JMenuItem("   Replace...   ");
		replaceMi.addActionListener(new ReplaceMiAction())  ;
		replaceMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,ActionEvent.CTRL_MASK));
		editMenu.add(replaceMi);
		editMenu.addSeparator();
		
		selectallMi= new JMenuItem()  ; 
		selectallMi.setText("   Select All   ") ;
		selectallMi.setMnemonic(KeyEvent.VK_A) ;
		selectallMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK));
		selectallMi.addActionListener(new SelectallMiAction() )  ;
		editMenu.add(selectallMi);
		
		/* Now the Format bar */
		
		formatMenu = new JMenu("   Format   ") ;
		mb.add(formatMenu) ;
		fontMi= new JMenuItem("Font..") ;
		fontMi.addActionListener(new FontMiAction()) ;
		formatMenu.add(fontMi);
		
		/* Now the View bar */
		
		viewMenu = new JMenu("   View   ") ;
		mb.add(viewMenu) ;
		statusMi= new JMenuItem("   Status   ") ;
		statusMi.addActionListener(new StatusMiAction())  ;
		viewMenu.add(statusMi);
		viewMenu.add(combo);
		
		/* Now for the Help Bar */
		
		helpMenu = new JMenu("   Help   ") ;
		mb.add(helpMenu)  ;
		helpMi = new JMenuItem("   View Help   ") ;
		helpMi.addActionListener(new HelpMiAction())  ;
		helpMenu.add(helpMi) ;
		aboutMi = new JMenuItem("   About   ") ;
		aboutMi.addActionListener(new AboutMiAction()) ;
		helpMenu.add(aboutMi) ;
		
		searcher.setText("Search");
		if(searcher.hasFocus()){
			searcher.selectAll();
		}
		searcher.addKeyListener(new KeyAdapter() {
		
			String search = ""  ;
			public void keyPressed(KeyEvent e){
				int key = e.getKeyCode() ;
				if(key == KeyEvent.VK_ENTER){
					search = searcher.getText();
					highlight(contentArea, search);
					searcher.setText("");
				}
			}
		});
		combo.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
					LooknFeel() ;
			}			
		}); 
		SwingUtilities.updateComponentTreeUI(combo);
		toolBar.add(searcher);
		
		closeTab.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK), "NonAction");
		closeTab.getActionMap().put("NonAction", null);
		closeTab.setBackground(Color.RED);
		closeTab.setForeground(Color.BLACK);
		closeTab.setSize(5,5);
		
		closeTab.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Component selected = tabPane.getSelectedComponent();
				if(selected!=null){
				if(contentArea.getText()!=null && contentArea.getText().length()!= 0){
					int result = JOptionPane.showConfirmDialog(rootPane, "Do You Want to Save ?") ;
					if(result == JOptionPane.YES_OPTION ){
							System.out.println("Exit : " + result); 
							saveContents();
					}
					tabPane.remove(selected);
					areas.remove(selected) ;
					}
				else{
						dispose();
					}
				}
				else{
					dispose();
				}
			}
		});
		
		mb.add(Box.createHorizontalGlue());
		mb.add(combo);
		mb.add(closeTab);
		tabPane.addTab(fileName,scrollPanel);
		
		add(mb,BorderLayout.NORTH);
		add(tabPane,BorderLayout.CENTER);
		add(toolBar,BorderLayout.SOUTH);
		setSize(500,500)  ;			
		addWindowListener(new WindowAdapter() 
		{ 															
			public void windowClosing(WindowEvent we)  {
				doExit()  ;				
			}
		}) ;
		setVisible(true) ;
		setDropTarget(new DropTarget(contentArea,this));
		
	}

	public void doExit()  {
		int result = 0  ;
		if(contentArea.getText()!=null && contentArea.getText().length()!= 0){
		result = JOptionPane.showConfirmDialog(this, "Do You Want to Save ?") ;
		if(result == JOptionPane.YES_OPTION ){
				System.out.println("Exit : " + result); 
				   saveContents();
				   dispose() ;
					
		}
		else if(result == JOptionPane.CANCEL_OPTION){
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		}
		else{
			System.exit(0);
		}
		}
	}
	public void search(String getString){
		String str = contentArea.getText() ;
		StringTokenizer st = new StringTokenizer(str," ");
		Vector<String> getallWords = new Vector<String>() ;
		while(st.hasMoreTokens()){
			String s = (String) st.nextToken();
			getallWords.add(s);
		}
		
			
		
	}
	public void openFileContents()  {
		JFileChooser jfc = new JFileChooser("C://Users//Adarsh//Desktop")  ;
		int result = jfc.showOpenDialog(this)  ;
		if(result == JFileChooser.APPROVE_OPTION)  {
			File file = jfc.getSelectedFile()  ;
			fileName = file.getName()  ;
			
			try {
			FileReader fr = new FileReader(file)  ;
			BufferedReader br = new BufferedReader(fr)  ;
			String input = br.readLine()  ;
			contentArea.setText("")   ;
			String fileContent = "" ;
			input = br.readLine()  ;
			while(input != null)  {
				fileContent += input + "\r\n"  ;
				input = br.readLine()  ;
			}
			
			br.close()  ;
			fr.close()  ;
			addTab(contentArea,file,fileContent) ;
			SwingUtilities.updateComponentTreeUI(this);
			}catch(Exception e)  {  e.printStackTrace()  ;  }
		
			}
	}
	public void addTab(JTextPane area,File f, String content){
		if(tabPane.getTabCount()==0){
			area.setText(content);
			JScrollPane sc = new JScrollPane(area);
			tabPane.addTab(f.getName(),sc);
			areas.add(area);
		}
		else{
			Iterator<JTextPane> it = areas.iterator() ;
			while(it.hasNext()&&!(it.next().equals(f.getName()))){
				JTextPane a = new JTextPane((syntax_analysis(Color.GREEN)));
				JScrollPane scroll = new JScrollPane(a);
				a.setText(content);
				tabPane.addTab(f.getName(),scroll);
				it.next() ;
			}
			areas.add(area);
			}
	}
	class MyHighlightPainter extends DefaultHighlightPainter{
		public MyHighlightPainter(Color color){
			super(color);
		}
	}
	MyHighlightPainter myHighlightPainter = new MyHighlightPainter(Color.RED);
	public void highlight(JTextComponent textComp, String pattern)  {
		removeHighlights(textComp);
		try{
			Highlighter hilite =  textComp.getHighlighter();
			Document doc = textComp.getDocument();
			String text = doc.getText(0, doc.getLength());
			int pos = 0;
			while((pos= text.toUpperCase().indexOf(pattern.toUpperCase(), pos))>=0){
				hilite.addHighlight(pos, pos+pattern.length(), myHighlightPainter);
				pos += pattern.length();
			}
		}
		catch(BadLocationException e){}
	}
	public void removeHighlights(JTextComponent textComp)  {
		Highlighter hilite = textComp.getHighlighter();
		Highlighter.Highlight[] hilites = hilite.getHighlights();
		for(int i=0;i<hilites.length;i++){
			if(hilites[i].getPainter() instanceof MyHighlightPainter){
				hilite.removeHighlight(hilites[i]);
			}
		}
	}
	public void writeFile(String filename, String fileContents)  {
			try  {
			PrintWriter out = new PrintWriter(new FileWriter(filename)) ;
			out.println(fileContents) ;
			out.flush() ;
			out.close() ;
			}catch(Exception ex)  {
				ex.printStackTrace() ;
			}
			
		}

	public void saveContents()  { 
			if(fileName.equals("untitled.txt"))  {
				saveasContents();
			}
			else  {
				writeFile(fileName, contentArea.getText()) ;
			}
 	}
	public void saveasContents()  {  
		JFileChooser jfc = new JFileChooser("D://Coding//TextEditor//Saved Stuff//")  ;
		int result = jfc.showSaveDialog(this)  ;
		if(result == JFileChooser.APPROVE_OPTION)  {
			File file = jfc.getSelectedFile()  ;
			fileName = file.getAbsolutePath() ;
			setTitle(fileName + " - My Editor")  ;
			writeFile(fileName+".txt", contentArea.getText()) ;
		}		
	}
	
	
	class NewMiAction implements ActionListener  {
		public void actionPerformed(ActionEvent ae)  {
			if((contentArea.getText()!= null) && (contentArea.getText().length()!=0))
				saveContents();
				fileName = "untitled.txt"  ;
				setTitle(fileName + " - Adarsh Editor") ;
				contentArea.setText("")  ;
		}
		
	}

	class OpenMiAction implements ActionListener  {
		public void actionPerformed(ActionEvent ae)  {
			openFileContents()  ;
		}
	}
	public String readFile(String filename)  {
			String fileContent = "" ;
			try  {
			File f = new File(filename) ;
			BufferedReader br = new BufferedReader(new FileReader(f)) ;
			String contents = br.readLine() ;
			
			while(contents != null)  {
				fileContent += contents + "\r\n"  ;
				contents = br.readLine() ;
			}
			}catch(Exception ex)  {
				ex.printStackTrace() ;
			}
			return fileContent ;

		}
	
	
	class SaveMiAction implements ActionListener  {
		public void actionPerformed(ActionEvent ae){
			saveContents() ;
		}
	}
	class SaveAsMiAction implements ActionListener   {
		public void actionPerformed(ActionEvent ae){
			saveasContents();
		}
		
	}
	class ExitMiAction implements ActionListener  {
		public void actionPerformed(ActionEvent ae)  {
			if((contentArea.getText()!= null) && (contentArea.getText().length()!=0))  {
				saveasContents();
				System.exit(0)  ;
			}
			else  
			{			
			System.exit(0) ;
			}
		}
	}
	class UndoMiAction implements ActionListener  {
		public void actionPerformed(ActionEvent ae)  {
			UndoManager undo = new UndoManager();
			if(ae.getActionCommand().equals("Undo")){
				undo.undo();
			}
		}
	}
	
	class FindMiAction implements ActionListener  {
		public void actionPerformed(ActionEvent ae)  {
		}
	}
	class ReplaceMiAction implements ActionListener  {
		public void actionPerformed(ActionEvent ae)  {
		
		}
	}
	class SelectallMiAction implements ActionListener  {
		public void actionPerformed(ActionEvent ae)  {
		
		}
	}
	class FontMiAction implements ActionListener  {
		public void actionPerformed(ActionEvent ae)  {
		
		}
	}
	class StatusMiAction implements ActionListener  {
		public void actionPerformed(ActionEvent ae)  {
		
		}
	}
	class HelpMiAction implements ActionListener  {
		public void actionPerformed(ActionEvent ae)  {
		
		}
	}
	class AboutMiAction implements ActionListener  {
		public void actionPerformed(ActionEvent ae)  {
			showAboutDialog()  ;
	}
	}
	public void showAboutDialog()  {
		AboutDialog ad = new AboutDialog(this) ;
	}
 	class AboutDialog extends JDialog  {
		private static final long serialVersionUID = 1L;

		public AboutDialog(JFrame owner)  {
 			super(owner, "About Dialog", true);
 			Container contentPane = getContentPane();
 			contentPane.add(new JLabel(
 				"<HTML><H1><I>Adarsh's Personal Editor</I></H1><HR>"
 				+ "Created By <B><C>Adarsh S. Rao </C></B>.... </HTML>"),
 				BorderLayout.CENTER);
 			JButton ok = new JButton("   Ok   ");
 			ok.addActionListener(new
 				ActionListener()  {
 					public void actionPerformed(ActionEvent evt)  {
 						setVisible(false);
 					}
 				}
			);
 			JPanel panel = new JPanel();
			
 			panel.add(ok);
 			contentPane.add(panel, BorderLayout.SOUTH);
			setSize(250, 150);
			setVisible(true)  ;
		}
	}
 	public void dragEnter(DropTargetDragEvent dtde) {
		
	}

	public void dragExit(DropTargetEvent dte) {
		
	}

	
	public void dragOver(DropTargetDragEvent dtde) {
		
	}

	
	public void drop(DropTargetDropEvent dtde) {
		dtde.acceptDrop(DnDConstants.ACTION_COPY);
		
		if(dtde.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
			try {
				Object files = dtde.getTransferable().
						getTransferData(DataFlavor.javaFileListFlavor);
				String strFiles = files.toString()   ;
					System.out.println(strFiles);
						
					strFiles = strFiles.substring(strFiles.indexOf("[")+1, strFiles.indexOf("]"));
					String[] fileNames = strFiles.split(",");
					for(String fileName : fileNames){
						System.out.println("path is:" + fileName );
						
						try {
							if((contentArea.getText()!= null) && (contentArea.getText().length()!=0)){
								JPanel myNewPanel = new JPanel()  ;
								myNewPanel.setLayout(new BorderLayout())  ;
								JTextPane newContentArea = new JTextPane(syntax_analysis(Color.GREEN))  ;
								newContentArea.setText(readFile(fileName.trim()))  ;
								JScrollPane myNewScrollPane = new JScrollPane(newContentArea);
								myNewScrollPane.setAutoscrolls(true);
								myNewPanel.add(myNewScrollPane)  ;
								tabPane.addTab(fileName, myNewPanel);
								}
							else{
								tabPane.removeAll()  ;
								
								contentArea.setText(readFile(fileName.trim()));
								tabPane.addTab(fileName, scrollPanel)  ;
								SwingUtilities.updateComponentTreeUI(this);
								}
						}
						catch(Exception e)  {  e.printStackTrace()  ;  }
					}
			} catch (UnsupportedFlavorException | IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		
	}
	public static void main(String[] args)  {
		MyEditor myf = new MyEditor("Adarsh's Editor")  ;
		
	}
}

