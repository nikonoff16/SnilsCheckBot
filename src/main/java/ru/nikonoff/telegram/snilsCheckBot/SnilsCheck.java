package ru.nikonoff.telegram.snilsCheckBot;

public class SnilsCheck {
    private int controlSum;
    private String traceBack;
    private String snils;
    private Boolean snilsValid;

    public void setSnils(String number) {
        // Метод производит общий рассчет СНИЛС, заполняет данные объекта значениями
        String[] snilsArr = parseSnils(number);
        int localControlSum = computeControlSum(snilsArr[0]);
        int nativeControlSum;
        // Проверяем, не присутствуют ли недопустимые символы в контрольной сумме
        try {
            nativeControlSum = Integer.parseInt(snilsArr[1].trim());
        } catch (Exception e) { nativeControlSum = -1; }

        // Делаем оценку полученных данных
        if (localControlSum == -1 || nativeControlSum == -1) {
            traceBack = "При вводе СНИЛС была допущена ошибка: такого СНИЛС не может существовать.";
            snilsValid = false;
        }

        else if (nativeControlSum == localControlSum) {
            traceBack = "СНИЛС валиден";
            snilsValid = true;
        }
        else {
            traceBack = "СНИЛС не валиден: заявленная и реальная контрольные суммы не совпадают";
            snilsValid = false;
        }

        // исключаем пробелы из оригинального значения и также записываем в переменные объекта
        snils = number.trim();
        controlSum = localControlSum;
    }

    public String getTraceBack() {
        return traceBack;
    }

    public int getControlSum() {
        return controlSum;
    }

    public String getSnils() {
        return snils;
    }

    public Boolean getSnilsValid() {

        return snilsValid;
    }

    private int computeControlSum(String number) {
        // Метод рассчитывает контрольную сумму СНИЛСа по его телу
        int localSnilsBody;

        // Проверяет, числовое ли значение поступило для обработки
        try {
            localSnilsBody = Integer.parseInt(number);
        } catch (Exception e) { return -1; }

        // Исключает значения, ниже которых номера СНИЛС не выдаются
        if (localSnilsBody <= 1001998) { return -1;}

        // Создает набор переменных для своей работы
        int localControlSum = 0;
        int multiplyer = 1;
        int iter;
        int previousNum = 0;
        int sameNumCounter = 0;

        // Рассчитывает контрольную сумму по алгоритму
        for (int i = 1; i < 10; i++) {
            iter = localSnilsBody % 10;
            // Проверяем, есть ли повторяющие более двух раз числа
            if (i == 1) {previousNum = iter;}
            if (previousNum != iter && sameNumCounter < 3) {
                previousNum = iter;
                sameNumCounter = 1;
            }
            else {
                sameNumCounter++;
                // Не даем завершиться проверке, если найдены идущие подряд цифры (больше 2-х)
                if (sameNumCounter > 2) {
                    System.out.println("Код сработал");
                    return -1;
                }
            }
            // Считаем текущее значение
            localControlSum = localControlSum + iter * multiplyer;
            localSnilsBody = localSnilsBody / 10;
            multiplyer++;
        }

        // Делаем исключение для числа 100
        localControlSum = localControlSum % 101;
        if (localControlSum == 100) {
            localControlSum = 0;
        }
        return localControlSum;
    }
    private String[] parseSnils(String snilsNumber) {
        // Метод принимает строку СНИЛС, разбивает ее на тело и контрольную сумму
        // затем возвращает массив с этими значениями
        String[] result = new String[2];
        snilsNumber = snilsNumber.replaceAll("[^0-9]", "");

        // Проверка длины СНИЛСа
        if (snilsNumber.length() != 11) {
            result[0] = "";
            result[1] = "";
            return result;
        }
        result[0] = snilsNumber.substring(0,9);
        result[1] = snilsNumber.substring(9);

        return result;
    }
}
