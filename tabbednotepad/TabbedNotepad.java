import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import java.io.IOException;
import java.net.*;
import javax.xml.bind.*;
import javax.xml.parsers.*;
import javax.swing.text.*;
import java.awt.datatransfer.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


//                 Main class
public final class TabbedNotepad extends JFrame {

    JMenuBar mb;

    JSplitPane jsplit;

    JTabbedPane _tabbedPane;

    JToolBar statusBar;

    JLabel readylabel;

    JLabel filenameLabel = new JLabel("");
    JLabel rowLabel = new JLabel("Row : ");
    JLabel colLabel = new JLabel("Col : ");

    JToolBar _toolbar;

    static int count = 1;

    UndoManager _undoManager = new UndoManager();
    Action undoAction = new PerformUndoAction(_undoManager);
    Action redoAction = new PerformRedoAction(_undoManager);

    Clipboard clip = getToolkit().getSystemClipboard();

    ButtonGroup buttonGroup;

    Toolkit toolkit = Toolkit.getDefaultToolkit();

    JPopupMenu _popupMenu;

    // getCenterPoints() function that returns the
    // center point of the screen
    public Point getCenterPoints() {
        Point pt = new Point(0, 0);
        Dimension d = toolkit.getScreenSize();
        pt.x = d.width / 3;
        pt.y = d.height / 4;

        return pt;
    }
	
