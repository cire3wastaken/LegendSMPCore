package legendsmpcore.customitems.items;

/**
 * Enum containing all custom items, for disabling & enabling specific items
 * */
public enum Items {
    GHASTBOW("ghastbow"),
    HYPERION("hyperion"),
    THORHAMMER("thorhammer"),
    VAMPIREBLADE("vampireblade"),
    SUMMONINGSWORD("summoningsword"),
    WITCHSCYHTE("witchscythe");

    public final String name;

    Items(String name) {
        this.name = name;
    }
}
