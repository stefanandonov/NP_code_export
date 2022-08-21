import java.util.ArrayList;
import java.util.List;


public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);
        
        System.out.println(player.toString());
        System.out.println("First test");
        
        
        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();
        
        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();
        
        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();
        
        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();
        
        
        System.out.println(player.toString());
        System.out.println("Second test");
        
        
        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();
        
        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();
        
        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();
        
        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();
        
        
        System.out.println(player.toString());
        System.out.println("Third test");
        
        
        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();
        
        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();
        
        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();
        
        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();
        
        
        System.out.println(player.toString());
        
    }
}
class Song{
    String title;
    String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Song{" + "title=" + title + ", artist=" + artist + '}';
    }
    
}

class MP3Player {
    List<Song> songList;
    int currentSong;
    
    State play;
    State pause;
    State stop;
    State fwd;
    State rew;
    
    final void createStates(){
        play = new PlayState(this);
        pause = new PauseState(this);
        stop = new StopState(this);
        fwd = new FWDState(this);
        rew = new REWState(this);
        state = stop;
    }

    public MP3Player(List<Song> songList) {
        this.songList = songList;
        currentSong = 0;
        createStates();
    }

    public State getPlay() {
        return play;
    }

    public void setPlay(State play) {
        this.play = play;
    }

    public State getPause() {
        return pause;
    }

    public void setPause(State pause) {
        this.pause = pause;
    }

    public State getStop() {
        return stop;
    }

    public void setStop(State stop) {
        this.stop = stop;
    }

    public State getFwd() {
        return fwd;
    }

    public void setFwd(State fwd) {
        this.fwd = fwd;
    }

    public State getRew() {
        return rew;
    }

    public void setRew(State rew) {
        this.rew = rew;
    }
    
    State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
    
    public Song getCurrentSong() {
        return songList.get(currentSong);
    }
    
    public void setSongIndex(int currentSong) {
        this.currentSong = currentSong % songList.size();
    }

    public int getSongIndex() {
        return currentSong;
    }
    
    
    public void songFWD(){
        currentSong = (currentSong + 1) % songList.size();
    }
    
    public void songREW(){
        currentSong = (currentSong + songList.size() - 1) % songList.size();
    }
    
    public void pressPlay(){
        state.pressPlay();
    }
    
    public void pressStop(){
        state.pressStop();
    }
    
    public void pressFWD(){
        state.pressFwd();
        state.forward();
    }
    
    public void pressREW(){
        state.pressRew();
        state.reward();
    }
    
    void printCurrentSong(){
        System.out.println(getCurrentSong());
    }
    
    @Override
    public String toString() {
        return "MP3Player{currentSong = " + currentSong + ", songList = " + songList + "}";
    }
    
    
}

interface State{
    void pressPlay();
    void pressStop();
    void pressFwd();
    void pressRew();
    void forward();
    void reward();
}


abstract class AbstractState implements State{
    MP3Player mp3;

    public AbstractState(MP3Player mp3) {
        this.mp3 = mp3;
    }
    
}

class FWDState extends AbstractState{
    public FWDState(MP3Player mp3) {
        super(mp3);
    }
    
    @Override
    public void pressPlay() {
        System.out.println("Illegal action");
    }

    @Override
    public void pressStop() {
        System.out.println("Illegal action");
    }

    @Override
    public void pressFwd() {
        System.out.println("Illegal action");
    }

    @Override
    public void pressRew() {
        System.out.println("Illegal action");
    }

    @Override
    public void forward() {
        mp3.songFWD();
        mp3.setState(mp3.getPause());
    }

    @Override
    public void reward() {
        System.out.println("Illegal action");
    }
    
}


class REWState extends AbstractState{
    public REWState(MP3Player mp3) {
        super(mp3);
    }
    
    @Override
    public void pressPlay() {
        System.out.println("Illegal action");
    }

    @Override
    public void pressStop() {
        System.out.println("Illegal action");
    }

    @Override
    public void pressFwd() {
        System.out.println("Illegal action");
    }

    @Override
    public void pressRew() {
        System.out.println("Illegal action");
    }

    @Override
    public void forward() {
        System.out.println("Illegal action");
    }

    @Override
    public void reward() {
        mp3.songREW();
        mp3.setState(mp3.getPause());
    }
    
}


class PlayState extends AbstractState{

    public PlayState(MP3Player mp3) {
        super(mp3);
    }

    @Override
    public void pressPlay() {
        System.out.println("Song is already playing");
    }

    @Override
    public void pressStop() {
        System.out.println("Song " + mp3.getSongIndex() + " is paused");
        mp3.setState(mp3.getPause());
    }

    @Override
    public void pressFwd() {
        System.out.println("Forward...");
        mp3.setState(mp3.getFwd());
    }

    @Override
    public void pressRew() {
        System.out.println("Reward...");
        mp3.setState(mp3.getRew());
    }

    @Override
    public void forward() {
        System.out.println("Illegal action");
    }

    @Override
    public void reward() {
        System.out.println("Illegal action");
    }
    
}


class StopState extends AbstractState {

    public StopState(MP3Player mp3) {
        super(mp3);
    }

    @Override
    public void pressPlay() {
        System.out.println("Song " + mp3.getSongIndex() + " is playing");
        mp3.setState(mp3.getPlay());
    }

    @Override
    public void pressStop() {
        System.out.println("Songs are already stopped");
    }

    @Override
    public void pressFwd() {
        System.out.println("Forward...");
        mp3.setState(mp3.getFwd());
    }

    @Override
    public void pressRew() {
        System.out.println("Reward...");
        mp3.setState(mp3.getRew());
    }

    @Override
    public void forward() {
        System.out.println("Illegal action");
    }

    @Override
    public void reward() {
        System.out.println("Illegal action");
    }
    
}

class PauseState extends AbstractState{

    public PauseState(MP3Player mp3) {
        super(mp3);
    }

    @Override
    public void pressPlay() {
        System.out.println("Song " + mp3.getSongIndex() + " is playing");
        mp3.setState(mp3.getPlay());
    }

    @Override
    public void pressStop() {
        System.out.println("Songs are stopped");
        mp3.setSongIndex(0);
        mp3.setState(mp3.getStop());
    }

    @Override
    public void pressFwd() {
        System.out.println("Forward...");
        mp3.setState(mp3.getFwd());
    }

    @Override
    public void pressRew() {
        System.out.println("Reward...");
        mp3.setState(mp3.getRew());
    }

    @Override
    public void forward() {
        System.out.println("Illegal action");
    }

    @Override
    public void reward() {
        System.out.println("Illegal action");
    }
}