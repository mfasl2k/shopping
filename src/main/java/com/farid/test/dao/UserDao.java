package com.farid.test.dao;

import com.farid.test.pojo.User;
import com.farid.test.utils.HibernateTemplate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Queue;

@Repository
public class UserDao extends HibernateTemplate<User> {
    public boolean save (User data){
        return saveData(data);
    }

    public List<User> getDatas() {
        Session sess = open();
        try{
            Query query = sess.createQuery("FROM User");
            return query.getResultList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            close(sess);
        }
    }
}
