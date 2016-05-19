/*
package com.timetracker.DAO.Implementation;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository("productCategoryModelDaoImpl")
public class ProductCategoryModelDaoImpl implements ProductCategoryModelDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProductCategoryModel> getProductCategoryModelListByProductCategoryId(int categoryId, int maxResults, int resultsFromCurrentIndex) {
        Query query = entityManager.createQuery("FROM ProductCategoryModel P WHERE P.category.id=:id", ProductCategoryModel.class)
                .setParameter("id", categoryId)
                .setMaxResults(maxResults)
                .setFirstResult(resultsFromCurrentIndex);
        return query.getResultList();
    }

    @Override
    public int getNumberOfAllRecordsCurrentProductCategory(int productCategoryId) {
        return entityManager.createNamedQuery("ProductCategoryModel.getNumberOfAllRecords")
                .setParameter("categoryId", productCategoryId).getResultList().size();
    }

}
*/
