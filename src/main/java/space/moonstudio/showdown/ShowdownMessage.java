package space.moonstudio.showdown;

import org.bukkit.ChatColor;

public enum ShowdownMessage {

    SUCCESS(ChatColor.GREEN + "[Showdown] Успешно!"),

    KIT(ChatColor.AQUA + "[Showdown] Вы выбрали набор %kit%"),

    JOIN(ChatColor.GREEN + "[Showdown] " + ChatColor.GOLD + "%nick% " + ChatColor.GREEN + "присоединился к ШД, " +
        ChatColor.GOLD + "%players%" + ChatColor.GREEN + "/" + ChatColor.GOLD + "10"),

    LEAVE(ChatColor.GRAY + "[Showdown] " + ChatColor.GOLD + "%nick% " + ChatColor.GRAY + "покинул ШД, " +
            ChatColor.GOLD + "%players%" + ChatColor.GRAY + "/" + ChatColor.GOLD + "10"),

    CONTAINS_IMPERMISSIBLE_SYMBOLS(ChatColor.RED + "[Showdown] Название содержит запрещенные символы!"),

    ALREADY_IN_GAME(ChatColor.RED + "[Showdown] Вы уже находитесь в ШД, чтобы ее покинуть - заберите ставку!"),
    NOT_ENOUGH_MONEY(ChatColor.RED + "[Showdown] Недостаточно денег!"),

    KIT_ALREADY_EXISTS(ChatColor.RED + "[Showdown] Кит с таким именем уже существует!"),

    GAME_START_MINUTES(ChatColor.GREEN + "[Showdown] Игра начнется через %time% минут(ы)"),

    GAME_START_SECONDS(ChatColor.GREEN + "[Showdown] Игра начнется через %time% секунд(ы)"),

    GAME_START_TITLE(ChatColor.GREEN + "ШД началось!"),

    GAME_START_SUBTITLE(ChatColor.AQUA + "Осталось " + ChatColor.GOLD + "5 " + ChatColor.AQUA + "минут до конца"),

    NOT_ENOUGH_PLAYERS(ChatColor.RED + "[Showdown] Недостаточно игроков для начала игры"),

    CANT_USE_COMMANDS(ChatColor.RED + "[Showdown] Вы не можете использовать команды во время игры!"),

    CREATE_MAP(ChatColor.GREEN + "[Showdown] Карта создана! Ее ID: " + ChatColor.GOLD + "%id%"),

    TAKE_ITEM_IN_HAND(ChatColor.RED + "[Showdown] Возьмите предмет в руку, он будет использоваться как иконка для набора!"),

    FIRST_PLACE(ChatColor.AQUA + "[Showdown] Вы заняли 1-ое место, ваша награда - " + ChatColor.YELLOW + "%money% " +
        ShowdownManager.COIN.getItemMeta().getDisplayName()),

    SECOND_PLACE(ChatColor.AQUA + "[Showdown] Игрок " + ChatColor.YELLOW + "%nick%" + ChatColor.AQUA +
        " занял 2-ое место, его награда составила " + ChatColor.YELLOW + "%money% " +
            ShowdownManager.COIN.getItemMeta().getDisplayName()),

    THIRD_PLACE(ChatColor.AQUA + "[Showdown] Игрок " + ChatColor.YELLOW + "%nick%" + ChatColor.AQUA +
            " занял 3-е место, его награда составила " + ChatColor.YELLOW + "%money% " +
            ShowdownManager.COIN.getItemMeta().getDisplayName()),

    OTHER_PLACE(ChatColor.AQUA + "[Showdown] Игрок " + ChatColor.YELLOW + "%nick%" + ChatColor.AQUA +
            " покидает игру, он занял %place%-е место!"),

    NO_WORLDS_AVAILABLE(ChatColor.RED + "[Showdown] Нет доступных миров для начала игры"),

    CANT_GO_OUT_OF_BORDER(ChatColor.RED + "[Showdown] Нельзя выйти за пределы карты"),

    MAP_WITH_THIS_ID_NOT_FOUND(ChatColor.RED + "[Showdown] Карта с таким ID не найдена"),

    HELP_1(ChatColor.LIGHT_PURPLE + "[Showdown] Список команд:"),
    HELP_2(ChatColor.LIGHT_PURPLE + "/shd help - Показать список команд"),
    HELP_3(ChatColor.LIGHT_PURPLE + "/shd create kit <название> - Создать новый набор предметов"),
    HELP_4(ChatColor.LIGHT_PURPLE + "/shd create map - Создать новую карту"),
    HELP_5(ChatColor.LIGHT_PURPLE + "/shd set spawnpoint <номер карты> <номер точки от 1 до 10> - Установить точку спавна на карте"),
    HELP_6(ChatColor.LIGHT_PURPLE + "/shd set border <номер карты> <номер точки от 1 до 2> - Установить точки границы карты"),
    HELP_7(ChatColor.LIGHT_PURPLE + "ПРИМЕЧАНИЕ: Карта не будет считаться играбельной до " +
            "установки всех 10-ти точек спавна и 2-х точек границ");

    private final String message;

    ShowdownMessage(String message) { this.message = message; }

    @Override
    public String toString() { return message; }
}
