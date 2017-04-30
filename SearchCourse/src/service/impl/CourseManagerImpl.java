package service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dao.CourseDao;
import pojo.Classtable;
import service.CourseManager;
@Service
public class CourseManagerImpl implements CourseManager {

	@Autowired
	private CourseDao courseDao;
	@Transactional
	@Override
	public Classtable insertCourse(Classtable ct) {
		// TODO Auto-generated method stub
		
		return courseDao.insertCourse(ct);
	}

	@Transactional
	@Override
	public List<Classtable> queryCourse(String params) {
		return courseDao.queryCourse(params);
	}

}
