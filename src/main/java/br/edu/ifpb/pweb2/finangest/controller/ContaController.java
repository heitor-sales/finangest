package br.edu.ifpb.pweb2.finangest.controller;

import java.util.List;
import java.util.Collections; 

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import br.edu.ifpb.pweb2.finangest.service.ContaService;
import br.edu.ifpb.pweb2.finangest.model.Conta;
import br.edu.ifpb.pweb2.finangest.model.Correntista;
import br.edu.ifpb.pweb2.finangest.repository.CorrentistaRepository; 
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired 
    private CorrentistaRepository correntistaRepository;

    @RequestMapping("/form")
    public ModelAndView getForm(ModelAndView modelAndView){
        modelAndView.setViewName("/contas/form");
        modelAndView.addObject("conta", new Conta(new Correntista()));
        return modelAndView;
    }

    @ModelAttribute("correntistaItems")
    public List<Correntista> getCorrentistas(){
        try {
            
            return correntistaRepository.findAll();
        } catch (Exception e) {
            
            e.printStackTrace(); 
            return Collections.emptyList(); 
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST) // Use 'value' para clareza
    public ModelAndView adicioneConta(Conta conta, ModelAndView modelAndView){
        // A lógica do service já trata de associar o Correntista completo à Conta
        contaService.save(conta);
        modelAndView.setViewName("contas/list"); // Redireciona para a página de listagem
        modelAndView.addObject("contas", contaService.findall()); // Adiciona todas as contas para exibir
        return modelAndView;
    }

    // Opcional: Um método para listar todas as contas (se não for feito automaticamente pelo /save)
    @RequestMapping("/list")
    public ModelAndView listContas(ModelAndView modelAndView) {
        modelAndView.setViewName("contas/list");
        modelAndView.addObject("contas", contaService.findall());
        return modelAndView;
    }
}