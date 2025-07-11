package br.edu.ifpb.pweb2.finangest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoConta {

    private Long id;

    // Representação da relação Many-to-One com Transacao
    private Transacao transacao_id;

    // Representação da relação Many-to-One com Conta
    private Conta conta_id;
}