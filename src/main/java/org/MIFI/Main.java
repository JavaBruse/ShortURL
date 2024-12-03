package org.MIFI;

import org.MIFI.GRUD.LinkDAO;
import org.MIFI.GRUD.UserDAO;
import org.MIFI.utils.ConfigUtils;
import org.MIFI.utils.DataBaseUtils;

public class Main {
    public static void main(String[] args) {
        DataBaseUtils.getInstance().connect();
        ConfigUtils.getInstance();
        UserDAO userdao = new UserDAO();
        LinkDAO linkDAO = new LinkDAO();
//        for (int i = 1; i < 10; i++) {
//            User user = new User();
//            String UUIDx =UUID.randomUUID().toString();
//            user.setUUID(UUIDx);
//            user.setName("name" + i);
//            userdao.save(user);
//            for (int j = 0; j < 5; j++) {
//                Link link = new Link(UUIDx,"linkLong"+i+j,"LinkShort" + i+j, System.currentTimeMillis()+ 10* 24 * 60 * 60 * 1000, 10);
//                linkDAO.save(link);
//            }
//        }
//        for (Link link: linkDAO.findByUUID("90315dc9-6960-4ec8-b9d1-4d934502c197")){
//            System.out.println(link);
//        }
        System.out.println(userdao.findByUUID("90315dc9-6960-4ec8-b9d1-4d934502c197"));

        System.out.println("Отработано!");
    }
}