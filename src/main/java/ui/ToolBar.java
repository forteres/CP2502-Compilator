package ui;

import javax.swing.*;

public class ToolBar {
    JToolBar toolBar;

    public ToolBar() {
        toolBar = new JToolBar();

       JButton fileButton = new JButton("F");
       JButton folderButton = new JButton("P");
       JButton saveButton = new JButton("S");
       JButton cutButton = new JButton("A");
       JButton copyButton = new JButton("C");
       JButton pasteButton = new JButton("P");
       JButton buildButton = new JButton("B");
       JButton runButton = new JButton("R");
       JButton sosButton = new JButton("L");

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
