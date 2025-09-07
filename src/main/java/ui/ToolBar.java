package ui;

import javax.swing.*;
import java.awt.*;

public class ToolBar {
    JToolBar toolBar;
    MenuBar menuBar;

    private static ImageIcon loadIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public ToolBar(MenuBar menuBar) {
        int iconSize = 24;

        toolBar = new JToolBar();
        this.menuBar = menuBar;

        JButton newFileButton   = new JButton(loadIcon("icons/addFile.png", iconSize, iconSize));
        newFileButton.addActionListener(e -> {menuBar.newAction();});
        newFileButton.setToolTipText("Novo Arquivo");
        JButton openFileButton = new JButton(loadIcon("icons/folder.png", iconSize, iconSize));
        openFileButton.addActionListener(e -> {menuBar.openAction();});
        openFileButton.setToolTipText("Abrir Arquivo");
        JButton saveButton   = new JButton(loadIcon("icons/save.png", iconSize, iconSize));
        saveButton.addActionListener(e -> {menuBar.saveAction();});
        saveButton.setToolTipText("Salvar Arquivo");
        JButton cutButton    = new JButton(loadIcon("icons/cut.png", iconSize, iconSize));
        cutButton.addActionListener(e -> menuBar.cutAction());
        cutButton.setToolTipText("Recortar");
        JButton copyButton   = new JButton(loadIcon("icons/copy.png", iconSize, iconSize));
        copyButton.addActionListener(e -> menuBar.copyAction());
        copyButton.setToolTipText("Copiar");
        JButton pasteButton  = new JButton(loadIcon("icons/paste.png", iconSize, iconSize));
        pasteButton.addActionListener(e -> menuBar.pasteAction());
        pasteButton.setToolTipText("Colar");
        JButton buildButton  = new JButton(loadIcon("icons/hammer.png", iconSize, iconSize));
        buildButton.addActionListener(e -> menuBar.buildAction());
        buildButton.setToolTipText("Compilar");
        JButton runButton    = new JButton(loadIcon("icons/play.png", iconSize, iconSize));
        runButton.addActionListener(e -> menuBar.runAction());
        runButton.setToolTipText("Executar");
        JButton sosButton    = new JButton(loadIcon("icons/sos.png", iconSize, iconSize));
        sosButton.setToolTipText("Ajuda");

        toolBar.add(newFileButton);
        toolBar.add(openFileButton);
        toolBar.add(saveButton);

        toolBar.addSeparator();
        toolBar.add(cutButton);
        toolBar.add(copyButton);
        toolBar.add(pasteButton);

        toolBar.addSeparator();
        toolBar.add(buildButton);
        toolBar.add(runButton);

        toolBar.addSeparator();
        toolBar.add(sosButton);
    }
}
