package br.edu.ifpb.pweb2.finangest.service;
import br.edu.ifpb.pweb2.finangest.model.Conta;
import br.edu.ifpb.pweb2.finangest.repository.ContaRepository;
import br.edu.ifpb.pweb2.finangest.repository.CorrentistaRepository;
import br.edu.ifpb.pweb2.finangest.model.Correntista;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class ContaService implements Service<Conta,Integer> {
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private CorrentistaRepository correntistaRepository;

    @Override
    public List<Conta> findall() {
        return contaRepository.findAll();
}

    @Override
    public Conta findByID(Integer id) {
        return contaRepository.findById(id);
    }

    @Override
    public Conta save(Conta conta) {
        Correntista correntista=correntistaRepository.findById(conta.getCorrentista().getId());
        conta.setCorrentista(correntista);
        return contaRepository.save(conta);
        
    
    
   
}

}





