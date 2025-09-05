package app;

import ui.Ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        //Ui screen = new Ui();

        String texto = "begin teste\n" +
                "       set                  a\n" +
                "= 10\n" +
                "show               (\n" +
                "3.14 ,cont\n" +
                "               \" abrir\n" +
                ") @           ";

        InputStream input = new ByteArrayInputStream(texto.getBytes(StandardCharsets.UTF_8));

        try {
            Linguagem20252.analisar(input);
        } catch (ParseException e) {
            System.out.println(e);
        }

    }
}
