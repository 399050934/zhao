package dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import dao.CourseDao;
import pojo.Classtable;
@Repository
public class CourseDaoImpl implements CourseDao {

	@PersistenceContext(name="mysql")
	private EntityManager manager;
	
	@Override
	public Classtable insertCourse(Classtable ct1) {
		// TODO Auto-generated method stub
		
		manager.persist(ct1);
		return ct1;
	}

	@Override
	public List<Classtable> queryCourse(String params) {
		// TODO Auto-generated method stub
		Classtable ct = new Classtable();
		List<Classtable> table = manager.createQuery("from Classtable table where table.parameter = :params")
				.setParameter("params", params)
				.getResultList();
		return table;
	}

}
