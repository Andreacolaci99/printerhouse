package ph.id.printerhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/printerhouse/home")
public class PreventivoController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/contattaci")
    public String indexContatto() {
        return "/subpages/contatto";
    }

    @PostMapping("/contattaci")
    public String inviaRichiesta(@RequestParam String servizio,
            @RequestParam String nome,
            @RequestParam String cognome,
            @RequestParam String email,
            @RequestParam(required = false) String azienda,
            @RequestParam String messaggio) {

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

        return "redirect:/printerhouse/home/grazie"; // Pagina di conferma
    }

    @GetMapping("/grazie")
    public String paginaGrazie() {
        return "grazie"; // Thymeleaf view: grazie.html
    }

}
