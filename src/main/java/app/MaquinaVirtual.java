package app;
import org.javatuples.Triplet;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static ui.Ui.*;

public class MaquinaVirtual {
    private ArrayList<Object> memoria;
    private ArrayList<Triplet<Integer, String, Object>> listaDeInstrucoes;
    private int topo;
    private int ponteiro;
    private JTextArea resultArea;

    public MaquinaVirtual(ArrayList<Triplet<Integer, String, Object>> listaDeInstrucoes, JTextArea resultArea) {
        this.memoria = new ArrayList<>();
        this.listaDeInstrucoes = listaDeInstrucoes;
        this.topo = -1;
        this.ponteiro = 1;
        this.resultArea = resultArea;
    }

    public void executarInstrucoes(){
        boolean stopped = false;
        while(!stopped){
            Triplet<Integer, String, Object> instrucao = listaDeInstrucoes.get(ponteiro-1);
            switch (instrucao.getValue1()){
                case "ALI":
                    System.out.println("ALI " + instrucao.getValue2());
                    ALI((Integer) instrucao.getValue2());
                    break;
                case "ALR":
                    System.out.println("ALR " + instrucao.getValue2());
                    ALR((Integer) instrucao.getValue2());
                    break;
                case "ALS":
                    System.out.println("ALS " + instrucao.getValue2());
                    ALS((Integer) instrucao.getValue2());
                    break;
                case "ALB":
                    System.out.println("ALB " + instrucao.getValue2());
                    ALB((Integer) instrucao.getValue2());
                    break;
                case "LDI":
                    System.out.println("LDI " + instrucao.getValue2());
                    LDI((Integer) instrucao.getValue2());
                    break;
                case "LDR":
                    System.out.println("LDR " + instrucao.getValue2());
                    LDR((Float) instrucao.getValue2());
                    break;
                case "LDS":
                    System.out.println("LDS " + instrucao.getValue2());
                    LDS((String) instrucao.getValue2());
                    break;
                case "LDB":
                    System.out.println("LDB " + instrucao.getValue2());
                    LDB((Integer) instrucao.getValue2());
                    break;
                case "LDV":
                    System.out.println("LDV " + instrucao.getValue2());
                    LDV((Integer) instrucao.getValue2());
                    break;
                case "STR":
                    System.out.println("STR " + instrucao.getValue2());
                    STR((Integer) instrucao.getValue2());
                    break;
                case "LDX":
                    System.out.println("LDX ");
                    LDX();
                    break;
                case "STX":
                    System.out.println("STX ");
                    STX();
                    break;
                case "ADD":
                    System.out.println("ADD ");
                    ADD();
                    break;
                case "SUB":
                    System.out.println("SUB ");
                    SUB();
                    break;
                case "MUL":
                    System.out.println("MUL ");
                    MUL();
                    break;
                case "DIV":
                    System.out.println("DIV ");
                    try {
                        DIV();
                    }catch (IllegalArgumentException e){
                        stopped = true;
                        System.out.println(e.getMessage());
                    }
                    break;
                case "MOD":
                    System.out.println("MOD ");
                    try {
                        MOD();
                    }catch (IllegalArgumentException e){
                        stopped = true;
                        System.out.println(e.getMessage());
                    }
                    break;
                case "REM":
                    System.out.println("REM ");
                    try {
                        REM();
                    }catch (IllegalArgumentException e){
                        stopped = true;
                        System.out.println(e.getMessage());
                    }
                    break;
                case "POW":
                    System.out.println("POW ");
                    POW();
                    break;
                case "AND":
                    System.out.println("AND ");
                    AND();
                    break;
                case "OR":
                    System.out.println("OR ");
                    OR();
                    break;
                case "NOT":
                    System.out.println("NOT ");
                    NOT();
                    break;
                case "SMR":
                    System.out.println("SMR ");
                    SMR();
                    break;
                case "BGR":
                    System.out.println("BGR ");
                    BGR();
                    break;
                case "SME":
                    System.out.println("SME ");
                    SME();
                    break;
                case "BGE":
                    System.out.println("BGE ");
                    BGE();
                    break;
                case "EQL":
                    System.out.println("EQL ");
                    EQL();
                    break;
                case "DIF":
                    System.out.println("DIF ");
                    DIF();
                    break;
                case "JMF":
                    System.out.println("JMF " + instrucao.getValue2());
                    JMF((Integer) instrucao.getValue2());
                    break;
                case "JMT":
                    System.out.println("JMT " + instrucao.getValue2());
                    JMT((Integer) instrucao.getValue2());
                    break;
                case "JMP":
                    System.out.println("JMP " + instrucao.getValue2());
                    JMP((Integer) instrucao.getValue2());
                    break;
                case "REA":
                    System.out.println("REA " + instrucao.getValue2());
                    REA((Integer) instrucao.getValue2());
                    break;
                case "WRT":
                    System.out.println("WRT ");
                    WRT();
                    break;
                case "STP":
                    System.out.println("STP ");
                    stopped = true;
                    break;
            }
        }
    }

