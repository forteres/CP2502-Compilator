package app;

import java.util.HashMap;
import java.util.Map;

/**
 * Tabela de Símbolos conforme especificação GLC 2025.2
 * Tupla: (identificador, categoria, base, tamanho)
 * categoria: 0=programa, 1=num, 2=real, 3=text, 4=flag
 * base: endereço lógico base-1 (primeira posição utilizável = 1)
 * tamanho: "–" se escalar; N>0 se vetor
 */
public class TabelaSimbolos {
    
    public static class Simbolo {
        public String identificador;
        public int categoria;     // 0=programa, 1=num, 2=real, 3=text, 4=flag
        public int base;          // endereço lógico base-1
        public String tamanho;    // "–" se escalar; N>0 se vetor
        
        public Simbolo(String identificador, int categoria, int base, String tamanho) {
            this.identificador = identificador;
            this.categoria = categoria;
            this.base = base;
            this.tamanho = tamanho;
        }
        
        public boolean isVetor() {
            return !tamanho.equals("–");
        }
        
        public int getTamanhoNumerico() {
            return tamanho.equals("–") ? 1 : Integer.parseInt(tamanho);
        }
        
        @Override
        public String toString() {
            return String.format("(%s, %d, %d, %s)", identificador, categoria, base, tamanho);
        }
    }
    
    private Map<String, Simbolo> tabela;
    
    public TabelaSimbolos() {
        this.tabela = new HashMap<>();
    }
    
    /**
     * Insere um símbolo na tabela
     */
    public void inserir(String identificador, int categoria, int base, String tamanho) {
        tabela.put(identificador, new Simbolo(identificador, categoria, base, tamanho));
    }
    
    /**
     * Busca um símbolo na tabela
     */
    public Simbolo buscar(String identificador) {
        return tabela.get(identificador);
    }
    
    /**
     * Verifica se um identificador já existe na tabela
     */
    public boolean existe(String identificador) {
        return tabela.containsKey(identificador);
    }
    
    /**
     * Remove um símbolo da tabela
     */
    public void remover(String identificador) {
        tabela.remove(identificador);
    }
    
    /**
     * Limpa a tabela
     */
    public void limpar() {
        tabela.clear();
    }
    
    /**
     * Retorna representação string da tabela
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TABELA DE SÍMBOLOS ===\n");
        sb.append("Identificador\tCategoria\tBase\tTamanho\n");
        for (Simbolo s : tabela.values()) {
            sb.append(String.format("%s\t\t%d\t\t%d\t%s\n", 
                s.identificador, s.categoria, s.base, s.tamanho));
        }
        return sb.toString();
    }
}
