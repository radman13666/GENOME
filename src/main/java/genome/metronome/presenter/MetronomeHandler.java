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

import genome.metronome.model.MetronomeSettings;
import genome.metronome.model.MetronomeType;
import genome.metronome.model.SoundSettings;
import genome.metronome.utils.MetronomeContract;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void stopMetronome() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void initialize() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void clean() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void reset() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void updateMetronomeSettings(MetronomeType metType,
                                      HashMap<String, Number> settings) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void updateMetronomeSetting(MetronomeType metType, String settingKey,
                                     Number setting) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void updateMetronomeType(MetronomeType metType) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void updateSoundSetting(String soundName, String soundFilePath) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public SoundSettings getSoundSettings() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String getSoundSetting(String soundName) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public LinkedHashMap<String, ? extends MetronomeSettings> 
        getMetronomePresets(MetronomeType metType) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public MetronomeSettings getMetronomePreset(MetronomeType metType,
                                              String presetName) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void loadMetronomePreset(MetronomeType metType, String presetName) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void saveMetronomePreset(MetronomeType metType, String presetName,
                                  HashMap<String, Number> settings) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
