package ui;

import javax.swing.*;

public class MenuBar {
    JMenuBar menuBar;
            public MenuBar() {
                menuBar = new JMenuBar();

                JMenu menuFile = getMenuFile();

                JMenu menuEdit = getMenuEdit();

                JMenu menuCompilator = getMenuCompilator();

                menuBar.add(menuFile);
                menuBar.add(menuEdit);
                menuBar.add(menuCompilator);
            }

    private static JMenu getMenuFile() {
        JMenu menuFile = new JMenu("Arquivo");

        JMenuItem menuFile_New = new JMenuItem("Novo");
        menuFile_New.addActionListener(e -> newAction());
        JMenuItem menuFile_Open = new JMenuItem("Abrir");
        menuFile_Open.addActionListener(e -> openAction());
        JMenuItem menuFile_Save = new JMenuItem("Salvar");
        menuFile_Save.addActionListener(e -> saveAction());
        JMenuItem menuFile_SaveAs = new JMenuItem("Salvar Como");
        menuFile_SaveAs.addActionListener(e -> saveAsAction());
        JMenuItem menuFile_Exit = new JMenuItem("Sair");
        menuFile_Exit.addActionListener(e -> exitAction());

        menuFile.add(menuFile_New);
        menuFile.add(menuFile_Open);
        menuFile.add(menuFile_Save);
        menuFile.add(menuFile_SaveAs);
        menuFile.add(menuFile_Exit);
        return menuFile;
    }
    private static JMenu getMenuEdit() {
        JMenu menuEdit = new JMenu("Edição");

        JMenuItem menuEdit_Cut = new JMenuItem("Recortar");
        menuEdit_Cut.addActionListener(e -> cutAction());
        JMenuItem menuEdit_Copy = new JMenuItem("Copiar");
        menuEdit_Copy.addActionListener(e -> copyAction());
        JMenuItem menuEdit_Paste = new JMenuItem("Colar");
        menuEdit_Paste.addActionListener(e -> pasteAction());

        menuEdit.add(menuEdit_Cut);
        menuEdit.add(menuEdit_Copy);
        menuEdit.add(menuEdit_Paste);
        return menuEdit;
    }
    private static JMenu getMenuCompilator() {
        JMenu menuCompilator = new JMenu("Compilação");

        JMenuItem menuCompilator_Build = new JMenuItem("Compilar");
        menuCompilator_Build.addActionListener(e -> buildAction());
        JMenuItem menuCompilator_Run = new JMenuItem("Executar");
        menuCompilator_Run.addActionListener(e -> runAction());

        menuCompilator.add(menuCompilator_Build);
        menuCompilator.add(menuCompilator_Run);
        return menuCompilator;
    }

    //##Button Functions

    //File Functions
    public static void newAction(){

    }
    public static void openAction(){

    }
    public static void saveAction(){

    }
    public static void saveAsAction(){

    }
    public static void exitAction(){

    }

    //Edit Functions
    public static void cutAction(){
    }
    public static void copyAction(){
    }
    public static void pasteAction(){

    }

    //Compilator Functions
    public static void buildAction(){
    }
    public static void runAction(){
    }
}
