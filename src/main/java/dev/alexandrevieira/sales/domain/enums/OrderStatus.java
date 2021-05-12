package dev.alexandrevieira.sales.domain.enums;

public enum OrderStatus {
    AWAITING_PAYMENT(0),
    PAID(1),
    CANCELLED(2);
    private final int code;

    OrderStatus(int code) {
        this.code = code;
    }

    int getCode() {
        return code;
    }

    public static OrderStatus byCode(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (code == status.getCode()) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code");
    }
}
