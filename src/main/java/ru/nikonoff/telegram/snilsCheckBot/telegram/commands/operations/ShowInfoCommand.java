package ru.nikonoff.telegram.snilsCheckBot.telegram.commands.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.nikonoff.telegram.snilsCheckBot.Utils;

public class ShowInfoCommand extends OperationCommand {
    private Logger logger = LoggerFactory.getLogger(ShowInfoCommand.class);

    public ShowInfoCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));
        sendAnswer(absSender, chat.getId(),
                this.getCommandIdentifier(), userName, "ТЕСТ ТЕСТ ТЕСТ");
        logger.debug(String.format("Пользователь %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }
}
