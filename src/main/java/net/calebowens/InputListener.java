package net.calebowens;

import com.nthbyte.dialogue.event.ValidateInputEvent;
import com.nthbyte.dialogue.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * The class that listens to player input. Developers can validate the input value and it's format.
 */
public class InputListener implements Listener {

    // Here, we listen for input from a prompt with a specific id.
    @EventHandler
    public void onInputValidate(ValidateInputEvent e){

        Player player = e.getResponder();
        String promptId = e.getPromptId();
        // We only want to listen for a specific prompt.
        if(!promptId.equalsIgnoreCase("choose_a_number")){
            return;
        }

        // We know that we can safely turn it into an integer because we made the InputPromptType a INTEGER.
        // See DialogueCommand#createChooseNumberDialogue
        int input = Integer.parseInt(e.getInput());
        if(input < 1 || input > 10){
            e.setValidInput(false);
            player.sendMessage(Utils.tr("This is not a valid response!"));
        }

    }

}
