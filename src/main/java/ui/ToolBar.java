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

        JButton fileButton   = new JButton(loadIcon("icons/addFile.png", iconSize, iconSize));
        JButton folderButton = new JButton(loadIcon("icons/folder.png", iconSize, iconSize));
        JButton saveButton   = new JButton(loadIcon("icons/save.png", iconSize, iconSize));
        JButton cutButton    = new JButton(loadIcon("icons/cut.png", iconSize, iconSize));
        cutButton.addActionListener(e -> menuBar.cutAction());
        JButton copyButton   = new JButton(loadIcon("icons/copy.png", iconSize, iconSize));
        copyButton.addActionListener(e -> menuBar.copyAction());
        JButton pasteButton  = new JButton(loadIcon("icons/paste.png", iconSize, iconSize));
        pasteButton.addActionListener(e -> menuBar.pasteAction());
        JButton buildButton  = new JButton(loadIcon("icons/hammer.png", iconSize, iconSize));
        JButton runButton    = new JButton(loadIcon("icons/play.png", iconSize, iconSize));
        JButton sosButton    = new JButton(loadIcon("icons/sos.png", iconSize, iconSize));

        toolBar.add(fileButton);
        toolBar.add(folderButton);
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
