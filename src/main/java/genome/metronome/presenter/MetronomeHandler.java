/*
 * Copyright (C) 2017 William Kibirango <williamkaos.kibirango76@gmail.com>
 *
 * This file is part of GENOME.
 *
 * GENOME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GENOME is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GENOME.  If not, see <http://www.gnu.org/licenses/>.
 */
package genome.metronome.presenter;

import genome.metronome.utils.MetronomeConstants;
import genome.metronome.utils.MetronomeContract;
import genome.metronome.utils.MetronomeDependencyInjector;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class MetronomeHandler implements MetronomeContract.Presenter, 
                                               Observer {
  
  private MetronomeContract.View view;
  private GapMetronome gapMetronome;
  private TimedMetronome timedMetronome;
  private SpeedMetronome speedMetronome;

  public MetronomeHandler(MetronomeContract.View view) {
    setView(view);
  }

  private MetronomeContract.View getView() {
    return view;
  }

  private void setView(MetronomeContract.View view) {
    this.view = view;
  }

  private GapMetronome getGapMetronome() {
    return gapMetronome;
  }

  private void setGapMetronome(GapMetronome gapMetronome) {
    this.gapMetronome = gapMetronome;
  }

  private TimedMetronome getTimedMetronome() {
    return timedMetronome;
  }

  private void setTimedMetronome(TimedMetronome timedMetronome) {
    this.timedMetronome = timedMetronome;
  }

  private SpeedMetronome getSpeedMetronome() {
    return speedMetronome;
  }

  private void setSpeedMetronome(SpeedMetronome speedMetronome) {
    this.speedMetronome = speedMetronome;
  }

  @Override
  public void playMetronome(MetronomeType type) {
    SoundRez soundRez = MetronomeDependencyInjector.getSoundRez();
    switch (type) {
      case GAP:
        getGapMetronome().setSoundRez(soundRez);
        getGapMetronome().play(); 
        break;
      case TIMED:
        getTimedMetronome().setSoundRez(soundRez);
        getTimedMetronome().play(); 
        break;
      case SPEED:
        getSpeedMetronome().setSoundRez(soundRez);
        getSpeedMetronome().play(); 
        break;
      default: break;
    }
  }

  @Override
  public void stopMetronome(MetronomeType type) {
    switch (type) {
      case GAP:
        getGapMetronome().stop(); 
        getGapMetronome().setSoundRez(null);
        break;
      case TIMED:
        getTimedMetronome().stop(); 
        getTimedMetronome().setSoundRez(null);
        break;
      case SPEED:
        getSpeedMetronome().stop(); 
        getSpeedMetronome().setSoundRez(null);
        break;
      default: break;
    }
  }

  @Override
  public void initialize() {
    ClassLoader loader = this.getClass().getClassLoader();
    try {
      //1. acquire system resources
      SoundRez soundRez = MetronomeDependencyInjector.getSoundRez();
      soundRez.getResources();
      
      //2. acquire sounds
      if (!(soundRez.getSoundsFromFiles(
        loader.getResource(MetronomeConstants
          .DEFAULT_ACCENT_SOUND_FILE).getFile(),
        loader.getResource(MetronomeConstants
          .DEFAULT_BEAT_SOUND_FILE).getFile(), 
        loader.getResource(MetronomeConstants
          .DEFAULT_CLICK_SOUND_FILE).getFile(), 
        loader.getResource(MetronomeConstants
          .DEFAULT_TEMPO_CHANGE_SOUND_FILE).getFile()
      )))
        getView().displayMessage("Sound files are unprepared.\n");
      
      //3. create all metronomes and register as an observer
      setGapMetronome(new GapMetronome());
      getGapMetronome().addObserver(this);
      setTimedMetronome(new TimedMetronome());
      getTimedMetronome().addObserver(this);
      setSpeedMetronome(new SpeedMetronome());
      getSpeedMetronome().addObserver(this);
      
    } catch (LineUnavailableException e) {
      if (e.getMessage() != null)
        getView().displayMessage(
          "Required system resources are unavailable. " + 
          e.getMessage()
        );
      else 
        getView()
          .displayMessage("Required system resources are unavailable.");
    } catch (IOException e) {
      if (e.getMessage() != null)
        getView().displayMessage("I/O Error occured. " + e.getMessage());
      else getView().displayMessage("I/O Error occured.");
    } catch (UnsupportedAudioFileException e) {
      if (e.getMessage() != null)
        getView().displayMessage(
          "Sound files specified are unsupported. " + 
          e.getMessage()
        );
      else 
        getView().displayMessage("Sound files specified are unsupported.");
    }
  }

  @Override
  public void clean() {
    //1. release system resources
    MetronomeDependencyInjector.getSoundRez().releaseResources();
    
    //2. deregister handler
    getTimedMetronome().deleteObservers();
    getGapMetronome().deleteObservers();
    getSpeedMetronome().deleteObservers();
  }

  @Override
  public void updateMetronomeSettings(MetronomeType type,
                                      HashMap<String, Number> settings) {
    switch (type) {
      case GAP:
        getGapMetronome().bulkSet(settings);
        getView().displayMetronomeSettings(type, 
                                           getGapMetronome().getSettings());
        break;
      case TIMED:
        getTimedMetronome().bulkSet(settings);
        getView().displayMetronomeSettings(type, 
                                           getTimedMetronome().getSettings());
        break;
      case SPEED:
        getSpeedMetronome().bulkSet(settings);
        getView().displayMetronomeSettings(type, 
                                           getSpeedMetronome().getSettings());
        break;
      default: break;
    }
  }

  @Override
  public Metronome registerObserver(MetronomeType type, Observer ob) {
    switch (type) {
      case GAP:
        getGapMetronome().addObserver(ob); 
        return getGapMetronome();
      case TIMED:
        getTimedMetronome().addObserver(ob); 
        return getTimedMetronome();
      case SPEED:
        getSpeedMetronome().addObserver(ob); 
        return getSpeedMetronome();
      default: return null;
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof Metronome && 
        ((String) arg).equals(MetronomeConstants.Metronome.
          AudioTasks.M_AUTO_STOPPED)) {
      ((Metronome) o).executor.shutdown();
      while (((Metronome) o).executor.isTerminated()) {}
      ((Metronome) o).autoStopped = false;
//      System.out.println("Tasks terminated...");
    }
  }
}
