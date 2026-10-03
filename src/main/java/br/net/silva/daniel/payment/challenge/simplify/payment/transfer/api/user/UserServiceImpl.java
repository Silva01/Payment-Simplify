package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.user;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.Wallet;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletRepository;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletTypeEnum;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
  private final WalletRepository walletRepository;

  public UserServiceImpl(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Transactional
  public UserResponse createUser(UserRequest request) {
    if (walletRepository.findByCpf(request.getCpf()).isPresent()) {
      throw new UserAlreadyExistsException("User with CPF " + request.getCpf() + " already exists");
    }

    if (walletRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
    }

    Wallet wallet = new Wallet(
        null,
        request.getName(),
        request.getCpf(),
        request.getEmail(),
        request.getPassword(),
        BigDecimal.ZERO,
        request.getType()
    );

    Wallet savedWallet = walletRepository.save(wallet);

    return new UserResponse(
        savedWallet.getId(),
        savedWallet.getName(),
        savedWallet.getCpf(),
        savedWallet.getEmail(),
        savedWallet.getType().name()
    );
  }
}
