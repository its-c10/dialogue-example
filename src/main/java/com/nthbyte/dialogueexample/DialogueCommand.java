package com.nthbyte.dialogueexample;

import com.nthbyte.dialogue.*;
import com.nthbyte.dialogue.action.context.ActionContext;
import com.nthbyte.dialogue.action.context.LocationContext;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class DialogueCommand implements CommandExecutor {

    private final List<String> VALID_PLANETS = Arrays.asList("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player) || args.length == 0) return false;

        Player player = (Player) sender;
        Dialogue dialogue = null;
        String firstArg = args[0];
        if(firstArg.equalsIgnoreCase("trivia")){
            dialogue = createTriviaDialogue(sender);
        }else if(firstArg.equalsIgnoreCase("age")){
            dialogue = new Dialogue.Builder()
                .addPrompt(
                    new Prompt.Builder()
                        .setText("&eWhat is your age?")
                        .setType(PromptInputType.INTEGER)
                        // Code that runs when the plugin receives the input.
                        .addReceiveInputAction( (context, input) -> {
                            context.getResponder().sendMessage("You are " + input + " years old!");
                        })
                )
                // Sequence to exit the dialogue.
                .setEscapeSequence("exit")
                // Code that runs when the dialogue ends.
                // Whether the prompt gets repeated when you give invalid input.
                .setRepeatPrompt(true)
                .build();
        }else if(firstArg.equalsIgnoreCase("number")){
            dialogue = createChooseNumberDialogue();
        }else if(firstArg.equalsIgnoreCase("tp")){
            dialogue = createTeleportDialogue();
        }

        if(dialogue != null){
            DialogueAPI.startDialogue(player, dialogue);
        }

        return false;
    }


    private Dialogue createTeleportDialogue(){

        /*
            Utilizes the default actions TELEPORT and STORE_INPUT.
         */
        return new Dialogue.Builder()
            .addPrompt(
                new Prompt.Builder()
                    .setType(PromptInputType.DECIMAL)
                    .setText("&eWhat do you want your X to be?")
                    .addReceiveInputAction(Action.STORE_INPUT, new ActionContext<>("x"))
                    .addReceiveInputAction(Action.MESSAGE, new ActionContext<>("&eYou will teleport to the X coordinate of &f%x%"))
            )
            .addPrompt(
                new Prompt.Builder()
                    .setType(PromptInputType.DECIMAL)
                    .setText("&eWhat do you want your Y to be?")
                    .addReceiveInputAction(Action.STORE_INPUT, new ActionContext<>("y"))
                    .addReceiveInputAction(Action.MESSAGE, new ActionContext<>("&eYou will teleport to the Y coordinate of &f%y%"))
            )
            .addPrompt(
                new Prompt.Builder()
                    .setType(PromptInputType.DECIMAL)
                    .setText("&eWhat do you want your Z to be?")
                    .addReceiveInputAction(Action.STORE_INPUT, new ActionContext<>("z"))
                    .addReceiveInputAction(Action.MESSAGE, new ActionContext<>("&eYou will teleport to the Z coordinate of &f%z%"))
            )
            .addEndAction(Action.TELEPORT, new LocationContext())
            .addEndAction(Action.MESSAGE, new ActionContext<>("&aYou have been teleported!"))
            .setEscapeSequence("exit")
            .build();
    }

    private Dialogue createChooseNumberDialogue(){
        return new Dialogue.Builder()
            .addPrompt(
                new Prompt.Builder()
                    .setId("choose_a_number")
                    .setType(PromptInputType.INTEGER)
                    .setText("&eChoose a number between 1 and 10")
                    .addReceiveInputAction( (context, input) -> {
                        context.getResponder().sendMessage(Utils.tr("&aYour number was " + input));
                    })
            )
            .addEndAction( (context, cause) -> {
                context.getResponder().sendMessage(ChatColor.BLUE + "This message is sent when the dialogue ends!");
            })
            .setRepeatPrompt(false)
        .build();
    }

    private Dialogue createTriviaDialogue(CommandSender sender){
        return new Dialogue.Builder()
            .addPrompt(
                new Prompt.Builder()
                    .setType(PromptInputType.LETTERS)
                    .setText("&eWhat is the nearest planet to the sun?")
                    .addReceiveInputAction( (context, input) -> {
                        if(input.equalsIgnoreCase("Mercury")){
                            context.getResponder().sendMessage(ChatColor.GREEN + "Your answer is correct!");
                        }else{
                            context.getResponder().sendMessage(ChatColor.RED + "Incorrect!");
                        }
                    })
                    // This method allows you to introduce your own validation for input.
                    .setOnValidateInputAction(input -> {
                        String properInput = StringUtils.capitalize(input.toLowerCase());
                        if(!VALID_PLANETS.contains(properInput)){
                            sender.sendMessage(ChatColor.RED + "This is not a valid planet!");
                            return false;
                        }
                        return true;
                    })
            )
            .addPrompt(
                new Prompt.Builder()
                    .setType(PromptInputType.INTEGER)
                    .setText("&eHow many inches are in a foot?")
                    .addReceiveInputAction( (context, input) -> {
                        int number = Integer.parseInt(input);
                        if(number == 12){
                            context.getResponder().sendMessage(ChatColor.GREEN + "Your answer is correct!");
                        }else{
                            context.getResponder().sendMessage(ChatColor.RED + "Incorrect!");
                        }
                    })
            )
            .addEndAction( (context, cause) -> {
                context.getResponder().sendMessage(ChatColor.BLUE + "This message is sent when the dialogue ends!");
            })
            .setEscapeSequence("exit")
            .build();
    }

}
