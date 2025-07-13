package br.edu.ifpb.pweb2.finangest.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Id;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
