package org.prgrms.kdt.voucher.controller;

import org.prgrms.kdt.app.configuration.io.InputHandler;
import org.prgrms.kdt.app.configuration.io.OutputHandler;
import org.prgrms.kdt.voucher.domain.Voucher;
import org.prgrms.kdt.voucher.service.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.prgrms.kdt.app.configuration.io.SystemMessage.*;

@Controller
public class VoucherController {

    private static final Logger logger = LoggerFactory.getLogger(VoucherController.class);
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;
    private final VoucherService voucherService;
    private final String EXIT = "exit";
    private final String CREATE = "create";
    private final String LIST = "list";
    private final String FIXED = "fixed";
    private final String PERCENT = "percent";

    public VoucherController(InputHandler inputHandler, OutputHandler outputHandler, VoucherService voucherService) {
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
        this.voucherService = voucherService;
    }

    public boolean startVoucherMenu() throws IOException {
        outputHandler.outputStartMessage();
        var menu = inputHandler.inputString();

        switch (menu) {
            case EXIT:
                outputHandler.outputSystemMessage(EXIT_PROGRAM.getMessage());
                return false;
            case CREATE:
                createVoucher();
                break;
            case LIST:
                List<Voucher> voucherList = voucherService.getAllVouchers();
                outputHandler.outputVouchers(voucherList);
                break;
            default:
                outputHandler.outputSystemMessage(EXCEPTION_VOUCHER_TYPE.getMessage());
                break;
        }

        return true;
    }


    private void createVoucher() throws IOException {
        while (true) {
            outputHandler.outputSystemMessage(CREATE_BOUCHER_TYPE.getMessage());
            var createVoucherType = inputHandler.inputString();

            var isRepeat = true;

            if (createVoucherType.equals(FIXED)) {
                isRepeat = createFixedAmountVoucher();
                if(!isRepeat)
                    break;
            } else if (createVoucherType.equals(PERCENT)) {
                isRepeat = createPercentDiscountVoucher();
                if(!isRepeat)
                    break;
            } else {
                outputHandler.outputSystemMessage(EXCEPTION_VOUCHER_TYPE.getMessage());
            }
        }
    }

    private boolean createFixedAmountVoucher() throws IOException {
        outputHandler.outputSystemMessage(CREATE_FIXED_BOUCHER.getMessage());

        var amount = 0;
        while (true) {
            amount = inputHandler.inputInt();

            if (amount <= 0) {
                outputHandler.outputSystemMessage(EXCEPTION_FIXED_AMOUNT_MINUS.getMessage());
                continue;
            }
            if (amount >= 100_000) {
                outputHandler.outputSystemMessage(EXCEPTION_FIXED_AMOUNT_OVER.getMessage());
                continue;
            }
            break;
        }

        var fixedAmountVoucherDto = new FixedAmountVoucherDto(UUID.randomUUID(), amount);
        voucherService.createVoucher(fixedAmountVoucherDto);
        return false;
    }

    private boolean createPercentDiscountVoucher() throws IOException {
        outputHandler.outputSystemMessage(CREATE_PERCENT_BOUCHER.getMessage());

        var percent = 0;
        while (true) {
            percent = inputHandler.inputInt();

            if (percent <= 0) {
                outputHandler.outputSystemMessage(EXCEPTION_PERCENT_MINUS.getMessage());
                continue;
            }
            if (percent >= 100) {
                outputHandler.outputSystemMessage(EXCEPTION_PERCENT_OVER.getMessage());
                continue;
            }
            break;
        }

        var percentDiscountVoucherDto = new PercentDiscountVoucherDto(UUID.randomUUID(), percent);
        voucherService.createVoucher(percentDiscountVoucherDto);
        return false;
    }

    //    var orderProperties = applicationContext.getBean(OrderProperties.class);
//    logger.error("logger name => {}", logger.getName());
//    logger.warn("version -> {}", orderProperties.getVersion());
//    logger.warn("minimumOrderAmount -> {}", orderProperties.getMinimumOrderAmount());
//    logger.warn("supportVendors -> {}", orderProperties.getSupportVendors());
//    logger.warn("description -> {}", orderProperties.getDescription());
}
