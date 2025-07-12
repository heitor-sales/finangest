package br.edu.ifpb.pweb2.finangest.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import br.edu.ifpb.pweb2.finangest.service.ContaService;
import br.edu.ifpb.pweb2.finangest.model.Conta;
import  br.edu.ifpb.pweb2.finangest.model.Correntista;
import br.edu.ifpb.pweb2.finangest.repository.CorrentistaRepository;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
@RequestMapping("/contas")

public class ContaController {
    @Autowired
    private ContaService contaService;
    @RequestMapping("/form")
    public ModelAndView getForm(ModelAndView mav){
        mav.setViewName("/contas/form");
        mav.addObject("conta", new Conta(new Correntista()));
        return mav;
    }
    @ModelAttribute("correntistaItems")
    public List<Correntista>getCorrentistas(){
        try {
            try {
                return CorrentistaRepository.findAll();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
@RequestMapping("/save", method=RequestMethod.POST)
public ModelAndView adicioneConta(Conta conta, ModelAndView mav){
contaService.save(conta);
mav.setViewName("contas/list");
mav.addObject("contas", contaService.findAll());
return mav;
}
   
    

}
