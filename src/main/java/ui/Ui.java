package ui;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.lang.System.exit;

public class Ui {
    JFrame screen;
    MenuBar menuBar;
    ToolBar toolBar;
    RSyntaxTextArea editArea;
    RTextScrollPane scrollArea;
    JTextField resultArea;
    JSplitPane splitPane;

    public Ui(){
        screen = new JFrame("Compilador");
        screen.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        screen.setLayout(new BorderLayout());
        screen.setSize(800,600);
        screen.setLocationRelativeTo(null);
        screen.setExtendedState(JFrame.MAXIMIZED_BOTH);

        editArea = new RSyntaxTextArea(20, 60);
        editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA); // alterar
        editArea.setCodeFoldingEnabled(true);

        scrollArea = new RTextScrollPane(editArea);

        resultArea = new JTextField();
        resultArea.setEditable(false);

        menuBar = new MenuBar(editArea, resultArea);
        screen.setJMenuBar(menuBar.menuBar);

        toolBar = new ToolBar(menuBar);
        screen.add(toolBar.toolBar, BorderLayout.NORTH);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollArea, resultArea);
        splitPane.setResizeWeight(0.7);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerSize(10);
        screen.add(splitPane);

        screen.setVisible(true);

        screen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuBar.exitAction();
            }
        });
    }
}
