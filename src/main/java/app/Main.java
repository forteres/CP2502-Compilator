package app;

import ui.Ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        /*
        // Cria a janela principal
        JFrame frame = new JFrame("Minha Primeira Tela Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // centraliza a janela

        // Painel para agrupar componentes
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Campo de texto
        JTextField textField = new JTextField();
        panel.add(textField, BorderLayout.NORTH);

        // Área de texto
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botão
        JButton button = new JButton("Clique aqui");
        panel.add(button, BorderLayout.SOUTH);

        // Ação do botão
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String texto = textField.getText();
                textArea.append("Você digitou: " + texto + "\n");
                textField.setText("");
            }
        });

        // Adiciona painel à janela
        frame.getContentPane().add(panel);
        frame.setVisible(true);

         */

        // Ideia base para criacao e uma tela acima
        Ui screen = new Ui();
    }
}
