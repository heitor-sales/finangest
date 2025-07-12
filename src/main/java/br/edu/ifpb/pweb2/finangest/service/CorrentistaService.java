package br.edu.ifpb.pweb2.finangest.service;

import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.pweb2.finangest.Util.PasswordUtil;
import br.edu.ifpb.pweb2.finangest.model.Correntista;
import br.edu.ifpb.pweb2.finangest.repository.CorrentistaRepository;

public class CorrentistaService {
    @Autowired
    public CorrentistaRepository correntistaRepository;

    public Correntista save (Correntista c){
        c.setSenha(PasswordUtil.hashPassword(c.getSenha()));
        return correntistaRepository.save(c);
    }
}
