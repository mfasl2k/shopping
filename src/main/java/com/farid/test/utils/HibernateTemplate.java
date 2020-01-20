package com.farid.test.utils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

public class HibernateTemplate<T> {
    @Autowired
    protected SessionFactory sessionFactory;

    public synchronized Transaction getTrans(Session sess){
        return sess.beginTransaction();
    }

    public synchronized Session open() {
        return sessionFactory.openSession();
    }

    public void close(Session sess){
        sess.close();
    }

    public synchronized void close(boolean success, Session sess, Transaction trans) {
        if (success)
            trans.commit();
        else
            trans.rollback();
        sess.close();
    }

    public void flushClear(Session sess) {
        sess.flush();
        sess.clear();
    }

    public void saveForBatch(Session sess, Object object) {
        sess.save(object);
    }

    public void saveOrUpdateForBatch(Session sess, Object object) {
        sess.saveOrUpdate(object);
    }

    public boolean saveData(Object object) throws HibernateException {
        //sess = sessionFactory.openSession();
        Session sess = open();
        Transaction trans = getTrans(sess);
        boolean isSuccess = false;
        try {
            //trans = sess.beginTransaction();
            sess.saveOrUpdate(object);
            //trans.commit();
            isSuccess = true;

        } catch (HibernateException e) {
            throw e;
        } finally {
            // sess.flush();
            // sess.clear();
            //sess.close();
            close(isSuccess, sess, trans);
        }
        return isSuccess;
    }

    public boolean deleteData(Object object) {
        //sess = sessionFactory.openSession();
        Session sess = open();
        Transaction trans = getTrans(sess);
        boolean isSuccess = false;
        try {
            //trans = sess.beginTransaction();
            sess.delete(object);
            isSuccess = true;
            //trans.commit();
            //return true;
        } catch (HibernateException e) {
            //trans.rollback();
            e.printStackTrace();
            throw e;
        } finally {
//			sess.close();
            close(isSuccess, sess, trans);
        }
        return isSuccess;
    }

    public List<T> findData(String hql, int startPosition, int maxResult, boolean isLimit) {
        //sess = sessionFactory.openSession();
        Session sess = open();
        try {
            TypedQuery<T> query = sess.createQuery(hql);
            if (isLimit) {
                query.setFirstResult(startPosition);
                query.setMaxResults(maxResult);
            }
            return findData(query);
        } catch (HibernateException e) {
            e.printStackTrace();
            throw e;
        } finally{
            close(sess);
        }
    }

    public List<T> findAllData(String hql) {
//		sess = sessionFactory.openSession();
        Session sess = open();
        try {
            TypedQuery<T> query = sess.createQuery(hql);
            return findData(query);
        } catch (HibernateException e) {
            e.printStackTrace();
            throw e;
        } finally{
            close(sess);
        }
    }

    public List<T> findData(TypedQuery<T> query) {
        try {
            @SuppressWarnings("unchecked")
            List<T> list = query.getResultList();
            return list;
        } catch (HibernateException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean updateData(Object object) {
        Session sess = open();
        Transaction trans = getTrans(sess);
        boolean isSuccess = false;
        try {
            sess.saveOrUpdate(object);
            isSuccess = true;
        } catch (HibernateException e) {
            trans.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            close(isSuccess, sess, trans);
        }
        return isSuccess;
    }

    @SuppressWarnings("unchecked")
    public T getById(String hql, long id) {
        return getById(hql, id, "id");
    }

    @SuppressWarnings("unchecked")
    public T getById(String hql, long id, String key) {
        Session sess = open();
        try {
            TypedQuery<T> query = sess.createQuery(hql);
            query.setParameter(key, id);
            if (query.getResultList().isEmpty()) {
                return null;
            }
            return query.getResultList().get(0);
        } catch (HibernateException e) {
            e.printStackTrace();
            throw e;
        } finally{
            close(sess);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> getList(String hql, Map<String, Object> params) {
        Session sess = open();
        try {
            TypedQuery<T> query = sess.createQuery(hql);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()){
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            if (query.getResultList().isEmpty()) {
                return null;
            }
            return query.getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            throw e;
        } finally{
            close(sess);
        }
    }

    public T getSingle(String hql, Map<String, Object> params) {
        List<T> list = getList(hql, params);
        if (list != null)
            return list.get(0);
        else
            return null;
    }
}
