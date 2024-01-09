package nl.hva.backend.enums;

public enum TagColor {
    PRIMARY("primary"),
    SECONDARY("secondary"),
    SUCCESS("success"),
    DANGER("danger"),
    WARNING("warning"),
    INFO("info"),
    LIGHT("light"),
    DARK("dark");

    private final String colorClass;

    TagColor(String colorClass) {
        this.colorClass = colorClass;
    }

    public String getColorClass() {
        return colorClass;
    }
}

