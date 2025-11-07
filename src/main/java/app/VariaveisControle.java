package app;

import java.util.ArrayList;
import java.util.List;

/**
 * Variáveis de controle conforme especificação GLC 2025.2
 */
public class VariaveisControle {
    
    // Variáveis principais
    public static int VT = 0;  // total de posições já alocadas na pilha de execução (base-1)
    public static int VP = 0;  // soma das posições a alocar na linha atual de declaração
    public static int categoriaAtual = 0;  // 1/2/3/4 (num/real/text/flag)
    
    // Listas para controle de declarações
    public static List<String> listaDeIdentificadoresDaLinha = new ArrayList<>();
    public static List<Integer> listaBasesDaLinha = new ArrayList<>();
    
    // Controle de vetores
    public static boolean temIndice = false;
    public static int baseDoUltimoVetor = 0;
    public static int tamanhoDoUltimoVetor = 0;
    
    // Controle de inicialização
    public static boolean houveInitLinha = false;
    public static int primeiroBaseInit = -1;
    
    // Controle de loops
    public static int inicioLoop = 0;
    
    // Informações do identificador atual (para ações semânticas)
    public static String identificadorAtual = "";
    public static int categoriaAtual_id = 0;
    public static int baseAtual = 0;
    public static String tamanhoAtual = "";
    
    /**
     * Reseta variáveis para nova linha de declaração (#D0)
     */
    public static void resetarLinhaDeclaracao() {
        listaDeIdentificadoresDaLinha.clear();
        listaBasesDaLinha.clear();
        VP = 0;
        houveInitLinha = false;
        primeiroBaseInit = -1;
    }
    
    /**
     * Reseta todas as variáveis
     */
    public static void resetarTudo() {
        VT = 0;
        VP = 0;
        categoriaAtual = 0;
        listaDeIdentificadoresDaLinha.clear();
        listaBasesDaLinha.clear();
        temIndice = false;
        baseDoUltimoVetor = 0;
        tamanhoDoUltimoVetor = 0;
        houveInitLinha = false;
        primeiroBaseInit = -1;
        inicioLoop = 0;
        identificadorAtual = "";
        categoriaAtual_id = 0;
        baseAtual = 0;
        tamanhoAtual = "";
    }
    
    /**
     * Converte categoria numérica para string
     */
    public static String categoriaParaString(int categoria) {
        switch (categoria) {
            case 0: return "programa";
            case 1: return "num";
            case 2: return "real";
            case 3: return "text";
            case 4: return "flag";
            default: return "desconhecido";
        }
    }
    
    /**
     * Retorna instrução de alocação conforme categoria
     */
    public static String getInstrucaoAlocacao(int categoria) {
        switch (categoria) {
            case 1: return "ALI";  // num
            case 2: return "ALR";  // real
            case 3: return "ALS";  // text
            case 4: return "ALB";  // flag
            default: return "ALI"; // padrão
        }
    }
    
    /**
     * Retorna instrução de constante conforme categoria
     */
    public static String getInstrucaoConstante(int categoria) {
        switch (categoria) {
            case 1: return "LDI";  // num
            case 2: return "LDR";  // real
            case 3: return "LDS";  // text
            case 4: return "LDB";  // flag
            default: return "LDI"; // padrão
        }
    }
}
