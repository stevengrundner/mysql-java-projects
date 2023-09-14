package projects.service;

import java.util.List;
import projects.exception.DbException;
import java.util.NoSuchElementException;
import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectService {
	private ProjectDao projectDao = new ProjectDao();

	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	} // END OF ADD PROJECT

	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	} // END OF FETCH ALL PROJECTS

	public Project fetchProjectById(Integer projectId) {
		return projectDao.fetchProjectById(projectId).orElseThrow(
				() -> new NoSuchElementException("Project with project ID=" + projectId + " does not exist."));
	} // END OF FETCH PROJECT BY ID

	public void modifyProjectDetails(Project project) {
		if (!projectDao.modifyProjectDetails(project)) {
			throw new DbException("Project with ID=" + project.getProjectId() + " does not exist.");
		}
	} // END OF MODIFY PROJECT DETAILS

	public void deleteProject(Integer projectId) {
		if (!projectDao.deleteProject(projectId)) {
			throw new DbException("Project with ID=" + projectId + " does not exist.");
		}
	} // END OF DELETE PROJECT
}
