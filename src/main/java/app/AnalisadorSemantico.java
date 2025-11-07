package app;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

import org.javatuples.Quartet;
import org.javatuples.Triplet;

public class AnalisadorSemantico {

    private ArrayList<Triplet<Integer,String,Object>> listaDeInstrucoes;
    private Map<String, Quartet<String, Integer, Integer, Object>> tabelaDeSimbolos = new HashMap<>();
    private int VT;
    private int VP;
    private ArrayList<String> listaDeIdentificadoresDaLinha;
    private ArrayList<Integer> listaBasesDaLinha;
    private int categoriaAtual;
    private Integer ponteiro;
    private Stack<Integer> pilhaDeDesvios;
    private boolean temIndice;
    private Integer baseDoUltimoVetor;
    private Integer tamanhoDoUltimoVetor;
    private boolean houveInitLinha;
    private Integer primeiroBaseInit;

    private int categoriaIdentificadorAtual;
    private int baseIdentificadorAtual;
    private Object tamanhoIdentificadorAtual;

    private String contexto;

    public AnalisadorSemantico() {
        this.listaDeInstrucoes = new ArrayList<>();
        this.tabelaDeSimbolos = new HashMap<>();
        this.VT = 0;
        this.VP = 0;
        this.listaDeIdentificadoresDaLinha = new ArrayList<>();
        this.listaBasesDaLinha = new ArrayList<>();
        this.ponteiro = 1;
        this.pilhaDeDesvios = new Stack<>();
        this.temIndice = false;
        this.houveInitLinha = false;
    }

    public ArrayList<Triplet<Integer,String,Object>> getListaDeInstrucoes(){
        return this.listaDeInstrucoes;
    }

    private void recuperarInfosDaTS(String identificador) {
        Quartet<String, Integer, Integer, Object> simbolo = tabelaDeSimbolos.get(identificador);
        if (simbolo == null) {
            throw new IllegalArgumentException("Identificador '" + identificador + "' não declarado");
        }
        this.categoriaIdentificadorAtual = simbolo.getValue1();
        this.baseIdentificadorAtual = simbolo.getValue2();
        this.tamanhoIdentificadorAtual = simbolo.getValue3();
    }

    public boolean verificaExpressaoEhNumerica(String expressao){
        return true;
    }

    // AÇÕES SEMANTICAS DA MINHA CABEÇA QUE ACHO QUE PRECISA

    public void ContextoDeclaracaoVetor(Integer tamanhoVetor){ // Isso vai logo antes de inicializar o vetor (<inic vetor>)
        if(tamanhoVetor == 1){
            this.contexto = "Declaração vetor com unico valor";
        }else {
            this.contexto = "lista completa de vetor";
        }
    }

    // FIM DAS AÇÕES SEMANTICAS DA MINHA CABEÇA

    public void P1(String nomeDoPrograma){
        tabelaDeSimbolos.put(nomeDoPrograma, new Quartet<>(nomeDoPrograma, 0, 0, "-"));
    }

