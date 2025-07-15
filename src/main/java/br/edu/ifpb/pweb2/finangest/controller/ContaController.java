package br.edu.ifpb.pweb2.finangest.controller;

import java.util.List;
import java.util.Optional; // Importe java.util.Optional
import java.math.BigDecimal;
import java.time.LocalDate; // Importe java.time.LocalDate
import java.time.YearMonth; // Importe java.time.YearMonth
import java.util.Collections;
import java.util.Comparator; // Importe java.util.Comparator

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import br.edu.ifpb.pweb2.finangest.service.ContaService;
import org.springframework.ui.Model; // *** CORRIGIDO: Importe o Model correto do Spring ***
import br.edu.ifpb.pweb2.finangest.model.Conta;
import br.edu.ifpb.pweb2.finangest.model.Correntista;
import br.edu.ifpb.pweb2.finangest.model.TipoMovimento;
import br.edu.ifpb.pweb2.finangest.model.Transacao;
import br.edu.ifpb.pweb2.finangest.repository.CorrentistaRepository;
import br.edu.ifpb.pweb2.finangest.repository.TransacaoRepository; // Importe TransacaoRepository
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private CorrentistaRepository correntistaRepository;

    @Autowired // *** Correção: Certifique-se que TransacaoRepository está injetado aqui ***
    private TransacaoRepository transacaoRepository;

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

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView adicioneConta(Conta conta, ModelAndView modelAndView){
        contaService.save(conta);
        modelAndView.setViewName("contas/list");
        modelAndView.addObject("contas", contaService.findall());
        return modelAndView;
    }

    @RequestMapping("/list")
    public ModelAndView listContas(ModelAndView modelAndView) {
        modelAndView.setViewName("contas/list");
        modelAndView.addObject("contas", contaService.findall());
        return modelAndView;
    }

    @RequestMapping
    public String redirectToContasList() {
        return "redirect:/contas/list";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getFormForEdit(@PathVariable(name = "id") Integer id, ModelAndView modelAndView) {
        modelAndView.setViewName("/contas/form");
        // A chamada ao findByID do ContaService aqui é adequada para edição (retorna Conta ou null)
        Conta conta = contaService.findByID(id);
        modelAndView.addObject("conta", conta);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public String deleteConta(@PathVariable(name = "id") Integer id) {
        contaService.deleteById(id);
        return "redirect:/contas/list";
    }

    @GetMapping("/{id}/extrato")
    public String exibirExtrato(
            @PathVariable("id") Integer id,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            Model model) { // Model do Spring, agora importado corretamente

        // Usando o método findById(Integer id) que retorna Optional<Conta>
        // Este método deve estar no seu ContaService e retornar Optional
        Optional<Conta> contaOpt = contaService.findById(id);

        if (contaOpt.isEmpty()) {
            return "redirect:/contas/list?error=contaNaoEncontrada";
        }
        Conta conta = contaOpt.get(); // Obtém a conta do Optional

        LocalDate dataInicialFiltro = null;
        LocalDate dataFinalFiltro = null;

        // Tenta parsear dataInicio e dataFim se não forem nulos ou vazios
        try {
            if (dataInicio != null && !dataInicio.trim().isEmpty()) {
                dataInicialFiltro = LocalDate.parse(dataInicio);
            }
            if (dataFim != null && !dataFim.trim().isEmpty()) {
                dataFinalFiltro = LocalDate.parse(dataFim);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Opcional: Adicionar uma mensagem de erro ao modelo para exibir na tela
            // model.addAttribute("erroData", "Formato de data inválido. Use AAAA-MM-DD.");
        }

        // Se o mês for selecionado, ele tem precedência sobre as datas de início/fim
        if (mes != null) {
            YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), mes);
            dataInicialFiltro = yearMonth.atDay(1);
            dataFinalFiltro = yearMonth.atEndOfMonth();
        }

        // 1. Calcular Saldo Inicial (transações antes do período filtrado)
        BigDecimal saldoInicial = BigDecimal.ZERO;
        List<Transacao> transacoesAnteriores;

        // *** CORREÇÃO AQUI: Não redeclarar transacaoRepository ***
        if (dataInicialFiltro != null) {
            // Usa a instância de transacaoRepository que foi @Autowired no topo da classe
            transacoesAnteriores = transacaoRepository.findByContaAndDataBefore(conta, dataInicialFiltro);
        } else {
            transacoesAnteriores = Collections.emptyList();
        }

        for (Transacao t : transacoesAnteriores) {
            if (t.getMovimento() == TipoMovimento.CREDITO) {
                saldoInicial = saldoInicial.add(t.getValor());
            } else {
                saldoInicial = saldoInicial.subtract(t.getValor());
            }
        }

        // 2. Obter transações do período filtrado
        List<Transacao> transacoesPeriodo;
        if (dataInicialFiltro != null && dataFinalFiltro != null) {
            transacoesPeriodo = transacaoRepository.findByContaAndDataBetween(conta, dataInicialFiltro, dataFinalFiltro);
        } else {
            // Se nenhum filtro de data/mês for aplicado, pega todas as transações da conta
            transacoesPeriodo = transacaoRepository.findByConta(conta);
        }

        // Ordenar as transações por data para o cálculo correto do saldo parcial
        transacoesPeriodo.sort(Comparator.comparing(Transacao::getData));

        // 3. Calcular Saldo Parcial para cada transação e Saldo Final
        BigDecimal saldoCorrente = saldoInicial;
        for (Transacao t : transacoesPeriodo) {
            if (t.getMovimento() == TipoMovimento.CREDITO) {
                saldoCorrente = saldoCorrente.add(t.getValor());
            } else {
                saldoCorrente = saldoCorrente.subtract(t.getValor());
            }
            t.setSaldoParcial(saldoCorrente); // Requer o campo @Transient saldoParcial em Transacao
        }

        BigDecimal saldoFinal = saldoCorrente;

        // Adicionar os dados ao modelo para a view
        model.addAttribute("conta", conta);
        model.addAttribute("transacoes", transacoesPeriodo);
        model.addAttribute("saldoInicial", saldoInicial);
        model.addAttribute("saldoFinal", saldoFinal);
        model.addAttribute("mes", mes); // Para manter o mês selecionado no formulário
        model.addAttribute("dataInicio", dataInicio); // Para manter a data de início no formulário
        model.addAttribute("dataFim", dataFim);     // Para manter a data de fim no formulário

        return "contas/extrato"; // Nome do seu template Thymeleaf (crie um arquivo extrato-conta.html na pasta templates)
    }

}