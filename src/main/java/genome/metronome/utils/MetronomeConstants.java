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

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class MetronomeConstants {
  
  public static final AudioFormat.Encoding ENCODING = AudioFormat
            .Encoding.PCM_SIGNED;
  public static final AudioFileFormat.Type TYPE = AudioFileFormat.Type.WAVE;

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
    
  }
  
  public final class ConstantTempoMetronome {
    
    public static final float MAX_TEMPO = 355.0f;
    public static final float MIN_TEMPO = 5.0f;
    public static final float DEFAULT_TEMPO = 70.0f;

    private ConstantTempoMetronome() {
    }
    
  }
  
  public final class VariableTempoMetronome {
    
    public static final float MAX_START_TEMPO = 354.0f;
    public static final float MIN_START_TEMPO = 50.0f;
    public static final float DEFAULT_START_TEMPO = 90.0f;
    public static final float MAX_END_TEMPO = 355.0f;
    public static final float MIN_END_TEMPO = 51.0f;
    public static final float DEFAULT_END_TEMPO = 120.0f;

    private VariableTempoMetronome() {
    }
    
  }
  
  public final class TimedMetronome {
    
    public static final int MAX_DURATION = 120;
    public static final int MIN_DURATION = 5;
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
    public static final float MAX_TEMPO_INCREMENT = 10.0f;
    public static final float MIN_TEMPO_INCREMENT = 2.0f;
    public static final float DEFAULT_TEMPO_INCREMENT = 5.0f;

    private SpeedMetronome() {
    }
    
  }
  
  public final class SoundRez {
    
    public static final String SOUNDS_DIRECTORY = "sounds";
    public static final String DEFAULT_ACCENT_SOUND_FILE = SOUNDS_DIRECTORY + 
                                                           "/" + "accent.wav";
    public static final String DEFAULT_BEAT_SOUND_FILE = SOUNDS_DIRECTORY + 
                                                           "/" + "beat.wav";
    public static final String DEFAULT_CLICK_SOUND_FILE = SOUNDS_DIRECTORY + 
                                                           "/" + "click.wav";
    public static final String DEFAULT_TEMPO_CHANGE_SOUND_FILE = 
            SOUNDS_DIRECTORY + "/" + "tempoChange.wav";
    public static final float SAMPLE_RATE = 44100.0f; //Hz
    public static final float FRAME_RATE = SAMPLE_RATE; //Hz
    public static final int SAMPLE_SIZE = 16; //bits
    public static final int FRAME_SIZE = 4; //bytes
    public static final int NUM_CHANNELS = 2;
    public static final float BIT_RATE = 1411.2f; //bits per second
    public static final float DURATION = 0.182f; //seconds
    public static final boolean BIG_ENDIAN = false;
//    public static final AudioFormat.Encoding ENCODING = AudioFormat
//            .Encoding.PCM_SIGNED;
//    public static final AudioFileFormat.Type TYPE = AudioFileFormat.Type.WAVE;

    private SoundRez() {
    }
    
  }
  
  public final class MetronomeSettingsKeys {
    
    public static final String TEMPO = "tempo";
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
    public static final String METRONOME_TYPE_SETTINGS_FILE = 
            SETTINGS_DIRECTORY + "/" + "metronomeType.txt";

    private Session() {
    }
    
  }
  
  public final class GapMetronomeSettings {
    
    public static final String GAP_METRONOME_SETTINGS_FILE = 
            Session.SETTINGS_DIRECTORY + "/" + "gapMetronome.txt";
    public static final String GAP_METRONOME_PRESETS_SETTINGS_FILE =
            Session.SETTINGS_DIRECTORY + "/" + "gapMetronomePresets.txt";

    private GapMetronomeSettings() {
    }
    
  }
  
  public final class TimedMetronomeSettings {
    
    public static final String TIMED_METRONOME_SETTINGS_FILE = 
            Session.SETTINGS_DIRECTORY + "/" + "timedMetronome.txt";
    public static final String TIMED_METRONOME_PRESETS_SETTINGS_FILE =
            Session.SETTINGS_DIRECTORY + "/" + "timedMetronomePresets.txt";

    private TimedMetronomeSettings() {
    }
    
  }
  
  public final class SpeedMetronomeSettings {
    
    public static final String SPEED_METRONOME_SETTINGS_FILE = 
            Session.SETTINGS_DIRECTORY + "/" + "speedMetronome.txt";
    public static final String SPEED_METRONOME_PRESETS_SETTINGS_FILE =
            Session.SETTINGS_DIRECTORY + "/" + "speedMetronomePresets.txt";

    private SpeedMetronomeSettings() {
    }
    
  }
  
  public final class RegularMetronomeSettings {
    
    public static final String REGULAR_METRONOME_SETTINGS_FILE = 
            Session.SETTINGS_DIRECTORY + "/" + "regularMetronome.txt";
    public static final String REGULAR_METRONOME_PRESETS_SETTINGS_FILE =
            Session.SETTINGS_DIRECTORY + "/" + "regularMetronomePresets.txt";

    private RegularMetronomeSettings() {
    }
    
  }
  
  public final class SoundSettings {
    
    public static final String ACCENT_SOUND_SETTINGS_FILE = 
            Session.SETTINGS_DIRECTORY + "/" + "accent.txt";
    public static final String BEAT_SOUND_SETTINGS_FILE = 
            Session.SETTINGS_DIRECTORY + "/" + "beat.txt";
    public static final String CLICK_SOUND_SETTINGS_FILE = 
            Session.SETTINGS_DIRECTORY + "/" + "click.txt";
    public static final String TEMPO_CHANGE_SOUND_SETTINGS_FILE = 
            Session.SETTINGS_DIRECTORY + "/" + "tempoChange.txt";

    private SoundSettings() {
    }
    
  }
}