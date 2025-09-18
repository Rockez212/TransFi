package project.transfi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.transfi.command.TransferRequest;
import project.transfi.service.AtmService;

@RestController
@RequestMapping("/user/atm")
@RequiredArgsConstructor
public class AtmController {
    private final AtmService atmService;

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody @Valid TransferRequest request) {
        atmService.withdraw(request);
        return ResponseEntity.ok("Withdraw Successful");
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody @Valid TransferRequest request) {
        atmService.deposit(request);
        return ResponseEntity.ok("Deposit Successful");
    }
}
