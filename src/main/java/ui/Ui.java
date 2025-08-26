package ui;

import javax.swing.*;
import java.awt.*;

public class Ui {
    JFrame screen;
    MenuBar menuBar;
    ToolBar toolBar;
    public Ui(){
        screen = new JFrame("Compilador");
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setSize(800,600);
        screen.setLocationRelativeTo(null);
        screen.setExtendedState(JFrame.MAXIMIZED_BOTH);

        menuBar = new MenuBar();
        screen.setJMenuBar(menuBar.menuBar);

        toolBar = new ToolBar();
        screen.add(toolBar.toolBar, BorderLayout.NORTH);

        screen.setVisible(true);
    }
}
