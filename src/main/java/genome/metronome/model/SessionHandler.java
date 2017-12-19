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
package genome.metronome.model;

import genome.metronome.utils.MetronomeContract;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class SessionHandler implements MetronomeContract.Model {
  
  private Session session;
  private static SessionHandler instance = null;

  private SessionHandler() {
  }
  
  public static SessionHandler getInstance() {
    if (instance == null) instance = new SessionHandler();
    return instance;
  }
  
  public static void destroyInstance() {
    instance = null;
  }

  private Session getSession() {
    return session;
  }

  private void setSession(Session session) {
    this.session = session;
  }
  
  private MetronomeType readMetronomeTypeFromFile() {
    return null;
  }
  
  private HashMap<String, Number> 
        readMetronomeSettingsFromFile(MetronomeType metType) {
    return null;
  }
  
  private LinkedHashMap<String, ? extends MetronomeSettings> 
        readMetronomePresetsFromFile(MetronomeType metType) {
    return null;
  }
  
  private String readSoundSettingFromFile(String soundKey) {
    return null;
  }
  
  private SoundSettings readSoundSettingsFromFile() {
    return null;
  }
  
  private void writeMetronomeTypeToFile(MetronomeType metType) {
    
  }
  
  private void writeMetronomeSettingsToFile(MetronomeType metType, 
                                            HashMap<String, Number> settings) {
    
  }
  
  private void 
        writeMetronomePresetsToFile(MetronomeType metType,
                                    LinkedHashMap<String,
                                        ? extends MetronomeSettings> presets) {
    
  }
  
  private void writeSoundSettingsToFile(SoundSettings soundSettings) {
    
  }

  @Override
  public HashMap<String, ? extends Number> 
        readMetronomeSettings(MetronomeType metType) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Number readMetronomeSetting(MetronomeType metType, 
                                     String settingsKey) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public MetronomeType readMetronomeType() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public LinkedHashMap<String, ? extends MetronomeSettings> 
        readMetronomePresets(MetronomeType metType) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public MetronomeSettings readMetronomePreset(MetronomeType metType,
                                               String presetKey) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public SoundSettings readSoundSettings() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String readSoundSetting(String soundKey) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void writeMetronomeSettings(MetronomeType metType,
                                     HashMap<String, Number> settings) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void writeMetronomeSetting(MetronomeType metType, String settingsKey,
                                    Number setting) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void writeMetronomeType(MetronomeType metType) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void writeMetronomePreset(MetronomeType metType, String presetName, 
                                   MetronomeSettings preset) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void writeSoundSetting(String soundKey, String soundSetting) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void writeSoundSettings(String accentSound, String beatFile,
                                 String clickFile, String tempoChangeSound) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void addMetronomePreset(MetronomeType metType, 
                                 String presetName, MetronomeSettings preset) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void removeMetronomePreset(MetronomeType metType, String presetName) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void removeMetronomePresets(MetronomeType metType, 
                                     String[] presetNames) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setDefaults() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void writeCurrentSessionToFile() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void readPreviousSessionFromFile() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
