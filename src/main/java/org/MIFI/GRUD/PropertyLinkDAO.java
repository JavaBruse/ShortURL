package org.MIFI.GRUD;

import org.MIFI.DataBaseUtils;
import org.MIFI.entity.Entity;

import java.sql.Statement;

public class PropertyLinkDAO implements DAO {

    private Statement statement;

    public PropertyLinkDAO() {
        this.statement = DataBaseUtils.getInstance().getStmt();
    }

    @Override
    public Entity findById(Long id) {
        return null;
    }

    @Override
    public boolean saveOrUpdate(Entity entity) {
        return false;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}
