package com.example.Polinom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private PolinomRepository polynomialRepository;
    @Autowired
    private PolinomService polinomService;

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("polynomial", new Polinom());
        return "polynomial_form";
    }

    @PostMapping("/submit")
    public String submitPolynomial(@ModelAttribute Polinom polynomial, Model model) {
        polynomialRepository.save(polynomial);
        model.addAttribute("polynomial", polynomial);
        return "check_root";
    }


    @GetMapping("/check")
    public String checkRoot(@RequestParam Long id, @RequestParam double root, Model model) {
        Polinom polynomial = polynomialRepository.findById(id).orElse(null);

        double result = polinomService.ifRoot(polynomial, root);

        model.addAttribute("polynomial", polynomial);

        // Set the message and its type based on the result
        if (result == 0) {
            model.addAttribute("message", "Числото " + root + " е корен на полинома.");
            model.addAttribute("messageType", "success"); // Indicate success for styling
        } else {
            model.addAttribute("message", "Числото " + root + " не е корен на полинома. Остатък: " + result);
            model.addAttribute("messageType", "error"); // Indicate error for styling
        }

        return "check_root";
    }

    @GetMapping("/roots")
    public String showRoots(@RequestParam Long id, Model model) {
        Polinom polynomial = polynomialRepository.findById(id).orElse(null);
        if (polynomial != null) {
            List<Double> roots = polinomService.theRoots(polynomial);
            model.addAttribute("polynomial", polynomial);
            model.addAttribute("roots", roots);
        } else {
            model.addAttribute("message", "Polynomial not found.");
            model.addAttribute("messageType", "error");
        }
        return "roots";
    }

}
