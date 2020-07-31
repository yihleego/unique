package io.leego.unique.server.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Yihleego
 */
@Controller
public class ForwardController {

    @GetMapping("unique")
    public String status() {
        return "/unique/console.html";
    }

}
