package com.example.springbootstructurever2.repository;

import com.example.springbootstructurever2.dto.response.PageResponse;
import com.example.springbootstructurever2.model.Address;
import com.example.springbootstructurever2.model.User;
import com.example.springbootstructurever2.repository.criteria.SearchCriteria;
import com.example.springbootstructurever2.repository.criteria.UserSearchCriteriaQueryConsumer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.springbootstructurever2.util.AppConst.SEARCH_OPERATOR;
import static com.example.springbootstructurever2.util.AppConst.SORT_BY;

@Repository
@Slf4j
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> getAllUsersWithSortByColumnsAndSearch(int pageNo, int pageSize, String search, String sort) {
        // query users
        StringBuilder sqlQuery = new StringBuilder("select new com.example.springbootstructurever2.dto.response.UserDetailResponse(u.id, u.firstName, u.lastName, u.phone, u.email) from User u where 1=1");
        if (StringUtils.hasLength(search)) {
            sqlQuery.append(" and lower(u.firstName) like lower(?1)");
            sqlQuery.append(" or lower(u.lastName) like lower(?2)");
            sqlQuery.append(" or lower(u.email) like lower(?3)");
        }

        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile(SORT_BY);
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                sqlQuery.append(String.format(" order by u.%s %s", matcher.group(1), matcher.group(3)));
            }
        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        if (StringUtils.hasLength(search)) {
            selectQuery.setParameter(1, String.format("%%%s%%", search));
            selectQuery.setParameter(2, String.format("%%%s%%", search));
            selectQuery.setParameter(3, String.format("%%%s%%", search));
        }
        selectQuery.setFirstResult(pageNo);
        selectQuery.setMaxResults(pageSize);

        log.info("Query: {}", selectQuery);
        List users = selectQuery.getResultList();

        // query no of records
        StringBuilder sqlCountQuery = new StringBuilder("select count(*) from User u where 1=1");
        if (StringUtils.hasLength(search)) {
            sqlCountQuery.append(" and lower(u.firstName) like lower(?1)");
            sqlCountQuery.append(" or lower(u.lastName) like lower(?2)");
            sqlCountQuery.append(" or lower(u.email) like lower(?3)");
        }

        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
        if (StringUtils.hasLength(search)) {
            selectCountQuery.setParameter(1, String.format("%%%s%%", search));
            selectCountQuery.setParameter(2, String.format("%%%s%%", search));
            selectCountQuery.setParameter(3, String.format("%%%s%%", search));
        }
        log.info("Count Query: {}", selectCountQuery);
        Long totalElement = (Long) selectCountQuery.getSingleResult();

        return PageResponse.builder()
                .page(pageNo)
                .size(pageSize)
                .totalPage(totalElement.intValue() / pageSize)
                .items(users)
                .build();
    }

    public PageResponse<?> getUsersWithCriteria(int pageNo, int pageSize, String sort, String address, String... search) {
        // get users
        List<SearchCriteria> criteriaList = new ArrayList<>();
        if (search != null) {
            // key:value
            Pattern pattern = Pattern.compile(SEARCH_OPERATOR);
            for (String s : search) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }
        log.info("Criteria list {}", criteriaList);

        List<User> users = this.getUsers(pageNo, pageSize, criteriaList, address, sort);

        // get total elements
        Long totalElements = this.getTotalElements(criteriaList);

        return PageResponse.builder()
                .page(pageNo)
                .size(pageSize)
                .totalPage(totalElements.intValue() / pageSize)
                .items(users)
                .build();
    }

    private List<User> getUsers(int pageNo, int pageSize, List<SearchCriteria> criteriaList, String address, String sortBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        // search condition
        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);

        if (StringUtils.hasLength(address)) {
            Join<Address, User> addressUserJoin = root.join("addresses");
            Predicate addressPredicate = criteriaBuilder.like(addressUserJoin.get("city"), String.format("%%%s%%", address));
            query.where(predicate, addressPredicate);
        } else {
            criteriaList.forEach(queryConsumer);
            predicate = queryConsumer.getPredicate();
            query.where(predicate);
        }

        // sort
        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile(SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                String sort = matcher.group(3);
                if (sort.equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(columnName)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(columnName)));
                }
            }
        }

        return entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();
    }

    private Long getTotalElements(List<SearchCriteria> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<User> root = query.from(User.class);

        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer searchConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);
        params.forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.select(criteriaBuilder.count(root));
        query.where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }
}
