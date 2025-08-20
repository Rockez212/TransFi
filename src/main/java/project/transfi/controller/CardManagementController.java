package project.transfi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.transfi.command.CreateCardCommand;
import project.transfi.service.CardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardManagementController {

    private final CardService cardService;


    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CreateCardCommand command) {
        cardService.create(command);
        return ResponseEntity.ok("Card created");
    }
}
