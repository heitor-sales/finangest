package br.edu.ifpb.pweb2.finangest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
// import java.util.Date;

import lombok.Data;

@Data
public class Transacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String descricao; //ex: “Pagamento do curso de desenho”

    private BigDecimal valor;

    private String movimento; //crédito ou débito
    
    private String comentario; //usado para melhor descrever as transações, mas não é obrigatório

    private LocalDate data;

    private Categoria categoria; //Salário, Invetimento, Saúde e Remédios e etc.

    private Conta conta;

    // @Override
    // public String toString() {
    //     return "Transacao{" +
    //             "id=" + id +
    //             ", data=" + data +
    //             ", descricao='" + descricao + '\'' +
    //             ", valor=" + valor +
    //             ", movimento='" + movimento + '\'' +
    //             ", comentario='" + comentario + '\'' +
    //             ", categoria=" + categoria +
    //             ", conta=" + conta +
    //             '}';
    // }

    // public Integer getId() {
    //     return id;
    // }

    // public void setId(Integer id) {
    //     this.id = id;
    // }

    // public LocalDate getData() {
    //     return data;
    // }

    // public void setData(LocalDate data) {
    //     this.data = data;
    // }

    // public String getDescricao() {
    //     return descricao;
    // }

    // public void setDescricao(String descricao) {
    //     this.descricao = descricao;
    // }

    // public BigDecimal getValor() {
    //     return valor;
    // }

    // public void setValor(BigDecimal valor) {
    //     this.valor = valor;
    // }

    // public String getMovimento() {
    //     return movimento;
    // }

    // public void setMovimento(String movimento) {
    //     this.movimento = movimento;
    // }

    // public String getComentario() {
    //     return comentario;
    // }

    // public void setComentario(String comentario) {
    //     this.comentario = comentario;
    // }

    // public Categoria getCategoria() {
    //     return categoria;
    // }

    // public void setCategoria(Categoria categoria) {
    //     this.categoria = categoria;
    // }

    // public Conta getConta() {
    //     return conta;
    // }

    // public void setConta(Conta conta) {
    //     this.conta = conta;
    // }
    
}
