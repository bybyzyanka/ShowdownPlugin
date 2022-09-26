package space.moonstudio.showdown;

public enum ShowdownGuiTitle {

    MENU("Меню ШД"),
    KIT_CREATE("Перенесите сюда предметы, входящие в набор"),
    KIT_PICK("Выбор набора");

    private final String title;

    ShowdownGuiTitle(String title) { this.title = title; }

    @Override
    public String toString() { return title; }
}
