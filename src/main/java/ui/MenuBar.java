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
        JMenuItem menuFile_Open = new JMenuItem("Abrir");
        JMenuItem menuFile_Save = new JMenuItem("Salvar");
        JMenuItem menuFile_SaveAs = new JMenuItem("Salvar Como");
        JMenuItem menuFile_Exit = new JMenuItem("Sair");
        menuFile.add(menuFile_New);
        menuFile.add(menuFile_Open);
        menuFile.add(menuFile_Save);
        menuFile.add(menuFile_SaveAs);
        menuFile.add(menuFile_Exit);
        return menuFile;
    }
    private static JMenu getMenuEdit() {
        JMenu menuEdit = new JMenu("Edição");
        JMenuItem menuEdit_Copy = new JMenuItem("Copiar");
        JMenuItem menuEdit_Cut = new JMenuItem("Recortar");
        JMenuItem menuEdit_Paste = new JMenuItem("Colar");
        menuEdit.add(menuEdit_Copy);
        menuEdit.add(menuEdit_Cut);
        menuEdit.add(menuEdit_Paste);
        return menuEdit;
    }
    private static JMenu getMenuCompilator() {
        JMenu menuCompilator = new JMenu("Compilação");
        JMenuItem menuCompilator_Build = new JMenuItem("Compilar");
        JMenuItem menuCompilator_Run = new JMenuItem("Executar");
        menuCompilator.add(menuCompilator_Build);
        menuCompilator.add(menuCompilator_Run);
        return menuCompilator;
    }
}
