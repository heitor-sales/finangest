package br.edu.ifpb.pweb2.finangest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.finangest.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Integer> {

}
