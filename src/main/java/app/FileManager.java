package app;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class FileManager {
    private Path ccFile = null;
    private String fileInitialState = "";
    public enum GuardDecision { PROCEED, ABORT }
    public FileManager(){}

    public boolean saveAs(String ccFileState) {
        JFileChooser fileChooser = new JFileChooser();
        if (ccFile != null) {
            fileChooser.setSelectedFile(ccFile.toFile());
        } else {
            fileChooser.setSelectedFile(new java.io.File("novoArquivo.txt"));
        }

        int confirm = fileChooser.showSaveDialog(null);
        if (confirm != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        Path newFilePath = fileChooser.getSelectedFile().toPath();
        if (!newFilePath.toString().toLowerCase().endsWith(".txt")) {
            newFilePath = Paths.get(newFilePath.toString() + ".txt");
        }

        if (Files.exists(newFilePath)) {
            int overwrite = JOptionPane.showConfirmDialog(
                    null,
                    "O arquivo já existe. Deseja substituir?",
                    "Confirmar substituição",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (overwrite != JOptionPane.YES_OPTION) {
                return false;
            }
        }

        try {
            Files.writeString(newFilePath, ccFileState, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            ccFile = newFilePath;
            fileInitialState = ccFileState;
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean save(String ccFileState) {
        if (Objects.equals(ccFileState, fileInitialState)) {
            return true;
        }

        if (ccFile == null) {
            return saveAs(ccFileState);
        }
        try {
            Files.writeString(ccFile, ccFileState, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            fileInitialState = ccFileState;
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean openFile(String ccFileState) throws IOException {
        if (needSavePrompt(ccFileState) == GuardDecision.ABORT) return false;

        JFileChooser fileChooser = new JFileChooser();
        int resultPath = fileChooser.showOpenDialog(null);
        if (resultPath != JFileChooser.APPROVE_OPTION) return false;

        Path path = fileChooser.getSelectedFile().toPath();
        if (!isTxt(path)) {
            JOptionPane.showMessageDialog(null, "Selecione apenas arquivos .txt!");
            return false;
        }

        ccFile = path;
        fileInitialState = Files.readString(path);
        return true;
    }

    public boolean newFile(String ccFileState) {
        if (needSavePrompt(ccFileState) == GuardDecision.ABORT) return false;
        ccFile = null;
        fileInitialState = "";
        return true;
    }

    public GuardDecision needSavePrompt(String ccFileState){
        if (ccFile == null && (ccFileState == null || ccFileState.isEmpty())) {
            return GuardDecision.PROCEED;
        }

        if (Objects.equals(ccFileState, fileInitialState)) {
            return GuardDecision.PROCEED;
        }

        int opt = JOptionPane.showConfirmDialog(
                null,
                "Você tem alterações não salvas. Deseja salvar?",
                "Arquivo modificado",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (opt == JOptionPane.CANCEL_OPTION || opt == JOptionPane.CLOSED_OPTION) {
            return GuardDecision.ABORT;
        }
        if (opt == JOptionPane.NO_OPTION) {
            return GuardDecision.PROCEED;
        }

        boolean ok = (ccFile != null) ? save(ccFileState) : saveAs(ccFileState);
        return ok ? GuardDecision.PROCEED : GuardDecision.ABORT;
    }

    public static boolean isTxt(Path p) {
        String name = p.getFileName().toString().toLowerCase();
        return name.endsWith(".txt");
    }

    public String getFileInitialState() {return fileInitialState;}
}
