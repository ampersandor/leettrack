package com.ampersandor.leettrack.repository;

import com.ampersandor.leettrack.model.MemberLike;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Repository
public class JpaMemberLikeRepository implements MemberLikeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<MemberLike> findByMemberId(Long memberId) {
        try {
            MemberLike result = entityManager.createQuery(
                            "SELECT ml FROM MemberLike ml WHERE ml.memberId = :memberId",
                            MemberLike.class)
                    .setParameter("memberId", memberId)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Long getLikeCount(Long memberId) {
        try {
            return entityManager.createQuery(
                            "SELECT ml.count FROM MemberLike ml WHERE ml.memberId = :memberId",
                            Long.class)
                    .setParameter("memberId", memberId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        }
    }

    public void save(MemberLike memberLike) {
        if (findByMemberId(memberLike.getMemberId()).isPresent()) {
            entityManager.merge(memberLike);
        } else {
            entityManager.persist(memberLike);
        }
        entityManager.flush();
    }
}