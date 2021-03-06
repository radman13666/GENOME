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
import java.io.IOException;
import java.io.InputStream;
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
  
  public boolean getSoundFromFile(SoundType type, InputStream soundFile) 
    throws IOException, UnsupportedAudioFileException {
    switch (type) {
      case ACCENT:
        setAccentSound(getAudioData(soundFile)); 
        return getAccentSound() != null;
      case BEAT:
        setBeatSound(getAudioData(soundFile)); 
        return getBeatSound() != null;
      case CLICK:
        setClickSound(getAudioData(soundFile)); 
        return getClickSound() != null;
      case TEMPO_CHANGE:
        setTempoChangeSound(getAudioData(soundFile)); 
        return getTempoChangeSound() != null;
      default:
        return false;
    }
  }
  
  public boolean getSoundsFromFiles(InputStream accentFile, 
                                    InputStream beatFile, 
                                    InputStream clickFile, 
                                    InputStream tempoChangeFile) 
    throws IOException, UnsupportedAudioFileException {
    return getSoundFromFile(SoundType.ACCENT, accentFile) &&
           getSoundFromFile(SoundType.BEAT, beatFile) &&
           getSoundFromFile(SoundType.CLICK, clickFile) && 
           getSoundFromFile(SoundType.TEMPO_CHANGE, tempoChangeFile);
  }
  
  public boolean isValid(InputStream soundFile) 
    throws IOException, UnsupportedAudioFileException {
    try (AudioInputStream ais 
      = AudioSystem.getAudioInputStream(soundFile)) {
      AudioFormat af = ais.getFormat();
      
      float bitRate 
        = af.getSampleRate() * af.getSampleSizeInBits() * af.getChannels();
      long numBits = ais.getFrameLength() * af.getFrameSize() * 8;
      float duration = numBits / bitRate;
      
      return af.getChannels() == MetronomeConstants.SoundRez.NUM_CHANNELS &&
             !af.isBigEndian() && 
             af.getEncoding() == MetronomeConstants.ENCODING &&
             af.getFrameSize() == MetronomeConstants.SoundRez.FRAME_SIZE &&
             af.getSampleSizeInBits() == MetronomeConstants.SoundRez.SAMPLE_SIZE 
             && Math.abs(af.getFrameRate() - 
                      MetronomeConstants.SoundRez.FRAME_RATE) < 
             MetronomeConstants.FLOAT_ERROR_BOUND &&
             Math.abs(af.getSampleRate() - 
                      MetronomeConstants.SoundRez.SAMPLE_RATE) < 
             MetronomeConstants.FLOAT_ERROR_BOUND &&
             duration < MetronomeConstants.SoundRez.DURATION;
    }
  }
  
  private byte[] getAudioData(InputStream soundFile) 
    throws IOException, UnsupportedAudioFileException {
    try (AudioInputStream ais 
      = AudioSystem.getAudioInputStream(soundFile)) {
      int numBytesRead, totalBytesRead = 0, 
        bufferSize 
        = (int) (ais.getFormat().getFrameSize() * ais.getFrameLength());
      byte[] data = new byte[bufferSize];
      
      while ((numBytesRead = ais.read(data)) != -1) {
        totalBytesRead += numBytesRead;
      }
      
      if (totalBytesRead <= bufferSize) return data;
      else return null;
    }
  }
  
  public boolean getResources() throws LineUnavailableException {
    if (getLine() == null) setLine(
      AudioSystem.getSourceDataLine(MetronomeConstants.DEFAULT_AUDIO_FORMAT)
    );
    if (!(getLine() == null || getLine().isOpen())) 
      getLine().open(MetronomeConstants.DEFAULT_AUDIO_FORMAT/*, 
        MetronomeConstants.Metronome.AudioTasks.SDL_BUFFER_SIZE*/);
    return getLine() != null && getLine().isOpen();
  }
  
  public void releaseResources() {
    if (getLine() != null && getLine().isOpen()) getLine().close();
    setLine(null);
  }

  public byte[] getAccentSound() {
    return accentSound;
  }

  private void setAccentSound(byte[] accentSound) {
    this.accentSound = accentSound;
  }

  public byte[] getBeatSound() {
    return beatSound;
  }

  private void setBeatSound(byte[] beatSound) {
    this.beatSound = beatSound;
  }

  public byte[] getClickSound() {
    return clickSound;
  }

  private void setClickSound(byte[] clickSound) {
    this.clickSound = clickSound;
  }

  public byte[] getTempoChangeSound() {
    return tempoChangeSound;
  }

  private void setTempoChangeSound(byte[] tempoChangeSound) {
    this.tempoChangeSound = tempoChangeSound;
  }

  public synchronized SourceDataLine getLine() {
    return line;
  }

  private void setLine(SourceDataLine line) {
    this.line = line;
  }
}
