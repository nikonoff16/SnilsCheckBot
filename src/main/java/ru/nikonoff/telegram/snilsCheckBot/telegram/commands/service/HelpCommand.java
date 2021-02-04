package ru.nikonoff.telegram.snilsCheckBot.telegram.commands.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.nikonoff.telegram.snilsCheckBot.Utils;

/**
 * Команда "Помощь"
 */
public class HelpCommand extends ServiceCommand {
    private Logger logger = LoggerFactory.getLogger(HelpCommand.class);

    public HelpCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Привет, я - простой бот для проверки СНИЛС на корректность и генерации псевдо-СНИЛС!\n\n" +
                        "С моей помощью можно легко проверить корректность контрольных сумм СНИЛСов,  " +
                        "исключая тем самым большинство ошибок переписывания номеров от руки.\n" +
                        "Еще здесь можно создать СНИЛС-подобные числа в неких тестовых целях, если это требуется.\n\n" +
                        "❗*Список команд*\n/settings - заглушка, на данный момент не используется\n" +
                        "/showinfo XXX-XXX-XXX YY' - получить полную информацию о СНИЛС\n" +
                        "/generate - создать валидный псевдо-СНИЛС\n/help - помощь\n\n" +
                        "Данные, переданные мне, не собираются, не сличаются ни с какой БД, и не передаются третьим лицам.\n\n " +
                        "Чтобы проверить СНИЛС, просто введите его и нажмите Ввод.\n" +
                        "Формат СНИЛС не имеет значения. Номера '12345678964' и '123-456-789 64' равнозначны для программы.\n" +
                        "Приятного использования\uD83D\uDE42");
        logger.debug(String.format("Пользователь %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }
}