    private double toDouble(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        throw new RuntimeException("Tipo não numérico: " + obj);
    }

    private int toInt(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        throw new RuntimeException("Índice não inteiro: " + obj);
    }

    private boolean toBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue() != 0.0;
        }
        throw new RuntimeException("Não foi possível converter para boolean: " + obj);
    }

    private boolean verificaTipoInteger(Object obj){
        Integer i = 0;
        return (obj.getClass().equals(i.getClass()));
    }

    private boolean verificaTipoFloat(Object obj){
        Double f = 0.0;
        return (obj.getClass().equals(f.getClass()));
    }

    private boolean verificaTipoNumerico(Object obj){
        return (verificaTipoFloat(obj) || verificaTipoInteger(obj));
    }

    private boolean verificaTipoString(Object obj){
        String s = "0";
        return (obj.getClass().equals(s.getClass()));
    }

    private boolean verificaTipoBoolean(Object obj){
        Boolean b = true;
        return (obj.getClass().equals(b.getClass()));
    }

    private void ALI(Integer deslocamento){ //gepetation
        int novoTopo = topo + deslocamento;

        while (memoria.size() <= novoTopo) {
            memoria.add(0);
        }

        for (int i = topo + 1; i <= novoTopo; i++) {
            memoria.set(i, 0);
        }

        topo = novoTopo;
        ponteiro++;
    }

    private void ALR(Integer deslocamento){
        int novoTopo = topo + deslocamento;

        while (memoria.size() <= novoTopo) {
            memoria.add(0.0);
        }

        for (int i = topo + 1; i <= novoTopo; i++) {
            memoria.set(i, 0.0);
        }

        topo = novoTopo;
        ponteiro++;
    }

    private void ALS(Integer deslocamento){
        for(int i = topo + 1; i<topo + deslocamento; i++){
            memoria.set(i,"");
        }
        topo = topo + deslocamento;
        ponteiro++;
    }

    private void ALB(Integer deslocamento){
        for(int i = topo + 1; i<topo + deslocamento; i++){
            memoria.set(i,false);
        }
        topo = topo + deslocamento;
        ponteiro++;
    }

    private void LDI(Integer constante){
        topo++;
        try {
            memoria.set(topo,constante);
        } catch (IndexOutOfBoundsException e){
            memoria.add(constante);
        }
        ponteiro++;
    }

    private void LDR(Float constante){
        topo++;
        try {
            memoria.set(topo,constante);
        } catch (IndexOutOfBoundsException e){
            memoria.add(constante);
        }
        ponteiro++;
    }

    private void LDS(String constante){
        topo++;
        try {
            memoria.set(topo,constante);
        } catch (IndexOutOfBoundsException e){
            memoria.add(constante);
        }
        ponteiro++;
    }

    private void LDB(Integer constante){
        boolean constanteParsed = constante.equals(1);
        topo++;
        try {
            memoria.set(topo,constanteParsed);
        } catch (IndexOutOfBoundsException e){
            memoria.add(constanteParsed);
        }
        ponteiro++;
    }

    private void LDV(Integer endereco){
        topo++;
        try {
            memoria.set(topo,memoria.get(endereco));
        } catch (IndexOutOfBoundsException e){
            memoria.add(memoria.get(endereco));
        }
        ponteiro++;
    }

    private void STR(Integer endereco){
        memoria.set(endereco,memoria.get(topo));
        topo--;
        ponteiro++;
    }

    private void LDX() {
        Object topoPilha = memoria.get(topo);
        if(!verificaTipoInteger(topoPilha)){
            throw new IllegalArgumentException("Indice nao é inteiro");
        }else{
            int indice = toInt(topoPilha);
            topo--;

            // Garante que a memória tenha tamanho suficiente
            while (memoria.size() <= indice) {
                memoria.add(null);
            }

            Object valor = memoria.get(indice);
            // Se for null, retorna 0.0 (valor padrão para num/real)
            memoria.set(++topo, valor != null ? valor : 0.0);
            ponteiro++;
        }
    }

    private void STX() {
        Object valor = memoria.get(topo);
        topo--;
        int indice = toInt(memoria.get(topo));
        topo--;

        // Expande memória se necessário
        while (memoria.size() <= indice) {
            memoria.add(null);
        }

        memoria.set(indice, valor);
        ponteiro++;
    }

    private void ADD() {
        Object b = memoria.get(topo);
        topo--;
        Object a = memoria.get(topo);
        if (verificaTipoFloat(a) || verificaTipoFloat(b)) {
            // Qualquer um for real, resultado é real
            double resultado = toDouble(a) + toDouble(b);
            memoria.set(topo, resultado);
        } else {
            // Ambos inteiros, mantém Integer
            int resultado = toInt(a) + toInt(b);
            memoria.set(topo, resultado);
        }
        ponteiro++;
    }

    private void SUB() {
        Object b = memoria.get(topo);
        Object a = memoria.get(topo - 1);
        if (verificaTipoFloat(a) || verificaTipoFloat(b)) {
            double resultado = toDouble(a) - toDouble(b);
            memoria.set(topo - 1, resultado);
        } else {
            int resultado = toInt(a) - toInt(b);
            memoria.set(topo - 1, resultado);
        }
        topo--;
        ponteiro++;
    }

    private void MUL() {
        Object b = memoria.get(topo);
        topo--;
        Object a = memoria.get(topo);
        if (verificaTipoFloat(a) || verificaTipoFloat(b)) {
            double resultado = toDouble(a) * toDouble(b);
            memoria.set(topo, resultado);
        } else {
            int resultado = toInt(a) * toInt(b);
            memoria.set(topo, resultado);
        }
    }

    private void DIV() {
        Object b = memoria.get(topo);
        Object a = memoria.get(topo - 1);

        double denominador = toDouble(b);
        if (denominador == 0.0) {
            throw new RuntimeException("Divisão por zero");
        }

        double resultado = toDouble(a) / denominador;
        memoria.set(topo - 1, resultado);
        topo--;
        ponteiro++;
    }

    private void MOD(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if(topoPilha.equals(0)){
            throw new IllegalArgumentException("RUNTIME error: divisao por 0");
        }
        if(verificaTipoNumerico(topoPilha) && verificaTipoNumerico(subtopo)) {
            memoria.set(topo - 1, ((Integer) subtopo) % ((Integer)topoPilha));
            topo--;
            ponteiro++;
        }
    }

    private void REM(){
        int signal = 1;
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if(topoPilha.equals(0)){
            throw new IllegalArgumentException("RUNTIME error: divisao por 0");
        }
        if(verificaTipoNumerico(topoPilha) && verificaTipoNumerico(subtopo)) {
            if ((Integer) subtopo < 0){
                signal = -1;
            }
            memoria.set(topo - 1, signal*(((Integer) subtopo) % ((Integer)topoPilha)));
            topo--;
            ponteiro++;
        }
    }

    private void POW() {
        Object b = memoria.get(topo);
        topo--;
        Object a = memoria.get(topo);

        double resultado = Math.pow(toDouble(a), toDouble(b));
        memoria.set(topo, resultado);
        ponteiro++;
    }

    private void AND() {
        Object b = memoria.get(topo);
        topo--;
        Object a = memoria.get(topo);
        memoria.set(topo, toBoolean(a) && toBoolean(b));
        ponteiro++;
    }

    private void OR(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if(verificaTipoBoolean(topoPilha) && verificaTipoBoolean(subtopo)){
            memoria.set(topo - 1, toBoolean(subtopo) || toBoolean(topoPilha));
            topo--;
            ponteiro++;
        }
    }

    private void NOT(){
        Object topoPilha = memoria.get(topo);
        if(verificaTipoBoolean(topoPilha)){
            memoria.set(topo, !toBoolean(topoPilha));
            ponteiro++;
        }
    }

    private void SMR() { // <
        Object b = memoria.get(topo);
        Object a = memoria.get(topo - 1);
        memoria.set(topo - 1, toDouble(a) < toDouble(b));
        topo--;
        ponteiro++;
    }

    private void BGR() { // >
        Object b = memoria.get(topo);
        Object a = memoria.get(topo - 1);
        memoria.set(topo - 1, toDouble(a) > toDouble(b));
        topo--;
        ponteiro++;
    }

    private void SME() { // <=
        Object b = memoria.get(topo);
        Object a = memoria.get(topo - 1);
        memoria.set(topo - 1, toDouble(a) <= toDouble(b));
        topo--;
        ponteiro++;
    }

    private void BGE() { // >=
        Object b = memoria.get(topo);
        Object a = memoria.get(topo - 1);
        memoria.set(topo - 1, toDouble(a) >= toDouble(b));
        topo--;
        ponteiro++;
    }

    private void EQL() { // ==
        Object b = memoria.get(topo);
        topo--;
        Object a = memoria.get(topo);
        memoria.set(topo, toDouble(a) == toDouble(b));
        ponteiro++;
    }

    private void DIF() { // !=
        Object b = memoria.get(topo);
        topo--;
        Object a = memoria.get(topo);
        memoria.set(topo, toDouble(a) != toDouble(b));
        ponteiro++;
    }

    private void JMF(Integer endereco){
        if(toBoolean(memoria.get(topo))){
            System.out.println(memoria.get(topo).getClass());
            ponteiro++;
        }else{
            ponteiro = endereco;
        }
        topo--;
    }

    private void JMT(Integer endereco){
        if((Boolean) memoria.get(topo)){
            ponteiro = endereco;
        }else{
            ponteiro++;
        }
        topo--;
    }
    private void JMP(Integer endereco){
        ponteiro = endereco;
    }

    private void REA(Integer tipo) {
        topo++;

        resultArea.append("\n>> ");
        resultArea.setEditable(true);
        resultArea.setCaretPosition(resultArea.getDocument().getLength());
        resultArea.requestFocus();

        waitingInput = true;

        synchronized (lock) {
            try {
                while (waitingInput) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Erro ao aguardar input do usuário", e);
            }
        }

        String input = lastInput;

        switch (tipo) {
            case 1 -> {
                try {
                    memoria.set(topo,Integer.parseInt(input));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("RUNTIME error: tipo incorreto - esperado int");
                }
            }
            case 2 -> {
                try {
                    memoria.set(topo,Float.parseFloat(input));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("RUNTIME error: tipo incorreto - esperado real");
                }
            }
            case 3 -> {
                if (input == null || input.isEmpty()) {
                    throw new RuntimeException("RUNTIME error: tipo incorreto - esperado char");
                }
                memoria.set(topo,input); // talvez validar UTF8/lexico antes?
            }
            default -> throw new RuntimeException("RUNTIME error: tipo incorreto");
        }
        ponteiro++;
    }


    private void WRT(){
        Object topoPilha = memoria.get(topo);
        resultArea.append(String.valueOf(topoPilha)); // talvez adicionar \n
        topo--;
        ponteiro++;
    }
}
