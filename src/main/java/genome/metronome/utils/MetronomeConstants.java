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

import javax.sound.sampled.AudioFormat;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class MetronomeConstants {
  
  public static final AudioFormat.Encoding ENCODING 
    = AudioFormat.Encoding.PCM_SIGNED;
  public static final AudioFormat DEFAULT_AUDIO_FORMAT 
    = new AudioFormat(ENCODING, SoundRez.SAMPLE_RATE, SoundRez.SAMPLE_SIZE, 
        SoundRez.NUM_CHANNELS, SoundRez.FRAME_SIZE, SoundRez.FRAME_RATE, 
        SoundRez.BIG_ENDIAN
    );
  public static final double FLOAT_ERROR_BOUND = 1E-7;

  private MetronomeConstants() {
  }
  
  public final class Metronome {
    
    public static final int MAX_MEASURE = 20;
    public static final int MIN_MEASURE = 2;
    public static final int NO_MEASURE = 0;
    public static final int COMMON_TIME = 4;
    public static final int MAX_SUB_DIVISION = 12;
    public static final int MIN_SUB_DIVISION = 2;
    public static final int NO_SUB_DIVISION = 0;

    private Metronome() {
    }
    
    public final class AudioTasks {
      public static final byte TEMPO_CHANGE_MARKER = 4;
      public static final byte ACCENT_MARKER = 3;
      public static final byte BEAT_MARKER = 2;
      public static final byte CLICK_MARKER = 1;
      public static final float DUTY_CYCLE = 0.5F; //of the period
      public static final String HOST = "localhost";
      public static final String SM_CURRENT_TEMPO = "currentTempo";
      public static final int SERVER_PORT = 6699;
      public static final int UNIT_BUFFER_SIZE = 1024; // 1 KiB
      public static final int CAT_BUFFER_SIZE = UNIT_BUFFER_SIZE * 160;
      public static final int BOS_BUFFER_SIZE = UNIT_BUFFER_SIZE * 80;
      public static final int BIS_BUFFER_SIZE = UNIT_BUFFER_SIZE * 160;
      public static final int WAT_BUFFER_SIZE = UNIT_BUFFER_SIZE * 16;
      //public static final int SDL_BUFFER_SIZE = UNIT_BUFFER_SIZE * 160;
      
      private AudioTasks() {
      }
      
    }
  }
  
  public final class ConstantTempoMetronome {
    
    public static final float MAX_TEMPO = 400.0F;
    public static final float MIN_TEMPO = 5.0F;
    public static final float DEFAULT_TEMPO = 70.0F;

    private ConstantTempoMetronome() {
    }
    
  }
  
  public final class VariableTempoMetronome {
    
    public static final float MAX_START_TEMPO 
      = ConstantTempoMetronome.MAX_TEMPO - 20.0F;
    public static final float MIN_START_TEMPO 
      = ConstantTempoMetronome.MIN_TEMPO + 10.0F;
    public static final float DEFAULT_START_TEMPO = 90.0F;
    public static final float MAX_END_TEMPO 
      = ConstantTempoMetronome.MAX_TEMPO - 10.0F;
    public static final float MIN_END_TEMPO 
      = ConstantTempoMetronome.MIN_TEMPO + 20.0F;
    public static final float DEFAULT_END_TEMPO = 120.0F;

    private VariableTempoMetronome() {
    }
    
  }
  
  public final class TimedMetronome {
    
    public static final int MAX_DURATION = 120;
    public static final int MIN_DURATION = 2;
    public static final int DEFAULT_DURATION = 10;

    private TimedMetronome() {
    }
    
  }
  
  public final class GapMetronome {
    
    public static final int MAX_LOUD_MEASURES = 16;
    public static final int MIN_LOUD_MEASURES = 1;
    public static final int DEFAULT_LOUD_MEASURES = 4;
    public static final int MAX_SILENT_MEASURES = 32;
    public static final int MIN_SILENT_MEASURES = 2;
    public static final int DEFAULT_SILENT_MEASURES = 4;
    public static final int MAX_GAP_LENGTH_INCREMENT = 8;
    public static final int MIN_GAP_LENGTH_INCREMENT = 0;
    public static final int DEFAULT_GAP_LENGTH_INCREMENT = 2;
    public static final int NO_GAP_LENGTH_INCREMENT = 0;
    public static final int MAX_GAP_REPETITIONS = 16;
    public static final int MIN_GAP_REPETITIONS = 2;
    public static final int DEFAULT_GAP_REPETITIONS = 8;
    public static final int INFINITE_GAP_REPETITIONS = 0;

    private GapMetronome() {
    }
    
  }
  
  public final class SpeedMetronome {
    
    public static final int MAX_TEMPO_LENGTH = 32;
    public static final int MIN_TEMPO_LENGTH = 2;
    public static final int DEFAULT_TEMPO_LENGTH = 4;
    public static final float MAX_TEMPO_INCREMENT = 10.0F;
    public static final float MIN_TEMPO_INCREMENT = 2.0F;
    public static final float DEFAULT_TEMPO_INCREMENT = 5.0F;

    private SpeedMetronome() {
    }
    
  }
  
  public final class SoundRez {
    
    public static final String SOUNDS_DIRECTORY = "sounds";
    public static final String DEFAULT_ACCENT_SOUND_FILE 
      = SOUNDS_DIRECTORY + "/" + "accent.wav";
    public static final String DEFAULT_BEAT_SOUND_FILE 
      = SOUNDS_DIRECTORY + "/" + "beat.wav";
    public static final String DEFAULT_CLICK_SOUND_FILE 
      = SOUNDS_DIRECTORY + "/" + "click.wav";
    public static final String DEFAULT_TEMPO_CHANGE_SOUND_FILE 
      = SOUNDS_DIRECTORY + "/" + "tempoChange.wav";
    public static final float SAMPLE_RATE = 44_100.0F; //Hz
    public static final float FRAME_RATE = SAMPLE_RATE; //Hz
    public static final int SAMPLE_SIZE = 16; //bits
    public static final int FRAME_SIZE = 4; //bytes
    public static final int NUM_CHANNELS = 2;
    public static final float DURATION = 0.250F; //seconds
    public static final boolean BIG_ENDIAN = false;

    private SoundRez() {
    }
    
  }
  
  public final class MetronomeSettingsKeys {
    
    public static final String TEMPO = "tempo";
    public static final String SUB_DIVISION = "subDivision";
    public static final String MEASURE = "measure";
    public static final String START_TEMPO = "startTempo";
    public static final String END_TEMPO = "endTempo";
    public static final String TEMPO_INCREMENT = "tempoIncrement";
    public static final String TEMPO_LENGTH = "tempoLength";
    public static final String LOUD_MEASURES = "loudMeasures";
    public static final String SILENT_MEASURES = "silentMeasures";
    public static final String GAP_LENGTH_INCREMENT = "gapLengthIncrement";
    public static final String GAP_REPETITIONS = "gapRepetitions";
    public static final String DURATION = "duration";
    public static final String ACCENT = "accent";
    public static final String BEAT = "beat";
    public static final String CLICK = "click";
    public static final String TEMPO_CHANGE = "tempoChange";

    private MetronomeSettingsKeys() {
    }
    
  }
  
  public final class Session {
    
    public static final String SETTINGS_DIRECTORY = "settings";
    public static final String METRONOME_TYPE_SETTINGS_FILE 
      = SETTINGS_DIRECTORY + "/" + "metronomeType.txt";

    private Session() {
    }
    
  }
  
  public final class GapMetronomeSettings {
    
    public static final String GAP_METRONOME_SETTINGS_FILE 
      = Session.SETTINGS_DIRECTORY + "/" + "gapMetronome.txt";
    public static final String GAP_METRONOME_PRESETS_SETTINGS_FILE 
      = Session.SETTINGS_DIRECTORY + "/" + "gapMetronomePresets.txt";

    private GapMetronomeSettings() {
    }
    
  }
  
  public final class TimedMetronomeSettings {
    
    public static final String TIMED_METRONOME_SETTINGS_FILE 
      = Session.SETTINGS_DIRECTORY + "/" + "timedMetronome.txt";
    public static final String TIMED_METRONOME_PRESETS_SETTINGS_FILE 
      = Session.SETTINGS_DIRECTORY + "/" + "timedMetronomePresets.txt";

    private TimedMetronomeSettings() {
    }
    
  }
  
  public final class SpeedMetronomeSettings {
    
    public static final String SPEED_METRONOME_SETTINGS_FILE 
      = Session.SETTINGS_DIRECTORY + "/" + "speedMetronome.txt";
    public static final String SPEED_METRONOME_PRESETS_SETTINGS_FILE 
      = Session.SETTINGS_DIRECTORY + "/" + "speedMetronomePresets.txt";

    private SpeedMetronomeSettings() {
    }
    
  }
  
  public final class RegularMetronomeSettings {
    
    public static final String REGULAR_METRONOME_SETTINGS_FILE 
      = Session.SETTINGS_DIRECTORY + "/" + "regularMetronome.txt";
    public static final String REGULAR_METRONOME_PRESETS_SETTINGS_FILE 
      = Session.SETTINGS_DIRECTORY + "/" + "regularMetronomePresets.txt";

    private RegularMetronomeSettings() {
    }
    
  }
  
  public final class SoundSettings {
    
    public static final String ACCENT_SOUND_SETTINGS_FILE 
      = Session.SETTINGS_DIRECTORY + "/" + "accent.txt";
    public static final String BEAT_SOUND_SETTINGS_FILE 
      = Session.SETTINGS_DIRECTORY + "/" + "beat.txt";
    public static final String CLICK_SOUND_SETTINGS_FILE 
      = Session.SETTINGS_DIRECTORY + "/" + "click.txt";
    public static final String TEMPO_CHANGE_SOUND_SETTINGS_FILE 
      = Session.SETTINGS_DIRECTORY + "/" + "tempoChange.txt";

    private SoundSettings() {
    }
    
  }
}
