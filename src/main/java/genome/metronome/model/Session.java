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

import java.util.LinkedHashMap;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class Session {
  
  private RegularMetronomeSettings regularMetronomeSettings;
  private GapMetronomeSettings gapMetronomeSettings;
  private TimedMetronomeSettings timedMetronomeSettings;
  private SpeedMetronomeSettings speedMetronomeSettings;
  private SoundSettings soundSettings;
  private LinkedHashMap<String, RegularMetronomeSettings> 
          regularMetronomePresets;
  private LinkedHashMap<String, GapMetronomeSettings> gapMetronomePresets;
  private LinkedHashMap<String, TimedMetronomeSettings> timedMetronomePresets;
  private LinkedHashMap<String, SpeedMetronomeSettings> speedMetronomePresets;
  private MetronomeType metronomeType;
  private static Session instance = null;

  private Session() {
  }
  
  public static Session getInstance() {
    if (instance == null) instance = new Session();
    return instance;
  }

  public RegularMetronomeSettings getRegularMetronomeSettings() {
    return regularMetronomeSettings;
  }

  public void setRegularMetronomeSettings(
                                          RegularMetronomeSettings 
                                                  regularMetronomeSettings) {
    this.regularMetronomeSettings = regularMetronomeSettings;
  }

  public GapMetronomeSettings getGapMetronomeSettings() {
    return gapMetronomeSettings;
  }

  public void setGapMetronomeSettings(GapMetronomeSettings 
          gapMetronomeSettings) {
    this.gapMetronomeSettings = gapMetronomeSettings;
  }

  public TimedMetronomeSettings getTimedMetronomeSettings() {
    return timedMetronomeSettings;
  }

  public void setTimedMetronomeSettings(TimedMetronomeSettings 
          timedMetronomeSettings) {
    this.timedMetronomeSettings = timedMetronomeSettings;
  }

  public SpeedMetronomeSettings getSpeedMetronomeSettings() {
    return speedMetronomeSettings;
  }

  public void setSpeedMetronomeSettings(SpeedMetronomeSettings 
          speedMetronomeSettings) {
    this.speedMetronomeSettings = speedMetronomeSettings;
  }

  public SoundSettings getSoundSettings() {
    return soundSettings;
  }

  public void setSoundSettings(SoundSettings soundSettings) {
    this.soundSettings = soundSettings;
  }

  public LinkedHashMap<String, RegularMetronomeSettings> 
        getRegularMetronomePresets() {
    return regularMetronomePresets;
  }

  public void setRegularMetronomePresets(
          LinkedHashMap<String, RegularMetronomeSettings> 
                  regularMetronomePresets) {
    this.regularMetronomePresets = regularMetronomePresets;
  }

  public LinkedHashMap<String, GapMetronomeSettings> getGapMetronomePresets() {
    return gapMetronomePresets;
  }

  public void setGapMetronomePresets(LinkedHashMap<String, 
          GapMetronomeSettings> gapMetronomePresets) {
    this.gapMetronomePresets = gapMetronomePresets;
  }

  public LinkedHashMap<String, TimedMetronomeSettings> 
        getTimedMetronomePresets() {
    return timedMetronomePresets;
  }

  public void setTimedMetronomePresets(LinkedHashMap<String, 
          TimedMetronomeSettings> timedMetronomePresets) {
    this.timedMetronomePresets = timedMetronomePresets;
  }

  public LinkedHashMap<String, SpeedMetronomeSettings> 
        getSpeedMetronomePresets() {
    return speedMetronomePresets;
  }

  public void setSpeedMetronomePresets(LinkedHashMap<String, 
          SpeedMetronomeSettings> speedMetronomePresets) {
    this.speedMetronomePresets = speedMetronomePresets;
  }

  public MetronomeType getMetronomeType() {
    return metronomeType;
  }

  public void setMetronomeType(MetronomeType metronomeType) {
    this.metronomeType = metronomeType;
  }
}
