package service;

import java.util.List;

import pojo.Classtable;

public interface CourseManager {
	public Classtable insertCourse(Classtable ct);
	public List<Classtable> queryCourse(String params);
}
