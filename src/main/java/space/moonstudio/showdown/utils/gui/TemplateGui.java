package space.moonstudio.showdown.utils.gui;

public enum TemplateGui {

    DEFAULT(1, 4, 6);

    int[] slots;

    TemplateGui(int... slots) { this.slots = slots; }

    public int getSlot(ButtonGui button, int size)
    {
        int value = slots[button.ordinal()];
        return size - (value == 0 ? size : value);
    }
}
