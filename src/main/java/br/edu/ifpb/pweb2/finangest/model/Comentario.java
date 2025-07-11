package br.edu.ifpb.pweb2.finangest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comentario {

    private Long id;
    private String texto;

    // Representação da relação Many-to-One com Transacao
    private Transacao transacao_id;
}