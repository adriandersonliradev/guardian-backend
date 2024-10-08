package com.guardiao.iot.infrastructure.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.guardiao.iot.entity.DocumentoEntity.Documento;
import com.guardiao.iot.entity.TipoDocumentoEntity.TipoDocumental;
import com.guardiao.iot.infrastructure.irepository.DocumentoRepository;
import org.hibernate.Session;

@Repository
@Transactional
public class DocumentoRepositoryImpl implements DocumentoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Documento> findAll() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM Documento", Documento.class).getResultList();
    }

    @Override
    public Optional<Documento> findById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return Optional.ofNullable(session.find(Documento.class, id));
    }

    @Override
    public Documento save(Documento documento) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(documento);
        return documento;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        Documento documento = session.find(Documento.class, id);
        if (documento != null) {
            session.delete(documento);
        } else {
            throw new Error("Documento não encontrado");
        }
    }

    @Override
    public List<Documento> findByTipoDocumentalId(Long tipoDocumentalId) {
        Session session = entityManager.unwrap(Session.class);
        return session
                .createQuery(" SELECT d FROM Documento d WHERE d.tipoDocumental.id = :tipoDocumentalId",
                        Documento.class)
                .setParameter("tipoDocumentalId", tipoDocumentalId)
                .getResultList();
    }

    /*@Override
    public List<Documento> findDocumentosExpirados(LocalDate dataAtual) {
        Session session = entityManager.unwrap(Session.class);
        return session
                .createQuery("SELECT d FROM Documento d WHERE d.tipoDocumental.dataExpiracao < :dataAtual",
                        Documento.class)
                .setParameter("dataAtual", dataAtual)
                .getResultList();
    }*/

    @Override
public List<Documento> findDocumentosExpirados(LocalDate dataAtual) {
    Session session = entityManager.unwrap(Session.class);
    
    // Consulta os documentos
    List<Documento> documentos = session
            .createQuery("SELECT d FROM Documento d", Documento.class)
            .getResultList();
    
    // Filtra os documentos expirados
    List<Documento> documentosExpirados = documentos.stream()
        .filter(doc -> isDocumentoExpirado(doc, dataAtual))
        .collect(Collectors.toList());
    
    return documentosExpirados;
}

    @Override
    public boolean existsById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        Long count = (Long) session.createQuery("SELECT COUNT(d) FROM Documento d WHERE d.id = :id")
                .setParameter("id", id)
                .uniqueResult();
        return count != null && count > 0;
    }

    private boolean isDocumentoExpirado(Documento documento, LocalDate dataAtual) {
        TipoDocumental tipoDocumental = documento.getTipoDocumental();
    
        // Se o tipo documental tiver um tempo de retenção, calcule a data de expiração
        if (tipoDocumental != null && tipoDocumental.getTempoRetencao() > 0) {
            // Soma o tempo de retenção (em dias) à dataHora do documento
            LocalDate dataExpiracao = documento.getDataHora().plusYears(tipoDocumental.getTempoRetencao());
            return dataExpiracao.isBefore(dataAtual); // Verifica se já expirou
        }
        
        return false;
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
    public void deleteAllInBatch(Iterable<Documento> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public List<Documento> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Documento> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Documento> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<Documento> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public Documento getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Documento getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public <S extends Documento> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public <S extends Documento> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public <S extends Documento> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public Page<Documento> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void delete(Documento entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll(Iterable<? extends Documento> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public <S extends Documento> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends Documento> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends Documento> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Documento> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

}
