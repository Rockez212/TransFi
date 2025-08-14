package project.transfi.command;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateCardCommand {
    public Long cardType;
}
