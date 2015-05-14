import java.util.ArrayList;

public class LexiconObject {

	private String lexemeForm;
	private String lexemeFormIPA;
	private String morphemeType;
	private String complexFormType;
	private String gloss;
	private String category;
	private ArrayList<String> glosses = new ArrayList<String>();
	private ArrayList<String> categories = new ArrayList<String>();

	public LexiconObject(String lexemeForm, String lexemeFormIPA, String morphemeType,
			String complexFormType, String gloss, String category) {
		this.lexemeForm = lexemeForm;
		this.lexemeFormIPA = lexemeFormIPA;
		this.morphemeType = morphemeType;
		this.complexFormType = complexFormType;
		this.gloss = gloss;
		this.category = category;
		glosses.add(gloss);
		categories.add(category);
	}
	
	// getMethods
	public String getLexemeForm() { return lexemeForm; }
	public String getLexemeFormIPA() { return lexemeFormIPA; }
	public String getMorphemeType() { return morphemeType; }
	public String getComplexFormType() { return complexFormType; }
	public String getGloss() { return gloss; }
	public ArrayList<String> getGlosses() { return glosses; }
	public String getCategory() { return category; }
	public ArrayList<String> getCategories() { return categories; }
	
	public int getSenseCount() { return glosses.size(); }
	public String getGloss(int sense) { return glosses.get(sense-1); }
	public String getCategory(int sense) { return categories.get(sense-1); }
	
	// setMethods
	public void setLexemeForm(String lexemeForm) { this.lexemeForm = lexemeForm; }
	public void setLexemeFormIPA(String lexemeFormIPA) { this.lexemeFormIPA = lexemeFormIPA; }
	public void setMorphemeType(String morphemeType) { this.morphemeType = morphemeType; }
	public void setComplexFormType(String complexFormType) { this.complexFormType = complexFormType; }
	public void setGloss(String gloss) { this.gloss = gloss; }
	public void setCategory(String category) { this.category = category; }
	public void setGloss(int sense, String gloss) { this.glosses.set(sense-1, gloss); }
	public void setCategory(int sense, String category) { this.categories.set(sense-1, category); }
	
	// addSense
	public void addSense(String gloss, String category) {
		glosses.add(gloss);
		categories.add(category);
	}
	
	// deleteSense
	public void deleteSense(int index) {
		glosses.remove(index-1);
		categories.remove(index-1);
	}
	
	// toString
	public String toString() {
		return this.lexemeForm;
	}
	
}
