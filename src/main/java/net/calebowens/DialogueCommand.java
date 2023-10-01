package net.calebowens;

import com.nthbyte.dialogue.*;
import com.nthbyte.dialogue.action.Action;
import com.nthbyte.dialogue.action.context.ActionContext;
import com.nthbyte.dialogue.action.context.LocationContext;
import com.nthbyte.dialogue.input.PromptInputType;
import com.nthbyte.dialogue.util.Utils;
import org.apache.commons.lang.CharUtils;
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
            dialogue = createAgeDialogue();
        }else if(firstArg.equalsIgnoreCase("number")){
            dialogue = createChooseNumberDialogue();
        }else if(firstArg.equalsIgnoreCase("tp")){
            dialogue = createTeleportDialogue();
        }else if(firstArg.equalsIgnoreCase("story")){
            dialogue = createStoryDialogue();
        }

        if(dialogue != null){
            DialogueAPI.startDialogue(player, dialogue);
        }

        return false;
    }

    private Dialogue createAgeDialogue(){
        return new Dialogue.Builder()
            .addPrompt(
                new Prompt.Builder()
                    .setText("&eWhat is your age?")
                    .setType(PromptInputType.INTEGER)
                    // Code that runs when the plugin receives the input.
                    .addReceiveInputAction( (context, input) -> {
                        context.getResponder().sendMessage("You are " + input + " years old!");
                    })
            )
            // Sequences to exit the dialogue.
            .setEscapeSequences("exit")
            // Code that runs when the dialogue ends.
            // Whether the prompt gets repeated when you give invalid input.
            .setRepeatPrompt(true)
            .build();
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
            .setEscapeSequences("exit")
            .build();
    }

    private Dialogue createStoryDialogue(){
        return new Dialogue.Builder()
            .addPrompt(
                new Prompt.Builder()
                    .addText("&bExposition&f occurs in the beginning of the story.")
                    .addText("&fIt gives background story on characters and setting.")
                    .addText("&fWhat will the exposition of your story be?")
                    .addReceiveInputAction(Action.STORE_INPUT, new ActionContext<>("exposition"))
                    .setDelay(20)
            )
            .addPrompt(
                new Prompt.Builder()
                    .addText("&bRising action&f is the moment that pretty much sets your story into action.")
                    .addText("&fIt's the portion of the roller coaster where you're climbing up to the peak.")
                    .addText("&fWhat will the rising action of your story be?")
                    .addReceiveInputAction(Action.STORE_INPUT, new ActionContext<>("rising_action"))
                    .setDelay(20)
            )
            .addPrompt(
                new Prompt.Builder()
                    .addText("&fThe &bclimax&f is the peak of tension.")
                    .addText("&fIt's the point in the story that everything changes. You're at the top of the rollercoaster")
                    .addText("&fand you're about to come down.")
                    .addText("&fWhat will the climax of your story be?")
                    .addReceiveInputAction(Action.STORE_INPUT, new ActionContext<>("climax"))
                    .setDelay(20)
            )
            .addPrompt(
                new Prompt.Builder()
                    .addText("&fThe &bfalling action&f is the point of the story where conflicts")
                    .addText("&fare being resolved.")
                    .addText("&fWhat will the &bfalling&f action of your story be?")
                    .addReceiveInputAction(Action.STORE_INPUT, new ActionContext<>("falling_action"))
                    .setDelay(20)
            )
            .addPrompt(
                new Prompt.Builder()
                    .addText("&fWe're at the last part of the story!")
                    .addText("&fThe &bresolution&f is the end of your story.")
                    .addText("&fIt's time to bring your story to a closing")
                    .addText("&fWhat will the resolution of your story be?")
                    .addReceiveInputAction(Action.STORE_INPUT, new ActionContext<>("resolution"))
                    .setDelay(20)
            )
            .addEndAction( (context, endCause) -> {
                if(endCause == DialogueEndCause.NO_MORE_PROMPTS){
                    String exposition = context.getStoredInput("exposition");
                    String risingAction = context.getStoredInput("rising_action");
                    String climax = context.getStoredInput("climax");
                    String fallingAction = context.getStoredInput("falling_action");
                    String resolution = context.getStoredInput("resolution");
                    Player player = context.getResponder();
                    player.sendMessage(Utils.tr("&bHere's your story:"));
                    player.sendMessage(exposition, risingAction, climax, fallingAction, resolution);
                }
            })
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
                    .setRetryLimit(3)
                    .stopDialogueUponFailure()
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
                    .setRetryLimit(3)
                    .stopDialogueUponFailure()
                    .setText("&eHow many inches are in a foot?")
                    .setOnValidateInputAction( input -> {
                        int number = Integer.parseInt(input);
                        if(number == 12){
                            sender.sendMessage(ChatColor.GREEN + "Your answer is correct!");
                            return true;
                        }else{
                            sender.sendMessage(ChatColor.RED + "Incorrect!");
                            return false;
                        }
                    })
            )
            .addEndAction( (context, cause) -> {
                context.getResponder().sendMessage(ChatColor.BLUE + "This message is sent when the dialogue ends!");
            })
            .setEscapeSequences("exit")
            .build();
    }

}