    // create constructor of TabbedNotepad class
    public TabbedNotepad() {
        setTitle("Notepad");
		
        mb = new JMenuBar();

        // File menu
        JMenu file = new JMenu("  File  ");

        // creating file menu itemas
        JMenuItem file_new = new JMenuItem("  New ");

        JMenuItem file_open = new JMenuItem("  Open ");

        JMenuItem file_save = new JMenuItem("  Save ");

        JMenuItem file_saveas = new JMenuItem("  Save As");

        JMenuItem file_saveall = new JMenuItem("  Save All");
       
        JMenuItem file_close = new JMenuItem("  Close");
        JMenuItem file_closeall = new JMenuItem("  Close All");

        JMenuItem file_exit = new JMenuItem("  Exit");

        // adding actions to file menu items
        File_MenuItemsAction file_action = new File_MenuItemsAction();

        file_new.addActionListener(e -> File_New_Action());
        file_open.addActionListener(file_action);
        file_save.addActionListener(file_action);
        file_saveas.addActionListener(file_action);
        file_saveall.addActionListener(file_action);
        file_close.addActionListener(file_action);
        file_closeall.addActionListener(file_action);

        file_exit.addActionListener(file_action);

        //add MenuListener to menu items
        JMenuItem[] filemenuitems = {
            file_save,
            file_saveas,
            file_saveall,
            file_close,
            file_closeall
        };

        Menus_MenuListener fml = new Menus_MenuListener(filemenuitems);
        file.addMenuListener(fml);

        // Edit menu
        JMenu edit = new JMenu("  Edit  ");

        //creating edit menu items
        JMenuItem edit_cut = new JMenuItem("  Cut  ");

        JMenuItem edit_copy = new JMenuItem("  Copy");

        JMenuItem edit_paste = new JMenuItem("  Paste");

        JMenuItem edit_undo = new JMenuItem("  Undo");

        JMenuItem edit_redo = new JMenuItem("  Redo");




        JMenuItem edit_find = new JMenuItem("  Find");

        JMenuItem edit_replace = new JMenuItem("  Replace");

        // adding actions to edit menu items
        Edit_MenuItemsAction edit_action = new Edit_MenuItemsAction();

        edit_cut.addActionListener(edit_action);
        edit_copy.addActionListener(edit_action);
        edit_paste.addActionListener(edit_action);

        edit_find.addActionListener(edit_action);
        edit_replace.addActionListener(edit_action);

        edit_undo.addActionListener(undoAction);
        edit_redo.addActionListener(redoAction);

        // add MenuListener to menu items
        JMenuItem[] editmenuitems = {
            edit_cut,
            edit_copy,
            edit_paste,
            edit_undo,
            edit_redo,
            edit_find,
            edit_replace
        };

        Menus_MenuListener eml = new Menus_MenuListener(editmenuitems);
        edit.addMenuListener(eml);

        // Run menu
        JMenu run = new JMenu("  Run  ");

        //creating run menu items
        JMenuItem run_run = new JMenuItem("  Run ");
    

        // adding actions to run menu items
        Run_MenuItemsAction run_action = new Run_MenuItemsAction();

        run_run.addActionListener(run_action);

        // adding file menuitems to file menu

        file.add(file_new);
        file.addSeparator();
        file.add(file_open);
        file.addSeparator();
        file.add(file_save);
        file.add(file_saveas);
        file.add(file_saveall);
        file.addSeparator();
        file.add(file_close);
        file.add(file_closeall);
        file.addSeparator();

        file.add(file_exit);
		
        // add file menu to menu bar mb
        mb.add(file);

        // adding edit menuitems to edit menu
        edit.add(edit_cut);
        edit.add(edit_copy);
        edit.add(edit_paste);
        edit.addSeparator();
        edit.add(edit_undo);
        edit.add(edit_redo);
        edit.addSeparator();
        edit.add(edit_find);
        edit.add(edit_replace);

        //add edit menu to mb
        mb.add(edit);

        // adding run menu items to run menu
        run.add(run_run);

        //add run menu to mb
        mb.add(run);

        // create _tabbedPane object & adding ChangeListener interface to it
        _tabbedPane = new JTabbedPane();
        _tabbedPane.setFont(new Font("Calibri", Font.PLAIN, 14));
        
        //set JMenubar
        setJMenuBar(mb);

        //creating & adding toolbar to north direction
        //_toolbar = new JToolBar();
        //_toolbar.setFloatable(false);

        // creating & adding statusbar to south direction
        statusBar = new JToolBar();
        statusBar.setFloatable(false);

        statusBar.add(new JLabel("                          "));
        statusBar.add(filenameLabel);
        statusBar.add(new JLabel("                                                            "));
        statusBar.add(rowLabel);
        statusBar.add(new JLabel("     "));
        statusBar.add(colLabel);

        // creating popup menu
        _popupMenu = new JPopupMenu();

        JMenuItem popup_edit_cut = new JMenuItem("  Cut                                     ");

        popup_edit_cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));

        JMenuItem popup_edit_copy = new JMenuItem("  Copy");

        popup_edit_copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));

        JMenuItem popup_edit_paste = new JMenuItem("  Paste");

        popup_edit_paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

        JMenuItem popup_edit_selectall = new JMenuItem("  Select All");

        JMenu popup_edit_changecase = new JMenu("  Change Case");
        JMenuItem popup_edit_changecase_upper = new JMenuItem("  Upper Case   ");
        JMenuItem popup_edit_changecase_lower = new JMenuItem("  Lower Case   ");
        JMenuItem popup_edit_changecase_sentence = new JMenuItem("  Sentence Case   ");

        popup_edit_changecase.add(popup_edit_changecase_upper);
        popup_edit_changecase.add(popup_edit_changecase_lower);
        popup_edit_changecase.add(popup_edit_changecase_sentence);

        JMenuItem popup_view_font = new JMenuItem("  Font ");

        popup_view_font.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));

        // add actions to popup menu items
        popup_edit_cut.addActionListener(edit_action);
        popup_edit_copy.addActionListener(edit_action);
        popup_edit_paste.addActionListener(edit_action);
        popup_edit_selectall.addActionListener(edit_action);
        popup_edit_changecase_upper.addActionListener(edit_action);
        popup_edit_changecase_lower.addActionListener(edit_action);
        popup_edit_changecase_sentence.addActionListener(edit_action);

        //adding popup menu items to _popupMenu
        _popupMenu.add(popup_edit_cut);
        _popupMenu.add(popup_edit_copy);
        _popupMenu.add(popup_edit_paste);
        _popupMenu.addSeparator();
        _popupMenu.add(popup_edit_selectall);
        _popupMenu.addSeparator();
        _popupMenu.add(popup_edit_changecase);
        _popupMenu.addSeparator();
        _popupMenu.add(popup_view_font);

        //add window listener to TabbedNotepad frame
        addWindowListener(new Load_Close_Frame_Action());

        //get content pane & adding toolbar,statusbar & jsplit to it
        Container cp = getContentPane();
        cp.add(_tabbedPane);
		
    }

    // Window menu item action
    class Window_MenuItemsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {

            String menutext = ae.getActionCommand().trim();

            if (_tabbedPane.getTabCount() > 0) {
                int tabcount = _tabbedPane.getTabCount();
                for (int i = 0; i < tabcount; i++) {
                    String title = _tabbedPane.getTitleAt(i).trim();
                    if (title.contains("*")) {
                        title = title.replace("*", "").trim();
                    }

                    if (title.equals(menutext)) {
                        _tabbedPane.setSelectedIndex(i);
                        setTitle("My Editor");
                    }
                }
            }
        }

    }

    // class for defining actions of file menu items
    class File_MenuItemsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() instanceof JMenuItem) {
                String item = evt.getActionCommand().trim();

                switch (item) {
                    case "New":
                        File_New_Action();
                        break;
                    case "Open":
                        File_Open_Action();
                        break;
                    case "Save":
                        File_Save_Action();
                        break;
                    case "Save As":
                        File_SaveAs_Action();
                        break;
                    case "Save All":
                        File_SaveAll_Action();
                        break;
                    case "Close":
                        File_Close_Action();
                        break;
                    case "Close All":
                        File_CloseAll_Action();
                        break;

                    case "Exit":
                        File_Exit_Action();
                        break;



                }
            }
        }
    }

    // class for defining actions of edit menu items
    class Edit_MenuItemsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() instanceof JMenuItem) {
                String item = evt.getActionCommand().trim();
                switch (item) {
                    case "Cut":
                        Edit_Cut_Action();
                        break;
                    case "Copy":
                        Edit_Copy_Action();
                        break;
                    case "Paste":
                        Edit_Paste_Action();
                        break;

                    case "Find":
                        Edit_Find_Action();
                        break;
                    case "Replace":
                        Edit_Replace_Action();
                        break;
                    case "Select All":
                        Edit_SelectAll_Action();
                        break;
                    case "Upper Case":
                        Edit_ChangeCase_UpperCase_Action();
                        break;
                    case "Lower Case":
                        Edit_ChangeCase_LowerCase_Action();
                        break;
                    case "Sentence Case":
                        Edit_ChangeCase_SentenceCase_Action();
                        break;
                    case "Next Document":
                        Edit_NextDocument_Action();
                        break;
                    case "Previous Document":
                        Edit_PreviousDocument_Action();
                        break;
                }
            }
        }
    }

    // class for defining actions of Run menu items
    class Run_MenuItemsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() instanceof JMenuItem) {
                String item = evt.getActionCommand().trim();
                switch (item) {
                    case "Run":
                        JDialog ra = new RunAction();
                        ra.setTitle("Run");
                        ra.setModal(true);
                        ra.setSize(450, 120);
                        ra.setResizable(false);
                        ra.setLocation(getCenterPoints().x + 100, getCenterPoints().y + 80);
                        ra.setVisible(true);
                        break;
                }
            }
        }
    }

    // tool abr button action class
    class ToolBarButtonsAction implements ActionListener {
        String type = "";
        public ToolBarButtonsAction(String s) {
            type = s;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            switch (type) {
                case "new":
                    File_New_Action();
                    break;
                case "open":
                    File_Open_Action();
                    break;
                case "save":
                    File_Save_Action();
                    break;
                case "saveas":
                    File_SaveAs_Action();
                    break;
                case "cut":
                    Edit_Cut_Action();
                    break;
                case "copy":
                    Edit_Copy_Action();
                    break;
                case "paste":
                    Edit_Paste_Action();
                    break;


            }
        }
    }

    // set items enable to false if tabcount=0 to menu items
    public class Menus_MenuListener implements MenuListener {
        JMenuItem[] list;

        Menus_MenuListener(JMenuItem[] lst) {
            list = lst;
        }

        @Override
        public void menuCanceled(MenuEvent ev) {}
        @Override
        public void menuDeselected(MenuEvent ev) {}
        @Override
        public void menuSelected(MenuEvent evt) {
            if (_tabbedPane.getTabCount() > 0) {
                for (JMenuItem item: list) {
                    item.setEnabled(true);
                }
            } else {
                for (JMenuItem item: list) {
                    item.setEnabled(false);
                }
            }
        }
    }

    // showing popupMenu on textpane
    class TextPane_MouseAction extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent evt) {
            if (evt.isPopupTrigger()) {
                _popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }

    // display row & col
    class CaretAction implements CaretListener {
        public int getRow(int pos, JTextPane textpane) {
            int rn = (pos == 0) ? 1 : 0;
            try {
                int offs = pos;
                while (offs > 0) {
                    offs = Utilities.getRowStart(textpane, offs) - 1;
                    rn++;
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            return rn;
        }

        public int getColumn(int pos, JTextPane textpane) {
            try {
                return pos - Utilities.getRowStart(textpane, pos) + 1;
            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            return -1;
        }

        @Override
        public void caretUpdate(CaretEvent evt) {
            JTextPane textpane = (JTextPane) evt.getSource();
            //Test.Highlighter obj = new Test.Highlighter();
            //JTextPane textpane = new Test().getInstanceOfJTextPane();
            //textpane.setText("public static");
            int row = getRow(evt.getDot(), textpane);
            int col = getColumn(evt.getDot(), textpane);
            rowLabel.setText("Row : " + row);
            colLabel.setText("Col : " + col);

        }

    }

    // functions

    // File -> New action

    public void File_New_Action() {
        //crerate textpane object
        // JTextPane _textPane=new JTextPane();
        JTextPane _textPane = new Test().getInstanceOfJTextPane();
        _textPane.setFont(new Font("Calibri", Font.PLAIN, 14));

        JScrollPane jsp = new JScrollPane(_textPane);
        // add key listener & Undoable edit listener to text pane
        _textPane.addKeyListener(new KeyTypedAction());
        _textPane.getDocument().addUndoableEditListener(_undoManager);
        //add tab to _tabbedPane with control textpane
        _tabbedPane.addTab("Document " + count + " ", jsp);
        //add caret listener & mouse listener to text pane
        _textPane.addCaretListener(new CaretAction());
        _textPane.addMouseListener(new TextPane_MouseAction());
        int index = _tabbedPane.getTabCount() - 1;

        _tabbedPane.setSelectedIndex(index);
		
        //change the title
        setTitle("My Editor");
        filenameLabel.setText("Document " + count);
		
        count++;
    }

    // File -> Open action
    public void File_Open_Action() {
        FileDialog fd = new FileDialog(new JFrame(), "Select File", FileDialog.LOAD);
        fd.setMultipleMode(true);
        fd.show();
        if (fd.getFiles() != null) {
            File[] files = fd.getFiles();
            for (File item: files) {
                String filename = item.toString();
                String file = filename;
                if (filename.contains("\\")) {
                    file = filename.substring(filename.lastIndexOf("\\") + 1);
                } else if (filename.contains("/")) {
                    file = filename.substring(filename.lastIndexOf("/") + 1);
                }

                int count = _tabbedPane.getTabCount();

                JTextPane _textPane = new Test().getInstanceOfJTextPane();
                _textPane.setFont(new Font("Calibri", Font.PLAIN, 14));


                JScrollPane jsp = new JScrollPane(_textPane);
                _textPane.addKeyListener(new KeyTypedAction());
                _textPane.getDocument().addUndoableEditListener(_undoManager);
                _textPane.addCaretListener(new CaretAction());
                _textPane.addMouseListener(new TextPane_MouseAction());
                _tabbedPane.addTab(file, jsp);
                _tabbedPane.setSelectedIndex(count);




                setTitle("My Editor");
                filenameLabel.setText(filename);
                

                BufferedReader d;
                StringBuffer sb = new StringBuffer();
                try {
                    d = new BufferedReader(new FileReader(filename));
                    String line;
                    while ((line = d.readLine()) != null)
                        sb.append(line + "\n");
                    _textPane.setText(sb.toString());
                    d.close();
                } catch (FileNotFoundException fe) {
                    System.out.println("File not Found");
                } catch (IOException ioe) {}

                _textPane.requestFocus();

            }
        }

    }

    // File -> Save action
    public void File_Save_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            String filename = filenameLabel.getText();
            int sel = _tabbedPane.getSelectedIndex();
            JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);
            if (filename.contains("\\") || filename.contains("/")) {
                File f = new File(filename);
                if (f.exists()) {
                    try {
                        DataOutputStream d = new DataOutputStream(new FileOutputStream(filename));
                        String line = textPane.getText();
                        d.writeBytes(line);
                        d.close();

                        String tabtext = _tabbedPane.getTitleAt(sel);
                        if (tabtext.contains("*")) {
                            tabtext = tabtext.replace("*", "");
                            _tabbedPane.setTitleAt(sel, tabtext);
                            setTitle("My Editor");

                        }

                    } catch (Exception ex) {
                        System.out.println("File not found");
                    }
                    textPane.requestFocus();
                }
            } else if (filename.contains("Document ")) {
                File_SaveAs_Action();
            }

        }
    }

    // File -> Save As action
    public void File_SaveAs_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            FileDialog fd = new FileDialog(new JFrame(), "Save File", FileDialog.SAVE);
            fd.show();
            if (fd.getFile() != null) {
                String filename = fd.getDirectory() + fd.getFile();
                int sel = _tabbedPane.getSelectedIndex();
                JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);
                try {
                    DataOutputStream d = new DataOutputStream(new FileOutputStream(filename));
                    String line = textPane.getText();
                    d.writeBytes(line);
                    d.close();

                    
                    filenameLabel.setText(filename);

                    String file = filename.substring(filename.lastIndexOf("\\") + 1);
                    _tabbedPane.setTitleAt(sel, file);



                    setTitle("My Editor");

                } catch (Exception ex) {
                    System.out.println("File not found");
                }
                textPane.requestFocus();

            }
        }
    }

    // File -> Save All action
    public void File_SaveAll_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            int maxindex = _tabbedPane.getTabCount() - 1;
            for (int i = 0; i <= maxindex; i++) {
                _tabbedPane.setSelectedIndex(i);
                String filename = filenameLabel.getText();
                int sel = _tabbedPane.getSelectedIndex();
                JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);
                if (filename.contains("\\") || filename.contains("/")) {
                    File f = new File(filename);
                    if (f.exists()) {
                        try {
                            DataOutputStream d = new DataOutputStream(new FileOutputStream(filename));
                            String line = textPane.getText();
                            d.writeBytes(line);
                            d.close();

                            String tabtext = _tabbedPane.getTitleAt(sel);
                            if (tabtext.contains("*")) {
                                tabtext = tabtext.replace("*", "");
                                _tabbedPane.setTitleAt(sel, tabtext);
                                setTitle("My Editor");

                            }

                        } catch (Exception ex) {
                            System.out.println("File not found");
                        }
                        textPane.requestFocus();
                    }
                }

            }
        }
    }

    // File -> Close action
    public void File_Close_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            int sel = _tabbedPane.getSelectedIndex();
            String tabtext = _tabbedPane.getTitleAt(sel);

            if (tabtext.contains("*")) {
                int n = JOptionPane.showConfirmDialog(null, "Do you want to save " + tabtext + " before close ?",
                    "Save or Not", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                tabtext.replace("*", "");

                if (n == 0) {
                    String filename = filenameLabel.getText();
                    JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);

                    if (filename.contains("\\") || filename.contains("/")) {
                        File_Save_Action();

                        _tabbedPane.remove(sel);


                        //adding all elements to list after removing the tab
                        for (int i = 0; i < _tabbedPane.getTabCount(); i++) {
                            String item = _tabbedPane.getTitleAt(i);
                            if (item.contains("*")) {
                                item = item.replace("*", "").trim();
                            }


                        }



                        rowLabel.setText("Row :");
                        colLabel.setText("Col :");

                        if (_tabbedPane.getTabCount() == 0) {
                            setTitle("Tabbed Notepad in Java");
                            filenameLabel.setText("");
                            rowLabel.setText("Row :");
                            colLabel.setText("Col :");
                        }

                    } else if (filename.contains("Document ")) {
                        File_SaveAs_Action();

                        _tabbedPane.remove(sel);


                        //adding all elements to list after removing the tab
                        for (int i = 0; i < _tabbedPane.getTabCount(); i++) {
                            String item = _tabbedPane.getTitleAt(i);
                            if (item.contains("*")) {
                                item = item.replace("*", "").trim();
                            }


                        }



                        rowLabel.setText("Row :");
                        colLabel.setText("Col :");

                        if (_tabbedPane.getTabCount() == 0) {
                            setTitle("Tabbed Notepad in Java");
                            filenameLabel.setText("");
                            rowLabel.setText("Row :");
                            colLabel.setText("Col :");
                        }
                    }

                }

                if (n == 1) {
                    _tabbedPane.remove(sel);


                    //adding all elements to list after removing the tab
                    for (int i = 0; i < _tabbedPane.getTabCount(); i++) {
                        String item = _tabbedPane.getTitleAt(i);
                        if (item.contains("*")) {
                            item = item.replace("*", "").trim();
                        }


                    }



                    rowLabel.setText("Row :");
                    colLabel.setText("Col :");

                    if (_tabbedPane.getTabCount() == 0) {
                        setTitle("Tabbed Notepad in Java");
                        filenameLabel.setText("");
                        rowLabel.setText("Row :");
                        colLabel.setText("Col :");
                    }
                }
            } else {
                _tabbedPane.remove(sel);


                //adding all elements to list after removing the tab
                for (int i = 0; i < _tabbedPane.getTabCount(); i++) {
                    String item = _tabbedPane.getTitleAt(i);
                    if (item.contains("*")) {
                        item = item.replace("*", "").trim();
                    }


                }



                rowLabel.setText("Row :");
                colLabel.setText("Col :");

                if (_tabbedPane.getTabCount() == 0) {
                    setTitle("Tabbed Notepad in Java");
                    filenameLabel.setText("");
                    rowLabel.setText("Row :");
                    colLabel.setText("Col :");
                }

            }
        } else {
            setTitle("Tabbed Notepad in Java");
            filenameLabel.setText("");
            rowLabel.setText("Row :");
            colLabel.setText("Col :");

        }
    }

    // File -> Close All action
    public void File_CloseAll_Action() throws IndexOutOfBoundsException {
        if (_tabbedPane.getTabCount() > 0) {
            for (int j = 0; j < _tabbedPane.getTabCount(); j++) {
                _tabbedPane.setSelectedIndex(j);
                int sel = _tabbedPane.getSelectedIndex();
                String tabtext = _tabbedPane.getTitleAt(sel);

                if (tabtext.contains("*")) {
                    int n = JOptionPane.showConfirmDialog(null, "Do you want to save " + tabtext + " before close ?",
                        "Save or Not", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                    tabtext.replace("*", "");

                    if (n == 0) {
                        String filename = filenameLabel.getText();
                        JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);

                        if (filename.contains("\\") || filename.contains("/")) {
                            File_Save_Action();

                            _tabbedPane.remove(sel);


                            //adding all elements to list after removing the tab
                            for (int i = 0; i < _tabbedPane.getTabCount(); i++) {
                                String item = _tabbedPane.getTitleAt(i);
                                if (item.contains("*")) {
                                    item = item.replace("*", "").trim();
                                }


                            }



                            File_CloseAll_Action();

                            rowLabel.setText("Row :");
                            colLabel.setText("Col :");
                        } else if (filename.contains("Document ")) {
                            File_SaveAs_Action();

                            _tabbedPane.remove(sel);


                            //adding all elements to list after removing the tab
                            for (int i = 0; i < _tabbedPane.getTabCount(); i++) {
                                String item = _tabbedPane.getTitleAt(i);
                                if (item.contains("*")) {
                                    item = item.replace("*", "").trim();
                                }


                            }



                            File_CloseAll_Action();

                            rowLabel.setText("Row :");
                            colLabel.setText("Col :");
                        }

                    }

                    if (n == 1) {
                        _tabbedPane.remove(sel);


                        //adding all elements to list after removing the tab
                        for (int i = 0; i < _tabbedPane.getTabCount(); i++) {
                            String item = _tabbedPane.getTitleAt(i);
                            if (item.contains("*")) {
                                item = item.replace("*", "").trim();
                            }


                        }



                        File_CloseAll_Action();

                        rowLabel.setText("Row :");
                        colLabel.setText("Col :");
                    }
                } else {
                    _tabbedPane.remove(sel);

                    //adding all elements to list after removing the tab
                    for (int i = 0; i < _tabbedPane.getTabCount(); i++) {
                        String item = _tabbedPane.getTitleAt(i);
                        if (item.contains("*")) {
                            item = item.replace("*", "").trim();
                        }


                    }



                    File_CloseAll_Action();

                    rowLabel.setText("Row :");
                    colLabel.setText("Col :");
                }
            }
        } else {
            setTitle("Tabbed Notepad in Java");
            filenameLabel.setText("");

            rowLabel.setText("Row :");
            colLabel.setText("Col :");
        }
    }

    // File -> Exit
    public void File_Exit_Action() {
        File_CloseAll_Action();
        if (_tabbedPane.getTabCount() == 0) {
            System.exit(0);
        }
    }

    // Edit -> Cut
    public void Edit_Cut_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            int sel = _tabbedPane.getSelectedIndex();
            JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);
            String selected_text = textPane.getSelectedText();
            StringSelection ss = new StringSelection(selected_text);
            clip.setContents(ss, ss);
            textPane.replaceSelection("");

            String tabtext = _tabbedPane.getTitleAt(sel);
            if (tabtext.contains("*")) {} else {
                _tabbedPane.setTitleAt(sel, _tabbedPane.getTitleAt(sel) + "*");

            }
        }
    }

    // Edit -> Copy
    public void Edit_Copy_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            int sel = _tabbedPane.getSelectedIndex();
            JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);
            String selected_text = textPane.getSelectedText();
            StringSelection ss = new StringSelection(selected_text);
            clip.setContents(ss, ss);

        }
    }

    // Edit -> Paste
    public void Edit_Paste_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            int sel = _tabbedPane.getSelectedIndex();
            JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);
            Transferable cliptran = clip.getContents(TabbedNotepad.this);
            try {
                String selected_text = (String) cliptran.getTransferData(DataFlavor.stringFlavor);
                textPane.replaceSelection(selected_text);

                // here you can direct use textPane.paste();

                String tabtext = _tabbedPane.getTitleAt(sel);
                if (tabtext.contains("*")) {} else {
                    _tabbedPane.setTitleAt(sel, _tabbedPane.getTitleAt(sel) + "*");

                }
            } catch (Exception exc) {
                System.out.println("error to paste");
            }
        }
    }

    int _lineCount;
    public int getLineCount(JTextPane textPane) {
        _lineCount = 0;
        Scanner scanner = new Scanner(textPane.getText());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            _lineCount++;
        }
        return _lineCount;
    }

    public int SetCursor(int newlineno, JTextPane textPane) {
        int pos = 0;
        int i = 0;
        String line = "";
        Scanner scanner = new Scanner(textPane.getText());
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            i++;
            if (newlineno > i) {
                pos = pos + line.length() + 1;
            }
        }
        return pos;
    }

    // Edit -> Find
    public void Edit_Find_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            int sel = _tabbedPane.getSelectedIndex();
            JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);

            String input = (String) JOptionPane.showInputDialog(null, "Enter Text to Find :  ", "Find", JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (input != null) {
                int start = textPane.getText().indexOf(input);
                int end = input.length();
                if (start >= 0 && end > 0) {
                    textPane.select(start, start + end);
                }
            }
        }
    }

    // Edit -> Replace
    JTextField findText;
    JTextField replaceText;
    JButton replaceButton;
    JButton cancelButton;
    JDialog jd;
    public void Edit_Replace_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            jd = new JDialog(new JDialog(), true);
            jd.setSize(360, 120);
            jd.setLocation(this.getCenterPoints().x + 150, this.getCenterPoints().y + 130);
            jd.setResizable(false);
            jd.setTitle("Replace");

            JPanel jp1 = new JPanel();
            JPanel jp2 = new JPanel();
            JLabel findwhat = new JLabel("Find What    :    ");
            JLabel replacewith = new JLabel("Replace With : ");
            findText = new JTextField(20);
            replaceText = new JTextField(20);



            replaceButton = new JButton("Replace All");
            cancelButton = new JButton("Cancel");

            replaceButton.addActionListener(new ReplaceText_Action());
            cancelButton.addActionListener(new ReplaceText_Action());

            jp1.add(findwhat);
            jp1.add(findText);
            jp1.add(replacewith);
            jp1.add(replaceText);
            jp2.add(replaceButton);
            jp2.add(cancelButton);

            jd.add(jp1, BorderLayout.CENTER);
            jd.add(jp2, BorderLayout.SOUTH);

            jd.show();
        }
    }

    class ReplaceText_Action implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            Object source = evt.getSource();
            if (source == replaceButton) {
                int sel = _tabbedPane.getSelectedIndex();
                JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);

                String find = findText.getText();
                String replace = replaceText.getText();

                textPane.setText(textPane.getText().replaceAll(find, replace));

                String tabtext = _tabbedPane.getTitleAt(sel);
                if (tabtext.contains("*")) {} else {
                    _tabbedPane.setTitleAt(sel, _tabbedPane.getTitleAt(sel) + "*");

                }
            } else if (source == cancelButton) {
                jd.dispose();
            }
        }
    }

    // Edit -> Select All
    public void Edit_SelectAll_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            int sel = _tabbedPane.getSelectedIndex();
            JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);

            textPane.selectAll();
        }
    }
	
    // Edit -> Change Case -> Upper Case
    public void Edit_ChangeCase_UpperCase_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            int sel = _tabbedPane.getSelectedIndex();
            JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);

            if (textPane.getSelectedText() != null) {
                textPane.replaceSelection(textPane.getSelectedText().toUpperCase());

                String tabtext = _tabbedPane.getTitleAt(sel);
                if (tabtext.contains("*")) {} else {
                    _tabbedPane.setTitleAt(sel, _tabbedPane.getTitleAt(sel) + "*");

                }
            }
        }
    }

    // Edit -> Change Case -> Lower Case
    public void Edit_ChangeCase_LowerCase_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            int sel = _tabbedPane.getSelectedIndex();
            JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);

            if (textPane.getSelectedText() != null) {
                textPane.replaceSelection(textPane.getSelectedText().toLowerCase());

                String tabtext = _tabbedPane.getTitleAt(sel);
                if (tabtext.contains("*")) {} else {
                    _tabbedPane.setTitleAt(sel, _tabbedPane.getTitleAt(sel) + "*");

                }
            }
        }
    }

    // Edit -> Change Case -> Sentence Case
    public void Edit_ChangeCase_SentenceCase_Action() {
        if (_tabbedPane.getTabCount() > 0) {
            int sel = _tabbedPane.getSelectedIndex();
            JTextPane textPane = (JTextPane)(((JScrollPane) _tabbedPane.getComponentAt(sel)).getViewport()).getComponent(0);

            if (textPane.getSelectedText() != null) {
                String s = textPane.getSelectedText();
                char ch = s.charAt(0);
                String ss = String.valueOf(ch).toUpperCase();
                String str = s.substring(1);
                str = ss + str;
                textPane.replaceSelection(str);

                String tabtext = _tabbedPane.getTitleAt(sel);
                if (tabtext.contains("*")) {} else {
                    _tabbedPane.setTitleAt(sel, _tabbedPane.getTitleAt(sel) + "*");

                }
            }
        }
    }

    // Edit -> Next Document
    public void Edit_NextDocument_Action() throws IndexOutOfBoundsException {
        if (_tabbedPane.getTabCount() > 0) {
            int tabindex = _tabbedPane.getTabCount() - 1;
            if (_tabbedPane.getSelectedIndex() == tabindex) {} else if (_tabbedPane.getSelectedIndex() < tabindex) {
                _tabbedPane.setSelectedIndex(_tabbedPane.getSelectedIndex() + 1);
            }
        }
    }

    // Edit -> Previous Document
    public void Edit_PreviousDocument_Action() throws IndexOutOfBoundsException {
        if (_tabbedPane.getTabCount() > 0) {
            int tabcount = _tabbedPane.getTabCount();
            if (_tabbedPane.getSelectedIndex() == 0) {} else {
                _tabbedPane.setSelectedIndex(_tabbedPane.getSelectedIndex() - 1);
            }
        }
    }

    // The Perform Undo Action class
    public class PerformUndoAction extends AbstractAction {
        UndoManager _manager;

        public PerformUndoAction(UndoManager manager) {
            this._manager = manager;
        }

        public void actionPerformed(ActionEvent evt) {
            try {
                _manager.undo();
            } catch (CannotUndoException e) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    // The Perform Redo Action class
    public class PerformRedoAction extends AbstractAction {
        UndoManager _manager;

        public PerformRedoAction(UndoManager manager) {
            this._manager = manager;
        }

        public void actionPerformed(ActionEvent evt) {
            try {
                _manager.redo();
            } catch (CannotRedoException e) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    // KeyTypedAction
    // if key is typed then add * to tab
    class KeyTypedAction implements KeyListener {
        @Override
        public void keyPressed(KeyEvent evt) {
            int keycode = evt.getKeyCode();
            boolean is_ControlDown = evt.isControlDown();

            if (keycode == KeyEvent.VK_X && is_ControlDown) {
                Edit_Cut_Action();
            } else if (keycode == KeyEvent.VK_C && is_ControlDown) {
                Edit_Copy_Action();
            } else if (keycode == KeyEvent.VK_V && is_ControlDown) {
                int sel = _tabbedPane.getSelectedIndex();
                String tabtext = _tabbedPane.getTitleAt(sel);
                if (tabtext.contains("*")) {} else {
                    _tabbedPane.setTitleAt(sel, _tabbedPane.getTitleAt(sel) + "*");

                }
            } else if (keycode == KeyEvent.VK_S && is_ControlDown) {
                File_Save_Action();
            }
        }

        @Override
        public void keyReleased(KeyEvent evt) {}

        @Override
        public void keyTyped(KeyEvent evt) {
            if (!evt.isControlDown()) {
                int sel = _tabbedPane.getSelectedIndex();
                String tabtext = _tabbedPane.getTitleAt(sel);
                if (tabtext.contains("*")) {} else {
                    _tabbedPane.setTitleAt(sel, _tabbedPane.getTitleAt(sel) + "*");

                }
            }
        }
    }

    // actions when frame is loading & closing
    class Load_Close_Frame_Action extends WindowAdapter {
        @Override
        public void windowOpened(WindowEvent evt) {
            File_New_Action();
        }

        @Override
        public void windowClosing(WindowEvent evt) {
            File_CloseAll_Action();
            if (_tabbedPane.getTabCount() == 0) {
                System.exit(0);
            }
        }
    }

    //mein method
    public static void main(String args[]) {
        try {

            JFrame.setDefaultLookAndFeelDecorated(false);
            TabbedNotepad tb = new TabbedNotepad();
            tb.setExtendedState(JFrame.MAXIMIZED_BOTH);
            tb.setSize(800, 600);
            tb.setLocation(100, 50);
            tb.setVisible(true);
        } catch (Exception e) {e.printStackTrace();}
    }
}