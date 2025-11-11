package ui;

import org.javatuples.Triplet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class InstructionTable extends JFrame {

    public InstructionTable(ArrayList<Triplet<Integer, String, Object>> listaDeInstrucoes) {
        super("Código objeto");

        String[] colunas = {"Código", "OPCODE", "Argumento"};

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0){
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };

        for (Triplet<Integer, String, Object> instrucao : listaDeInstrucoes) {
            modelo.addRow(new Object[]{
                    instrucao.getValue0(),
                    instrucao.getValue1(),
                    instrucao.getValue2()
            });
        }

        JTable tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);

        add(scroll, BorderLayout.CENTER);

        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

