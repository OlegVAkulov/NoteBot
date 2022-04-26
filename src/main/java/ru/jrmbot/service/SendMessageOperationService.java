package ru.jrmbot.service;

import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

import static java.lang.Math.toIntExact;
import static java.util.Arrays.asList;
import static ru.jrmbot.constant.VarConstant.*;

public class SendMessageOperationService {
    private final String GREETING_MESSAGE = "Привет, приступим к планированию. Нажмите \"Начать планирование\"";
    private final String PLANNING_MESSAGE = "Введите задачи, после ввода нажмите \"Закончить планирование\"";
    private final String END_PLANNING_MESSAGE = "Планирование окончено, для просмотра нажмите \"Показать дела\". Новые задачи можно добавить кнопкой \"Начать планирование\"";
    private final String DELETE_PLANNING_MESSAGE = "Все задачи удалены. Новые задачи можно добавить кнопкой \"Начать планирование\"";
    private final String INSTRUCTIONS = "Хотите прочесть инструкцию?";

    private final ButtonService buttonService = new ButtonService();

    public SendMessage createGreetingInformation(Update update) {
        SendMessage message = createSimpleMessage(update, GREETING_MESSAGE);
        ReplyKeyboardMarkup keyboardMarkup = buttonService.setButton(buttonService.createButtons(
                asList(START_PLANNING, END_PLANNING, SHOW_DEALS, DELETE_DEALS)));
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    public SendMessage createPlanningMessage(Update update) {
        return createSimpleMessage(update, PLANNING_MESSAGE);
    }

    public SendMessage createEndPlanningMessage(Update update) {
        return createSimpleMessage(update, END_PLANNING_MESSAGE);
    }
    public SendMessage createDeletePlanningMessage(Update update) {
        return createSimpleMessage(update, DELETE_PLANNING_MESSAGE);
    }

    public SendMessage createSimpleMessage(Update update, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText(message);
        return sendMessage;
    }

    public SendMessage createSimpleMessage(Update update, List<String> messages) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        StringBuilder message = new StringBuilder();
        for (String s : messages) {
            message.append(s + "\n");
        }
        sendMessage.setText(message.toString());
        return sendMessage;
    }


    public SendMessage createInstructionMessage(Update update) {
        SendMessage sendMessage = createSimpleMessage(update, INSTRUCTIONS);
        InlineKeyboardMarkup replyKeyboardMarkup = buttonService.setInlineKeyboard
                (buttonService.createInLineButton(YES));
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    public EditMessageText createEditMessage(Update update, String instruction) {
        EditMessageText editMessageText = new EditMessageText();
        long mesId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(toIntExact(mesId));
        editMessageText.setText(instruction);
        return editMessageText;

    }
}
