package ui;

import app.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuBar {
    JMenuBar menuBar;
    RSyntaxTextArea editArea;
    JTextArea resultArea;
    FileManager fileManager;
    JLabel fileNameStatus;

    public MenuBar(RSyntaxTextArea editArea, JTextArea resultArea, JLabel fileNameStatus) {
        menuBar = new JMenuBar();
        this.editArea = editArea;
        this.resultArea = resultArea;
        this.fileNameStatus = fileNameStatus;

        JMenu menuFile = getMenuFile();

        JMenu menuEdit = getMenuEdit();

        JMenu menuCompilator = getMenuCompilator();

        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuCompilator);

        fileManager = new FileManager();
    }

    private JMenu getMenuFile() {
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
    private JMenu getMenuEdit() {
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
    private JMenu getMenuCompilator() {
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
    public void newAction(){
        if(fileManager.newFile(editArea.getText())){
            editArea.setText(fileManager.getFileInitialState());
            if(!fileManager.getFileName().isEmpty()){
                fileNameStatus.setText(fileManager.getFileName());
            }else{
                fileNameStatus.setText("Arquivo novo");
                //limpar caixa de saida
            }
        }
    }
    public void openAction(){
        try {
            if(fileManager.openFile(editArea.getText())) {
                editArea.setText(fileManager.getFileInitialState());
                if(!fileManager.getFileName().isEmpty()){
                    fileNameStatus.setText(fileManager.getFileName());
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao abrir arquivo: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Erro inesperado: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    public void saveAction(){
        fileManager.save(editArea.getText());
        if(!fileManager.getFileName().isEmpty()){
            fileNameStatus.setText(fileManager.getFileName());
        }
    }
    public void saveAsAction(){
        fileManager.saveAs(editArea.getText());
        if(!fileManager.getFileName().isEmpty()){
            fileNameStatus.setText(fileManager.getFileName());
        }
    }

    //Edit Functions
    public void cutAction(){
        editArea.cut();
    }
    public void copyAction(){
        editArea.copy();
    }
    public void pasteAction(){
        editArea.paste();
    }

    //Compilator Functions
    public void buildAction(){
        if(!editArea.getText().isEmpty()) {
            InputStream inputLexico = new ByteArrayInputStream(editArea.getText().getBytes());
            try {
                List<Token> tokens = (List<Token>) Linguagem20252.analiseLexica(inputLexico);
                if(Linguagem20252.errosLexicosCount > 0){
                    this.resultArea.setText(TokenStringBuilder.formatTokenToString(tokens));
                }else{
                    InputStream inputSintatico = new ByteArrayInputStream(editArea.getText().getBytes());
                    StringBuilder errosSintaticos = Linguagem20252.analiseSintatica(inputSintatico);
                    if(Linguagem20252.errosSintaticosCount > 0) {
                        this.resultArea.setText(errosSintaticos.toString());
                    }else {
                        System.out.println("Programa compilado com sucesso!");
                    }
                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }else{
            this.resultArea.setText("Nada a Compilar"); // talvez tirar
        }
    }
    public void runAction(){
    }
    public void exitAction(){
        if (fileManager.needSavePrompt(editArea.getText()) == FileManager.GuardDecision.ABORT) {
            return;
        }
        System.exit(0);
    }
}
