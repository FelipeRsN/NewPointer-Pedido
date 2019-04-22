package newpointer.com.br.newpointerpedido.Model;

/**
 * Created by FelipeRsN on 6/24/16.
 */
public class ConfigModel {
    private String string_bd;
    private String estacao;
    private Double taxa;
    private int digito_verificador;
    private int pergunta_mesa;
    private String titulo_loja;
    private String nmin_mesa;
    private String nmax_mesa;
    private String dbbkp_date;
    private int busca_selection;
    private int operacao_selection;
    private int preconta;
    private int conferencia;

    public ConfigModel(String string_bd, String estacao, Double taxa, int digito_verificador, int pergunta_mesa, String titulo_loja, String nmin_mesa, String nmax_mesa, String dbbkp_date, int busca_selection, int operacao_selection, int preconta, int conferencia){
        this.string_bd = string_bd;
        this.estacao = estacao;
        this.taxa = taxa;
        this.digito_verificador = digito_verificador;
        this.pergunta_mesa = pergunta_mesa;
        this.titulo_loja = titulo_loja;
        this.nmin_mesa = nmin_mesa;
        this.nmax_mesa = nmax_mesa;
        this.dbbkp_date = dbbkp_date;
        this.busca_selection = busca_selection;
        this.operacao_selection = operacao_selection;
        this.preconta = preconta;
        this.conferencia = conferencia;
    }

    public int getPreconta() {
        return preconta;
    }

    public void setPreconta(int preconta) {
        this.preconta = preconta;
    }

    public int getConferencia() {
        return conferencia;
    }

    public void setConferencia(int conferencia) {
        this.conferencia = conferencia;
    }

    public String getString_bd() {
        return string_bd;
    }

    public void setString_bd(String string_bd) {
        this.string_bd = string_bd;
    }

    public String getEstacao() {
        return estacao;
    }

    public void setEstacao(String estacao) {
        this.estacao = estacao;
    }

    public Double getTaxa() {
        return taxa;
    }

    public void setTaxa(Double taxa) {
        this.taxa = taxa;
    }

    public int getDigito_verificador() {
        return digito_verificador;
    }

    public void setDigito_verificador(int digito_verificador) {
        this.digito_verificador = digito_verificador;
    }

    public int getPergunta_mesa() {
        return pergunta_mesa;
    }

    public void setPergunta_mesa(int pergunta_mesa) {
        this.pergunta_mesa = pergunta_mesa;
    }

    public String getTitulo_loja() {
        return titulo_loja;
    }

    public void setTitulo_loja(String titulo_loja) {
        this.titulo_loja = titulo_loja;
    }

    public String getNmin_mesa() {
        return nmin_mesa;
    }

    public void setNmin_mesa(String nmin_mesa) {
        this.nmin_mesa = nmin_mesa;
    }

    public String getNmax_mesa() {
        return nmax_mesa;
    }

    public void setNmax_mesa(String nmax_mesa) {
        this.nmax_mesa = nmax_mesa;
    }

    public String getDbbkp_date() {
        return dbbkp_date;
    }

    public void setDbbkp_date(String dbbkp_date) {
        this.dbbkp_date = dbbkp_date;
    }

    public int getBusca_selection() {
        return busca_selection;
    }

    public void setBusca_selection(int busca_selection) {
        this.busca_selection = busca_selection;
    }

    public int getOperacao_selection() {
        return operacao_selection;
    }

    public void setOperacao_selection(int operacao_selection) {
        this.operacao_selection = operacao_selection;
    }
}
