package project.transfi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.transfi.command.CardDetailsCommand;
import project.transfi.command.CreateCardCommand;
import project.transfi.command.TransferToCommand;
import project.transfi.entity.Card;
import project.transfi.service.CardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/cards")
public class CardManagementController {

    private final CardService cardService;


    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CreateCardCommand command) {
        cardService.create(command);
        return ResponseEntity.ok("Card created");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferToCommand transferCommand, CardDetailsCommand detailsCommand) {
        cardService.transferTo(transferCommand, detailsCommand);
        return ResponseEntity.ok("Successfully transferred");
    }

    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }
}
