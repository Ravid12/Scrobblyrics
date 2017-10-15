package application.view;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import utils.WebUtils;

public class MainWindowController{

	@FXML
	private Label artist;

	@FXML
	private Label song;

	@FXML
	private TextArea lyrics;

	@FXML
	private TextField userField;

	@FXML
	private TextArea songLyrics;
	
	@FXML
	private ImageView albumBack;
	
	private String data;


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

	}

	@FXML
	private void handleGetLyricsButton() {
		Task<Void> task = new Task<Void>() {
			@Override 
			public Void call() {
				try {
					lyrics.setText("Fetching Lyrics...");
					data = WebUtils.getRecentTracks(userField.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null; 
			}

			@Override protected void succeeded() {
				String songArtist = (data.substring(data.indexOf("#text")+8, data.indexOf("mbid")-3));				
				String songName = (data.substring(data.indexOf("name")+7, data.indexOf("streamable")-3));
				
				try {
					System.out.println(WebUtils.getSongInfo(songArtist, songName));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				
				if (songName.contains("-")) {
					songName = songName.substring(0, songName.indexOf("-"));
				}

				artist.setText(songArtist);
				song.setText(songName);

				songArtist = songArtist.replaceAll("[^A-Za-z0-9]+", "").toLowerCase();
				songName = songName.replaceAll("[^A-Za-z0-9]+", "").toLowerCase();

				if (songArtist.startsWith("the")) {
					songArtist = songArtist.substring(3);
				}

				String urlString = "https://azlyrics.com/lyrics/"+songArtist+"/"+songName+".html";
				WebUtils.pullHTML(urlString);

				String start = "<!-- Usage of azlyrics.com content by any third-party lyrics provider is prohibited by our licensing agreement. Sorry about that. -->";
				String end = "<!-- MxM banner -->";
				String lyricsT = WebUtils.pageHtml;
				try {
					lyricsT = (lyricsT.split(start))[1].split(end)[0];
					lyricsT = lyricsT.replace("<br>","").replace("</br>","").replace("<i>","").replace("</i>","").replace("</div>","").trim();
					lyricsT = lyricsT.replaceAll("[^A-Za-z0-9\\s.!?'\"()]+", "");
				} catch (Exception e){
					System.out.println("Lyrics not found on AZ lyrics...");
					lyricsT = "Sorry! Lyrics not found on AZ lyrics...";
				}
				lyrics.setText("");
				lyrics.setText(lyricsT);
			}

		};

		new Thread(task).start();


	}
}