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

import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class SoundRez {
  
  private byte[] accentSound;
  private byte[] beatSound;
  private byte[] clickSound;
  private byte[] tempoChangeSound;
  private SourceDataLine line;
  private static SoundRez instance = null;

  private SoundRez() {
  }
  
  public static SoundRez getInstance() {
    if (instance == null) instance = new SoundRez();
    return instance;
  }
  
  public static void destroyInstance() {
    instance = null;
  }
  
  public boolean prepareSound(String soundFile) {
    return false;
  }
  
  public boolean prepareSounds(String accentFile, String beatFile, 
                                                  String clickFile, 
                                                  String tempoChangeFile) {
    return false;
  }
  
  public boolean isValid(String soundFile) {
    return false;
  }
  
  private byte[] getAudioData(String soundFile) {
    return null;
  }
  
  public void getResources() {
    
  }
  
  public void releaseResources() {
    
  }

  public byte[] getAccentSound() {
    return accentSound;
  }

  public void setAccentSound(byte[] accentSound) {
    this.accentSound = accentSound;
  }

  public byte[] getBeatSound() {
    return beatSound;
  }

  public void setBeatSound(byte[] beatSound) {
    this.beatSound = beatSound;
  }

  public byte[] getClickSound() {
    return clickSound;
  }

  public void setClickSound(byte[] clickSound) {
    this.clickSound = clickSound;
  }

  public byte[] getTempoChangeSound() {
    return tempoChangeSound;
  }

  public void setTempoChangeSound(byte[] tempoChangeSound) {
    this.tempoChangeSound = tempoChangeSound;
  }

  public SourceDataLine getLine() {
    return line;
  }

  public void setLine(SourceDataLine line) {
    this.line = line;
  }
}
