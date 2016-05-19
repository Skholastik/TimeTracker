package com.timetracker.DAO.Implementation;

import com.timetracker.DAO.Interfaces.ReportDao;
import com.timetracker.Entities.Report;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("reportDaoImpl")
public class ReportDaoImpl implements ReportDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createReport(Report newReport) {
        entityManager.persist(newReport);
    }

    @Override
    public List<Report> getTaskAllReportList(int taskId) {
        return entityManager.createQuery("FROM Report WHERE ancestorTask.id=:id ", Report.class)
                .setParameter("id", taskId).getResultList();
    }
}
