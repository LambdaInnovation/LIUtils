package cn.liutils.crafting;

public class RecipeParsingException extends Exception {
	
	public RecipeParsingException() {
		super();
	}

	public RecipeParsingException(String message) {
		super(message);
		
	}
	
	public RecipeParsingException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
