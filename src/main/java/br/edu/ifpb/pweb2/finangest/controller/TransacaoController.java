package br.edu.ifpb.pweb2.finangest.controller;

import br.edu.ifpb.pweb2.finangest.model.Conta;
import br.edu.ifpb.pweb2.finangest.model.Transacao;
import br.edu.ifpb.pweb2.finangest.model.Comentario;
import br.edu.ifpb.pweb2.finangest.repository.CategoriaRepository;
import br.edu.ifpb.pweb2.finangest.repository.ContaRepository;
import br.edu.ifpb.pweb2.finangest.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Método para exibir o formulário de nova transação ou edição
    @GetMapping("/form")
    public ModelAndView getForm(@RequestParam(name = "contaId", required = false) Integer contaId, ModelAndView modelAndView) {
        modelAndView.setViewName("transacoes/form");
        Transacao transacao = new Transacao(); // Cria uma nova transação

        // Se um contaId for fornecido (ao clicar em "Registrar Nova Transação" no extrato)
        if (contaId != null) {
            Optional<Conta> contaOpt = contaRepository.findById(contaId);
            if (contaOpt.isPresent()) {
                Conta conta = contaOpt.get();
                transacao.setConta(conta);
                transacao.setData(LocalDate.now()); // Pré-preenche a data com a data atual
                transacao.setComentario(new Comentario()); // Garante que o objeto Comentario não seja nulo para o formulário
            } else {
                // Se contaId foi fornecido mas a conta não foi encontrada, redireciona com erro
                modelAndView.setViewName("redirect:/contas/list");
                modelAndView.addObject("errorMessage", "Conta não encontrada com o ID fornecido.");
                return modelAndView;
            }
        } else {
            // Caso acesse /transacoes/form diretamente sem contaId,
            // o campo de seleção de conta no futuro formulário seria necessário.
            // Para este cenário, como o link vem do extrato, é esperado que `contaId` esteja presente.
            // Se você quiser permitir adicionar transações sem um contaId inicial,
            // precisaria adicionar um campo de seleção de conta no formulário.
            // Por enquanto, inicializa uma conta vazia para evitar NullPointerException no Thymeleaf.
            transacao.setConta(new Conta());
            transacao.setComentario(new Comentario()); // Garante o objeto Comentario
        }

        modelAndView.addObject("transacao", transacao);
        modelAndView.addObject("categoriaItems", categoriaRepository.findAll()); // Carrega todas as categorias
        return modelAndView;
    }

    // Método para salvar uma transação (POST do formulário)
    @PostMapping("/save")
    public ModelAndView saveTransacao(@ModelAttribute Transacao transacao, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        // Validação da Conta
        if (transacao.getConta() == null || transacao.getConta().getId() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "A transação deve ser associada a uma conta válida.");
            modelAndView.setViewName("redirect:/transacoes/form"); // Retorna para o formulário se não houver conta
            return modelAndView;
        }

        // Busca a entidade Conta completa do banco de dados
        Optional<Conta> contaOpt = contaRepository.findById(transacao.getConta().getId());
        if (contaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Conta não encontrada para a transação.");
            modelAndView.setViewName("redirect:/contas/list");
            return modelAndView;
        }
        transacao.setConta(contaOpt.get()); // Associa a entidade Conta completa à transação

        // Validação da Categoria
        if (transacao.getCategoria() == null || transacao.getCategoria().getId() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Selecione uma categoria para a transação.");
            // Retorna para o formulário, mantendo o ID da conta
            modelAndView.setViewName("redirect:/transacoes/form?contaId=" + transacao.getConta().getId());
            return modelAndView;
        }

        // Busca a entidade Categoria completa do banco de dados
        categoriaRepository.findById(transacao.getCategoria().getId())
            .ifPresentOrElse(
                transacao::setCategoria,
                () -> {
                    // Categoria não encontrada, isso não deveria acontecer se o select é preenchido do banco
                    redirectAttributes.addFlashAttribute("errorMessage", "Categoria não encontrada para a transação.");
                    modelAndView.setViewName("redirect:/transacoes/form?contaId=" + transacao.getConta().getId());
                }
            );

        // Lidar com o Comentario: Se o texto do comentário estiver vazio, defina o objeto Comentario como null
        // para evitar salvar um Comentario vazio no banco, já que é @OneToOne.
        if (transacao.getComentario() != null && (transacao.getComentario().getTexto() == null || transacao.getComentario().getTexto().trim().isEmpty())) {
            transacao.setComentario(null);
        } else if (transacao.getComentario() != null && transacao.getComentario().getId() != null) {
            // Se for uma edição e o comentário já existe, o Hibernate/JPA deve lidar com a atualização.
            // Não precisamos buscar o Comentario aqui se o CascadeType.ALL está em Transacao.
        }


        transacaoRepository.save(transacao); // Salva a transação (e o comentário, se houver, devido ao CascadeType.ALL)
        redirectAttributes.addFlashAttribute("successMessage", "Transação salva com sucesso!");

        // Redireciona de volta para o extrato da conta à qual a transação foi registrada
        modelAndView.setViewName("redirect:/contas/" + transacao.getConta().getId() + "/extrato");
        return modelAndView;
    }

    // Método para exibir o formulário de edição de transação
    @GetMapping("/edit/{id}")
    public ModelAndView getFormForEdit(@PathVariable("id") Integer id, ModelAndView modelAndView) {
        modelAndView.setViewName("transacoes/form");
        Optional<Transacao> transacaoOpt = transacaoRepository.findById(id);

        if (transacaoOpt.isPresent()) {
            Transacao transacao = transacaoOpt.get();
            // Garante que o objeto Comentario não seja nulo no formulário para evitar NPE no th:field="*{comentario.texto}"
            if (transacao.getComentario() == null) {
                transacao.setComentario(new Comentario());
            }
            modelAndView.addObject("transacao", transacao);
            modelAndView.addObject("categoriaItems", categoriaRepository.findAll());
        } else {
            // Lidar com transação não encontrada
            modelAndView.setViewName("redirect:/contas/list");
            modelAndView.addObject("errorMessage", "Transação não encontrada para edição.");
        }
        return modelAndView;
    }

    // Método para deletar uma transação
    @GetMapping("/delete/{id}")
    public String deleteTransacao(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Optional<Transacao> transacaoOpt = transacaoRepository.findById(id);
        if (transacaoOpt.isPresent()) {
            Integer contaId = transacaoOpt.get().getConta().getId(); // Pega o ID da conta antes de deletar a transação
            transacaoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Transação excluída com sucesso!");
            return "redirect:/contas/" + contaId + "/extrato"; // Redireciona de volta para o extrato da conta
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Transação não encontrada para exclusão.");
            return "redirect:/contas/list"; // Redireciona para lista de contas se a transação não for encontrada
        }
    }
}