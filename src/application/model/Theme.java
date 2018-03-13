package application.model;

public enum Theme {
	DARK("Dark", "rgb(41,41,41)", "rgb(25,25,25)", "rgb(255,255,255)", "rgb(50,50,130)"),
	FROG("Frog", "rgb(0,162,232)", "rgb(153,217,234)", "rgb(0,0,0)", "rgb(181,230,29)"),
	PUNKDARK("Punk", "rgb(41,41,41)", "rgb(25,25,25)", "rgb(255,255,255)", "rgb(120,0,120)"),
	TRAFFIC("Traffic", "rgb(50,200,180)", "rgb(255,255,255)", "rgb(0,0,30)", "rgb(230,30,50)");

	public final String backdrop;
	public final String background;
	public final String text;
	public final String feature;
	private final String name;
	
	Theme(String name, String backdrop, String background, String text, String feature){
		this.backdrop=backdrop;
		this.background=background;
		this.text=text;
		this.feature=feature;
		this.name=name;
	}
	
	public String toString() {
		return this.name + " Theme";
	}
	
	public String getUrl(){
		return "./application/view/" + name + ".css";
	}
}
