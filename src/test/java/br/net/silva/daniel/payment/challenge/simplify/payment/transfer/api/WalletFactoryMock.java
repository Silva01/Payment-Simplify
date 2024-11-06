package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.Wallet;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletTypeEnum;

import java.math.BigDecimal;

public interface WalletFactoryMock {

    default Wallet createCommonWallet(Long idMock) {
        return new Wallet(
                idMock,
                "Teste",
                "12345678900",
                "teste@teste.com",
                "123",
                BigDecimal.valueOf(1000),
                WalletTypeEnum.COMUM
        );
    }
}
