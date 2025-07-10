package br.edu.ifpb.pweb2.finangest.model;

import lombok.Data;

import java.util.List;

@Data
public class Categoria {

    private Integer id;

    private String nome;
    private String natureza;  //ENTRADA, SAÍDA e INVESTIMENTO
    private Integer ordem;  //1, 2 ou 3
    private boolean ativa; //se a categoria está ativa ou não

    
    private List<Transacao> transacoes;

    // @Override
    // public String toString() {
    //     return "Categoria{" +
    //             "id=" + id +
    //             ", nome='" + nome + '\'' +
    //             ", natureza='" + natureza + '\'' +
    //             ", ordem=" + ordem +
    //             ", ativa=" + ativa +
    //             ", transacoes=" + transacoes +
    //             '}';
    // }

    // public Integer getId() {
    //     return id;
    // }

    // public void setId(Integer id) {
    //     this.id = id;
    // }

    // public String getNome() {
    //     return nome;
    // }

    // public void setNome(String nome) {
    //     this.nome = nome;
    // }

    // public String getNatureza() {
    //     return natureza;
    // }

    // public void setNatureza(String natureza) {
    //     this.natureza = natureza;
    // }

    // public Integer getOrdem() {
    //     return ordem;
    // }

    // public void setOrdem(Integer ordem) {
    //     this.ordem = ordem;
    // }

    // public boolean isAtiva() {
    //     return ativa;
    // }

    // public void setAtiva(boolean ativa) {
    //     this.ativa = ativa;
    // }

    // public List<Transacao> getTransacoes() {
    //     return transacoes;
    // }

    // public void setTransacoes(List<Transacao> transacoes) {
    //     this.transacoes = transacoes;
    // }
}
