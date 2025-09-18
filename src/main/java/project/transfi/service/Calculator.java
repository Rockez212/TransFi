package project.transfi.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class Calculator {

    public BigDecimal calculateAmountAfterFee(BigDecimal amountToTransfer, BigDecimal feePercent) {
        if (amountToTransfer.compareTo(BigDecimal.ZERO) <= 0 || feePercent.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount and fee must be positive");
        }
        BigDecimal amountWithFee = amountToTransfer
                .multiply(feePercent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return amountToTransfer.add(amountWithFee);
    }


}
