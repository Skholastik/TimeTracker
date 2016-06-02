package com.timetracker.DAO.Implementation;

import com.timetracker.DAO.Interfaces.ReportDao;
import com.timetracker.Entities.Project;
import com.timetracker.Entities.Report;
import com.timetracker.Entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public List<User> getTaskReporterList(int taskId) {
        return entityManager.createQuery("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.createdReportList r WHERE r.ancestorTask.id=:taskId", User.class)
                .setParameter("taskId", taskId).getResultList();
    }

    @Override
    public List<Report> getReportList(Integer reportType, String ownerName, int projectOrTaskId, int reporterId,
                                      String startDate, String endDate) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Report> query = builder.createQuery(Report.class);
        Root<Report> report = query.from(Report.class);

        Timestamp start = startDate.equals("0") ? null : Timestamp.valueOf(LocalDate.parse(startDate).atStartOfDay());
        Timestamp end = endDate.equals("0") ? null : Timestamp.valueOf(LocalDate.parse(endDate).atStartOfDay());

        List<Predicate> predicates = new ArrayList<>();

        if (projectOrTaskId == 0) {
            Join<Report, Project> project = report.join("ancestorProject");
            Join<Project, User> user = report.join("reporter");
            predicates.add(builder.equal(user.get("userName"), ownerName));
        }

        if (projectOrTaskId != 0) {

            if (reportType == 1)
                predicates.add(builder.equal(report.get("ancestorProject"), projectOrTaskId));

            if (reportType == 2)
                predicates.add(builder.equal(report.get("ancestorTask"), projectOrTaskId));
        }


        if (reporterId != 0)
            predicates.add(builder.equal(report.get("reporter"), reporterId));

        if (start != null && end == null)
            predicates.add(builder.greaterThanOrEqualTo(report.get("creationDateTime"), start));

        if (start == null && end != null)
            predicates.add(builder.lessThanOrEqualTo(report.get("creationDateTime"), end));

        if (start != null && end != null)
            predicates.add(builder.between(report.get("creationDateTime"), start, end));

        if (!predicates.isEmpty())
            query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query).getResultList();

    }

}

