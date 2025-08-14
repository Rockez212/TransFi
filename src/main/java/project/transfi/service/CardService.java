package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.transfi.command.CreateCardCommand;
import project.transfi.entity.Card;
import project.transfi.entity.User;

@Service
@RequiredArgsConstructor
public class CardService {


    private final AuthService authService;

    public void create(CreateCardCommand command) {

    }
}
