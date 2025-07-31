package ph.id.printerhouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import jakarta.validation.Valid;
import ph.id.printerhouse.model.Servizi;
import ph.id.printerhouse.repository.ServiziRepository;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private ServiziRepository serviziRepository;

    @GetMapping
    public String index(Model model, @RequestParam(name = "keyword", required = false) String name) {
        List<Servizi> listaservizi;
        if (name != null && !name.isBlank()) {
            listaservizi = serviziRepository.findByNameContainingIgnoreCase(name);
        } else {
            listaservizi = serviziRepository.findAll();
        }
        model.addAttribute("servizi", listaservizi);
        return "index";
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
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteServizio(@PathVariable("id") Integer id) {
        serviziRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editServizio(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("servizio", serviziRepository.findById(id).get());
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateServizio(@Valid @ModelAttribute("servizio") Servizi formServizi,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        serviziRepository.save(formServizi);
        return "redirect:/";
    }

    @GetMapping("/registratori")
    public String indexRegistratori() {
        return "subpages/registratoriCassa";
    }

    @GetMapping("/multifunzione")
    public String indexStampanti() {
        return "subpages/multifunzioni";
    }

    @GetMapping("/riciclo")
    public String indexRiciclo() {
        return "subpages/riciclo";
    }

    @GetMapping("/sito")
    public String indexSito() {
        return "subpages/siti";
    }

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/contattaci")
    public String indexContatto() {
        return "subpages/contatto";
    }

    @PostMapping("/contattaci")
    public String inviaRichiesta(
            @RequestParam String servizio,
            @RequestParam String nome,
            @RequestParam String cognome,
            @RequestParam String email,
            @RequestParam(required = false) String azienda,
            @RequestParam String messaggio,
            @RequestParam(name = "g-recaptcha-response") String recaptchaResponse, // Riceve token reCAPTCHA
            Model model) {

        // 🔑 La tua Secret Key (puoi tenerla anche qui per test, ma poi mettila in
        // application.properties!)
        String secretKey = "6LehhX4rAAAAAOPWAMKbq7kwtvozExoHHezJpRQG";

        // Verifica con Google reCAPTCHA
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.google.com/recaptcha/api/siteverify";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", secretKey);
        params.add("response", recaptchaResponse);

        ResponseEntity<String> recaptchaVerifyResponse = restTemplate.postForEntity(url, params, String.class);

        if (!recaptchaVerifyResponse.getBody().contains("\"success\": true")) {
            model.addAttribute("error", "Verifica CAPTCHA fallita. Riprova.");
            return "subpages/contatto"; // Ritorna alla pagina con messaggio di errore
        }

        // ✅ Se CAPTCHA OK → invia email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("printerhouse@gmail.com");
        message.setSubject("Richiesta Preventivo - " + servizio);
        message.setText("Nome: " + nome +
                "\nCognome: " + cognome +
                "\nEmail: " + email +
                "\nAzienda: " + azienda +
                "\nServizio richiesto: " + servizio +
                "\n\nMessaggio:\n" + messaggio);

        mailSender.send(message);

        return "redirect:/grazie"; // Pagina di conferma
    }

    @GetMapping("/grazie")
    public String paginaGrazie() {
        return "grazie"; // Thymeleaf view: grazie.html
    }
}
