package project.transfi.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class Calculator {

    public static BigDecimal calculateFee(BigDecimal amountToTransfer, BigDecimal fee) {
        if (amountToTransfer == BigDecimal.ZERO || fee == BigDecimal.ZERO) {
            throw new IllegalArgumentException("Amount to transfer cannot be null");
        }
        return amountToTransfer
                .multiply(fee)
                .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
    }


}
