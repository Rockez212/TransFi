package project.transfi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.transfi.command.CreateBankAccountCommand;
import project.transfi.service.BankAccountService;

@RestController
@RequestMapping("/user/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping("/create")
    public ResponseEntity<String> addBankAccount(CreateBankAccountCommand command) {
        bankAccountService.create(command);
        return ResponseEntity.ok("Bank account created");
    }

}
