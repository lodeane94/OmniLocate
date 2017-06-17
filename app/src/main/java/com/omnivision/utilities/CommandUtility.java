package com.omnivision.utilities;

import android.content.Context;

import com.omnivision.core.DaoSession;
import com.omnivision.core.PrepaidCredit;
import com.omnivision.core.PrepaidCreditDao;
import com.omnivision.dao.IPrepaidCreditDao;
import com.omnivision.dao.PrepaidCreditDaoImpl;
import com.tablayoutexample.lkelly.omnivision.OmniLocateApplication;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by Lodeane on 17/6/2017.
 */

public class CommandUtility {
    private static Context context = OmniLocateApplication.getInstance().getApplicationContext();
    private static DaoSession daoSession = OmniLocateApplication.getSession(context);
    private static IPrepaidCreditDao prepaidCreditDao = new PrepaidCreditDaoImpl(daoSession);

    private CommandUtility(){}

    /**
     * @author lkelly
     * @desc adds prepaid credit to the application's database
     * @params prepaidCredit
     * @return
     * */
    public static void addCredit(PrepaidCredit prepaidCredit) throws Exception{
        prepaidCreditDao.insert(prepaidCredit);
    }

    /**
     * @author lkelly
     * @desc activates unused prepaid credit on the device
     * @params
     * @return
     * */
    public static void activateCredit() throws Exception{
        prepaidCreditDao = new PrepaidCreditDaoImpl(daoSession);

        QueryBuilder<PrepaidCredit> queryBuilder = prepaidCreditDao.findByQuery();
        queryBuilder.where(PrepaidCreditDao.Properties.IsUsed.eq(false)).limit(1);
        PrepaidCredit prepaidCredit = queryBuilder.unique();

        if(prepaidCredit != null)
            DialingHandler.dial(prepaidCredit.getVoucherNum(),context);
    }


    /**
     * @author lkelly
     * @desc activates selected prepaidcredit on the device
     * @params voucherNumber
     * @return
     * */
    public static void activateCredit(String voucherNumber) throws Exception{
        DialingHandler.dial(voucherNumber,context);
    }
}
