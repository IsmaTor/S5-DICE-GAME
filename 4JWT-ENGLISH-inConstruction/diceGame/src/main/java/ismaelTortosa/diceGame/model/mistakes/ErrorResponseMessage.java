package ismaelTortosa.diceGame.model.mistakes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseMessage {

    private int statusCode;
    private String message, description;

}
