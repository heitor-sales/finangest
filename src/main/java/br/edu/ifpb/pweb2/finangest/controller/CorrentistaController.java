package br.edu.ifpb.pweb2.finangest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import  br.edu.ifpb.pweb2.finangest.Util.PasswordUtil;
import br.edu.ifpb.pweb2.finangest.model.Categoria;
import br.edu.ifpb.pweb2.finangest.model.Conta;
import br.edu.ifpb.pweb2.finangest.model.Correntista;
import br.edu.ifpb.pweb2.finangest.repository.ContaRepository;
import br.edu.ifpb.pweb2.finangest.repository.CorrentistaRepository;
import br.edu.ifpb.pweb2.finangest.repository.CategoriaRepository;


@Controller
@RequestMapping("/correntistas")
public class CorrentistaController {

    @Autowired
    private CorrentistaRepository correntistaRepository;
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private CategoriaRepository categoriaRepo;

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
        //attr.addFlashAttribute("mensagem","Correntista inserido com sucesso");
        model.addAttribute("correntistas", correntistaRepository.findAll());
        return "redirect:/correntistas/list";
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
@RequestMapping("/{id}/categoria/form")
public String getCategoriaForm(@PathVariable("id") Integer correntistaId, Model model, RedirectAttributes attr) {
    Correntista correntista = correntistaRepository.findById(correntistaId).orElse(null);
    if (correntista == null || !correntista.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado. Apenas administradores podem criar categorias.");
        return "redirect:/correntistas/list";
    }

    model.addAttribute("categoria", new Categoria());
    model.addAttribute("correntistaId", correntistaId);
    return "categorias/form";
}

@RequestMapping("/{id}/categoria/save")
public String salvarCategoria(@PathVariable("id") Integer correntistaId, Categoria categoria, RedirectAttributes attr) {
    Correntista correntista = correntistaRepository.findById(correntistaId).orElse(null);
    if (correntista == null || !correntista.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado. Apenas administradores podem salvar categorias.");
        return "redirect:/correntistas/list";
    }

    categoriaRepo.save(categoria);
    attr.addFlashAttribute("mensagem", "Categoria salva com sucesso.");
    return "redirect:/correntistas/" + correntistaId + "/categorias";
}
@RequestMapping("/{id}/categoria/edit/{categoriaId}")
public String editarCategoria(@PathVariable("id") Integer correntistaId, @PathVariable("categoriaId") Long categoriaId,
                              Model model, RedirectAttributes attr) {
    Correntista correntista = correntistaRepository.findById(correntistaId).orElse(null);
    if (correntista == null || !correntista.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado.");
        return "redirect:/correntistas/list";
    }

    Categoria categoria = categoriaRepo.findById(categoriaId).orElse(null);
    model.addAttribute("categoria", categoria);
    model.addAttribute("correntistaId", correntistaId);
    return "categorias/form";
}
@RequestMapping("/{id}/categoria/desativar/{categoriaId}")
public String desativarCategoria(@PathVariable("id") Integer correntistaId,
                                 @PathVariable("categoriaId") Long categoriaId,
                                 RedirectAttributes attr) {
    Correntista correntista = correntistaRepository.findById(correntistaId).orElse(null);
    if (correntista == null || !correntista.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado.");
        return "redirect:/correntistas/list";
    }

    Categoria categoria = categoriaRepo.findById(categoriaId).orElse(null);
    if (categoria != null) {
        categoria.setAtiva(false);
        categoriaRepo.save(categoria);
        attr.addFlashAttribute("mensagem", "Categoria desativada com sucesso.");
    }

    return "redirect:/correntistas/" + correntistaId + "/categorias";
}

