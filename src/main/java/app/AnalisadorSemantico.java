package app;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

import org.javatuples.Quartet;
import org.javatuples.Triplet;

public class AnalisadorSemantico {

    private ArrayList<Triplet<Integer, String, Object>> listaDeInstrucoes;
    private ArrayList<Triplet<Integer, String, Object>> listaDeInstrucoesTemp;
    private Map<String, Quartet<String, Integer, Integer, Object>> tabelaDeSimbolos = new HashMap<>();
    private int proxPosicaoPilhaLogica;
    private int VP;
    private ArrayList<String> listaDeIdentificadoresDaLinha;
    private ArrayList<Integer> listaBasesDaLinha;
    private int categoriaAtual;
    private Integer ponteiro;
    private Stack<Integer> pilhaDeDesvios;
    private Stack<Boolean> temIndice;
    private Integer baseDoUltimoVetor;
    private Integer tamanhoDoUltimoVetor;
    private boolean houveInitLinha;
    private Integer primeiroBaseInit;

    private Integer inicioLoop;

    private Stack<Integer> categoriaIdentificadorAtual;
    private Stack<Integer> baseIdentificadorAtual;
    private Stack<Object> tamanhoIdentificadorAtual;

    private String contexto; // Usado para distinguir contextos como "lista completa de vetor", "atribuição", etc.

    // Adicionado para rastrear índice corrente em listas de valores de vetor
    private int indiceCorrente;


    public AnalisadorSemantico() {
        this.listaDeInstrucoes = new ArrayList<>();
        this.listaDeInstrucoesTemp = new ArrayList<>();
        this.tabelaDeSimbolos = new HashMap<>();
        this.proxPosicaoPilhaLogica = 0;
        this.VP = 0;
        this.listaDeIdentificadoresDaLinha = new ArrayList<>();
        this.listaBasesDaLinha = new ArrayList<>();
        this.ponteiro = 1;
        this.pilhaDeDesvios = new Stack<>();
        this.temIndice = new Stack<>();
        this.categoriaIdentificadorAtual = new Stack<>();
        this.baseIdentificadorAtual = new Stack<>();
        this.tamanhoIdentificadorAtual = new Stack<>();
        this.houveInitLinha = false;
        this.primeiroBaseInit = -1;
        this.contexto = ""; // Inicializa vazio
        this.indiceCorrente = 0;
        this.baseDoUltimoVetor = 0; // teste
    }

    public ArrayList<Triplet<Integer, String, Object>> getListaDeInstrucoes() {
        return this.listaDeInstrucoes;
    }

    private void recuperarInfosDaTS(String identificador) {
        Quartet<String, Integer, Integer, Object> simbolo = tabelaDeSimbolos.get(identificador);
        if (simbolo == null) {
            throw new IllegalArgumentException("Identificador '" + identificador + "' não declarado");
        }
        this.categoriaIdentificadorAtual.add(simbolo.getValue1());
        this.baseIdentificadorAtual.add(simbolo.getValue2());
        this.tamanhoIdentificadorAtual.add(simbolo.getValue3());
    }

    // Auxiliar para mensagens de erro
    private String tipoToString(int tipo) {
        switch (tipo) {
            case 1: return "num";
            case 2: return "real";
            case 3: return "text";
            case 4: return "flag";
            default: return "desconhecido";
        }
    }

    public void desempilhaInfoDaTS(){
        this.categoriaIdentificadorAtual.pop();
        this.baseIdentificadorAtual.pop();
        this.tamanhoIdentificadorAtual.pop();
    }

    public void P1(String nomeDoPrograma) {
        tabelaDeSimbolos.put(nomeDoPrograma, new Quartet<>(nomeDoPrograma, 0, 0, "-"));
    }

