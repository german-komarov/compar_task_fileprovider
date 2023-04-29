package az.compar.fileprovider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AppController extends BaseController {
    @GetMapping("login")
    public String loginPage() {
        return "login";
    }
}
