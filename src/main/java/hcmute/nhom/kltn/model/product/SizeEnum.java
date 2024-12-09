package hcmute.nhom.kltn.model.product;

import lombok.Getter;

@Getter
public enum SizeEnum {
    S("SMA001", "001", "S"),
    M("SMA002","002", "M"),
    L("SMA003","003", "L"),
    XL("SMA004","004", "XL"),
    XXL("SMA005","005", "XXL"),
    XXXL("SMA006","006", "XXXL");

    private final String code;
    private final String displayCode;
    private final String name;

    SizeEnum(String code, String displayCode, String name) {
        this.code = code;
        this.displayCode = displayCode;
        this.name = name;
    }

    // Static method to find SizeEnum by name
    public static Size getByName(String name) {
        for (SizeEnum sizeEnum : SizeEnum.values()) {
            if (sizeEnum.getName().equalsIgnoreCase(name)) {
                return new Size(sizeEnum.getCode(), sizeEnum.getDisplayCode(), sizeEnum.getName());
            }
        }
        return null;  // Return null if no match is found
    }
}