    public void P2() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "STP", 0));
        this.ponteiro++;
    }

    public void D0() {
        this.contexto = "Definicao";
        this.listaDeIdentificadoresDaLinha.clear();
        this.listaBasesDaLinha.clear();
        this.VP = 0;
        this.houveInitLinha = false;
        this.primeiroBaseInit = -1;
    }

    public void D1(String identificador) {
        if (tabelaDeSimbolos.containsKey(identificador) || listaDeIdentificadoresDaLinha.contains(identificador)) {
            throw new IllegalArgumentException("Identificador '" + identificador + "' já declarado");
        }
        listaDeIdentificadoresDaLinha.add(identificador);
    }

    public void T(Integer categoria) {
        this.categoriaAtual = categoria;
    }

    public void V1(Integer valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor passado 0 ou menor");
        } else {
            this.tamanhoDoUltimoVetor = valor;
        }
    }

    public void V2() {
        for (String identificador : listaDeIdentificadoresDaLinha) {
            tabelaDeSimbolos.put(identificador, new Quartet<>(identificador, categoriaAtual, proxPosicaoPilhaLogica, tamanhoDoUltimoVetor));
            proxPosicaoPilhaLogica+=tamanhoDoUltimoVetor;
        }
        VP = tamanhoDoUltimoVetor;
    }

    public void E2() {
        for (String identificador : listaDeIdentificadoresDaLinha) {
            tabelaDeSimbolos.put(identificador, new Quartet<>(identificador, categoriaAtual, proxPosicaoPilhaLogica, "-"));
            proxPosicaoPilhaLogica += 1;
        }
        VP = 1;
    }

    public void D6() {
        int inicio = proxPosicaoPilhaLogica - listaDeIdentificadoresDaLinha.size() * VP;
        int j = 0;
        for (int i = 0; i < listaDeIdentificadoresDaLinha.size(); i++) {
            switch (this.categoriaAtual) {
                case 1:
                    this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "ALI", VP)); // Inteiro
                    break;
                case 2:
                    this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "ALR", VP)); // Real
                    break;
                case 3:
                    this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "ALS", VP)); // Texto
                    break;
                case 4:
                    this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "ALB", VP)); // Logico
                    break;
            }

            ++ponteiro;
            if(houveInitLinha) {
                for (Triplet<Integer, String, Object> instrucao : listaDeInstrucoesTemp) {
                    listaDeInstrucoes.add(instrucao.setAt0(ponteiro));
                    ++ponteiro;
                    listaDeInstrucoes.add(new Triplet<>(ponteiro, "STR", inicio + j));
                    ++ponteiro;
                    ++j;
                }
            }
        }
        listaDeInstrucoesTemp.clear();
        listaDeIdentificadoresDaLinha.clear();
        VP = 0;
        houveInitLinha = false;
    }

    public void setContexto(String novoContexto) {
        this.contexto = novoContexto;
    }

    public void IV(ArrayList<Object> listaDeValores) {
        if (listaDeValores.size() == 1){
            for(int i = 1; i < listaDeIdentificadoresDaLinha.size()-1; i++) {
                listaDeInstrucoesTemp.add(listaDeInstrucoesTemp.getFirst());
            }
        }
        else if (listaDeValores.size() != tamanhoDoUltimoVetor) {
            throw new IllegalArgumentException("Número de valores não corresponde ao tamanho do vetor");
        }
        houveInitLinha = true;
    }

    public void IE() {this.houveInitLinha = true;}

    public void C1(Integer valor) {
        if(contexto.equals("Definicao")) {
            if (categoriaAtual != 1) {
                throw new IllegalArgumentException("Valor passado não corresponde ao tipo especificado");
            }
            listaDeInstrucoesTemp.add(new Triplet<>(0, "LDI", valor));
        } else {
            listaDeInstrucoes.add(new Triplet<>(ponteiro, "LDI", valor));
            ponteiro++;
        }
    }

    public void C2(Float valor) {
        if(contexto.equals("Definicao")) {
            if (categoriaAtual != 2) {
                throw new IllegalArgumentException("Valor passado não corresponde ao tipo especificado");
            }
            listaDeInstrucoesTemp.add(new Triplet<>(0, "LDR", valor));
        } else {
            listaDeInstrucoes.add(new Triplet<>(ponteiro, "LDR", valor));
            ponteiro++;
        }
    }

    public void C3(String valor) {
        if(contexto.equals("Definicao")) {
            if (categoriaAtual != 3) {
                throw new IllegalArgumentException("Valor passado não corresponde ao tipo especificado");
            }
            listaDeInstrucoesTemp.add(new Triplet<>(0, "LDS", valor));
        } else {
            listaDeInstrucoes.add(new Triplet<>(ponteiro, "LDS", valor));
            ponteiro++;
        }
    }

    public void C4() {
        if(contexto.equals("Definicao")) {
            if (categoriaAtual != 4) {
                throw new IllegalArgumentException("Valor passado não corresponde ao tipo especificado");
            }
            listaDeInstrucoesTemp.add(new Triplet<>(0, "LDB", 1));
        } else {
            listaDeInstrucoes.add(new Triplet<>(ponteiro, "LDB", 1));
            ponteiro++;
        }
    }

    public void C5() {
        if(contexto.equals("Definicao")) {
            if (categoriaAtual != 4) {
                throw new IllegalArgumentException("Valor passado não corresponde ao tipo especificado");
            }
            listaDeInstrucoesTemp.add(new Triplet<>(0, "LDB", 0));
        } else {
            listaDeInstrucoes.add(new Triplet<>(ponteiro, "LDB", 0));
            ponteiro++;
        }
    }

    public void A1(String identificador) {
        recuperarInfosDaTS(identificador);
        temIndice.push(false);
    }

    public void I1() {
        this.temIndice.pop();
        this.temIndice.push(true);
    }

    public void A2() {
        boolean isVetor = !(tamanhoIdentificadorAtual.peek() instanceof String && tamanhoIdentificadorAtual.peek().equals("-"));
        if (!isVetor && this.temIndice.peek()) {
            this.temIndice.pop();
            throw new IllegalArgumentException("Variável escalar com índice");
        } else if (isVetor && !this.temIndice.peek()) {
            this.temIndice.pop();
            throw new IllegalArgumentException("Vetor sem índice");
        }
        this.temIndice.pop();
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDI", this.baseIdentificadorAtual.peek() - 1));
        ++this.ponteiro;
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "ADD", 0));
        ++this.ponteiro;
    }

    public void A2B() {
        boolean isVetor = !(tamanhoIdentificadorAtual.peek() instanceof String && tamanhoIdentificadorAtual.peek().equals("-"));
        if (!isVetor && this.temIndice.peek()) {
            this.temIndice.pop();
            throw new IllegalArgumentException("Variável escalar com índice");
        } else if (isVetor && !this.temIndice.peek()) {
            this.temIndice.pop();
            throw new IllegalArgumentException("Vetor sem índice");
        }
    }

    public void A3() {
        if (tamanhoIdentificadorAtual.peek() instanceof String && tamanhoIdentificadorAtual.peek().equals("-")) {
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "STR", this.baseIdentificadorAtual.peek()));
            ++this.ponteiro;
        } else {
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "STX", 0));
            ++this.ponteiro;
        }
        desempilhaInfoDaTS();
    }

    public void R1(String identificador) {
        recuperarInfosDaTS(identificador);
        temIndice.push(false);
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "REA", this.categoriaIdentificadorAtual.peek())); // teste READ aqui
        ++this.ponteiro;
    }

    public void R2() {
        A2B();
        if (!this.temIndice.peek()) {
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "REA", this.categoriaIdentificadorAtual.peek()));
            ++this.ponteiro;
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "STR", this.baseIdentificadorAtual.peek()));
            ++this.ponteiro;
        } else {
            // rip READ aqui
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDI", this.baseIdentificadorAtual.peek() - 1));
            ++this.ponteiro;
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "ADD", 0));
            ++this.ponteiro;
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "STX", 0));
            ++this.ponteiro;
        }
        this.temIndice.pop();
        desempilhaInfoDaTS();
    }

    public void S2(String identificador) {
        recuperarInfosDaTS(identificador);
        temIndice.push(false);
    }

    public void S3() {
        A2B();
        if (!this.temIndice.peek()) {
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDV", this.baseIdentificadorAtual.peek()));
            ++this.ponteiro;
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "WRT", 0));
            ++this.ponteiro;
        } else {
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDI", this.baseIdentificadorAtual.peek() - 1));
            ++this.ponteiro;
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "ADD", 0));
            ++this.ponteiro;
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDX", 0));
            ++this.ponteiro;
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "WRT", 0));
            ++this.ponteiro;
        }
        this.temIndice.pop();
        desempilhaInfoDaTS();
    }

    public void K1(Integer valor) {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDI", valor));
        ++this.ponteiro;
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "WRT", 0));
        ++this.ponteiro;
    }

    public void K2(Float valor) {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDR", valor));
        ++this.ponteiro;
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "WRT", 0));
        ++this.ponteiro;
    }

    public void K3(String valor) {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDS", valor));
        ++this.ponteiro;
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "WRT", 0));
        ++this.ponteiro;
    }

    public void F1() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "JMF", 0));
        this.pilhaDeDesvios.push(this.ponteiro);
        ++this.ponteiro;
    }

    public void F2() {
        int endJMF = this.pilhaDeDesvios.pop();
        this.listaDeInstrucoes.set(endJMF - 1, new Triplet<>(endJMF, "JMF", this.ponteiro + 1));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "JMP", 0));
        this.pilhaDeDesvios.push(this.ponteiro);
        ++this.ponteiro;
    }

    public void F3(boolean houveElse) {
        int endPendente = this.pilhaDeDesvios.pop();
        this.listaDeInstrucoes.set(endPendente - 1, new Triplet<>(endPendente, houveElse ? "JMP" : "JMF", this.ponteiro));
    }

    public void L0(){
        this.inicioLoop = this.ponteiro;
    }

    public void L1() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "JMF", 0));
        this.pilhaDeDesvios.push(this.ponteiro);
        ++this.ponteiro;
    }

    public void L2() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "JMP", this.inicioLoop));
        ++this.ponteiro;
        int endJMF = this.pilhaDeDesvios.pop();
        this.listaDeInstrucoes.set(endJMF - 1, new Triplet<>(endJMF, "JMF", this.ponteiro));
    }

    public void Requals() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "EQL", 0));
        ++this.ponteiro;
    }

    public void RnotEqual() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "DIF", 0));
        ++this.ponteiro;
    }

    public void Rsmaller() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "SMR", 0));
        ++this.ponteiro;
    }

    public void Rbigger() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "BGR", 0));
        ++this.ponteiro;
    }

    public void RsmallerEqual() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "SME", 0));
        ++this.ponteiro;
    }

    public void RbiggerEqual() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "BGE", 0));
        ++this.ponteiro;
    }

    public void ADD() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "ADD", 0));
        ++this.ponteiro;
    }

    public void SUB() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "SUB", 0));
        ++this.ponteiro;
    }

    public void OR() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "OR", 0));
        ++this.ponteiro;
    }

    public void MUL() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "MUL", 0));
        ++this.ponteiro;
    }

    public void DIV() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "DIV", 0));
        ++this.ponteiro;
    }

    public void MOD() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "MOD", 0));
        ++this.ponteiro;
    }

    public void REM() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "REM", 0));
        ++this.ponteiro;
    }

    public void AND() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "AND", 0));
        ++this.ponteiro;
    }

    public void POW() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "POW", 0));
        ++this.ponteiro;
    }

    public void E1(String identificador) {
        recuperarInfosDaTS(identificador);
        temIndice.push(false);
    }

    public void E3() {
        A2B();
        if (!this.temIndice.peek()) {
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDV", this.baseIdentificadorAtual.peek()));
            ++this.ponteiro;
        } else {
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDI", this.baseIdentificadorAtual.peek() - 1));
            ++this.ponteiro;
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "ADD", 0));
            ++this.ponteiro;
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDX", 0));
            ++this.ponteiro;
        }
        this.temIndice.pop();
        desempilhaInfoDaTS();
    }

    public void NOT() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "NOT", 0));
        ++this.ponteiro;
    }
}