    public void P2(){
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro,"STP",0));
        this.ponteiro++;
    }

    public void D0(){
        this.listaDeIdentificadoresDaLinha.clear();
        this.listaBasesDaLinha.clear();
        this.VP = 0;
        this.houveInitLinha = false;
        this.primeiroBaseInit = -1;
    }

    public void D1(String identificador){
        if (tabelaDeSimbolos.containsKey(identificador)) {
            throw new IllegalArgumentException("Identificador '" + identificador + "' já declarado");
        }
        listaDeIdentificadoresDaLinha.add(identificador);
    }

    public void T(Integer categoria){
        this.categoriaAtual = categoria;
    }

    public void V1(Integer valor){
        if(valor <= 0){
            throw new IllegalArgumentException("valor passado 0 ou menor");
        }else {
            this.tamanhoDoUltimoVetor = valor;
        }
    }

    public void V2() {
        int base;
        for (String identificador : listaDeIdentificadoresDaLinha) {
            base = VT + 1;
            tabelaDeSimbolos.put(identificador, new Quartet<>(identificador, categoriaAtual, base, tamanhoDoUltimoVetor));
            VT += tamanhoDoUltimoVetor;
            VP += tamanhoDoUltimoVetor;
            baseDoUltimoVetor = base;
            listaBasesDaLinha.add(base);
        }
    }

    public void E2() {
        int base;
        for (String identificador : listaDeIdentificadoresDaLinha) {
            base = VT + 1;
            tabelaDeSimbolos.put(identificador, new Quartet<>(identificador, categoriaAtual, base, "-"));
            VT += 1;
            VP += 1;
            listaBasesDaLinha.add(base);
        }
    }

    public void D6(){
        switch (this.categoriaAtual){
            case 1:
                this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro,"ALI",this.VP));
                break;
            case 2:
                this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro,"ALR",this.VP));
                break;
            case 3:
                this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro,"ALS",this.VP));
                break;
            case 4:
                this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro,"ALB",this.VP));
                break;
        }
        ++this.ponteiro;
        if(this.houveInitLinha){
            for(int k = 2; k<this.listaDeIdentificadoresDaLinha.size();k++){
                this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro,"LDV",this.primeiroBaseInit));
                ++this.ponteiro;
                this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro,"STR",this.listaBasesDaLinha.get(k)));
                ++this.ponteiro;
            }
        }else{
            this.primeiroBaseInit = -1;
            this.listaDeIdentificadoresDaLinha.clear();
            this.listaBasesDaLinha.clear();
        }

    }

    public void IV(ArrayList<Object> listaDeValores){ // Esse aqui vai ser esquisito de integrar, la no analisador sintatico tem q salvar todos os valores do vetor pra passar aqui
        if()
    }

    public void IE(){
        return; // Diz pra gerar a <expressão>, eu n tenho mta ideia de como fazer isso
    }

    public void VAL(){

    }

    public void C1(Integer valor){
        this.listaDeInstrucoes.add(new Triplet<>(ponteiro,"LDI",valor));
        ++this.ponteiro;
    }

    public void C2(Float valor){
        this.listaDeInstrucoes.add(new Triplet<>(ponteiro,"LDR",valor));
        ++this.ponteiro;
    }

    public void C3(String valor){
        this.listaDeInstrucoes.add(new Triplet<>(ponteiro,"LDS",valor));
        ++this.ponteiro;
    }

    public void C4(){
        this.listaDeInstrucoes.add(new Triplet<>(ponteiro,"LDI",1));
        ++this.ponteiro;
    }

    public void C5(){
        this.listaDeInstrucoes.add(new Triplet<>(ponteiro,"LDI",0));
        ++this.ponteiro;
    }

    public void A1(String identificador) {
        recuperarInfosDaTS(identificador);
        temIndice = false;
    }

    public void I1(String expressao){
        if(verificaExpressaoEhNumerica(expressao)){
            this.temIndice = true;
        }else{
            throw new IllegalArgumentException("Expressão não numerica");
        }
    }

    public void A2(){
        if((Objects.equals(this.contexto, "Declaração vetor com unico valor") || Objects.equals(this.contexto, "lista completa de vetor")) && !this.temIndice){
            throw new IllegalArgumentException("Vetor sem indice");
        }else if(!(Objects.equals(this.contexto, "Declaração vetor com unico valor") || Objects.equals(this.contexto, "lista completa de vetor")) && this.temIndice){
            throw new IllegalArgumentException("Variavel escalar com indice");
        }
    }

    public void A3(){
        if((Objects.equals(this.contexto, "Declaração vetor com unico valor") || Objects.equals(this.contexto, "lista completa de vetor"))){
           // AQUI NAO ENTENDI AO CERTO OQ FAZER
        }else{
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro,"STR",this.VP)); // AQUI COLOQUEI VP AO INVES DE BASE PQ SEI LA OQ ELE QUIS DIZER COM BASE
        }
    }

    public void R1(String identificador) {
        recuperarInfosDaTS(identificador); // Armazena nas vars temporárias!
        temIndice = false;
    }

}
