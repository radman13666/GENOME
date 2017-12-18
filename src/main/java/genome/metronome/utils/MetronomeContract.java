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
package genome.metronome.utils;

import genome.metronome.model.MetronomeSettings;
import genome.metronome.model.MetronomeType;
import genome.metronome.model.SoundSettings;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class MetronomeContract {

  private MetronomeContract() {
  }
  
  public interface Model {
    HashMap<String, ? extends Number> 
        readMetronomeSettings(MetronomeType metType);
    Number readMetronomeSetting(MetronomeType metType, String settingsKey);
    MetronomeType readMetronomeType();
    LinkedHashMap<String, ? extends MetronomeSettings> 
        readMetronomePresets(MetronomeType metType);
    MetronomeSettings readMetronomePreset(MetronomeType metType, 
                                          String presetKey);
    SoundSettings readSoundSettings();
    String readSoundSetting(String soundKey);
    void writeMetronomeSettings(MetronomeType metType, 
                                HashMap<String, Number> settings);
    void writeMetronomeSetting(MetronomeType metType, String settingsKey, 
                                                      Number setting);
    void writeMetronomeType(MetronomeType metType);
    void writeMetronomePreset(String presetName, MetronomeSettings preset);
    void writeSoundSetting(String soundKey, String soundSetting);
    void writeSoundSettings(String accentSound, String beatFile, 
                                                String clickFile, 
                                                String tempoChangeSound);
    void addMetronomePreset(String presetName, MetronomeSettings preset);
    void removeMetronomePreset(MetronomeType metType, String presetName);
    void removeMetronomePresets(MetronomeType metType, String[] presetNames);
    void setDefaults();
    void writeCurrentSessionToFile();
    void readPreviousSessionFromFile();
  }
  
  public interface View {
    void initialize();
    void displayMessage(String message);
    void displayMetronomeSettings(HashMap<String, Number> metronomeSettings);
    void displayMetronomeSetting(String settingKey, Number setting);
    void displaySoundSettings(SoundSettings soundSettings);
    void displaySoundSetting(String soundSettingKey, String soundSetting);
    void displayMetronomePresets(LinkedHashMap<String,
            ? extends MetronomeSettings> presets);
    void displayMetronomePreset(String presetKey, MetronomeSettings preset);
  }
  
  public interface Presenter {
    void playMetronome();
    void stopMetronome();
    void initialize();
    void clean();
    void reset();
    void updateMetronomeSettings(MetronomeType metType, 
                                 HashMap<String, Number> settings);
    void updateMetronomeSetting(MetronomeType metType, String settingKey, 
                                                       Number setting);
    void updateMetronomeType(MetronomeType metType);
    void updateSoundSetting(String soundName, String soundFilePath);
    SoundSettings getSoundSettings();
    String getSoundSetting(String soundName);
    LinkedHashMap<String, ? extends MetronomeSettings> 
        getMetronomePresets(MetronomeType metType);
    MetronomeSettings getMetronomePreset(MetronomeType metType, 
                                         String presetName);
    void loadMetronomePreset(MetronomeType metType, String presetName);
    void saveMetronomePreset(MetronomeType metType, 
                             String presetName, 
                             HashMap<String, Number> settings);
  }
}
