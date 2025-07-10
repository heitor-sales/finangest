package br.edu.ifpb.pweb2.finangest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifpb.pweb2.finangest.model.Correntista;
import br.edu.ifpb.pweb2.finangest.repository.CorrentistaRepository;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/correntistas")
public class CorrentistaController {

    @Autowired
    private CorrentistaRepository correntistaRepository;

    @RequestMapping("/form")
    public String getForm(Correntista correntista, Model model) {
        model.addAttribute("correntista", correntista);
        return "correntistas/form";
    }

    @RequestMapping("/save")
    public String save(Correntista correntista, Model model) {

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

        // Validação do email
        if (correntista.getEmail() == null || correntista.getEmail().trim().isEmpty()) {
            model.addAttribute("mensagem", "O email é obrigatório.");
            model.addAttribute("correntista", correntista);
            return "correntistas/form";
        }

        // Se passou por todas as validações, salva e lista
        correntistaRepository.save(correntista);
        model.addAttribute("correntistas", correntistaRepository.findAll());
        return "correntistas/list";
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
}
