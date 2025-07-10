package br.edu.ifpb.pweb2.finangest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
// import java.util.Date;
import java.util.HashSet;
// import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class Conta implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String numero;

    private String descricao; // ex: “Minha conta no BB”

    private String tipo; //ex: “CORRENTE”

    private LocalDate diaFechamento; //apenas para tipo == cartão

    private Set<Transacao> transacoes = new HashSet<Transacao>();

    private Correntista correntista;

    public BigDecimal getSaldo() {
        BigDecimal total = BigDecimal.ZERO;
        for (Transacao t : this.transacoes) {
            total = total.add(t.getValor());
        }
        return total;
    }

    // @Override
    // public String toString() {
    //     return "Conta{" +
    //             "id=" + id +
    //             ", numero='" + numero + '\'' +
    //             ", descricao='" + descricao + '\'' +
    //             ", tipo='" + tipo + '\'' +
    //             ", diaFechamento=" + diaFechamento +
    //             ", correntista=" + correntista +
    //             ", transacoes=" + transacoes +
    //             '}';
    // }

    // public Integer getId() {
    //     return id;
    // }

    // public void setId(Integer id) {
    //     this.id = id;
    // }

    // public String getNumero() {
    //     return numero;
    // }

    // public void setNumero(String numero) {
    //     this.numero = numero;
    // }

    // public String getDescricao() {
    //     return descricao;
    // }

    // public void setDescricao(String descricao) {
    //     this.descricao = descricao;
    // }

    // public String getTipo() {
    //     return tipo;
    // }

    // public void setTipo(String tipo) {
    //     this.tipo = tipo;
    // }

    // public LocalDate getDiaFechamento() {
    //     return diaFechamento;
    // }

    // public void setDiaFechamento(LocalDate diaFechamento) {
    //     this.diaFechamento = diaFechamento;
    // }

    // public Correntista getCorrentista() {
    //     return correntista;
    // }

    // public void setCorrentista(Correntista correntista) {
    //     this.correntista = correntista;
    // }

    // public Set<Transacao> getTransacoes() {
    //     return transacoes;
    // }

    // public void setTransacoes(Set<Transacao> transacoes) {
    //     this.transacoes = transacoes;
    // }
}