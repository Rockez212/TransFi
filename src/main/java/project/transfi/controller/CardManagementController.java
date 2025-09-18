package project.transfi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.transfi.command.CreateCardCommand;
import project.transfi.dto.CardDto;
import project.transfi.service.CardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/cards")
public class CardManagementController {

    private final CardService cardService;


    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody @Valid CreateCardCommand command) {
        cardService.create(command);
        return ResponseEntity.ok("Card created");
    }


    @GetMapping()
    public ResponseEntity<List<CardDto>> getCards() {
        return ResponseEntity.ok(cardService.getCards());
    }
}
