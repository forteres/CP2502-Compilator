package ui;

import javax.swing.*;

public class MenuBar {
    JMenuBar menuBar;
            public MenuBar() {
                menuBar = new JMenuBar();

                JMenu menuFile = new JMenu("Arquivo");
                JMenuItem menuEdit = new JMenu("Edição");
                JMenuItem menuCompilator = new JMenu("Compilação");

                menuBar.add(menuFile);
                menuBar.add(menuEdit);
                menuBar.add(menuCompilator);
            }
}
