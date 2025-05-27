package com.vr61v.repositories;

import com.vr61v.entities.Booking;
import com.vr61v.exceptions.RepositoryException;
import com.vr61v.utils.RepositorySessionManager;
import org.hibernate.Session;

import java.util.Optional;

public class BookingsRepository implements Repository<Booking, String> {

    private final RepositorySessionManager sessionManager;

    public BookingsRepository(RepositorySessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Booking save(Booking entity) {
        if (entity == null) {
            throw new RepositoryException("Booking is null");
        }

        try (Session session = sessionManager.getSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }

        return entity;
    }

    @Override
    public Optional<Booking> findById(String id) {
        if (id == null || id.isEmpty()) {
            throw new RepositoryException("Id is null or empty");
        }

        try (Session session = sessionManager.getSession()) {
            return Optional.ofNullable(session.find(Booking.class, id));
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Booking update(Booking entity) {
        if (entity == null) {
            throw new RepositoryException("Booking is null");
        }

        try (Session session = sessionManager.getSession()) {
            session.beginTransaction();
            entity = session.merge(entity);
            session.getTransaction().commit();
        }

        return entity;
    }

    @Override
    public boolean delete(Booking entity) {
        if (entity == null) {
            throw new RepositoryException("Booking is null");
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
