package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

public enum WalletTypeEnum {
    COMUM(1),
    LOJISTA(2);

    private final Integer value;

    WalletTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static WalletTypeEnum fromValue(int value) {
        for (WalletTypeEnum tipo : WalletTypeEnum.values()) {
            if (tipo.value == value) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Valor inválido: " + value);
    }
}
