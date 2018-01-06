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

import genome.metronome.model.GapMetronomeSettings;
import genome.metronome.model.MetronomeSettings;
import genome.metronome.model.MetronomeType;
import genome.metronome.model.RegularMetronomeSettings;
import genome.metronome.model.SoundSettings;
import genome.metronome.model.SpeedMetronomeSettings;
import genome.metronome.model.TimedMetronomeSettings;
import genome.metronome.utils.MetronomeConstants;
import genome.metronome.utils.MetronomeContract;
import genome.metronome.utils.MetronomeDependencyInjector;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class MetronomeHandler implements MetronomeContract.Presenter {
  
  private MetronomeContract.View view;
  private MetronomeContract.Model model;
  private RegularMetronome regularMetronome;
  private GapMetronome gapMetronome;
  private TimedMetronome timedMetronome;
  private SpeedMetronome speedMetronome;

  public MetronomeHandler(MetronomeContract.View view,
                          MetronomeContract.Model model) {
    setView(view);
    setModel(model);
  }

  public MetronomeContract.View getView() {
    return view;
  }

  public void setView(MetronomeContract.View view) {
    this.view = view;
  }

  public MetronomeContract.Model getModel() {
    return model;
  }

  public void setModel(MetronomeContract.Model model) {
    this.model = model;
  }

  public RegularMetronome getRegularMetronome() {
    return regularMetronome;
  }

  public void setRegularMetronome(RegularMetronome regularMetronome) {
    this.regularMetronome = regularMetronome;
  }

  public GapMetronome getGapMetronome() {
    return gapMetronome;
  }

  public void setGapMetronome(GapMetronome gapMetronome) {
    this.gapMetronome = gapMetronome;
  }

  public TimedMetronome getTimedMetronome() {
    return timedMetronome;
  }

  public void setTimedMetronome(TimedMetronome timedMetronome) {
    this.timedMetronome = timedMetronome;
  }

  public SpeedMetronome getSpeedMetronome() {
    return speedMetronome;
  }

  public void setSpeedMetronome(SpeedMetronome speedMetronome) {
    this.speedMetronome = speedMetronome;
  }

  @Override
  public void playMetronome() {
    switch (model.readMetronomeType()) {
      case REG:
        getRegularMetronome().play(); break;
      case GAP:
        getGapMetronome().play(); break;
      case TIMED:
        getTimedMetronome().play(); break;
      case SPEED:
        getSpeedMetronome().play(); break;
      default: break;
    }
  }

  @Override
  public void stopMetronome() {
    switch (model.readMetronomeType()) {
      case REG:
        getRegularMetronome().stop(); break;
      case GAP:
        getGapMetronome().stop(); break;
      case TIMED:
        getTimedMetronome().stop(); break;
      case SPEED:
        getSpeedMetronome().stop(); break;
      default: break;
    }
  }

  @Override
  public void initialize() {
    try {
      //1. acquire system resources
      SoundRez soundRez = MetronomeDependencyInjector.getSoundRez();
      soundRez.getResources();
      
      //2. retrieve previous session
      model.readPreviousSessionFromFile();
      
      //3. acquire sounds
      soundRez.prepareSounds(
        model.readSoundSetting(MetronomeConstants.MetronomeSettingsKeys.ACCENT),
        model.readSoundSetting(MetronomeConstants.MetronomeSettingsKeys.BEAT), 
        model.readSoundSetting(MetronomeConstants.MetronomeSettingsKeys.CLICK), 
        model.readSoundSetting(
          MetronomeConstants.MetronomeSettingsKeys.TEMPO_CHANGE)
      );
      
      //4. create all metronomes and load their previous settings
      setRegularMetronome(
        new RegularMetronome(
          (Float) model.readMetronomeSetting(
            MetronomeType.REG, 
            MetronomeConstants.MetronomeSettingsKeys.TEMPO
          ), 
          (Integer) model.readMetronomeSetting(
            MetronomeType.REG, 
            MetronomeConstants.MetronomeSettingsKeys.MEASURE
          ), 
          (Integer) model.readMetronomeSetting(
            MetronomeType.REG, 
            MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION
          )
        )
      );
      
      setGapMetronome(
        new GapMetronome(
          (Integer) model.readMetronomeSetting(
            MetronomeType.GAP, 
            MetronomeConstants.MetronomeSettingsKeys.LOUD_MEASURES
          ),
          (Integer) model.readMetronomeSetting(
            MetronomeType.GAP, 
            MetronomeConstants.MetronomeSettingsKeys.SILENT_MEASURES
          ),
          (Integer) model.readMetronomeSetting(
            MetronomeType.GAP, 
            MetronomeConstants.MetronomeSettingsKeys.GAP_LENGTH_INCREMENT
          ),
          (Integer) model.readMetronomeSetting(
            MetronomeType.GAP, 
            MetronomeConstants.MetronomeSettingsKeys.GAP_REPETITIONS
          ),
          (Float) model.readMetronomeSetting(
            MetronomeType.GAP, 
            MetronomeConstants.MetronomeSettingsKeys.TEMPO
          ),
          (Integer) model.readMetronomeSetting(
            MetronomeType.GAP, 
            MetronomeConstants.MetronomeSettingsKeys.MEASURE
          ), 
          (Integer) model.readMetronomeSetting(
            MetronomeType.GAP, 
            MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION
          )
        )
      );
      
      setTimedMetronome(
        new TimedMetronome(
          (Integer) model.readMetronomeSetting(
            MetronomeType.TIMED, 
            MetronomeConstants.MetronomeSettingsKeys.DURATION
          ),
          (Float) model.readMetronomeSetting(
            MetronomeType.TIMED, 
            MetronomeConstants.MetronomeSettingsKeys.TEMPO
          ),
          (Integer) model.readMetronomeSetting(
            MetronomeType.TIMED, 
            MetronomeConstants.MetronomeSettingsKeys.MEASURE
          ), 
          (Integer) model.readMetronomeSetting(
            MetronomeType.TIMED, 
            MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION
          )
        )
      );
      
      setSpeedMetronome(
        new SpeedMetronome(
          (Integer) model.readMetronomeSetting(
            MetronomeType.SPEED, 
            MetronomeConstants.MetronomeSettingsKeys.TEMPO_LENGTH
          ),
          (Float) model.readMetronomeSetting(
            MetronomeType.SPEED, 
            MetronomeConstants.MetronomeSettingsKeys.TEMPO_INCREMENT
          ),
          (Float) model.readMetronomeSetting(
            MetronomeType.SPEED, 
            MetronomeConstants.MetronomeSettingsKeys.START_TEMPO
          ),
          (Float) model.readMetronomeSetting(
            MetronomeType.SPEED, 
            MetronomeConstants.MetronomeSettingsKeys.END_TEMPO
          ),
          (Integer) model.readMetronomeSetting(
            MetronomeType.TIMED, 
            MetronomeConstants.MetronomeSettingsKeys.MEASURE
          ), 
          (Integer) model.readMetronomeSetting(
            MetronomeType.TIMED, 
            MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION
          )
        )
      );
      
      //5. query the last used metronome and give it the sound resources
      switch (model.readMetronomeType()) {
        case REG:
          getRegularMetronome().setSoundRez(soundRez); break;
        case GAP:
          getGapMetronome().setSoundRez(soundRez); break;
        case TIMED:
          getTimedMetronome().setSoundRez(soundRez); break;
        case SPEED:
          getSpeedMetronome().setSoundRez(soundRez); break;
        default: break;
      }
    } catch (LineUnavailableException e) {
      if (e.getMessage() != null)
        view.displayMessage(
          "Required system resources are unavailable.\n\n" + 
          e.getMessage()
        );
      else 
        view.displayMessage("Required system resources are unavailable.\n\n");
    } catch (IOException e) {
      if (e.getMessage() != null)
        view.displayMessage("I/O Error occured.\n\n" + e.getMessage());
      else view.displayMessage("I/O Error occured.\n\n");
    } catch (UnsupportedAudioFileException e) {
      if (e.getMessage() != null)
        view.displayMessage(
          "Sound files specified are unsupported.\n\n" + 
          e.getMessage()
        );
      else 
        view.displayMessage("Sound files specified are unsupported.\n\n");
    }
  }

  @Override
  public void clean() {
    try {      
      //1. release system resources
      switch (model.readMetronomeType()) {
        case REG:
          getRegularMetronome().getSoundRez().releaseResources(); break;
        case GAP:
          getGapMetronome().getSoundRez().releaseResources(); break;
        case TIMED:
          getTimedMetronome().getSoundRez().releaseResources(); break;
        case SPEED:
          getSpeedMetronome().getSoundRez().releaseResources(); break;
        default: break;
      }
      
      //2. store current session
      model.writeCurrentSessionToFile();
    } catch (IOException e) {
      if (e.getMessage() != null)
        view.displayMessage("I/O Error occured.\n\n" + e.getMessage());
      else view.displayMessage("I/O Error occured.\n\n");
    }
  }

  @Override
  public void reset(MetronomeType currentType, MetronomeType targetType) {
    updateMetronomeType(targetType);
    
    SoundRez soundRez = MetronomeDependencyInjector.getSoundRez();
    
    switch (currentType) {
      case REG:
        getRegularMetronome().setSoundRez(null); break;
      case GAP:
        getGapMetronome().setSoundRez(null); break;
      case TIMED:
        getTimedMetronome().setSoundRez(null); break;
      case SPEED:
        getSpeedMetronome().setSoundRez(null); break;
      default: break;
    }
    
    switch (targetType) {
      case REG:
        getRegularMetronome().setSoundRez(soundRez); break;
      case GAP:
        getGapMetronome().setSoundRez(soundRez); break;
      case TIMED:
        getTimedMetronome().setSoundRez(soundRez); break;
      case SPEED:
        getSpeedMetronome().setSoundRez(soundRez); break;
      default: break;
    }
  }

  @Override
  public void updateMetronomeSettings(MetronomeType metType,
                                      HashMap<String, Number> settings) {
    model.writeMetronomeSettings(metType, settings);
  }

  @Override
  public void updateMetronomeSetting(MetronomeType metType, String settingKey,
                                     Number setting) {
    model.writeMetronomeSetting(metType, settingKey, setting);
  }

  @Override
  public void updateMetronomeType(MetronomeType metType) {
    model.writeMetronomeType(metType);
  }

  @Override
  public void updateSoundSetting(String soundName, String soundFilePath) {
    model.writeSoundSetting(soundName, soundFilePath);
  }

  @Override
  public SoundSettings getSoundSettings() {
    return model.readSoundSettings();
  }

  @Override
  public String getSoundSetting(String soundName) {
    return model.readSoundSetting(soundName);
  }

  @Override
  public LinkedHashMap<String, ? extends MetronomeSettings> 
        getMetronomePresets(MetronomeType metType) {
    return model.readMetronomePresets(metType);
  }

  @Override
  public MetronomeSettings getMetronomePreset(MetronomeType metType,
                                              String presetName) {
    return model.readMetronomePreset(metType, presetName);
  }

  @Override
  public void loadMetronomePreset(MetronomeType metType, String presetName) {
    MetronomeSettings preset = model.readMetronomePreset(metType, presetName);
    HashMap<String, Number> settings 
      = getHashMapFromMetronomeSettings(metType, preset);
    switch (metType) {
      case REG:
        getRegularMetronome().bulkSet(settings); break;
      case GAP:
        getGapMetronome().bulkSet(settings); break;
      case TIMED:
        getTimedMetronome().bulkSet(settings); break;
      case SPEED:
        getSpeedMetronome().bulkSet(settings); break;
      default: break;
    }
  }

  @Override
  public void saveMetronomePreset(MetronomeType metType, String presetName,
                                  HashMap<String, Number> settings) {
    MetronomeSettings preset 
      = getMetronomeSettingsFromHashMap(metType, settings);
    model.addMetronomePreset(metType, presetName, preset);
  }
  
  private MetronomeSettings 
    getMetronomeSettingsFromHashMap(MetronomeType metType, 
                                    HashMap<String, Number> hashMap) {
    MetronomeSettings settings = null;
    switch (metType) {
      case REG:
        settings = new RegularMetronomeSettings(
          (Float) hashMap.get(MetronomeConstants.MetronomeSettingsKeys.TEMPO),
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.MEASURE),
          (Integer) 
            hashMap.get(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION)
        ); break;
      case GAP:
        settings = new GapMetronomeSettings(
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.LOUD_MEASURES),
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.SILENT_MEASURES),
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.GAP_LENGTH_INCREMENT),
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.GAP_REPETITIONS),
          (Float) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.TEMPO),
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.MEASURE),
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.SUB_DIVISION)
        ); break;
      case TIMED:
        settings = new TimedMetronomeSettings(
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.DURATION),
          (Float) hashMap.get(MetronomeConstants.MetronomeSettingsKeys.TEMPO),
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.MEASURE),
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.SUB_DIVISION)
        ); break;
      case SPEED:
        settings = new SpeedMetronomeSettings(
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.TEMPO_LENGTH),
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.TEMPO_INCREMENT),
          (Float) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.START_TEMPO),
          (Float) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.END_TEMPO),
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.MEASURE),
          (Integer) hashMap.get(MetronomeConstants
            .MetronomeSettingsKeys.SUB_DIVISION)
        ); break;
      default: break;
    }
    return settings;
  }
  
  private HashMap<String, Number> 
    getHashMapFromMetronomeSettings(MetronomeType metType, 
                                    MetronomeSettings settings) {
    HashMap<String, Number> hashMap = new HashMap<>();
    switch (metType) {
      case REG:
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO, 
                    ((RegularMetronomeSettings) settings).getTempo());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                    ((RegularMetronomeSettings) settings).getMeasure());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                    ((RegularMetronomeSettings) settings).getSubDivision());
        break;
      case GAP:
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.LOUD_MEASURES, 
                    ((GapMetronomeSettings) settings).getLoudMeasures());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.SILENT_MEASURES, 
                    ((GapMetronomeSettings) settings).getSilentMeasures());
        hashMap.put(
          MetronomeConstants.MetronomeSettingsKeys.GAP_LENGTH_INCREMENT, 
          ((GapMetronomeSettings) settings).getGapLengthIncrement()
        );
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.GAP_REPETITIONS, 
                    ((GapMetronomeSettings) settings).getGapRepetitions());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO, 
                    ((GapMetronomeSettings) settings).getTempo());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                    ((GapMetronomeSettings) settings).getMeasure());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                    ((GapMetronomeSettings) settings).getSubDivision());
        break;
      case TIMED:
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.DURATION, 
                    ((TimedMetronomeSettings) settings).getDuration());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO, 
                    ((TimedMetronomeSettings) settings).getTempo());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                    ((TimedMetronomeSettings) settings).getMeasure());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                    ((TimedMetronomeSettings) settings).getSubDivision());
        break;
      case SPEED:
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.START_TEMPO, 
                    ((SpeedMetronomeSettings) settings).getStartTempo());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.END_TEMPO, 
                    ((SpeedMetronomeSettings) settings).getEndTempo());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO_LENGTH, 
                    ((SpeedMetronomeSettings) settings).getTempoLength());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO_INCREMENT, 
                    ((SpeedMetronomeSettings) settings).getTempoIncrement());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                    ((SpeedMetronomeSettings) settings).getMeasure());
        hashMap.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                    ((SpeedMetronomeSettings) settings).getSubDivision());
        break;
      default: break;
    }
    return hashMap;
  }
}
