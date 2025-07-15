package br.edu.ifpb.pweb2.finangest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Conta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String numero;

    private String descricao; // ex: “Minha conta no BB”

    @Enumerated(EnumType.STRING) // Armazena o nome do enum (CORRENTE, CARTAO_CREDITO) no BD
    private TipoConta tipo; //ex: “CORRENTE”

    // private String tipo; //ex: “CORRENTE”

    private Integer diaFechamento; //apenas para tipo == cartão

    private Set<Transacao> transacoes = new HashSet<Transacao>();

    private Correntista correntista;

    public Conta(Correntista correntista){
        this.correntista=correntista;
    }

    public BigDecimal getSaldo() {
        BigDecimal total = BigDecimal.ZERO;
        for (Transacao t : this.transacoes) {
            total = total.add(t.getValor());
        }
        return total;
    }

    // public void addTransacao(Transacao transacao) {
    //     this.transacoes.add(transacao);
    //     transacao.setConta(this);
    // }
}