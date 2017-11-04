package vsg.model;

/**
 * Created by Denis Orlov.
 */
public class Items {

	private String[] categoryPaths;

	private String[] categoryIdPaths;

	private String repositoryId;

	private String id;


	private String creationDate;


	private String route;

	private String longDescription;

	private String active;
	private String displayName;

	public String[] getCategoryPaths() {
		return categoryPaths;
	}

	public void setCategoryPaths(String[] categoryPaths) {
		this.categoryPaths = categoryPaths;
	}

	public String[] getCategoryIdPaths() {
		return categoryIdPaths;
	}

	public void setCategoryIdPaths(String[] categoryIdPaths) {
		this.categoryIdPaths = categoryIdPaths;
	}

	public String getRepositoryId() {
		return repositoryId;
	}

	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	@Override
	public String toString() {
		return "ClassPojo [categoryPaths = " + categoryPaths + ", categoryIdPaths = " + categoryIdPaths + ", repositoryId = " + repositoryId + ", id = " + id + ", creationDate = " + creationDate + ", route = " + route + ", longDescription = " + longDescription + ", active = " + active + ", displayName = " + displayName + "]";
	}
}
