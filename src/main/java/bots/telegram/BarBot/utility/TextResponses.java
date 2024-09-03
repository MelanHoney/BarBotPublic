package bots.telegram.BarBot.utility;

import lombok.Getter;

@Getter
public enum TextResponses {
    ADD_TO_GROUP_CHAT_MESSAGE("Чтобы начать пользоваться ботом добавьте его в групповой чат."),
    ALREADY_REGISTERED_MESSAGE("Братанчик, ты уже зарегистрирован. Напиши барбот помощь, чтобы узнать все доступные команды."),
    SUCCESS_REGISTRATION_MESSAGE("%s, ты успешно зарегистрировался!\nНапиши барбот помощь чтобы узнать доступные команды"),
    WRONG_COMMAND_MESSAGE("Я не знаю такую команду. Напиши барбот помощь чтобы получить список доступных команд."),
    CHANGE_NAME_SUCCESS_MESSAGE("Имя успешно изменено."),
    TOO_LONG_NICKNAME_MESSAGE("Братанчик, твое новое имя слишком длинное"),
    NOT_REGISTERED_MESSAGE("Братанчик, ты еще не зарегистрирован. Напиши /start@BarCockBot для регистрации."),
    COCK_ALREADY_ROLLED_TODAY_MESSAGE("%s, продовжуй играть завтра.")
    ;

    private final String text;

    TextResponses(String text) {
        this.text = text;
    }
}
