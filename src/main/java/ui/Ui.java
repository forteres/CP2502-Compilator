package ui;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Ui {
    JFrame screen;
    MenuBar menuBar;
    ToolBar toolBar;
    RSyntaxTextArea editArea;
    RTextScrollPane scrollArea;
    RSyntaxTextArea resultArea;
    JSplitPane splitPane;
    JLabel fileNameStatus;
    JLabel lineColumnStatus;
    JPanel centerPanel;
    JPanel statusBar;

    public static final Object lock = new Object();
    public static volatile boolean waitingInput = false;
    public static String lastInput = "";

    public Ui(){
        //Screen
        screen = new JFrame("Compilador");
        screen.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        screen.setLayout(new BorderLayout());
        screen.setSize(800,600);
        screen.setLocationRelativeTo(null);
        screen.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //TextBoxes
        editArea = new RSyntaxTextArea(20, 60);
        editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA); // alterar
        editArea.setCodeFoldingEnabled(true);

        scrollArea = new RTextScrollPane(editArea);

        resultArea = new RSyntaxTextArea(20, 60);
        resultArea.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);
        resultArea.setCodeFoldingEnabled(true);
        resultArea.setEditable(false);
        resultArea.setHighlightCurrentLine(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setText("");

        RTextScrollPane resultAreaScrollPane = new RTextScrollPane(resultArea);

        //Labels
        fileNameStatus = new JLabel("Novo Arquivo");
        lineColumnStatus = new JLabel("Linha: -, Coluna: -");

        centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.add(fileNameStatus);

        statusBar = new JPanel(new BorderLayout());
        statusBar.add(lineColumnStatus, BorderLayout.WEST);
        statusBar.add(centerPanel, BorderLayout.CENTER);
        screen.add(statusBar, BorderLayout.SOUTH);

        //Menus
        menuBar = new MenuBar(editArea, resultArea,fileNameStatus);
        screen.setJMenuBar(menuBar.menuBar);

        toolBar = new ToolBar(menuBar);
        screen.add(toolBar.toolBar, BorderLayout.NORTH);

        //Layout
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollArea, resultAreaScrollPane);
        splitPane.setResizeWeight(0.7);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerSize(10);
        screen.add(splitPane);

        //Misc
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");

        UIManager.put("FileChooser.openDialogTitleText", "Abrir Arquivo");
        UIManager.put("FileChooser.openButtonText", "Abrir");
        UIManager.put("FileChooser.openButtonToolTipText", "Abrir o arquivo selecionado");
        UIManager.put("FileChooser.cancelButtonText", "Cancelar");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Cancelar a seleção");
        UIManager.put("FileChooser.lookInLabelText", "Buscar em");
        UIManager.put("FileChooser.fileNameLabelText", "Nome do arquivo");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Tipo de arquivo");
        UIManager.put("FileChooser.acceptAllFileFilterText", "Todos os Arquivos");

        screen.setVisible(true);

        //Listeners
        screen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuBar.exitAction();
            }
        });
        editArea.addCaretListener(e -> {
            int pos = editArea.getCaretPosition();
            try {
                int line = editArea.getLineOfOffset(pos) + 1;
                int column = pos - editArea.getLineStartOffset(line - 1) + 1;
                lineColumnStatus.setText("Linha: " + line + ", Coluna: " + column);
            } catch (Exception ex) {
                lineColumnStatus.setText("Linha: -, Coluna: -");
            }
        });

        resultArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (waitingInput && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    synchronized (lock) {

                        String[] lines = resultArea.getText().split("\n");
                        String lastLine = lines[lines.length - 1];

                        if (lastLine.startsWith(">> ")) {
                            lastInput = lastLine.substring(3).trim();
                        } else {
                            lastInput = lastLine.trim();
                        }

                        resultArea.setEditable(false);
                        resultArea.append("\n");

                        waitingInput = false;
                        lock.notifyAll();
                    }

                    e.consume();
                }
            }
        });

    }
}
