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
    public List<Report> getTaskAllReportList(int taskId) {
        return entityManager.createQuery("FROM Report WHERE ancestorTask.id=:id ", Report.class)
                .setParameter("id", taskId).getResultList();
    }

    @Override
    public List<Report> getProjectReportList(String ownerName, int projectId, int creatorId,
                                             String startDate, String endDate) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Report> query = builder.createQuery(Report.class);
        Root<Report> report = query.from(Report.class);

        Timestamp start = startDate.equals("0") ? null : Timestamp.valueOf(LocalDate.parse(startDate).atStartOfDay());
        Timestamp end = endDate.equals("0") ? null : Timestamp.valueOf(LocalDate.parse(endDate).atStartOfDay());

        List<Predicate> predicates = new ArrayList<>();

        if (projectId == 0) {
            Join<Report, Project> project = report.join("ancestorProject");
            Join<Project, User> user = report.join("reporter");
            predicates.add(builder.equal(user.get("userName"), ownerName));
        }

        if (projectId != 0)
            predicates.add(builder.equal(report.get("ancestorProject"), projectId));

        if (creatorId != 0)
            predicates.add(builder.equal(report.get("creator"), creatorId));

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

    @Override
    public List<Report> getTaskReportList(String ownerName, int taskId, int creatorId, String startDate, String endDate) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Report> query = builder.createQuery(Report.class);
        Root<Report> report = query.from(Report.class);

        Timestamp start = startDate.equals("0") ? null : Timestamp.valueOf(LocalDate.parse(startDate).atStartOfDay());
        Timestamp end = endDate.equals("0") ? null : Timestamp.valueOf(LocalDate.parse(endDate).atStartOfDay());

        List<Predicate> predicates = new ArrayList<>();

        if (taskId == 0) {
            Join<Report, Project> project = report.join("ancestorProject");
            Join<Project, User> user = report.join("creator");
            predicates.add(builder.equal(user.get("userName"), ownerName));
        }

        if (taskId != 0)
            predicates.add(builder.equal(report.get("ancestorTask"), taskId));

        if (creatorId != 0)
            predicates.add(builder.equal(report.get("creator"), creatorId));

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

