package com.seeyoo.zm.visit.service;

import com.seeyoo.zm.visit.util.EntityUtils;
import com.seeyoo.zm.visit.util.PageParam;
import com.seeyoo.zm.visit.bean.VisitStatisBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IncomeService {
    @PersistenceContext
    EntityManager entityManager;

    public Map<String,Object> findIncomeDailysByPage(PageParam pageParam, Timestamp start, Timestamp end,Integer assetsId) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder countSelectSql = new StringBuilder();
        countSelectSql.append("select count(id) from Calendar where 1=1 ");

        StringBuilder selectSql = new StringBuilder();
        selectSql.append("SELECT date(dday) as visitDate, count(*) - 1 as visitCount FROM ( SELECT datelist as dday FROM calendar  UNION ALL SELECT time FROM visit_record ");
        if (assetsId!=null){
            selectSql.append(" where assets_id=").append(assetsId);
        }
        selectSql.append(") a where 1=1");
        Map<String, Object> params = new HashMap<>();
        StringBuilder whereSql = new StringBuilder();
        StringBuffer sWhereSql = new StringBuffer();
        if (start != null) {
            whereSql.append(" and datelist >= :startTime");
            params.put("startTime", start);
            sWhereSql.append(" and a.dday >= :startTime");
        }
        if (end != null) {
            whereSql.append(" and datelist <= :endTime");
            sWhereSql.append(" and a.dday <= :endTime");
            params.put("endTime", end);
        }
        String countSql = new StringBuilder().append(countSelectSql).append(whereSql).toString();
        Query countQuery = this.entityManager.createQuery(countSql);
        this.setParameters(countQuery, params);
        Long count = (Long) countQuery.getSingleResult();

        String querySql = new StringBuilder().append(selectSql).append(sWhereSql).append(" GROUP BY visitDate").toString();
        Query query = this.entityManager.createNativeQuery(querySql);
        this.setParameters(query, params);
        if (pageParam != null) { //分页
            query.setFirstResult(pageParam.getStart());
            query.setMaxResults(pageParam.getLength());
        }
        List<VisitStatisBean> incomeDailyList = EntityUtils.castEntity(query.getResultList(), VisitStatisBean.class, new VisitStatisBean());
        map.put("total",count);
        if (pageParam != null) { //分页
            Pageable pageable = new PageRequest(pageParam.getPage(), pageParam.getLength());
            Page<VisitStatisBean> incomeDailyPage = new PageImpl<VisitStatisBean>(incomeDailyList, pageable, count);
            map.put("page",incomeDailyPage);
        } else { //不分页
            Page<VisitStatisBean> incomeDailyPage= new PageImpl<VisitStatisBean>(incomeDailyList);
            map.put("page",incomeDailyPage);
        }
        return map;
    }

    /**
     * 给hql参数设置值
     *
     * @param query  查询
     * @param params 参数
     */
    private void setParameters(Query query, Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }
}
