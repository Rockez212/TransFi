package project.transfi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.transfi.command.TransferRequest;
import project.transfi.service.TransferService;

@RestController
@RequestMapping("/user/transfer")
@RequiredArgsConstructor
public class TransferManagementController {


    private final TransferService transferService;

    @PostMapping()
    public ResponseEntity<String> transferCard(@RequestBody @Valid TransferRequest request) {
        transferService.transferTo(request);
        return ResponseEntity.ok("Transfer successful");
    }


}
