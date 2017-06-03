package com.omnivision.core;

import java.util.List;

/**
 * Created by lkelly on 3/7/2017.
 */

public interface IPrepaidCredit {
    Boolean addCredit();
    Boolean removeCredit(String voucherNum);
    Boolean validateCreditFormat();
    Boolean isUsed(String voucherNum);
    List<String> getPrepaidCredits();
}
