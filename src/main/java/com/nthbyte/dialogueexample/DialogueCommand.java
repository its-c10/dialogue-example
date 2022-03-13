package com.nthbyte.dialogueexample;

import com.nthbyte.dialogue.*;
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
                        .setOnReceiveInputAction( (p, input) -> {
                            sender.sendMessage("You are " + input + " years old!");
                        })
                )
                // Sequence to exit the dialogue.
                .setEscapeSequence("exit")
                // Code that runs when the dialogue ends.
                .setEndAction(cause -> {
                    sender.sendMessage(ChatColor.BLUE + "This message is sent when the dialogue ends!");
                })
                .build();
        }else if(firstArg.equalsIgnoreCase("number")){
            dialogue = createChooseNumberDialogue(sender);
        }

        if(dialogue != null){
            DialogueAPI.startDialogue(player, dialogue);
        }

        return false;
    }

    private Dialogue createChooseNumberDialogue(CommandSender sender){
        return new Dialogue.Builder()
            .addPrompt(
                new Prompt.Builder()
                    .setId("choose_a_number")
                    .setType(PromptInputType.INTEGER)
                    .setText("&eChoose a number between 1 and 10")
                    .setOnReceiveInputAction( (responder, input) -> {
                        sender.sendMessage(Utils.tr("&aYour number was " + input));
                    })
            )
        .build();
    }

    private Dialogue createTriviaDialogue(CommandSender sender){
        return new Dialogue.Builder()
            .addPrompt(
                new Prompt.Builder()
                    .setType(PromptInputType.LETTERS)
                    .setText("&eWhat is the nearest planet to the sun?")
                    .setOnReceiveInputAction( (player, input) -> {
                        if(input.equalsIgnoreCase("Mercury")){
                            sender.sendMessage(ChatColor.GREEN + "Your answer is correct!");
                        }else{
                            sender.sendMessage(ChatColor.RED + "Incorrect!");
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
                    .setOnReceiveInputAction( (responder, input) -> {
                        int number = Integer.parseInt(input);
                        if(number == 12){
                            sender.sendMessage(ChatColor.GREEN + "Your answer is correct!");
                        }else{
                            sender.sendMessage(ChatColor.RED + "Incorrect!");
                        }
                    })
            )
            .setEndAction(cause -> {
                sender.sendMessage(ChatColor.BLUE + "This message is sent when the dialogue ends!");
            })
            .setEscapeSequence("exit")
            .build();
    }

}
