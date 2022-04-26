package ru.jrmbot.core;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jrmbot.service.ButtonService;
import ru.jrmbot.service.SendMessageOperationService;
import ru.jrmbot.store.HashMapStore;

import java.time.LocalDate;

import static java.util.Arrays.asList;
import static ru.jrmbot.constant.VarConstant.*;

public class CoreBot extends TelegramLongPollingBot {
    private SendMessageOperationService service = new SendMessageOperationService();
    private HashMapStore store = new HashMapStore();
    private boolean startPlaning;

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case START:
                    execute(service.createInstructionMessage(update));
                    execute(service.createGreetingInformation(update));
                    break;
                case START_PLANNING:
                    startPlaning = true;
                    execute(service.createPlanningMessage(update));
                    break;
                case END_PLANNING:
                    startPlaning = false;
                    execute(service.createEndPlanningMessage(update));
                    break;
                case SHOW_DEALS:
                    if (!startPlaning) {

                        execute(service.createSimpleMessage(update, store.selectAll(LocalDate.now())));
                    }
                    break;
                case DELETE_DEALS:
                    store = new HashMapStore();
                    execute(service.createDeletePlanningMessage(update));
                default:
                    if (startPlaning) {
                        store.save(LocalDate.now(), update.getMessage().getText());
                    }
            }
        }
        if (update.hasCallbackQuery()) {
            String instruction = "Бот для формирования списка дел на день. Чтобы воспользоваться ботом следуйте за инструкцией. " +
                    "Новые дела можно добавить кнопкой \"Начать планирование\"";
            String callDate = update.getCallbackQuery().getData();
            switch (callDate) {
                case YES:
                    EditMessageText text = service.createEditMessage(update, instruction);
                    try {
                        execute(text);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "PRIME48_bot";
    }

    @Override
    public String getBotToken() {
        return "5252974991:AAFnjk_q1EP5y-T5L_xl3PNpH-Wj4kvtLb4";
    }
}
