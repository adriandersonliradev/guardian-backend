package com.guardiao.iot.infrastructure.repository;

import com.guardiao.iot.entity.TipoDocumentoEntity.TipoDocumental;
import com.guardiao.iot.infrastructure.irepository.TipoDocumentalRepository;
import org.hibernate.Session;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TipoDocumentalRepositoryImpl implements TipoDocumentalRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TipoDocumental> findAll() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM TipoDocumental", TipoDocumental.class).getResultList();
    }

    @Override
    @Transactional
    public Optional<TipoDocumental> findById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return Optional.ofNullable(session.find(TipoDocumental.class, id));
    }

    @Override
    public TipoDocumental save(TipoDocumental tipoDocumental) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(tipoDocumental);
        return tipoDocumental;
    }

    @Override
    public void deleteById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        TipoDocumental tipoDocumental = session.find(TipoDocumental.class, id);
        if (tipoDocumental != null) {
            session.delete(tipoDocumental);
        }
    }

    @Override
public boolean existsByNomeDocumento(String nomeDocumento) {
    Session session = entityManager.unwrap(Session.class);
    Long count = (Long) session.createQuery("SELECT COUNT(t) FROM TipoDocumental t WHERE LOWER(t.nomeDocumento) = LOWER(:nomeDocumento)")
            .setParameter("nomeDocumento", nomeDocumento)
            .uniqueResult();
    return count != null && count > 0;
}

@Override
public Optional<TipoDocumental> findByNomeDocumento(String nomeDocumento) {
    Session session = entityManager.unwrap(Session.class);
    TipoDocumental tipoDocumental = (TipoDocumental) session.createQuery("FROM TipoDocumental t WHERE LOWER(t.nomeDocumento) = LOWER(:nomeDocumento)")
            .setParameter("nomeDocumento", nomeDocumento)
            .uniqueResult();
    
    return Optional.ofNullable(tipoDocumental);
}

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllByIdInBatch'");
    }

    @Override
    public void deleteAllInBatch() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public void deleteAllInBatch(Iterable<TipoDocumental> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public List<TipoDocumental> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends TipoDocumental> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends TipoDocumental> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<TipoDocumental> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public TipoDocumental getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public TipoDocumental getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public <S extends TipoDocumental> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public <S extends TipoDocumental> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public <S extends TipoDocumental> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public Page<TipoDocumental> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void delete(TipoDocumental entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll(Iterable<? extends TipoDocumental> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public boolean existsById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

    @Override
    public <S extends TipoDocumental> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends TipoDocumental> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends TipoDocumental> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends TipoDocumental> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    

    
}
