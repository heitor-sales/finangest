package br.edu.ifpb.pweb2.finangest.repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.edu.ifpb.pweb2.finangest.model.Conta; 

@Component
public class ContaRepository {

   
    private Map<Integer, Conta> repositorio = new HashMap<>();

    public Conta findById(Integer id) {
        return repositorio.get(id);
    }

    // O método save deve receber e retornar Conta
    public Conta save(Conta conta) {
        Integer id = null;
        // Se o ID da conta for nulo, gera um novo ID
        id = (conta.getId() == null) ? this.getMaxId() : conta.getId();
        conta.setId(id);
        repositorio.put(id, conta);
        return conta;
    }

    
    public List<Conta> findAll() {
        
        return repositorio.values().stream().collect(Collectors.toList());
    }

    public Integer getMaxId() {
        List<Conta> contas = findAll(); 
        if (contas == null || contas.isEmpty()) {
            return 1;
        }
        
        
        Conta contaMaxId = contas
                .stream()
                .max(Comparator.comparing(Conta::getId)) 
                .orElseThrow(() -> new NoSuchElementException("Não foi possível encontrar o ID máximo.")); 
        
        return contaMaxId.getId() == null ? 1 : contaMaxId.getId() + 1;
    }
}