   @RequestMapping("/{id}/admin/conta/form")
public String getAdminContaForm(@PathVariable("id") Integer correntistaId, Model model, RedirectAttributes attr) {
    Correntista admin = correntistaRepository.findById(correntistaId).orElse(null);

    if (admin == null || !admin.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado.");
        return "redirect:/correntistas/list";
    }

    model.addAttribute("conta", new Conta());
    model.addAttribute("correntistas", correntistaRepository.findAll()); // escolher para quem é a conta
    return "contas/form-admin";
}

@RequestMapping("/{id}/admin/conta/save")
public String salvarAdminConta(@PathVariable("id") Integer correntistaId, Conta conta, RedirectAttributes attr) {
    Correntista admin = correntistaRepository.findById(correntistaId).orElse(null);

    if (admin == null || !admin.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado.");
        return "redirect:/correntistas/list";
    }

    contaRepository.save(conta);
    attr.addFlashAttribute("mensagem", "Conta criada com sucesso.");
    return "redirect:/correntistas/list";
}
@RequestMapping("/{id}admin/conta/edit/{contaId}")
public StringeditarContaAdmin(@PathVariable("id") Integer correntistaId,
                                @PathVariable("contaId") Long contaId,
                                Model model, RedirectAttributes attr) {
    Correntista admin = correntistaRepository.findById(correntistaId).orElse(null);

    if (admin == null || !admin.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado.");
        return "redirect:/correntistas/list";
    }
    Conta conta = contaRepository.findById(contaId).orElse(null);
    if (conta != null) {
        contaRepository.delete(conta); // cascade.ALL garante a remoção de transações
        attr.addFlashAttribute("mensagem", "Conta e transações removidas.");
    } else {
        attr.addFlashAttribute("mensagem", "Conta não encontrada.");
    }
     return "redirect:/correntistas/list";
}
   @RequestMapping("/{id}/admin/conta/delete/{contaId}")
public String deletarContaAdmin(@PathVariable("id") Integer correntistaId,
                                @PathVariable("contaId") Long contaId,
                                RedirectAttributes attr) {
    Correntista admin = correntistaRepository.findById(correntistaId).orElse(null);

    if (admin == null || !admin.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado.");
        return "redirect:/correntistas/list";
    }

    Conta conta = contaRepository.findById(contaId).orElse(null);
    if (conta != null) {
        contaRepository.delete(conta); // cascade.ALL garante a remoção de transações
        attr.addFlashAttribute("mensagem", "Conta e transações removidas.");
    } else {
        attr.addFlashAttribute("mensagem", "Conta não encontrada.");
    }

    return "redirect:/correntistas/list";
}
@RequestMapping("/{id}/admin/correntista/form")
public String getCorrentistaAdminForm(@PathVariable("id") Integer adminId, Model model, RedirectAttributes attr) {
    Correntista admin = correntistaRepository.findById(adminId).orElse(null);
    if (admin == null || !admin.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado.");
        return "redirect:/correntistas/list";
    }

    model.addAttribute("correntista", new Correntista());
    return "correntistas/form-admin";
}

@RequestMapping("/{id}/admin/correntista/save")
public String salvarCorrentistaAdmin(@PathVariable("id") Integer adminId, Correntista correntista, Model model, RedirectAttributes attr) {
    Correntista admin = correntistaRepository.findById(adminId).orElse(null);
    if (admin == null || !admin.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado.");
        return "redirect:/correntistas/list";
    }

    if (correntista.getSenha() != null && !correntista.getSenha().trim().isEmpty()) {
        String senhaHash = PasswordUtil.hashPassword(correntista.getSenha());
        correntista.setSenha(senhaHash);
    }

    correntistaRepository.save(correntista);
    attr.addFlashAttribute("mensagem", "Correntista salvo com sucesso.");
    return "redirect:/correntistas/list";
}
@RequestMapping("/{id}/admin/correntista/edit/{editId}")
public String editarCorrentistaAdmin(@PathVariable("id") Integer adminId,
                                     @PathVariable("editId") Integer editId,
                                     Model model, RedirectAttributes attr) {
    Correntista admin = correntistaRepository.findById(adminId).orElse(null);
    if (admin == null || !admin.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado.");
        return "redirect:/correntistas/list";
    }

    Correntista editado = correntistaRepository.findById(editId).orElse(null);
    model.addAttribute("correntista", editado);
    return "correntistas/form-admin";
}
@RequestMapping("/{id}/admin/correntista/delete/{deleteId}")
public String excluirCorrentistaAdmin(@PathVariable("id") Integer adminId,
                                      @PathVariable("deleteId") Integer deleteId,
                                      RedirectAttributes attr) {
    Correntista admin = correntistaRepository.findById(adminId).orElse(null);
    if (admin == null || !admin.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado.");
        return "redirect:/correntistas/list";
    }

    Correntista correntista = correntistaRepository.findById(deleteId).orElse(null);
    if (correntista != null) {
        correntistaRepository.delete(correntista); // Apaga as contas e transações em cascata
        attr.addFlashAttribute("mensagem", "Correntista e suas contas foram removidos.");
    } else {
        attr.addFlashAttribute("mensagem", "Correntista não encontrado.");
    }

    return "redirect:/correntistas/list";
}
@RequestMapping("/{adminId}/admin/correntista/bloquear/{targetId}")
public String bloquearCorrentista(@PathVariable("adminId") Integer adminId,
                                  @PathVariable("targetId") Integer targetId,
                                  RedirectAttributes attr) {
    Correntista admin = correntistaRepository.findById(adminId).orElse(null);
    if (admin == null || !admin.isAdmin()) {
        attr.addFlashAttribute("mensagem", "Acesso negado.");
        return "redirect:/correntistas/list";
    }
    Correntista alvo = correntistaRepository.findById(targetId).orElse(null);
    if (alvo != null) {
        alvo.setAtivo(false);
        correntistaRepository.save(alvo);
        attr.addFlashAttribute("mensagem", "Correntista bloqueado com sucesso.");
    }
      return "redirect:/correntistas/list";
}
}
