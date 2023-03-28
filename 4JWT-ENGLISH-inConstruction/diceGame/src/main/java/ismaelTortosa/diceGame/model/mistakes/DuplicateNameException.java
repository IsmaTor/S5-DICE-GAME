package ismaelTortosa.diceGame.model.mistakes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DuplicateNameException extends RuntimeException {

    String message;
}
