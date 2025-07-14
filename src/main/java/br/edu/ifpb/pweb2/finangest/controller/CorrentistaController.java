package br.edu.ifpb.pweb2.finangest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import  br.edu.ifpb.pweb2.finangest.Util.PasswordUtil;
import br.edu.ifpb.pweb2.finangest.model.Conta;
import br.edu.ifpb.pweb2.finangest.model.Correntista;
import br.edu.ifpb.pweb2.finangest.repository.ContaRepository;
import br.edu.ifpb.pweb2.finangest.repository.CorrentistaRepository;


@Controller
@RequestMapping("/correntistas")
public class CorrentistaController {

    @Autowired
    private CorrentistaRepository correntistaRepository;
    @Autowired
    private ContaRepository contaRepository;

    @RequestMapping("/form")
    public String getForm(Correntista correntista, Model model) {
        model.addAttribute("correntista", correntista);
        return "correntistas/form";
    }

    @RequestMapping("/save")
    public String save(Correntista correntista, Model model, RedirectAttributes attr) {

        // Validação do nome
        if (correntista.getNome() == null || correntista.getNome().trim().isEmpty()) {
            model.addAttribute("mensagem", "O nome do correntista é obrigatório.");
            model.addAttribute("correntista", correntista);
            return "correntistas/form";
        }

        if (correntista.getNome().length() > 50) {
            model.addAttribute("mensagem", "O nome do correntista deve ter no máximo 50 caracteres.");
            model.addAttribute("correntista", correntista);
            return "correntistas/form";
        }

        // Validação da senha
        if (correntista.getSenha() == null || correntista.getSenha().trim().isEmpty()) {
            model.addAttribute("mensagem", "A senha é obrigatória.");
            model.addAttribute("correntista", correntista);
            return "correntistas/form";
        }

        String senhaHash = PasswordUtil.hashPassword(correntista.getSenha());
        correntista.setSenha(senhaHash);
        // Validação do email
        if (correntista.getEmail() == null || correntista.getEmail().trim().isEmpty()) {
            model.addAttribute("mensagem", "O email é obrigatório.");
            model.addAttribute("correntista", correntista);
            return "correntistas/form";
        }

        // Se passou por todas as validações, salva e lista
        
        correntistaRepository.save(correntista);
        attr.addFlashAttribute("mensagem","Correntista inserido com sucesso.");
        model.addAttribute("correntistas", correntistaRepository.findAll());
        return "redirect:list";
    }

    @RequestMapping("/list")
    public String listAll(Model model) {
        model.addAttribute("correntistas", correntistaRepository.findAll());
        return "correntistas/list";
    }

    @RequestMapping("/{id}")
    public String getCorrentistaById(@PathVariable(value = "id") Integer id, Model model) {
        model.addAttribute("correntista", correntistaRepository.findById(id));
        return "correntistas/form";
    }
    @RequestMapping("/{id}/conta/form")
public String getContaForm(@PathVariable("id") Integer correntistaId, Model model) {
    Conta conta = new Conta();
    conta.setCorrentista(correntistaRepository.findById(correntistaId).orElse(null));
    model.addAttribute("conta", conta);
    return "contas/form"; // esse será o formulário de criação de conta
}

@RequestMapping("/{id}/conta/save")
public String salvarConta(@PathVariable("id") Integer correntistaId, Conta conta, RedirectAttributes attr) {
    Correntista correntista = correntistaRepository.findById(correntistaId).orElse(null);
    if (correntista == null) {
        attr.addFlashAttribute("mensagem", "Correntista não encontrado.");
        return "redirect:/correntistas/list";
    }

    conta.setCorrentista(correntista);
    contaRepository.save(conta);

    attr.addFlashAttribute("mensagem", "Conta criada com sucesso.");
    return "redirect:/correntistas/" + correntistaId;
}

   
    
}
