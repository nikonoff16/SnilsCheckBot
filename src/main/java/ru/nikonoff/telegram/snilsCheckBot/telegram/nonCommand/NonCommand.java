package ru.nikonoff.telegram.snilsCheckBot.telegram.nonCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nikonoff.telegram.snilsCheckBot.SnilsCheck;
import ru.nikonoff.telegram.snilsCheckBot.exceptions.IllegalSettingsException;
import ru.nikonoff.telegram.snilsCheckBot.telegram.Bot;

/**
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 */
public class NonCommand {
    private Logger logger = LoggerFactory.getLogger(NonCommand.class);

    public String nonCommandExecute(Long chatId, String userName, String text) {
        logger.debug(String.format("Пользователь %s. Начата обработка сообщения \"%s\", не являющегося командой",
                userName, text));

        Settings settings;
        String answer;
        try {
            logger.debug(String.format("Пользователь %s. Пробуем создать объект настроек из сообщения \"%s\"",
                    userName, text));
            // Унаследованный код. Понять, как работает, и удалить
            settings = createSettings(text);
            saveUserSettings(chatId, settings);

            // Обработка простого запроса проверки СНИЛС
            SnilsCheck snils = new SnilsCheck();
            snils.setSnils(text);
            Boolean checkResult = snils.getSnilsValid();

            logger.debug(String.format("Пользователь %s. СНИЛС номер \"%s\" проверен и сохранен в объекте",
                    userName, text));
            if (checkResult) {
                answer = text + " - СНИЛС валиден!";
            }
            else {
                answer = text + " не прошел проверку";
            }
        } catch (Exception e) {
            logger.debug(String.format("Пользователь %s. Не удалось создать объект настроек из сообщения \"%s\". " +
                    "%s. %s", userName, text, e.getClass().getSimpleName(), e.getMessage()));
            answer = "Простите, я не понимаю Вас. Похоже, что Вы ввели сообщение, не соответствующее формату, или " +
                    "использовали слишком большие числа\n\n" +
                    "Возможно, Вам поможет /help";
        }

        logger.debug(String.format("Пользователь %s. Завершена обработка сообщения \"%s\", не являющегося командой",
                    userName, text));
        return answer;
    }

    /**
     * Создание настроек из полученного пользователем сообщения
     * @param text текст сообщения
     * @throws IllegalArgumentException пробрасывается, если сообщение пользователя не соответствует формату
     */
    private Settings createSettings(String text) throws IllegalArgumentException {
        //отсекаем файлы, стикеры, гифки и прочий мусор
        if (text == null) {
            throw new IllegalArgumentException("Сообщение не является текстом");
        }
        text = text.replaceAll("-", "")//избавляемся от отрицательных чисел (умники найдутся)
                .replaceAll(", ", ",")//меняем ошибочный разделитель "запятая+пробел" на запятую
                .replaceAll(" ", ",");//меняем разделитель-пробел на запятую
        String[] parameters = text.split(",");
        if (parameters.length != 3) {
            throw new IllegalArgumentException(String.format("Не удалось разбить сообщение \"%s\" на 3 составляющих",
                    text));
        }
        int min = Integer.parseInt(parameters[0]);
        int max = Integer.parseInt(parameters[1]);
        int listCount = Integer.parseInt(parameters[2]);

        validateSettings(min, max, listCount);
        return new Settings(min, max, listCount);
    }

    /**
     * Валидация настроек
     */
    private void validateSettings(int min, int max, int listCount) {
        if (min == 0 || max == 0 || listCount == 0) {
            throw new IllegalSettingsException("\uD83D\uDCA9 Ни один из параметров не может равняться 0");
        }
    }

    /**
     * Добавление настроек пользователя в мапу, чтобы потом их использовать для этого пользователя при генерации файла
     * Если настройки совпадают с дефолтными, они не сохраняются, чтобы впустую не раздувать мапу
     * @param chatId id чата
     * @param settings настройки
     */
    private void saveUserSettings(Long chatId, Settings settings) {
        if (!settings.equals(Bot.getDefaultSettings())) {
            Bot.getUserSettings().put(chatId, settings);
        } else {
            Bot.getUserSettings().remove(chatId);
        }
    }
}