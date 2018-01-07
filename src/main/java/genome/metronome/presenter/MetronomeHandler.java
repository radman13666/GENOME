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
    switch (getModel().readMetronomeType()) {
      case REG:
        getRegularMetronome().play();
        getModel().writeMetronomeSettings(MetronomeType.REG, 
                                          getRegularMetronome().getSettings());
        getView().displayMetronomeSettings(getModel().readMetronomeSettings(
          MetronomeType.REG));
        break;
      case GAP:
        getGapMetronome().play(); 
        getModel().writeMetronomeSettings(MetronomeType.GAP, 
                                          getGapMetronome().getSettings());
        getView().displayMetronomeSettings(getModel().readMetronomeSettings(
          MetronomeType.GAP));
        break;
      case TIMED:
        getTimedMetronome().play(); 
        getModel().writeMetronomeSettings(MetronomeType.TIMED, 
                                          getTimedMetronome().getSettings());
        getView().displayMetronomeSettings(getModel().readMetronomeSettings(
          MetronomeType.TIMED));
        break;
      case SPEED:
        getSpeedMetronome().play(); 
        getModel().writeMetronomeSettings(MetronomeType.SPEED, 
                                          getSpeedMetronome().getSettings());
        getView().displayMetronomeSettings(getModel().readMetronomeSettings(
          MetronomeType.SPEED));
        break;
      default: break;
    }
  }

  @Override
  public void stopMetronome() {
    switch (getModel().readMetronomeType()) {
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
      getModel().readPreviousSessionFromFile();
      
      //3. acquire sounds
      if (!(soundRez.getSoundsFromFiles(
        getSoundSetting(MetronomeConstants.MetronomeSettingsKeys.ACCENT),
        getSoundSetting(MetronomeConstants.MetronomeSettingsKeys.BEAT), 
        getSoundSetting(MetronomeConstants.MetronomeSettingsKeys.CLICK), 
        getSoundSetting(MetronomeConstants.MetronomeSettingsKeys.TEMPO_CHANGE)
      )))
        getView().displayMessage("Sound files are unprepared.\n");
      
      //4. create all metronomes and load their previous settings
      setRegularMetronome(new RegularMetronome());
      getRegularMetronome()
        .bulkSet(getModel().readMetronomeSettings(MetronomeType.REG));
      
      setGapMetronome(new GapMetronome());
      getGapMetronome()
        .bulkSet(getModel().readMetronomeSettings(MetronomeType.GAP));
      
      setTimedMetronome(new TimedMetronome());
      getTimedMetronome()
        .bulkSet(getModel().readMetronomeSettings(MetronomeType.TIMED));
      
      setSpeedMetronome(new SpeedMetronome());
      getSpeedMetronome()
        .bulkSet(getModel().readMetronomeSettings(MetronomeType.SPEED));
      
      //5. query the last used metronome and give it the sound resources
      switch (getModel().readMetronomeType()) {
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
        getView().displayMessage(
          "Required system resources are unavailable.\n\n" + 
          e.getMessage()
        );
      else 
        getView()
          .displayMessage("Required system resources are unavailable.\n\n");
    } catch (IOException e) {
      if (e.getMessage() != null)
        getView().displayMessage("I/O Error occured.\n\n" + e.getMessage());
      else getView().displayMessage("I/O Error occured.\n\n");
    } catch (UnsupportedAudioFileException e) {
      if (e.getMessage() != null)
        getView().displayMessage(
          "Sound files specified are unsupported.\n\n" + 
          e.getMessage()
        );
      else 
        getView().displayMessage("Sound files specified are unsupported.\n\n");
    }
  }

  @Override
  public void clean() {
    try {      
      //1. release system resources
      MetronomeDependencyInjector.getSoundRez().releaseResources();
      
      //2. store current session
      getModel().writeCurrentSessionToFile();
    } catch (IOException e) {
      if (e.getMessage() != null)
        getView().displayMessage("I/O Error occured.\n\n" + e.getMessage());
      else getView().displayMessage("I/O Error occured.\n\n");
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
    switch (metType) {
      case REG:
        getRegularMetronome().bulkSet(settings);
        getModel().writeMetronomeSettings(metType, 
                                     getRegularMetronome().getSettings());
        break;
      case GAP:
        getGapMetronome().bulkSet(settings);
        getModel().writeMetronomeSettings(metType, 
                                          getGapMetronome().getSettings());
        break;
      case TIMED:
        getTimedMetronome().bulkSet(settings);
        getModel().writeMetronomeSettings(metType,
                                     getTimedMetronome().getSettings());
        break;
      case SPEED:
        getSpeedMetronome().bulkSet(settings);
        getModel().writeMetronomeSettings(metType, 
                                     getSpeedMetronome().getSettings());
        break;
      default: break;
    }
    getView().displayMetronomeSettings(getModel().readMetronomeSettings(
          metType));
  }

  @Override
  public void updateMetronomeSetting(MetronomeType metType, String settingKey,
                                     Number setting) {
    switch (metType) {
      case REG:
        switch (settingKey) {
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO:
            getRegularMetronome().setTempo((Float) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getRegularMetronome().getTempo());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.MEASURE:
            getRegularMetronome().setMeasure((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getRegularMetronome().getMeasure());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION:
            getRegularMetronome().setSubDivision((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getRegularMetronome().getSubDivision());
            break;
        } 
        break;
      case GAP:
        switch (settingKey) {
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO:
            getGapMetronome().setTempo((Float) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getGapMetronome().getTempo());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.MEASURE:
            getGapMetronome().setMeasure((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getGapMetronome().getMeasure());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION:
            getGapMetronome().setSubDivision((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getGapMetronome().getSubDivision());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.LOUD_MEASURES:
            getGapMetronome().setLoudMeasures((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getGapMetronome().getLoudMeasures());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.SILENT_MEASURES:
            getGapMetronome().setSilentMeasures((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getGapMetronome().getSilentMeasures());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.GAP_LENGTH_INCREMENT:
            getGapMetronome().setGapLengthIncrement((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getGapMetronome().getGapLengthIncrement());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.GAP_REPETITIONS:
            getGapMetronome().setGapRepetitions((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getGapMetronome().getGapRepetitions());
            break;
        } 
        break;
      case TIMED:
        switch (settingKey) {
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO:
            getTimedMetronome().setTempo((Float) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getTimedMetronome().getTempo());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.MEASURE:
            getTimedMetronome().setMeasure((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getTimedMetronome().getMeasure());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION:
            getTimedMetronome().setSubDivision((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getTimedMetronome().getSubDivision());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.DURATION:
            getTimedMetronome().setDuration((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getTimedMetronome().getDuration());
            break;
        } 
        break;
      case SPEED:
        switch (settingKey) {
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO_LENGTH:
            getSpeedMetronome().setTempoLength((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getSpeedMetronome().getTempoLength());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.MEASURE:
            getSpeedMetronome().setMeasure((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getSpeedMetronome().getMeasure());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION:
            getSpeedMetronome().setSubDivision((Integer) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getSpeedMetronome().getSubDivision());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO_INCREMENT:
            getSpeedMetronome().setTempoIncrement((Float) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getSpeedMetronome().getTempoIncrement());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.START_TEMPO:
            getSpeedMetronome().setStartTempo((Float) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getSpeedMetronome().getStartTempo());
            break;
          case MetronomeConstants.MetronomeSettingsKeys.END_TEMPO:
            getSpeedMetronome().setEndTempo((Float) setting);
            getModel().writeMetronomeSetting(
              metType, settingKey, getSpeedMetronome().getEndTempo());
            break;
        } 
        break;
    }
    getView().displayMetronomeSetting(
      settingKey, getModel().readMetronomeSetting(metType, settingKey));
  }

  @Override
  public void updateMetronomeType(MetronomeType metType) {
    getModel().writeMetronomeType(metType);
  }

  @Override
  public void updateSoundSetting(String soundName, String soundFilePath) {
    SoundRez soundRez = MetronomeDependencyInjector.getSoundRez();
    SoundType type = null;
    switch (soundName) {
      case MetronomeConstants.MetronomeSettingsKeys.ACCENT:
        type = SoundType.ACCENT; break;
      case MetronomeConstants.MetronomeSettingsKeys.BEAT:
        type = SoundType.BEAT; break;
      case MetronomeConstants.MetronomeSettingsKeys.CLICK:
        type = SoundType.CLICK; break;
      case MetronomeConstants.MetronomeSettingsKeys.TEMPO_CHANGE:
        type = SoundType.TEMPO_CHANGE;
    }
    try {
      if (soundRez.getSoundFromFile(type, soundFilePath)) {
        getModel().writeSoundSetting(soundName, soundFilePath);
        getView().displaySoundSetting(soundName, getSoundSetting(soundName));
      } else getView().displayMessage("Sound file is unprepared.\n");
    } catch (IOException e) {
      if (e.getMessage() != null)
        getView().displayMessage("I/O Error occured.\n\n" + e.getMessage());
      else getView().displayMessage("I/O Error occured.\n\n");
    } catch (UnsupportedAudioFileException e) {
      if (e.getMessage() != null)
        getView().displayMessage(
          "Sound file specified is unsupported.\n\n" + 
          e.getMessage()
        );
      else 
        getView().displayMessage("Sound file specified is unsupported.\n\n");
    }
  }

  @Override
  public HashMap<String, String> getSoundSettings() {
    HashMap<String, String> soundSettings = new HashMap<>();
    SoundSettings sounds = getModel().readSoundSettings();
    soundSettings.put(MetronomeConstants.MetronomeSettingsKeys.ACCENT, 
                      sounds.getAccentSound());
    soundSettings.put(MetronomeConstants.MetronomeSettingsKeys.BEAT, 
                      sounds.getBeatSound());
    soundSettings.put(MetronomeConstants.MetronomeSettingsKeys.CLICK, 
                      sounds.getClickSound());
    soundSettings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO_CHANGE, 
                      sounds.getTempoChangeSound());
    return soundSettings;
  }

  @Override
  public String getSoundSetting(String soundName) {
    return getModel().readSoundSetting(soundName);
  }

  @Override
  public LinkedHashMap<String, ? extends MetronomeSettings> 
        getMetronomePresets(MetronomeType metType) {
    return getModel().readMetronomePresets(metType);
  }

  @Override
  public MetronomeSettings getMetronomePreset(MetronomeType metType,
                                              String presetName) {
    return getModel().readMetronomePreset(metType, presetName);
  }

  @Override
  public void loadMetronomePreset(MetronomeType metType, String presetName) {
    MetronomeSettings preset 
      = getModel().readMetronomePreset(metType, presetName);
    HashMap<String, Number> presetSettings 
      = getHashMapFromMetronomeSettings(metType, preset);
    switch (metType) {
      case REG:
        getRegularMetronome().bulkSet(presetSettings); break;
      case GAP:
        getGapMetronome().bulkSet(presetSettings); break;
      case TIMED:
        getTimedMetronome().bulkSet(presetSettings); break;
      case SPEED:
        getSpeedMetronome().bulkSet(presetSettings); break;
      default: break;
    }
    getView().displayMetronomeSettings(presetSettings);
  }

  @Override
  public void saveMetronomePreset(MetronomeType metType, String presetName,
                                  HashMap<String, Number> settings) {
    MetronomeSettings preset 
      = getMetronomeSettingsFromHashMap(metType, settings);
    getModel().addMetronomePreset(metType, presetName, preset);
    getView().displayMetronomePreset(presetName, getModel().readMetronomePreset(
                                     metType, presetName));
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
