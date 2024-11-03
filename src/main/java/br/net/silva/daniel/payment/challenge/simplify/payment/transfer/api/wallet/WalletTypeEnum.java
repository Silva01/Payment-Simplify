package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

public enum WalletTypeEnum {
    COMUM(1L),
    LOJISTA(2L);

    private final Long value;

    WalletTypeEnum(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
