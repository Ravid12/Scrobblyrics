package application.view;

import application.MainApp;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import utils.FileUtils;
import utils.WebUtils;

public class MainWindowController{

	@FXML
	private ComboBox<Theme> cb;
	
	@FXML
	private Label artist;

	@FXML
	private Label song;

	@FXML
	private TextArea lyrics;

	@FXML
	private TextField userField;
	
	private MainApp mainApp;
	
	private String data;
	
	private String newUser, previousUser;
	
	private String[] songData = new String[3];
	
	private boolean isFirstSong = true;
	
	private ObservableList<Theme> items = FXCollections.observableArrayList(Theme.values());
	
	private Timeline timer = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent event) {
	        new Thread(new TaskDisplayLyrics<Void>()).start();
	    }
	}));
	
	
	/**
	 * The constructor.
	 * The constructor is called before the initialize() method.
	 */
	public MainWindowController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		cb.setPromptText("Select Theme...");
		cb.setItems(items);
		
		userField.setText(FileUtils.readLastUser());
	}

	@FXML
	private void handleGetLyricsButton() {
		newUser = userField.getText();
		FileUtils.writeLastUser(newUser);
		if (!newUser.equals(previousUser)) {
			timer.stop();
			previousUser=newUser;
			
	        new Thread(new TaskDisplayLyrics<Void>()).start();
	        timer.setCycleCount(Timeline.INDEFINITE);
	        timer.play();
		}
	}
	
	@FXML
	private void handleThemeChange() {		
		ObservableList<String> sheets = mainApp.getPrimaryStage().getScene().getRoot().getStylesheets();
		sheets.remove(0, sheets.size());
		sheets.add(cb.getValue().getUrl());
	}
	
	class TaskDisplayLyrics<Void> extends Task<Void>{
		@Override 
		public Void call() {
			try {
				if(lyrics.getText().length()<1) {
					lyrics.setText("Fetching Lyrics...");
				}
				data = WebUtils.getRecentTracks(newUser);
				String songArtist = (data.substring(data.indexOf("#text")+8, data.indexOf("mbid")-3));				
				String songName = (data.substring(data.indexOf("name")+7, data.indexOf("streamable")-3));

				//TODO: use this to show album as background
//				try {
//					System.out.println(WebUtils.getSongInfo(songArtist, songName));
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
				
				//TODO: this is a temporary solution for removing "- Remastered (year)" or "- single" suffixes
				if (songName.contains("-")) {
					songName = songName.substring(0, songName.indexOf("-"));
				}

				boolean songChanged = !(songName.equals(songData[0])) || !(songArtist.equals(songData[1])); 
				
				if (isFirstSong || songChanged) {
					// save name and artist
					songData[0]=songName;
					songData[1]=songArtist;
					
					// Gets rid of non alphanumeric characters and sets to lowercase for url
					songArtist = songArtist.replaceAll("[^A-Za-z0-9]+", "").toLowerCase();
					songName = songName.replaceAll("[^A-Za-z0-9]+", "").toLowerCase();
					if (songArtist.startsWith("the")) {
						songArtist = songArtist.substring(3);
					}
					
					String urlString = "https://azlyrics.com/lyrics/"+songArtist+"/"+songName+".html";
					
					
					String lyricsT = WebUtils.pullHTML(urlString);
	
					// Lyrics on AZ lyrics are between these two tags
					String start = "<!-- Usage of azlyrics.com content by any third-party lyrics provider is prohibited by our licensing agreement. Sorry about that. -->";
					String end = "<!-- MxM banner -->";
					
					
					try {
						// Gets lyrics and removes all special characters and HTML elements
						lyricsT = (lyricsT.split(start))[1].split(end)[0];
						lyricsT = lyricsT.replace("<br>","").replace("</br>","").replace("<i>","").replace("</i>","").replace("</div>","").replace("&quot;",  "\"").trim();
						lyricsT = lyricsT.replaceAll("[^A-Za-z0-9\\s.!?'\"()\\[\\]]+", "");
						songData[2]=lyricsT;
					} catch (Exception e){
						System.out.println("Lyrics not found on AZ lyrics...");
						lyricsT = "Sorry! Lyrics not found on AZ lyrics...";
						songData[2]=lyricsT;
					}
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null; 
			
		}
		

		//TODO: The GUI freezes during this function...
		@Override protected void succeeded() {
			// Only update display if the SONG NAME changed
			boolean songChanged = (!song.getText().equals(songData[0]) || !artist.getText().equals(songData[1]));
			
			if (isFirstSong || songChanged){
				isFirstSong=false;
				song.setText(songData[0]);
				artist.setText(songData[1]);	
				lyrics.setText(songData[2]);
			}
		}
	};

	
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}


