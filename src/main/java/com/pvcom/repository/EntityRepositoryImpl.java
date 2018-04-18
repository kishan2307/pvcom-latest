package com.pvcom.repository;

import com.pvcom.common.WorkflowConstants;
import com.pvcom.model.UserWorkflows;
import com.pvcom.model.Workflow;
import com.pvcom.projections.SearchBean;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

@Repository
public class EntityRepositoryImpl implements EntityRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object> search(SearchBean searchBean,String type) {
        List<Object> list = new ArrayList<>();
        try {
            Query q = createQuery(searchBean,type);
            if (q != null) {
                list = q.getResultList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Query createQuery(@NotNull SearchBean searchBean, String type) {

        List<String> Andbuffer = new LinkedList<String>();
        List<String> Orbuffer = new LinkedList<String>();
        StringBuffer queryString = new StringBuffer();
        Map<String, Object> map = new LinkedHashMap<>();
        if (searchBean.getFrom() != null && searchBean.getTo() != null) {
            Andbuffer.add("DATE(w.date) >=STR_TO_DATE(:from,'%d-%m-%Y') AND DATE(w.date) <=STR_TO_DATE(:to,'%d-%m-%Y')");
            map.put("from", searchBean.getFrom());
            map.put("to", searchBean.getTo());
        } else if (searchBean.getFrom() != null) {
            Andbuffer.add(" DATE(w.date) >=STR_TO_DATE(:from,'%d-%m-%Y')");
            map.put("from", searchBean.getFrom());
        } else if (searchBean.getTo() != null) {
            Andbuffer.add(" DATE(w.date) <=STR_TO_DATE(:to,'%d-%m-%Y')");
            map.put("to", searchBean.getTo());
        }

        Andbuffer.add("w.status=:sts");
        if ("case".equals(type)) {
            map.put("sts", WorkflowConstants.DeaultValues.WORKFLOW_TYPE_WORKLOW_STATUS.getIntValue());
        } else {
            map.put("sts", WorkflowConstants.DeaultValues.WORKFLOW_TYPE_ENTRY_STATUS.getIntValue());
        }


        if (!StringUtils.isEmpty(searchBean.getLocal_uniq_id())) {
            Orbuffer.add("w.local_uniq_id LIKE :ref");
            map.put("ref", "%" + searchBean.getLocal_uniq_id() + "%");
        }
        if (!StringUtils.isEmpty(searchBean.getSource())) {
            Orbuffer.add("w.source LIKE :source");
            map.put("source", "%" + searchBean.getSource() + "%");
        }

        if (!StringUtils.isEmpty(searchBean.getCountry())) {
            Orbuffer.add("w.country LIKE :country");
            map.put("country", searchBean.getCountry());
        }

        if (!StringUtils.isEmpty(searchBean.getLp_name())) {
            Orbuffer.add("w.lp_name LIKE :lp");
            map.put("lp", "%" + searchBean.getLp_name() + "%");
        }

        if (!StringUtils.isEmpty(searchBean.getDrugs())) {
            Orbuffer.add("w.drugs LIKE :drug");
            map.put("drug", "%" + searchBean.getDrugs() + "%");
        }

        if (!StringUtils.isEmpty(searchBean.getExpedite())) {
            Orbuffer.add("w.expedite LIKE :expedite");
            map.put("expedite", searchBean.getExpedite());
        }

        if (!StringUtils.isEmpty(searchBean.getSeriousness())) {
            Orbuffer.add("w.seriousness LIKE :seriousness");
            map.put("seriousness", searchBean.getSeriousness());
        }

        if (!StringUtils.isEmpty(searchBean.getDate())) {
            Orbuffer.add("DATE(w.date)=STR_TO_DATE(:date,'%d-%m-%Y')");
            map.put("date", searchBean.getDate());
        }

        if (!StringUtils.isEmpty(searchBean.getCompany_receive_date())) {
            Orbuffer.add("DATE(w.company_receive_date)=STR_TO_DATE(:company_receive_date,'%d-%m-%Y')");
            map.put("company_receive_date", searchBean.getCompany_receive_date());
        }

        if (!Andbuffer.isEmpty()) {
            int i = 0;
            for (String s : Andbuffer) {
                if (i > 0) {
                    queryString.append(" AND ");
                }
                queryString.append(s);
                i++;
            }
        }

        if (!Orbuffer.isEmpty()) {
            int i = 0;
            if (!StringUtils.isEmpty(queryString.toString())) {
                queryString.append(" AND ( ");
            } else {
                queryString.append("( ");
            }
            for (String s : Orbuffer) {
                if (i > 0) {
                    queryString.append(" OR ");
                }
                queryString.append(s);
                i++;
            }
            queryString.append(" ) ");
        }
        Query q = null;
        if (!StringUtils.isEmpty(queryString.toString())) {
            if ("case".equals(type)) {
                q = em.createQuery("SELECT u FROM UserWorkflows u join u.workflow w where " + queryString.toString());
            } else {
                q = em.createQuery("SELECT w FROM Workflow w where " + queryString.toString());
            }
            for (String name : map.keySet()) {
                q.setParameter(name, map.get(name));
            }
        }
        return q;
    }
}