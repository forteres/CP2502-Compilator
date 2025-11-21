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

    // Adicionado para inferência de tipos em expressões
    private Stack<Integer> pilhaTipos = new Stack<>(); // Tipos: 1=num, 2=real, 3=text, 4=flag

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

    // Chamar isso APÓS o parse completo da <expressão> (ex: no final de I1, após reduzir a expressão do índice)
    public boolean verificaExpressaoEhNumerica() {
        if (pilhaTipos.isEmpty()) {
            throw new IllegalArgumentException("Expressão inválida: sem tipo inferido");
        }
        int tipo = pilhaTipos.pop();
        if (tipo != 1 && tipo != 2) {
            throw new IllegalArgumentException("Expressão não numérica: tipo inferido é " + tipoToString(tipo));
        }
        return true;
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

    // Auxiliar para inferir tipo resultante de operações aritméticas (promove para real se necessário)
    private int inferirTipoArit(int t1, int t2) {
        if (t1 == 3 || t2 == 3 || t1 == 4 || t2 == 4) {
            throw new IllegalArgumentException("Tipos incompatíveis para operação aritmética: " + tipoToString(t1) + " e " + tipoToString(t2));
        }
        if (t1 == 2 || t2 == 2) return 2; // Promote to real
        return 1; // num
    }

    // Auxiliar para lógicos (deve ser flag)
    private int inferirTipoLogico(int t1, int t2) {
        if (t1 != 4 || t2 != 4) {
            throw new IllegalArgumentException("Tipos incompatíveis para operação lógica: " + tipoToString(t1) + " e " + tipoToString(t2));
        }
        return 4;
    }

    // Auxiliar para relacionais (tipos devem matching, resultado flag)
    private int inferirTipoRelacional(int t1, int t2) {
        if (t1 != t2) {
            throw new IllegalArgumentException("Tipos incompatíveis para operação relacional: " + tipoToString(t1) + " e " + tipoToString(t2));
        }
        return 4;
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
            pilhaTipos.push(1); // num
        }
    }

    public void C2(Float valor) {
        if(contexto.equals("Definicao")) {
            if (categoriaAtual != 2) {
                throw new IllegalArgumentException("Valor passado não corresponde ao tipo especificado");
            }
            listaDeInstrucoesTemp.add(new Triplet<>(0, "LDR", valor));
        } else {
            pilhaTipos.push(2); // real
        }
    }

    public void C3(String valor) {
        if(contexto.equals("Definicao")) {
            if (categoriaAtual != 3) {
                throw new IllegalArgumentException("Valor passado não corresponde ao tipo especificado");
            }
            listaDeInstrucoesTemp.add(new Triplet<>(0, "LDS", valor));
        } else {
            pilhaTipos.push(3); // text
        }
    }

    public void C4() {
        if(contexto.equals("Definicao")) {
            if (categoriaAtual != 4) {
                throw new IllegalArgumentException("Valor passado não corresponde ao tipo especificado");
            }
            listaDeInstrucoesTemp.add(new Triplet<>(0, "LDB", 1));
        } else {
            pilhaTipos.push(4); // flag
        }
    }

    public void C5() {
        if(contexto.equals("Definicao")) {
            if (categoriaAtual != 4) {
                throw new IllegalArgumentException("Valor passado não corresponde ao tipo especificado");
            }
            listaDeInstrucoesTemp.add(new Triplet<>(0, "LDB", 0));
        } else {
            pilhaTipos.push(4); // flag
        }
    }

    public void A1(String identificador) {
        recuperarInfosDaTS(identificador);
        temIndice.push(false);
    }

    public void I1() {
        // Após parse da <expressão> do índice, verifique tipo (sem param String, usa pilha)
        verificaExpressaoEhNumerica(); // Lança erro se não numérico
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
        // Assuma RHS <expressão> gerou valor e tipo no topo
        int tipoRHS = pilhaTipos.pop(); // Tipo do RHS
        if (tipoRHS != categoriaIdentificadorAtual.peek()) {
            desempilhaInfoDaTS();
            throw new IllegalArgumentException("Atribuição com tipos incompatíveis: LHS " + tipoToString(categoriaIdentificadorAtual.peek()) + ", RHS " + tipoToString(tipoRHS));
        }
        if (tamanhoIdentificadorAtual.peek() instanceof String && tamanhoIdentificadorAtual.peek().equals("-")) {
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "STR", this.baseIdentificadorAtual));
            ++this.ponteiro;
        } else {
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDI", this.baseIdentificadorAtual.peek() - 1));
            ++this.ponteiro;
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "ADD", 0));
            ++this.ponteiro;
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "STX", 0));
            ++this.ponteiro;
        }
        desempilhaInfoDaTS();
    }

    public void R1(String identificador) {
        recuperarInfosDaTS(identificador);
        temIndice.push(false);
    }

    public void R2() {
        A2B();
        if (!this.temIndice.peek()) {
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "REA", this.categoriaIdentificadorAtual.peek()));
            ++this.ponteiro;
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "STR", this.baseIdentificadorAtual.peek()));
            ++this.ponteiro;
        } else {
            this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "REA", this.categoriaIdentificadorAtual.peek()));
            ++this.ponteiro;
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
        pilhaTipos.push(1);
    }

    public void K2(Float valor) {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDR", valor));
        ++this.ponteiro;
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "WRT", 0));
        ++this.ponteiro;
        pilhaTipos.push(2);
    }

    public void K3(String valor) {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "LDS", valor));
        ++this.ponteiro;
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "WRT", 0));
        ++this.ponteiro;
        pilhaTipos.push(3);
    }

    public void F1() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "JMF", 0));
        this.pilhaDeDesvios.push(this.ponteiro);
        ++this.ponteiro;
        // Verifique tipo da condição (deve ser flag)
        int tipoCond = pilhaTipos.pop();
        if (tipoCond != 4) {
            throw new IllegalArgumentException("Condição de if deve ser flag, mas é " + tipoToString(tipoCond));
        }
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

    public void L1() {
        this.inicioLoop = this.ponteiro;
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "JMF", 0));
        this.pilhaDeDesvios.push(this.ponteiro);
        ++this.ponteiro;
        // Verifique tipo da condição (flag)
        int tipoCond = pilhaTipos.pop();
        if (tipoCond != 4) {
            throw new IllegalArgumentException("Condição de loop deve ser flag, mas é " + tipoToString(tipoCond));
        }
    }

    public void L2() {
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "JMP", this.inicioLoop));
        ++this.ponteiro;
        int endJMF = this.pilhaDeDesvios.pop();
        this.listaDeInstrucoes.set(endJMF - 1, new Triplet<>(endJMF, "JMF", this.ponteiro));
    }

    public void Requals() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoRelacional(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "EQL", 0));
        ++this.ponteiro;
    }

    public void RnotEqual() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoRelacional(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "DIF", 0));
        ++this.ponteiro;
    }

    public void Rsmaller() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoRelacional(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "SMR", 0));
        ++this.ponteiro;
    }

    public void Rbigger() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoRelacional(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "BGR", 0));
        ++this.ponteiro;
    }

    public void RsmallerEqual() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoRelacional(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "SME", 0));
        ++this.ponteiro;
    }

    public void RbiggerEqual() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoRelacional(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "BGE", 0));
        ++this.ponteiro;
    }

    public void ADD() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoArit(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "ADD", 0));
        ++this.ponteiro;
    }

    public void SUB() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoArit(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "SUB", 0));
        ++this.ponteiro;
    }

    public void OR() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoLogico(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "OR", 0));
        ++this.ponteiro;
    }

    public void MUL() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoArit(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "MUL", 0));
        ++this.ponteiro;
    }

    public void DIV() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoArit(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "DIV", 0));
        ++this.ponteiro;
    }

    public void MOD() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoArit(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "MOD", 0));
        ++this.ponteiro;
    }

    public void REM() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoArit(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "REM", 0));
        ++this.ponteiro;
    }

    public void AND() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoLogico(t1, t2));
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "AND", 0));
        ++this.ponteiro;
    }

    public void POW() {
        int t2 = pilhaTipos.pop();
        int t1 = pilhaTipos.pop();
        pilhaTipos.push(inferirTipoArit(t1, t2));
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
        pilhaTipos.push(categoriaIdentificadorAtual.peek()); // Push tipo do identificador (escalar ou elemento de vetor)
    }

    public void NOT() {
        int t = pilhaTipos.pop();
        if (t != 4) {
            throw new IllegalArgumentException("NOT requer flag, mas é " + tipoToString(t));
        }
        pilhaTipos.push(4);
        this.listaDeInstrucoes.add(new Triplet<>(this.ponteiro, "NOT", 0));
        ++this.ponteiro;
    }
}