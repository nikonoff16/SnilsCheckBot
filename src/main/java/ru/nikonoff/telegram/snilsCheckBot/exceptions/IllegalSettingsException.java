package ru.nikonoff.telegram.snilsCheckBot.exceptions;

/**
 * Исключение, пробрасываемое в случае получения невалидных настроек выгружаемого файла
 */
public class IllegalSettingsException extends IllegalArgumentException {

    public IllegalSettingsException(String s) {
        super(s);
    }
}