package dao;

import java.util.List;

import pojo.Classtable;

public interface CourseDao {
	public Classtable insertCourse(Classtable ct);
	public List<Classtable> queryCourse(String params);
}
