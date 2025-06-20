package ph.id.printerhouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import ph.id.printerhouse.model.Servizi;
import ph.id.printerhouse.repository.ServiziRepository;

@Controller
@RequestMapping("/printerhouse/home")
public class IndexController {

    @Autowired
    private ServiziRepository serviziRepository;

    @GetMapping()
    public String index(Model model, @RequestParam(name = "keyword", required = false) String name) {
        List<Servizi> listaservizi;
        if (name != null && !name.isBlank()) {
            listaservizi = serviziRepository.findByNameContainingIgnoreCase(name);
        } else {
            listaservizi = serviziRepository.findAll();
        }
        model.addAttribute("servizi", listaservizi);
        return "/index";
    }

    @GetMapping("/creaservizio")
    public String getFormServizio(Model model) {
        model.addAttribute("servizio", new Servizi());
        return "create";
    }

    @PostMapping("/creaservizio")
    public String postFormServizio(@Valid @ModelAttribute("servizio") Servizi formServizi,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("servizio", formServizi);
            return "create";
        }
        serviziRepository.save(formServizi);
        return "redirect:/printerhouse/home";
    }

    @PostMapping("/delete/{id}")
    public String deleteServizio(@PathVariable("id") Integer id) {

        serviziRepository.deleteById(id);
        return "redirect:/printerhouse/home";
    }

    @GetMapping("/edit/{id}")
    public String editServizio(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("servizio", serviziRepository.findById(id).get());
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateServizio(@Valid @ModelAttribute("servizio") Servizi formServizi, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        serviziRepository.save(formServizi);
        return "redirect:/printerhouse/home";
    }

    @GetMapping("/registratori")
    public String indexRegistratori() {
        return "/subpages/registratoriCassa";
    }

    @GetMapping("/multifunzione")
    public String indexStampanti() {
        return "/subpages/multifunzioni";
    }
    
    @GetMapping("/riciclo")
    public String indexRiciclo() {
        return "/subpages/riciclo";
    }
    
    @GetMapping("/sito")
    public String indexSito() {
        return "/subpages/siti";
    }
  
}
