package com.omnivision.dao;

//import com.omnivision.core.CommandHistoryDao;
import com.omnivision.core.CommandHistory;
import com.omnivision.core.CommandHistoryDao;
import com.omnivision.core.DaoSession;
import com.tablayoutexample.lkelly.omnivision.OmniLocateApplication;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Lodeane on 4/7/2017.
 */

public class CommandHistoryDaoImpl implements ICommandHistoryDao {
    DaoSession daoSession;
    CommandHistoryDao commandHistoryDao;

    public CommandHistoryDaoImpl(DaoSession daoSession){
        this.daoSession = daoSession;
        this.commandHistoryDao = daoSession.getCommandHistoryDao();
    }

    @Override
    public void insert(CommandHistory value) {
        commandHistoryDao.insert(value);
    }

    @Override
    public void insertAll(List<CommandHistory> objects) {
        for(CommandHistory value : objects){
            commandHistoryDao.insert(value);
        }
    }

    @Override
    public List<CommandHistory> findAll() {
        return commandHistoryDao.loadAll();
    }

    @Override
    public List<CommandHistory> findAllByOwnerId(Long value) {
        return daoSession.getPhoneDao().load(value).getCommandHistories();
    }

    @Override
    public List<CommandHistory> getAllByCmdIssuer(String key) {
        CommandHistoryDao.dropTable(daoSession.getDatabase(),true);
        CommandHistoryDao.createTable(daoSession.getDatabase(),true);

        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        CommandHistory commandHistory = new CommandHistory(OmniLocateApplication.getPhoneInstance().getId(),"Find",timeStamp,"688");
        commandHistoryDao.insert(commandHistory);
        CommandHistory commandHistory1 = new CommandHistory(OmniLocateApplication.getPhoneInstance().getId(),"Stolen",timeStamp,"688");
        commandHistoryDao.insert(commandHistory1);

        QueryBuilder<CommandHistory> queryBuilder = commandHistoryDao.queryBuilder();
        queryBuilder.where(CommandHistoryDao.Properties.IssuedBy.eq(key)).build();

        return queryBuilder.list();
    }

    //TODO method unimplemented since values might be duplicated
    @Override
    public CommandHistory findByValue(String value) {
        return null;
    }

    @Override
    public QueryBuilder<CommandHistory> findByQuery() {
        QueryBuilder<CommandHistory> queryBuilder = commandHistoryDao.queryBuilder();
        return queryBuilder;
    }

    @Override
    public CommandHistory find(Long key) {
        return commandHistoryDao.load(key);
    }

    @Override
    public void update(CommandHistory object) {
        commandHistoryDao.update(object);
    }

    @Override
    public void delete(CommandHistory object) {
        commandHistoryDao.delete(object);
    }
}
