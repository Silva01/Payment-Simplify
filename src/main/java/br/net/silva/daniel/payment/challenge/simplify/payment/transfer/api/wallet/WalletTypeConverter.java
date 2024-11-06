package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class WalletTypeConverter implements AttributeConverter<WalletTypeEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(WalletTypeEnum attribute) {
        return attribute.getValue();
    }

    @Override
    public WalletTypeEnum convertToEntityAttribute(Integer dbData) {
        return WalletTypeEnum.fromValue(dbData);
    }
}
