package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {

	// scanner instantiation
	private Scanner scanner = new Scanner(System.in);

	// project service instantiation
	private ProjectService projectService = new ProjectService();

	private Project curProject;
	
	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project",
			"2) List projects",
			"3) Select Project"
			);
	// @formatter:on

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// calls process user selections
		new ProjectsApp().processUserSelections();

	}
//selection block
	private void processUserSelections() {
		boolean done = false;
		//while not done
		while(!done) {
			try {
				//retrieving user selection
				int selection = getUserSelection();
				
				switch(selection) {
				case -1:
					done = exitMenu();
					break;
					
				case 1:
					createProject();
					break;
					
				case 2:
					createProject();
					break;
					
				case 3:
					selectProject();
					break;
					
				default:
					System.out.println("\n" + selection + " isn't valid. Please enter a valid selection.");
					break;
				}
			}
			//exception catch
			catch(Exception e) {
				System.out.println("\nError: " + e + " Try again.");
			}
		}
	}

	
	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
		
		curProject = null;
		
		curProject = projectService.fetchProjectById(projectId);
	}
	
	
	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects:");
		
		projects.forEach(project -> System.out.println("   " + project.getProjectId() + ": " + project.getProjectName()));
	}
	

	
	//declaration for project parameter prompts.
	private void createProject() {
		//used with scanner inputs
		String projectName = getStringInput("Enter project name");
		BigDecimal estimatedHours = getDecimalInput("Enter estimation for hours spent");
		BigDecimal actualHours = getDecimalInput("Enter hours actually spent");
		Integer difficulty = getIntInput("Enter the project difficulty on a scale from 1-5");
		String notes = getStringInput("Enter any notes for the project");
	
		//calling project
		Project project = new Project();
		
		//setters to transfer data input via scanner to project.(i)
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("Project creation: " + dbProject + " Success");
	}
	
	
	
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + "isn't a valid decimal number");
		}
	}
	
	
	private boolean exitMenu() {
		System.out.println("Menu exited");
		return true;
	}
	
	
	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter menu selection");
		return Objects.isNull(input) ? -1 : input;
	}
	
	
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " isn't a valid number");
		}
	}
	
	
	//converting data into readable gui output
	private String getStringInput(String prompt) {
		System.out.println(prompt + ": ");
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
	}
	
	
	private void printOperations() {
		System.out.println("\n Select available entry, or press enter to quit:");
		
		//a little complex so i decided to copy the lambda expression instead of implementing a for loop
		operations.forEach(line -> System.out.println(" " + line));
		
		if(Objects.isNull(curProject)) {
			System.out.println("\nThere is no current project");
		} else {
			System.out.println("\nCurrent project: " + curProject);
		}
	}
	
}