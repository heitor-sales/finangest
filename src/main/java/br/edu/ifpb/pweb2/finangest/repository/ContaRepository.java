package br.edu.ifpb.pweb2.finangest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import br.edu.ifpb.pweb2.finangest.model.Conta;

@Component
public interface ContaRepository  extends JpaRepository<Conta, Integer>{

   


   
}