package ismaelTortosa.diceGame.model.confirmations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Confirmation {
    private String confirmed;

    public boolean isConfirmed(){
        return "Yes".equalsIgnoreCase(confirmed);
    }

}
