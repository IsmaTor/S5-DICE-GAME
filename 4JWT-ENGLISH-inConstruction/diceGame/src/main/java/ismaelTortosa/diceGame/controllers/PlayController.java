package ismaelTortosa.diceGame.controllers;

import ismaelTortosa.diceGame.model.services.IPlayServicesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@RequestMapping("/players")
public class PlayController {

    private static final Logger LOGGER = Logger.getLogger(PlayController.class.getName());

    @Autowired
    private IPlayServicesDAO playServices;
}
