package space.moonstudio.showdown;

import java.util.Arrays;

public enum ShowdownMessage {

    SUCCESS(),
    KIT(),
    JOIN(),
    LEAVE(),
    CONTAINS_IMPERMISSIBLE_SYMBOLS(),
    ALREADY_IN_GAME(),
    NOT_ENOUGH_MONEY(),
    KIT_ALREADY_EXISTS(),
    GAME_START_MINUTES(),
    GAME_START_SECONDS(),
    GAME_START_TITLE(),
    GAME_START_SUBTITLE(),
    NOT_ENOUGH_PLAYERS(),
    CANT_USE_COMMANDS(),
    CREATE_MAP(),
    TAKE_ITEM_IN_HAND(),
    FIRST_PLACE(),
    SECOND_PLACE(),
    THIRD_PLACE(),
    OTHER_PLACE(),
    NO_WORLDS_AVAILABLE(),
    CANT_GO_OUT_OF_BORDER(),
    MAP_WITH_THIS_ID_NOT_FOUND(),
    HELP_1(),
    HELP_2(),
    HELP_3(),
    HELP_4(),
    HELP_5(),
    HELP_6(),
    HELP_7();

    private String message;

    static
    {
        Arrays.stream(values()).forEach(message ->
            message.message = ShowdownPlugin.getInstance().getConfig().getString("messages." + message.name()));
    }

    @Override
    public String toString() { return message; }
}
