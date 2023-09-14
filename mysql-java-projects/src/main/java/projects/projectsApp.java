package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class projectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;

	// @formatter:off
		private List<String> operations = List.of(
			"1) Add a project",
			"2) List projects",
			"3) Select a project",
			"4) Update project details",
			"5) Delete a project"
				);
		// @formatter:on

	public static void main(String[] args) {
		new projectsApp().processUserSelections();
	} // END OF MAIN

	private void processUserSelections() {
		boolean done = false;

		while (!done) {
			try {
				int selection = getUserSelection();

				switch (selection) {
				case -1:
					done = exitMenu();
					break;

				case 1:
					createProject();
					break;
				case 2:
					listProjects();
					break;

				case 3:
					selectProject();
					break;

				case 4:
					updateProjectDetails();
					break;

				case 5:
					deleteProject();
					break;

				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
					break;
				}
			} catch (Exception e) {
				System.out.println("\nError: " + e + " Try again.");
			}
		}
	} // END OF PROCESS USER SELECTIONS

	private void deleteProject() {
		listProjects(); // call method -> list projects

		Integer projectId = getIntInput("Enter the ID of the project to delete");

		projectService.deleteProject(projectId);
		System.out.println("Project " + projectId + " was deleted.");

// add a check to see if the project ID in the current project is the same as the ID entered. If so, set the value of the curProject to null	
		if (Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId))
			curProject = null;
	} // END OF DELETE PROJECT

	private void updateProjectDetails() {
		if (Objects.isNull(curProject)) {
			System.out.println("Please select a project");
			return;
			// above - check to see if current project is null then add the message to
			// select a project
		} // END OF UPDATE PROJECT DETAILS

		String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		BigDecimal estimatedHours = getDecimalInput(
				"Enter the estimated hours [" + curProject.getEstimatedHours() + "]");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
		Integer difficulty = getIntInput("Enter the project difficulty [" + curProject.getDifficulty() + "]");
		String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");

		Project project = new Project();

		project.setProjectId(curProject.getProjectId());
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);

// BELOW - call the project.service.modifyProjectDetqails() Pass the Project object as a parameter. Let eclipse create the method
// for you in ProjectService.java		
		projectService.modifyProjectDetails(project);

//BELOW -reread the current project to pick up the changers by calling projectService.fetchProjectById(). Pass the project ID
//obtained from curProject
		curProject = projectService.fetchProjectById(curProject.getProjectId());

	} // END OF updateProjectDetails method

	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
		curProject = null;
		curProject = projectService.fetchProjectById(projectId);
	}// END OF SELECT PROJECT

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();

		System.out.println("\nProjects");

		projects.forEach(project -> System.out.println(" " + project.getProjectId() + ": " + project.getProjectName()));
	}// END OF LIST PROJECTS

	private void createProject() {
		String projectName = getStringInput("Enter projectName");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");

		Project project = new Project();

		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);

		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
	}// END OF CREATE PROJECT

	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	} // END OF GET BIG DECIMAL INPUT

	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	} // END OF EXIT MENU

	private int getUserSelection() {
		printOperations();

		Integer input = getIntInput("Enter a menu selection");

		return Objects.isNull(input) ? -1 : input;
	} // END OF GET USER SELECTION

	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	} // END OF GET USER INPUT

	private String getStringInput(String prompt) {
		System.out.println(prompt + " : ");
		String input = scanner.nextLine();

		return input.isBlank() ? null : input.trim();
	} // END OF GET STRING INPUT

	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");
		operations.forEach(line -> System.out.println(" " + line));

		if (Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		} else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	} // END OF PRINT OPERATIONS

}