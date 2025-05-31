package com.vr61v.repositories;

import com.vr61v.exceptions.RepositoryException;
import com.vr61v.utils.RepositorySessionManager;
import org.hibernate.Session;

import java.util.Optional;

public abstract class Repository<T, ID> {

    protected final RepositorySessionManager sessionManager;
    public final Class<T> clazz;

    public Repository(RepositorySessionManager sessionManager, Class<T> clazz) {
        this.sessionManager = sessionManager;
        this.clazz = clazz;
    }

    public boolean save(T entity) {
        if (entity == null) {
            throw new RepositoryException("Entity is null");
        }

        try (Session session = sessionManager.getSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<T> findById(ID id) {
        if (id == null) {
            throw new RepositoryException("Id is null");
        }

        try (Session session = sessionManager.getSession()) {
            return Optional.ofNullable(session.find(clazz, id));
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public T update(T entity) {
        if (entity == null) {
            throw new RepositoryException("Entity is null");
        }

        try (Session session = sessionManager.getSession()) {
            session.beginTransaction();
            entity = session.merge(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }

        return entity;
    }

    public boolean delete(T entity) {
        if (entity == null) {
            throw new RepositoryException("Entity is null");
        }

        try (Session session = sessionManager.getSession()) {
            session.beginTransaction();
            session.remove(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
