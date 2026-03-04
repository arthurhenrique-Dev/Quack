package com.quack.quack_app.Application.Ports.Output.Services;

import java.util.UUID;

public interface TwoFAService {

    String setupTwoFa();

    boolean checkCode(String secret, String code);

    String getQrCodeUrl(String secret, UUID id);
}
