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
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

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
  
  public boolean prepareSound(SoundType type, String soundFile) 
    throws IOException, UnsupportedAudioFileException {
    if (isValid(soundFile)) {
      switch (type) {
        case ACCENT:
          setAccentSound(getAudioData(soundFile)); return true;
        case BEAT:
          setBeatSound(getAudioData(soundFile)); return true;
        case CLICK:
          setClickSound(getAudioData(soundFile)); return true;
        case TEMPO_CHANGE:
          setTempoChangeSound(getAudioData(soundFile)); return true;
        default:
          return false;
      }
    } else return false;
  }
  
  public boolean prepareSounds(String accentFile, String beatFile, 
                                                  String clickFile, 
                                                  String tempoChangeFile) 
    throws IOException, UnsupportedAudioFileException{
    return prepareSound(SoundType.ACCENT, accentFile) &&
           prepareSound(SoundType.BEAT, beatFile) &&
           prepareSound(SoundType.CLICK, clickFile) && 
           prepareSound(SoundType.TEMPO_CHANGE, tempoChangeFile);
  }
  
  public boolean isValid(String soundFile) 
    throws IOException, UnsupportedAudioFileException {
    File file = new File(soundFile);
    try (AudioInputStream ais 
      = AudioSystem.getAudioInputStream(file)) {
      AudioFormat af = ais.getFormat();
      AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
      
      float bitRate = af.getSampleRate() * af.getSampleSizeInBits()
                * af.getChannels();
      float duration = (ais.getFrameLength() * af.getFrameSize() * 8)
                / bitRate;
      
      return af.getChannels() == MetronomeConstants.SoundRez.NUM_CHANNELS &&
             !af.isBigEndian() && 
             af.getFrameSize() == MetronomeConstants.SoundRez.FRAME_SIZE &&
             af.getEncoding() == MetronomeConstants.ENCODING &&
             aff.getType() == MetronomeConstants.TYPE &&
             Math.abs(af.getFrameRate() - 
                      MetronomeConstants.SoundRez.FRAME_RATE) < 
             MetronomeConstants.FLOAT_ERROR_BOUND &&
             Math.abs(bitRate - MetronomeConstants.SoundRez.BIT_RATE) < 
             MetronomeConstants.FLOAT_ERROR_BOUND &&
             Math.abs(duration - MetronomeConstants.SoundRez.DURATION) < 
             MetronomeConstants.FLOAT_ERROR_BOUND;
    }
  }
  
  private byte[] getAudioData(String soundFile) 
    throws IOException, UnsupportedAudioFileException {
    try (AudioInputStream ais 
      = AudioSystem.getAudioInputStream(new File(soundFile))) {
      int frameSize = ais.getFormat().getFrameSize(), 
        numBytesRead, totalBytesRead = 0, 
        bufferSize = (int) Math.ceil(frameSize * ais.getFrameLength());
      byte[] data = new byte[bufferSize];
      
      while ((numBytesRead = ais.read(data)) != -1) {
        totalBytesRead += numBytesRead;
      }
      
      if (totalBytesRead <= bufferSize) return data;
      else return null;
    }
  }
  
  public void getResources() throws LineUnavailableException {
    setLine(AudioSystem.getSourceDataLine(MetronomeConstants
      .DEFAULT_AUDIO_FORMAT));
  }
  
  public void releaseResources() {
    setLine(null);
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
