package app;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.javatuples.Triplet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static ui.Ui.*;

public class MaquinaVirtual {
    private ArrayList<Object> memoria;
    private ArrayList<Triplet<Integer, String, Object>> listaDeInstrucoes;
    private int topo;
    private int ponteiro;
    private RSyntaxTextArea resultArea;

    public MaquinaVirtual(ArrayList<Triplet<Integer, String, Object>> listaDeInstrucoes, RSyntaxTextArea resultArea) {
        this.memoria = new ArrayList<>();
        this.listaDeInstrucoes = listaDeInstrucoes;
        this.topo = -1;
        this.ponteiro = 0;
        this.resultArea = resultArea;
    }

    public void executarInstrucoes(){
        boolean stopped = false;
        while(!stopped){
            Triplet<Integer, String, Object> instrucao = listaDeInstrucoes.get(ponteiro);
            switch (instrucao.getValue1()){
                case "ALI":
                    ALI((Integer) instrucao.getValue2());
                    break;
                case "ALR":
                    ALR((Integer) instrucao.getValue2());
                    break;
                case "ALS":
                    ALS((Integer) instrucao.getValue2());
                    break;
                case "ALB":
                    ALB((Integer) instrucao.getValue2());
                    break;
                case "LDI":
                    LDI((Integer) instrucao.getValue2());
                    break;
                case "LDR":
                    LDR((Double) instrucao.getValue2());
                    break;
                case "LDS":
                    LDS((String) instrucao.getValue2());
                    break;
                case "LDB":
                    LDB((Integer) instrucao.getValue2());
                    break;
                case "LDV":
                    LDV((Integer) instrucao.getValue2());
                    break;
                case "STR":
                    STR((Integer) instrucao.getValue2());
                    break;
                case "LDX":
                    // Nao sei exatamente como implementar
                    break;
                case "STX":
                    // Nao sei exatamente como implementar
                    break;
                case "ADD":
                    ADD();
                    break;
                case "SUB":
                    SUB();
                    break;
                case "MUL":
                    MUL();
                    break;
                case "DIV":
                    try {
                        DIV();
                    }catch (IllegalArgumentException e){
                        stopped = true;
                        System.out.println(e.getMessage());
                    }
                    break;
                case "MOD":
                    try {
                        MOD();
                    }catch (IllegalArgumentException e){
                        stopped = true;
                        System.out.println(e.getMessage());
                    }
                    break;
                case "REM":
                    try {
                        REM();
                    }catch (IllegalArgumentException e){
                        stopped = true;
                        System.out.println(e.getMessage());
                    }
                    break;
                case "POW":
                    POW();
                    break;
                case "AND":
                    AND();
                    break;
                case "OR":
                    OR();
                    break;
                case "NOT":
                    NOT();
                    break;
                case "SMR":
                    SMR();
                    break;
                case "BGR":
                    BGR();
                    break;
                case "SME":
                    SME();
                    break;
                case "BGE":
                    BGE();
                    break;
                case "EQL":
                    EQL();
                    break;
                case "DIF":
                    DIF();
                    break;
                case "JMF":
                    JMF((Integer) instrucao.getValue2());
                    break;
                case "JMT":
                    JMT((Integer) instrucao.getValue2());
                    break;
                case "JMP":
                    JMP((Integer) instrucao.getValue2());
                    break;
                case "REA":
                    REA((Integer) instrucao.getValue2());
                    break;
                case "WRT":
                    WRT();
                    break;
                case "STP":
                    stopped = true;
                    break;
            }
        }
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

    private void ALI(Integer deslocamento){
        for(int i = topo + 1; i<topo + deslocamento; i++){
            memoria.set(i,0);
        }
        topo = topo + deslocamento;
        ponteiro++;
    }

    private void ALR(Integer deslocamento){
        for(int i = topo + 1; i<topo + deslocamento; i++){
            memoria.set(i,0.0);
        }
        topo = topo + deslocamento;
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
        memoria.set(topo,constante);
        ponteiro++;
    }

    private void LDR(Double constante){
        topo++;
        memoria.set(topo,constante);
        ponteiro++;
    }

    private void LDS(String constante){
        topo++;
        memoria.set(topo,constante);
        ponteiro++;
    }

    private void LDB(Integer constante){
        boolean constanteParsed = constante.equals(1);
        topo++;
        memoria.set(topo,constanteParsed);
        ponteiro++;
    }

    private void LDV(Integer endereco){
        topo++;
        memoria.set(topo,memoria.get(endereco));
        ponteiro++;
    }

    private void STR(Integer endereco){
        memoria.set(endereco,memoria.get(topo));
        topo--;
        ponteiro++;
    }

    /*
    private LDX(){

    }

    private STX(){

    }
    */

    private void ADD(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if(verificaTipoNumerico(topoPilha) && verificaTipoNumerico(subtopo)) {
            if(verificaTipoFloat(topoPilha) || verificaTipoFloat(subtopo)){
                memoria.set(topo - 1, ((Double) subtopo) + ((Double) topoPilha));
            }else {
                memoria.set(topo - 1, ((Integer) subtopo) + ((Integer) topoPilha));
            }
            topo--;
            ponteiro++;
        }
    }

    private void SUB(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if(verificaTipoNumerico(topoPilha) && verificaTipoNumerico(subtopo)) {
            if(verificaTipoFloat(topoPilha) || verificaTipoFloat(subtopo)){
                memoria.set(topo - 1, ((Double) subtopo) - ((Double) topoPilha));
            }else {
                memoria.set(topo - 1, ((Integer) subtopo) - ((Integer) topoPilha));
            }
            topo--;
            ponteiro++;
        }
    }

    private void MUL(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if(verificaTipoNumerico(topoPilha) && verificaTipoNumerico(subtopo)) {
            if(verificaTipoFloat(topoPilha) || verificaTipoFloat(subtopo)){
                memoria.set(topo - 1, ((Double) subtopo) * ((Double) topoPilha));
            }else {
                memoria.set(topo - 1, ((Integer) subtopo) * ((Integer) topoPilha));
            }
            topo--;
            ponteiro++;
        }
    }

    private void DIV(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if(topoPilha.equals(0)){
            throw new IllegalArgumentException("RUNTIME error: divisao por 0");
        }
        if(verificaTipoNumerico(topoPilha) && verificaTipoNumerico(subtopo)) {
            if(verificaTipoFloat(topoPilha) || verificaTipoFloat(subtopo)){
                memoria.set(topo - 1, ((Double) subtopo) / ((Double) topoPilha));
            }else {
                memoria.set(topo - 1, ((Integer) subtopo) / ((Integer) topoPilha));
            }
            topo--;
            ponteiro++;
        }
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

    private void POW(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if(verificaTipoNumerico(topoPilha) && verificaTipoNumerico(subtopo)) {
            memoria.set(topo - 1, Math.pow((Double) subtopo, ((Double) topoPilha)));
            topo--;
            ponteiro++;
        }
    }

    private void AND(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if(verificaTipoBoolean(topoPilha) && verificaTipoBoolean(subtopo)){
            memoria.set(topo - 1, (Boolean) subtopo && (Boolean) topoPilha);
            topo--;
            ponteiro++;
        }
    }

    private void OR(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if(verificaTipoBoolean(topoPilha) && verificaTipoBoolean(subtopo)){
            memoria.set(topo - 1, (Boolean) subtopo || (Boolean) topoPilha);
            topo--;
            ponteiro++;
        }
    }

    private void NOT(){
        Object topoPilha = memoria.get(topo);
        if(verificaTipoBoolean(topoPilha)){
            memoria.set(topo, !((Boolean) topoPilha));
            ponteiro++;
        }
    }

    private void SMR(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if (verificaTipoNumerico(topoPilha) && verificaTipoNumerico(subtopo)){
            if ((Double) subtopo < (Double) topoPilha){
                memoria.set(topo - 1, true);
            }else{
                memoria.set(topo - 1, false);
            }
            topo--;
            ponteiro++;
        }
    }

    private void BGR(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if (verificaTipoNumerico(topoPilha) && verificaTipoNumerico(subtopo)){
            if ((Double) subtopo > (Double) topoPilha){
                memoria.set(topo - 1, true);
            }else{
                memoria.set(topo - 1, false);
            }
            topo--;
            ponteiro++;
        }
    }

    private void SME(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if (verificaTipoNumerico(topoPilha) && verificaTipoNumerico(subtopo)){
            if ((Double) subtopo <= (Double) topoPilha){
                memoria.set(topo - 1, true);
            }else{
                memoria.set(topo - 1, false);
            }
            topo--;
            ponteiro++;
        }
    }

    private void BGE(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if (verificaTipoNumerico(topoPilha) && verificaTipoNumerico(subtopo)){
            if ((Double) subtopo >= (Double) topoPilha){
                memoria.set(topo - 1, true);
            }else{
                memoria.set(topo - 1, false);
            }
            topo--;
            ponteiro++;
        }
    }

    private void EQL(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if(topoPilha.equals(subtopo)){
            memoria.set(topo - 1, true);
        }else{
            memoria.set(topo - 1, false);
        }
        topo--;
        ponteiro++;
    }

    private void DIF(){
        Object topoPilha = memoria.get(topo);
        Object subtopo = memoria.get(topo -1);
        if(topoPilha.equals(subtopo)){
            memoria.set(topo - 1, false);
        }else{
            memoria.set(topo - 1, true);
        }
        topo--;
        ponteiro++;
    }

    private void JMF(Integer endereco){
        if((Boolean) memoria.get(topo)){
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

        resultArea.append(">> ");
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
        resultArea.append((String) topoPilha); // talvez adicionar \n
        topo--;
        ponteiro++;
    }
}
