package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Gerador de Código Intermediário conforme especificação GLC 2025.2
 * Gera instruções no formato: (ponteiro, instrução, parâmetro)
 */
public class GeradorCodigo {
    
    public static class Instrucao {
        public int numero;
        public String operacao;
        public int parametro;
        
        public Instrucao(int numero, String operacao, int parametro) {
            this.numero = numero;
            this.operacao = operacao;
            this.parametro = parametro;
        }
        
        @Override
        public String toString() {
            return String.format("(%d, %s, %d)", numero, operacao, parametro);
        }
    }
    
    private List<Instrucao> codigo;
    private int ponteiro;
    private Stack<Integer> pilhaDeDesvios;
    
    public GeradorCodigo() {
        this.codigo = new ArrayList<>();
        this.ponteiro = 1; // inicia em 1 conforme especificação
        this.pilhaDeDesvios = new Stack<>();
    }
    
    /**
     * Gera uma instrução: gerar instrução(ponteiro, instrução, parâmetro); ++ponteiro
     */
    public void gerarInstrucao(String operacao, int parametro) {
        codigo.add(new Instrucao(ponteiro, operacao, parametro));
        ponteiro++;
    }
    
    /**
     * Gera instrução e retorna o endereço (para desvios)
     */
    public int gerarInstrucaoComEndereco(String operacao, int parametro) {
        int endereco = ponteiro;
        gerarInstrucao(operacao, parametro);
        return endereco;
    }
    
    /**
     * Empilha endereço para resolução posterior (desvios)
     */
    public void empilharDesvio(int endereco) {
        pilhaDeDesvios.push(endereco);
    }
    
    /**
     * Desempilha e ajusta endereço de desvio
     */
    public void ajustarDesvio() {
        if (!pilhaDeDesvios.isEmpty()) {
            int endereco = pilhaDeDesvios.pop();
            codigo.get(endereco - 1).parametro = ponteiro; // ajusta para ponteiro atual
        }
    }
    
    /**
     * Ajusta endereço específico
     */
    public void ajustarEndereco(int endereco, int novoParametro) {
        if (endereco > 0 && endereco <= codigo.size()) {
            codigo.get(endereco - 1).parametro = novoParametro;
        }
    }
    
    /**
     * Retorna o ponteiro atual
     */
    public int getPonteiro() {
        return ponteiro;
    }
    
    /**
     * Retorna o código gerado
     */
    public List<Instrucao> getCodigo() {
        return new ArrayList<>(codigo);
    }
    
    /**
     * Limpa o código gerado
     */
    public void limpar() {
        codigo.clear();
        ponteiro = 1;
        pilhaDeDesvios.clear();
    }
    
    /**
     * Retorna representação string do código
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CÓDIGO INTERMEDIÁRIO ===\n");
        for (Instrucao inst : codigo) {
            sb.append(inst.toString()).append("\n");
        }
        return sb.toString();
    }
}
