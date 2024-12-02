package org.MIFI.GRUD;

import org.MIFI.entity.Entity;

public class UserDAO implements DAO {
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
