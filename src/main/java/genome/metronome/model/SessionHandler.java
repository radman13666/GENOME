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

import genome.metronome.utils.MetronomeConstants;
import genome.metronome.utils.MetronomeContract;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class SessionHandler implements MetronomeContract.Model {
  
  private Session session;
  private static SessionHandler instance = null;

  private SessionHandler() {
    setSession(Session.getInstance());
  }
  
  public static SessionHandler getInstance() {
    if (instance == null) instance = new SessionHandler();
    return instance;
  }
  
  public static void destroyInstance() {
    Session.destroyInstance();
    instance = null;
  }

  private Session getSession() {
    return session;
  }

  private void setSession(Session session) {
    this.session = session;
  }
  
  MetronomeType readMetronomeTypeFromFile(File file) 
          throws IOException {
    MetronomeType metType = null;
    try (Scanner input = new Scanner(file);) {
      String metTypeStr = input.next();
      switch (metTypeStr) {
        case "REG": metType = MetronomeType.REG; break;
        case "GAP": metType = MetronomeType.GAP; break;
        case "TIMED": metType = MetronomeType.TIMED; break;
        case "SPEED": metType = MetronomeType.SPEED; break;
      }
    }
    return metType;
  }
  
  HashMap<String, Number> 
        readMetronomeSettingsFromFile(MetronomeType metType, File file) 
                throws IOException {
    int loudMeasures, silentMeasures, gapLengthIncrement, 
            gapRepetitions, measure, subDivision, duration, tempoLength;
    float tempo, tempoIncrement, startTempo, endTempo;
    HashMap<String, Number> settings = new HashMap<>();
    try (Scanner input = new Scanner(file);) {
      switch (metType) {
        case REG:
          tempo = input.nextFloat();
          measure = input.nextInt();
          subDivision = input.nextInt();
          
          settings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO, tempo);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                       measure);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                       subDivision);
          break;
        case GAP:
          loudMeasures = input.nextInt();
          silentMeasures = input.nextInt();
          gapLengthIncrement = input.nextInt();
          gapRepetitions = input.nextInt();
          tempo = input.nextFloat();
          measure = input.nextInt();
          subDivision = input.nextInt();
          
          settings.put(MetronomeConstants.MetronomeSettingsKeys.LOUD_MEASURES, 
                       loudMeasures);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.SILENT_MEASURES,
                       silentMeasures);
          settings.put(MetronomeConstants.MetronomeSettingsKeys
                  .GAP_LENGTH_INCREMENT, gapLengthIncrement);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.GAP_REPETITIONS,
                       gapRepetitions);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO, tempo);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                       measure);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                       subDivision);
          break;
        case TIMED:
          duration = input.nextInt();
          tempo = input.nextFloat();
          measure = input.nextInt();
          subDivision = input.nextInt();
          
          settings.put(MetronomeConstants.MetronomeSettingsKeys.DURATION, 
                       duration);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO, tempo);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                       measure);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                       subDivision);
          break;
        case SPEED:
          tempoLength = input.nextInt();
          tempoIncrement = input.nextFloat();
          startTempo = input.nextFloat();
          endTempo = input.nextFloat();
          measure = input.nextInt();
          subDivision = input.nextInt();
          
          settings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO_LENGTH, 
                       tempoLength);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO_INCREMENT,
                       tempoIncrement);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.START_TEMPO, 
                       startTempo);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.END_TEMPO, 
                       endTempo);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                       measure);
          settings.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                       subDivision);
          break;
      }
    }
    return settings;
  }
  
  LinkedHashMap<String, ? extends MetronomeSettings> 
        readMetronomePresetsFromFile(MetronomeType metType, File file) 
        throws IOException {
    int loudMeasures, silentMeasures, gapLengthIncrement, 
            gapRepetitions, measure, subDivision, duration, tempoLength;
    float tempo, tempoIncrement, startTempo, endTempo;
    String presetName;
    LinkedHashMap<String, MetronomeSettings> presets 
            = new LinkedHashMap<>();
    try (Scanner fileScanner = new Scanner(file);) {
      while (fileScanner.hasNext()) {
        String line = fileScanner.nextLine();
        try (Scanner lineScanner = new Scanner(line);) {
          switch (metType) {
            case REG:
              presetName = lineScanner.next();
              tempo = lineScanner.nextFloat();
              measure = lineScanner.nextInt();
              subDivision = lineScanner.nextInt();
              presets.put(presetName, 
                             new RegularMetronomeSettings(
                                     tempo, 
                                     measure, 
                                     subDivision
                             )
              ); break;
            case GAP:
              presetName = lineScanner.next();
              loudMeasures = lineScanner.nextInt();
              silentMeasures = lineScanner.nextInt();
              gapLengthIncrement = lineScanner.nextInt();
              gapRepetitions = lineScanner.nextInt();
              tempo = lineScanner.nextFloat();
              measure = lineScanner.nextInt();
              subDivision = lineScanner.nextInt();
              presets.put(presetName, 
                             new GapMetronomeSettings(
                                     loudMeasures,
                                     silentMeasures,
                                     gapLengthIncrement,
                                     gapRepetitions,
                                     tempo,
                                     measure,
                                     subDivision
                             )
              ); break;
            case TIMED:
              presetName = lineScanner.next();
              duration = lineScanner.nextInt();
              tempo = lineScanner.nextFloat();
              measure = lineScanner.nextInt();
              subDivision = lineScanner.nextInt();
              presets.put(presetName, 
                             new TimedMetronomeSettings(
                                     duration,
                                     tempo,
                                     measure,
                                     subDivision
                             )
              ); break;
            case SPEED:
              presetName = lineScanner.next();
              tempoLength = lineScanner.nextInt();
              tempoIncrement = lineScanner.nextFloat();
              startTempo = lineScanner.nextFloat();
              endTempo = lineScanner.nextFloat();
              measure = lineScanner.nextInt();
              subDivision = lineScanner.nextInt();
              presets.put(presetName, 
                             new SpeedMetronomeSettings(
                                     tempoLength,
                                     tempoIncrement,
                                     startTempo,
                                     endTempo,
                                     measure,
                                     subDivision
                             )
              ); break;
          }
        }
      }
    }
    return presets;
  }
  
  String readSoundSettingFromFile(String soundKey, File file) 
          throws IOException {
    String sound = null;
    switch (soundKey) {
      case MetronomeConstants.MetronomeSettingsKeys.ACCENT:
        try (Scanner accIn = new Scanner(file);) {
          sound = accIn.next();
        }
        break;
      case MetronomeConstants.MetronomeSettingsKeys.BEAT:
        try (Scanner beatIn = new Scanner(file);) {
          sound = beatIn.next();
        }
        break;
      case MetronomeConstants.MetronomeSettingsKeys.CLICK:
        try (Scanner clickIn = new Scanner(file);) {
          sound = clickIn.next();
        }
        break;
      case MetronomeConstants.MetronomeSettingsKeys.TEMPO_CHANGE:
        try (Scanner tmpChIn = new Scanner(file);) {
          sound = tmpChIn.next();
        }
        break;
    }
    return sound;
  }
  
  SoundSettings readSoundSettingsFromFile(File acc, 
                                                  File beat, 
                                                  File click, 
                                                  File tmpCh) 
          throws IOException {
    return new SoundSettings(
            readSoundSettingFromFile(MetronomeConstants
                    .MetronomeSettingsKeys.ACCENT, acc), 
            readSoundSettingFromFile(MetronomeConstants
                    .MetronomeSettingsKeys.BEAT, beat), 
            readSoundSettingFromFile(MetronomeConstants
                    .MetronomeSettingsKeys.CLICK, click), 
            readSoundSettingFromFile(MetronomeConstants
                    .MetronomeSettingsKeys.TEMPO_CHANGE, tmpCh)
    );
  }
  
  void writeMetronomeTypeToFile(MetronomeType metType, File file) 
          throws IOException {
    try (PrintWriter output = new PrintWriter(file);) {
      output.print(metType);
    }
  }
  
  void writeMetronomeSettingsToFile(MetronomeType metType, 
                                            HashMap<String, Number> settings,
                                            File file) throws IOException {
    int loudMeasures, silentMeasures, gapLengthIncrement, 
            gapRepetitions, measure, subDivision, duration, tempoLength;
    float tempo, tempoIncrement, startTempo, endTempo;
    try (PrintWriter output = new PrintWriter(file);) {
      switch (metType) {
        case REG:
          tempo = (Float) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.TEMPO);
          measure = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.MEASURE);
          subDivision = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.SUB_DIVISION);
          output.printf("%.1f %d %d", tempo, measure, subDivision);
          break;
        case GAP:
          loudMeasures = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.LOUD_MEASURES);
          silentMeasures = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.SILENT_MEASURES);
          gapLengthIncrement = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.GAP_LENGTH_INCREMENT);
          gapRepetitions = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.GAP_REPETITIONS);
          tempo = (Float) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.TEMPO);
          measure = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.MEASURE);
          subDivision = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.SUB_DIVISION);
          output.printf("%d %d %d %d %.1f %d %d", 
                        loudMeasures, silentMeasures, gapLengthIncrement, 
                        gapRepetitions, tempo, measure, subDivision);
          break;
        case TIMED:
          duration = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.DURATION);
          tempo = (Float) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.TEMPO);
          measure = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.MEASURE);
          subDivision = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.SUB_DIVISION);
          output.printf("%d %.1f %d %d", duration, tempo, measure, subDivision);
          break;
        case SPEED:
          tempoLength = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.TEMPO_LENGTH);
          tempoIncrement = (Float) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.TEMPO_INCREMENT);
          startTempo = (Float) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.START_TEMPO);
          endTempo = (Float) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.END_TEMPO);
          measure = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.MEASURE);
          subDivision = (Integer) settings.get(MetronomeConstants
                  .MetronomeSettingsKeys.SUB_DIVISION);
          output.printf("%d %.1f %.1f %.1f %d %d", 
                        tempoLength, tempoIncrement, startTempo, endTempo,
                        measure, subDivision);
          break;
      }
    }
  }
  
  void writeMetronomePresetsToFile(MetronomeType metType,
                                    LinkedHashMap<String,
                                        ? extends MetronomeSettings> presets,
                                    File file) throws IOException {
    int loudMeasures, silentMeasures, gapLengthIncrement, 
            gapRepetitions, measure, subDivision, duration, tempoLength;
    float tempo, tempoIncrement, startTempo, endTempo;
    String[] keys = presets.keySet().toArray(new String[presets.size()]);
    try (PrintWriter output = new PrintWriter(file);) {
      switch (metType) {
        case REG:
          LinkedHashMap<String, RegularMetronomeSettings> regPresets 
                  = (LinkedHashMap<String, RegularMetronomeSettings>) presets;
          for (String key : keys) {
            tempo = regPresets.get(key).getTempo();
            measure = regPresets.get(key).getMeasure();
            subDivision = regPresets.get(key).getSubDivision();
            output.printf("%s %.1f %d %d\n", key, tempo, measure, subDivision);
          }
          break;
        case GAP:
          LinkedHashMap<String, GapMetronomeSettings> gapPresets 
                  = (LinkedHashMap<String, GapMetronomeSettings>) presets;
          for (String key : keys) {
            loudMeasures = gapPresets.get(key).getLoudMeasures();
            silentMeasures = gapPresets.get(key).getSilentMeasures();
            gapLengthIncrement = gapPresets.get(key).getGapLengthIncrement();
            gapRepetitions = gapPresets.get(key).getGapRepetitions();
            tempo = gapPresets.get(key).getTempo();
            measure = gapPresets.get(key).getMeasure();
            subDivision = gapPresets.get(key).getSubDivision();
            output.printf("%s %d %d %d %d %.1f %d %d\n", key,
                          loudMeasures, silentMeasures, gapLengthIncrement, 
                          gapRepetitions, tempo, measure, subDivision);
          }
          break;
        case TIMED:
          LinkedHashMap<String, TimedMetronomeSettings> timedPresets 
                  = (LinkedHashMap<String, TimedMetronomeSettings>) presets;
          for (String key : keys) {
            duration = timedPresets.get(key).getDuration();
            tempo = timedPresets.get(key).getTempo();
            measure = timedPresets.get(key).getMeasure();
            subDivision = timedPresets.get(key).getSubDivision();
            output.printf("%s %d %.1f %d %d\n", key,
                          duration, tempo, measure, subDivision);
          }
          break;
        case SPEED:
          LinkedHashMap<String, SpeedMetronomeSettings> speedPresets 
                  = (LinkedHashMap<String, SpeedMetronomeSettings>) presets;
          for (String key : keys) {
            tempoLength = speedPresets.get(key).getTempoLength();
            tempoIncrement = speedPresets.get(key).getTempoIncrement();
            startTempo = speedPresets.get(key).getStartTempo();
            endTempo = speedPresets.get(key).getEndTempo();
            measure = speedPresets.get(key).getMeasure();
            subDivision = speedPresets.get(key).getSubDivision();
            output.printf("%s %d %.1f %.1f %.1f %d %d\n", key,
                          tempoLength, tempoIncrement, startTempo, endTempo, 
                          measure, subDivision);
          }
          break;
      }
    }
  }
  
  void writeSoundSettingsToFile(SoundSettings soundSettings, 
                                        File accent,
                                        File beat,
                                        File click,
                                        File tmpCh) throws IOException {
    try (PrintWriter accOut = new PrintWriter(accent);
            PrintWriter beatOut = new PrintWriter(beat);
            PrintWriter clickOut = new PrintWriter(click);
            PrintWriter tmpChOut = new PrintWriter(tmpCh);) {
      accOut.print(soundSettings.getAccentSound());
      beatOut.print(soundSettings.getBeatSound());
      clickOut.print(soundSettings.getClickSound());
      tmpChOut.print(soundSettings.getTempoChangeSound());
    }
  }

  @Override
  public HashMap<String, Number> readMetronomeSettings(MetronomeType metType) {
    HashMap<String, Number> settings = new HashMap<>();
    switch (metType) {
      case REG:
        settings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO, 
                     readMetronomeSetting(metType, 
                               MetronomeConstants.MetronomeSettingsKeys.TEMPO));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                     readMetronomeSetting(metType, 
                             MetronomeConstants.MetronomeSettingsKeys.MEASURE));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                     readMetronomeSetting(metType, 
                        MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION));
        break;
      case GAP:
        settings.put(MetronomeConstants.MetronomeSettingsKeys.LOUD_MEASURES, 
                     readMetronomeSetting(metType, 
                       MetronomeConstants.MetronomeSettingsKeys.LOUD_MEASURES));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.SILENT_MEASURES, 
                     readMetronomeSetting(metType, 
                     MetronomeConstants.MetronomeSettingsKeys.SILENT_MEASURES));
        settings.put(
                MetronomeConstants.MetronomeSettingsKeys.GAP_LENGTH_INCREMENT, 
                readMetronomeSetting(metType, 
                MetronomeConstants.MetronomeSettingsKeys.GAP_LENGTH_INCREMENT));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.GAP_REPETITIONS, 
                     readMetronomeSetting(metType, 
                     MetronomeConstants.MetronomeSettingsKeys.GAP_REPETITIONS));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO, 
                     readMetronomeSetting(metType, 
                               MetronomeConstants.MetronomeSettingsKeys.TEMPO));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                     readMetronomeSetting(metType, 
                             MetronomeConstants.MetronomeSettingsKeys.MEASURE));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                     readMetronomeSetting(metType, 
                        MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION));
        break;
      case TIMED:
        settings.put(MetronomeConstants.MetronomeSettingsKeys.DURATION, 
                     readMetronomeSetting(metType, 
                            MetronomeConstants.MetronomeSettingsKeys.DURATION));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO, 
                     readMetronomeSetting(metType, 
                               MetronomeConstants.MetronomeSettingsKeys.TEMPO));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                     readMetronomeSetting(metType, 
                             MetronomeConstants.MetronomeSettingsKeys.MEASURE));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                     readMetronomeSetting(metType, 
                        MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION));
        break;
      case SPEED:
        settings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO_LENGTH, 
                     readMetronomeSetting(metType, 
                        MetronomeConstants.MetronomeSettingsKeys.TEMPO_LENGTH));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO_INCREMENT, 
                     readMetronomeSetting(metType, 
                     MetronomeConstants.MetronomeSettingsKeys.TEMPO_INCREMENT));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.START_TEMPO, 
                     readMetronomeSetting(metType, 
                         MetronomeConstants.MetronomeSettingsKeys.START_TEMPO));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.END_TEMPO, 
                     readMetronomeSetting(metType, 
                           MetronomeConstants.MetronomeSettingsKeys.END_TEMPO));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                     readMetronomeSetting(metType, 
                             MetronomeConstants.MetronomeSettingsKeys.MEASURE));
        settings.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                     readMetronomeSetting(metType, 
                        MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION));
        break;
    }
    return settings;
  }

  @Override
  public Number readMetronomeSetting(MetronomeType metType, 
                                     String settingsKey) {
    switch (metType) {
      case REG:
        switch (settingsKey) {
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO:
            return getSession().getRegularMetronomeSettings().getTempo();
          case MetronomeConstants.MetronomeSettingsKeys.MEASURE:
            return getSession().getRegularMetronomeSettings().getMeasure();
          case MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION:
            return getSession().getRegularMetronomeSettings().getSubDivision();
        }
      case GAP:
        switch (settingsKey) {
          case MetronomeConstants.MetronomeSettingsKeys.LOUD_MEASURES:
            return getSession().getGapMetronomeSettings().getLoudMeasures();
          case MetronomeConstants.MetronomeSettingsKeys.SILENT_MEASURES:
            return getSession().getGapMetronomeSettings().getSilentMeasures();
          case MetronomeConstants.MetronomeSettingsKeys.GAP_LENGTH_INCREMENT:
            return getSession().getGapMetronomeSettings()
                    .getGapLengthIncrement();
          case MetronomeConstants.MetronomeSettingsKeys.GAP_REPETITIONS:
            return getSession().getGapMetronomeSettings().getGapRepetitions();
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO:
            return getSession().getGapMetronomeSettings().getTempo();
          case MetronomeConstants.MetronomeSettingsKeys.MEASURE:
            return getSession().getGapMetronomeSettings().getMeasure();
          case MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION:
            return getSession().getGapMetronomeSettings().getSubDivision();
        }
      case TIMED:
        switch (settingsKey) {
          case MetronomeConstants.MetronomeSettingsKeys.DURATION:
            return getSession().getTimedMetronomeSettings().getDuration();
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO:
            return getSession().getTimedMetronomeSettings().getTempo();
          case MetronomeConstants.MetronomeSettingsKeys.MEASURE:
            return getSession().getTimedMetronomeSettings().getMeasure();
          case MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION:
            return getSession().getTimedMetronomeSettings().getSubDivision();
        }
      case SPEED:
        switch (settingsKey) {
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO_LENGTH:
            return getSession().getSpeedMetronomeSettings().getTempoLength();
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO_INCREMENT:
            return getSession().getSpeedMetronomeSettings().getTempoIncrement();
          case MetronomeConstants.MetronomeSettingsKeys.START_TEMPO:
            return getSession().getSpeedMetronomeSettings().getStartTempo();
          case MetronomeConstants.MetronomeSettingsKeys.END_TEMPO:
            return getSession().getSpeedMetronomeSettings().getEndTempo();
          case MetronomeConstants.MetronomeSettingsKeys.MEASURE:
            return getSession().getSpeedMetronomeSettings().getMeasure();
          case MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION:
            return getSession().getSpeedMetronomeSettings().getSubDivision();
        }
      default:
        return null;
    }
  }

  @Override
  public MetronomeType readMetronomeType() {
    return getSession().getMetronomeType();
  }

  @Override
  public LinkedHashMap<String, ? extends MetronomeSettings> 
        readMetronomePresets(MetronomeType metType) {
    switch (metType) {
      case REG:
        return getSession().getRegularMetronomePresets();
      case GAP:
        return getSession().getGapMetronomePresets();
      case TIMED:
        return getSession().getTimedMetronomePresets();
      case SPEED:
        return getSession().getSpeedMetronomePresets();
      default:
        return null;
    }
  }

  @Override
  public MetronomeSettings readMetronomePreset(MetronomeType metType,
                                               String presetKey) {
    switch (metType) {
      case REG:
        return getSession().getRegularMetronomePresets().get(presetKey);
      case GAP:
        return getSession().getGapMetronomePresets().get(presetKey);
      case TIMED:
        return getSession().getTimedMetronomePresets().get(presetKey);
      case SPEED:
        return getSession().getSpeedMetronomePresets().get(presetKey);
      default:
        return null;
    }
  }

  @Override
  public SoundSettings readSoundSettings() {
    return getSession().getSoundSettings();
  }

  @Override
  public String readSoundSetting(String soundKey) {
    switch (soundKey) {
      case MetronomeConstants.MetronomeSettingsKeys.ACCENT:
        return getSession().getSoundSettings().getAccentSound();
      case MetronomeConstants.MetronomeSettingsKeys.BEAT:
        return getSession().getSoundSettings().getBeatSound();
      case MetronomeConstants.MetronomeSettingsKeys.CLICK:
        return getSession().getSoundSettings().getTempoChangeSound();
      case MetronomeConstants.MetronomeSettingsKeys.TEMPO_CHANGE:
        return getSession().getSoundSettings().getTempoChangeSound();
      default:
        return null;
    }
  }

  @Override
  public void writeMetronomeSettings(MetronomeType metType,
                                     HashMap<String, Number> settings) {
    switch (metType) {
      case REG:
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys.TEMPO,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.TEMPO
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys.MEASURE,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.MEASURE
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants
                                      .MetronomeSettingsKeys.SUB_DIVISION,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.SUB_DIVISION
                              )
        );
        break;
      case GAP:
        writeMetronomeSetting(metType, 
                              MetronomeConstants
                                      .MetronomeSettingsKeys.LOUD_MEASURES,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.LOUD_MEASURES
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants
                                      .MetronomeSettingsKeys.SILENT_MEASURES,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.SILENT_MEASURES
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys
                                      .GAP_LENGTH_INCREMENT,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.GAP_REPETITIONS
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys.TEMPO,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.TEMPO
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys.MEASURE,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.MEASURE
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants
                                      .MetronomeSettingsKeys.SUB_DIVISION,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.SUB_DIVISION
                              )
        );
        break;
      case TIMED:
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys.DURATION,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.DURATION
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys.TEMPO,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.TEMPO
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys.MEASURE,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.MEASURE
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants
                                      .MetronomeSettingsKeys.SUB_DIVISION,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.SUB_DIVISION
                              )
        );
      case SPEED:
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys
                                      .TEMPO_LENGTH,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.TEMPO_LENGTH
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys
                                      .TEMPO_INCREMENT,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.TEMPO_INCREMENT
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys
                                      .START_TEMPO,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.START_TEMPO
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys
                                      .END_TEMPO,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.END_TEMPO
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys.MEASURE,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.MEASURE
                              )
        );
        writeMetronomeSetting(metType, 
                              MetronomeConstants.MetronomeSettingsKeys
                                      .SUB_DIVISION,
                              settings.get(MetronomeConstants
                                      .MetronomeSettingsKeys.SUB_DIVISION
                              )
        );
        break;
    }
  }

  @Override
  public void writeMetronomeSetting(MetronomeType metType, String settingsKey,
                                    Number setting) {
    switch (metType) {
      case REG:
        switch (settingsKey) {
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO:
            getSession().getRegularMetronomeSettings()
                    .setTempo((Float) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.MEASURE:
            getSession().getRegularMetronomeSettings()
                    .setMeasure((Integer) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION:
            getSession().getRegularMetronomeSettings()
                    .setSubDivision((Integer) setting); break;
        } 
        break;
      case GAP:
        switch (settingsKey) {
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO:
            getSession().getGapMetronomeSettings()
                    .setTempo((Float) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.MEASURE:
            getSession().getGapMetronomeSettings()
                    .setMeasure((Integer) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION:
            getSession().getGapMetronomeSettings()
                    .setSubDivision((Integer) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.LOUD_MEASURES:
            getSession().getGapMetronomeSettings()
                    .setLoudMeasures((Integer) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.SILENT_MEASURES:
            getSession().getGapMetronomeSettings()
                    .setSilentMeasures((Integer) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.GAP_LENGTH_INCREMENT:
            getSession().getGapMetronomeSettings()
                    .setGapLengthIncrement((Integer) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.GAP_REPETITIONS:
            getSession().getGapMetronomeSettings()
                    .setGapRepetitions((Integer) setting); break;
        } 
        break;
      case TIMED:
        switch (settingsKey) {
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO:
            getSession().getTimedMetronomeSettings()
                    .setTempo((Float) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.MEASURE:
            getSession().getTimedMetronomeSettings()
                    .setMeasure((Integer) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION:
            getSession().getTimedMetronomeSettings()
                    .setSubDivision((Integer) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.DURATION:
            getSession().getTimedMetronomeSettings()
                    .setDuration((Integer) setting); break;
        } 
        break;
      case SPEED:
        switch (settingsKey) {
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO_LENGTH:
            getSession().getSpeedMetronomeSettings()
                    .setTempoLength((Integer) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.MEASURE:
            getSession().getSpeedMetronomeSettings()
                    .setMeasure((Integer) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION:
            getSession().getSpeedMetronomeSettings()
                    .setSubDivision((Integer) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.TEMPO_INCREMENT:
            getSession().getSpeedMetronomeSettings()
                    .setTempoIncrement((Float) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.START_TEMPO:
            getSession().getSpeedMetronomeSettings()
                    .setStartTempo((Float) setting); break;
          case MetronomeConstants.MetronomeSettingsKeys.END_TEMPO:
            getSession().getSpeedMetronomeSettings()
                    .setEndTempo((Float) setting); break;
        } 
        break;
    }
  }

  @Override
  public void writeMetronomeType(MetronomeType metType) {
    getSession().setMetronomeType(metType);
  }

  @Override
  public void updateMetronomePreset(MetronomeType metType, String presetName, 
                                   MetronomeSettings preset) {
    switch (metType) {
      case REG:
        getSession().getRegularMetronomePresets()
                .replace(presetName, (RegularMetronomeSettings) preset);
        break;
      case GAP:
        getSession().getGapMetronomePresets()
                .replace(presetName, (GapMetronomeSettings) preset);
        break;
      case TIMED:
        getSession().getTimedMetronomePresets()
                .replace(presetName, (TimedMetronomeSettings) preset);
        break;
      case SPEED:
        getSession().getSpeedMetronomePresets()
                .replace(presetName, (SpeedMetronomeSettings) preset);
    }
  }

  @Override
  public void writeSoundSetting(String soundKey, String soundSetting) {
    switch (soundKey) {
      case MetronomeConstants.MetronomeSettingsKeys.ACCENT:
        getSession().getSoundSettings().setAccentSound(soundSetting);
        break;
      case MetronomeConstants.MetronomeSettingsKeys.BEAT:
        getSession().getSoundSettings().setBeatSound(soundSetting);
        break;
      case MetronomeConstants.MetronomeSettingsKeys.CLICK:
        getSession().getSoundSettings().setClickSound(soundSetting);
        break;
      case MetronomeConstants.MetronomeSettingsKeys.TEMPO_CHANGE:
        getSession().getSoundSettings().setTempoChangeSound(soundSetting);
        break;
    }
  }

  @Override
  public void writeSoundSettings(String accentSound, String beatSound,
                                 String clickSound, String tempoChangeSound) {
    writeSoundSetting(MetronomeConstants.MetronomeSettingsKeys.ACCENT, 
                      accentSound);
    writeSoundSetting(MetronomeConstants.MetronomeSettingsKeys.BEAT, 
                      beatSound);
    writeSoundSetting(MetronomeConstants.MetronomeSettingsKeys.CLICK, 
                      clickSound);
    writeSoundSetting(MetronomeConstants.MetronomeSettingsKeys.TEMPO_CHANGE, 
                      tempoChangeSound);
  }

  @Override
  public void addMetronomePreset(MetronomeType metType, 
                                 String presetName, 
                                 MetronomeSettings preset) {
    switch (metType) {
      case REG:
        getSession().getRegularMetronomePresets()
                .put(presetName, (RegularMetronomeSettings) preset);
        break;
      case GAP:
        getSession().getGapMetronomePresets()
                .put(presetName, (GapMetronomeSettings) preset);
        break;
      case TIMED:
        getSession().getTimedMetronomePresets()
                .put(presetName, (TimedMetronomeSettings) preset);
        break;
      case SPEED:
        getSession().getSpeedMetronomePresets()
                .put(presetName, (SpeedMetronomeSettings) preset);
        break;
    }
  }

  @Override
  public void removeMetronomePreset(MetronomeType metType, String presetName) {
    switch (metType) {
      case REG:
        getSession().getRegularMetronomePresets().remove(presetName);
        break;
      case GAP:
        getSession().getGapMetronomePresets().remove(presetName);
        break;
      case TIMED:
        getSession().getTimedMetronomePresets().remove(presetName);
        break;
      case SPEED:
        getSession().getSpeedMetronomePresets().remove(presetName);
        break;
    }
  }

  @Override
  public void removeMetronomePresets(MetronomeType metType, 
                                     String[] presetNames) {
    for (String preset : presetNames) {
      removeMetronomePreset(metType, preset);
    }
  }

  @Override
  public void setDefaults() {
    //set defaults for:
    // --> Regular, Gap, Timed and Speed Metronomes
    // --> Accent, Beat, Click and Tempo change sounds
    // --> metronome type
    // --> create new preset objects, since there are no presets by default
    
    //Regular metronome
    getSession().setRegularMetronomeSettings(
            new RegularMetronomeSettings(
                    MetronomeConstants.ConstantTempoMetronome.DEFAULT_TEMPO, 
                    MetronomeConstants.Metronome.COMMON_TIME, 
                    MetronomeConstants.Metronome.NO_SUB_DIVISION
            )
    );
    
    //Gap metronome
    getSession().setGapMetronomeSettings(
            new GapMetronomeSettings(
                    MetronomeConstants.GapMetronome.DEFAULT_LOUD_MEASURES, 
                    MetronomeConstants.GapMetronome.DEFAULT_SILENT_MEASURES, 
                    MetronomeConstants.GapMetronome
                            .DEFAULT_GAP_LENGTH_INCREMENT, 
                    MetronomeConstants.GapMetronome.DEFAULT_GAP_REPETITIONS, 
                    MetronomeConstants.ConstantTempoMetronome.DEFAULT_TEMPO, 
                    MetronomeConstants.Metronome.COMMON_TIME, 
                    MetronomeConstants.Metronome.NO_SUB_DIVISION
            )
    );
    
    //Timed metronome
    getSession().setTimedMetronomeSettings(
            new TimedMetronomeSettings(
                    MetronomeConstants.TimedMetronome.DEFAULT_DURATION, 
                    MetronomeConstants.ConstantTempoMetronome.DEFAULT_TEMPO, 
                    MetronomeConstants.Metronome.COMMON_TIME, 
                    MetronomeConstants.Metronome.NO_SUB_DIVISION
            )
    );
    
    //Speed metronome
    getSession().setSpeedMetronomeSettings(
            new SpeedMetronomeSettings(
                    MetronomeConstants.SpeedMetronome.DEFAULT_TEMPO_LENGTH, 
                    MetronomeConstants.SpeedMetronome.DEFAULT_TEMPO_INCREMENT, 
                    MetronomeConstants.VariableTempoMetronome
                            .DEFAULT_START_TEMPO, 
                    MetronomeConstants.VariableTempoMetronome.DEFAULT_END_TEMPO,  
                    MetronomeConstants.Metronome.COMMON_TIME, 
                    MetronomeConstants.Metronome.NO_SUB_DIVISION
            )
    );
    
    //Accent, Beat, Click and Tempo change sounds
    getSession().setSoundSettings(
            new SoundSettings(
                    MetronomeConstants.SoundRez.DEFAULT_ACCENT_SOUND_FILE, 
                    MetronomeConstants.SoundRez.DEFAULT_BEAT_SOUND_FILE, 
                    MetronomeConstants.SoundRez.DEFAULT_CLICK_SOUND_FILE, 
                    MetronomeConstants.SoundRez.DEFAULT_TEMPO_CHANGE_SOUND_FILE
            )
    );
    
    //metronome type
    getSession().setMetronomeType(MetronomeType.REG);
    
    //create new preset objects
    getSession().setRegularMetronomePresets(new LinkedHashMap<>());
    getSession().setGapMetronomePresets(new LinkedHashMap<>());
    getSession().setTimedMetronomePresets(new LinkedHashMap<>());
    getSession().setSpeedMetronomePresets(new LinkedHashMap<>());
  }

  @Override
  public void writeCurrentSessionToFile() throws IOException {
    //if the session is not null, write the data it holds to file.
    //if the files do not exist, they'll be created prior to writing.
    //if the files do exist, they'll be truncated prior to writing.
    
    File metTypeSettingsFile 
          = new File(MetronomeConstants.Session.METRONOME_TYPE_SETTINGS_FILE),
          regSettingsFile 
          = new File(MetronomeConstants
                  .RegularMetronomeSettings.REGULAR_METRONOME_SETTINGS_FILE),
          gapSettingsFile 
          = new File(MetronomeConstants
                  .GapMetronomeSettings.GAP_METRONOME_SETTINGS_FILE),
          timedSettingsFile 
          = new File(MetronomeConstants
                  .TimedMetronomeSettings.TIMED_METRONOME_SETTINGS_FILE),
          speedSettingsFile 
          = new File(MetronomeConstants
                  .SpeedMetronomeSettings.SPEED_METRONOME_SETTINGS_FILE),
          regPresetsFile 
          = new File(MetronomeConstants
                  .RegularMetronomeSettings
                  .REGULAR_METRONOME_PRESETS_SETTINGS_FILE),
          gapPresetsFile 
          = new File(MetronomeConstants
                  .GapMetronomeSettings.GAP_METRONOME_PRESETS_SETTINGS_FILE),
          timedPresetsFile 
          = new File(MetronomeConstants
                  .TimedMetronomeSettings
                  .TIMED_METRONOME_PRESETS_SETTINGS_FILE),
          speedPresetsFile 
          = new File(MetronomeConstants
                  .SpeedMetronomeSettings
                  .SPEED_METRONOME_PRESETS_SETTINGS_FILE),
          accentFile 
          = new File(MetronomeConstants
                  .SoundSettings.ACCENT_SOUND_SETTINGS_FILE),
          beatFile 
          = new File(MetronomeConstants
                  .SoundSettings.BEAT_SOUND_SETTINGS_FILE),
          clickFile 
          = new File(MetronomeConstants
                  .SoundSettings.CLICK_SOUND_SETTINGS_FILE),
          tmpChFile 
          = new File(MetronomeConstants
                  .SoundSettings.TEMPO_CHANGE_SOUND_SETTINGS_FILE);
    
    if (getSession() != null) {  
      writeMetronomeTypeToFile(readMetronomeType(), metTypeSettingsFile);
      writeMetronomeSettingsToFile(MetronomeType.REG,
                                   readMetronomeSettings(MetronomeType.REG),
                                   regSettingsFile);
      writeMetronomeSettingsToFile(MetronomeType.GAP,
                                   readMetronomeSettings(MetronomeType.GAP),
                                   gapSettingsFile);
      writeMetronomeSettingsToFile(MetronomeType.TIMED,
                                   readMetronomeSettings(MetronomeType.TIMED),
                                   timedSettingsFile);
      writeMetronomeSettingsToFile(MetronomeType.SPEED, 
                                   readMetronomeSettings(MetronomeType.SPEED),
                                   speedSettingsFile);
      writeMetronomePresetsToFile(MetronomeType.REG,
                                  (LinkedHashMap<String, 
                                          RegularMetronomeSettings>) 
                                        readMetronomePresets(MetronomeType.REG),
                                  regPresetsFile);
      writeMetronomePresetsToFile(MetronomeType.GAP,
                                  (LinkedHashMap<String, 
                                          GapMetronomeSettings>) 
                                        readMetronomePresets(MetronomeType.GAP),
                                  gapPresetsFile);
      writeMetronomePresetsToFile(MetronomeType.TIMED,
                                  (LinkedHashMap<String, 
                                          TimedMetronomeSettings>) 
                                      readMetronomePresets(MetronomeType.TIMED),
                                  timedPresetsFile);
      writeMetronomePresetsToFile(MetronomeType.SPEED,
                                  (LinkedHashMap<String, 
                                          SpeedMetronomeSettings>) 
                                      readMetronomePresets(MetronomeType.SPEED),
                                  speedPresetsFile);
      writeSoundSettingsToFile(readSoundSettings(),
                               accentFile, beatFile, clickFile, tmpChFile);
    }
  }

  @Override
  public void readPreviousSessionFromFile() throws IOException {
    
    //1. read the settings from file, if they exist, and load them to
    //  the session object if it exists.
    //2. if not, load defaults to the session object, if it exists.

    File metTypeSettingsFile 
            = new File(MetronomeConstants.Session.METRONOME_TYPE_SETTINGS_FILE),
            regSettingsFile 
            = new File(MetronomeConstants
                    .RegularMetronomeSettings.REGULAR_METRONOME_SETTINGS_FILE),
            gapSettingsFile 
            = new File(MetronomeConstants
                    .GapMetronomeSettings.GAP_METRONOME_SETTINGS_FILE),
            timedSettingsFile 
            = new File(MetronomeConstants
                    .TimedMetronomeSettings.TIMED_METRONOME_SETTINGS_FILE),
            speedSettingsFile 
            = new File(MetronomeConstants
                    .SpeedMetronomeSettings.SPEED_METRONOME_SETTINGS_FILE),
            regPresetsFile 
            = new File(MetronomeConstants
                    .RegularMetronomeSettings
                    .REGULAR_METRONOME_PRESETS_SETTINGS_FILE),
            gapPresetsFile 
            = new File(MetronomeConstants
                    .GapMetronomeSettings.GAP_METRONOME_PRESETS_SETTINGS_FILE),
            timedPresetsFile 
            = new File(MetronomeConstants
                    .TimedMetronomeSettings
                    .TIMED_METRONOME_PRESETS_SETTINGS_FILE),
            speedPresetsFile 
            = new File(MetronomeConstants
                    .SpeedMetronomeSettings
                    .SPEED_METRONOME_PRESETS_SETTINGS_FILE),
            accentFile 
            = new File(MetronomeConstants
                    .SoundSettings.ACCENT_SOUND_SETTINGS_FILE),
            beatFile 
            = new File(MetronomeConstants
                    .SoundSettings.BEAT_SOUND_SETTINGS_FILE),
            clickFile 
            = new File(MetronomeConstants
                    .SoundSettings.CLICK_SOUND_SETTINGS_FILE),
            tmpChFile 
            = new File(MetronomeConstants
                    .SoundSettings.TEMPO_CHANGE_SOUND_SETTINGS_FILE);
    
    if (getSession() != null) {
      if (!(metTypeSettingsFile.exists() &
            regSettingsFile.exists() &
            gapSettingsFile.exists() &
            timedSettingsFile.exists() &
            speedSettingsFile.exists() &
            regPresetsFile.exists() &
            gapPresetsFile.exists() &
            timedPresetsFile.exists() &
            speedPresetsFile.exists() &
            accentFile.exists() &
            beatFile.exists() &
            clickFile.exists() &
            tmpChFile.exists())) {
        setDefaults();
      } else {
        writeMetronomeType(readMetronomeTypeFromFile(metTypeSettingsFile));
        writeMetronomeSettings(MetronomeType.REG, 
                               readMetronomeSettingsFromFile(MetronomeType.REG,
                                                             regSettingsFile)
        );
        writeMetronomeSettings(MetronomeType.GAP, 
                               readMetronomeSettingsFromFile(MetronomeType.GAP,
                                                             gapSettingsFile)
        );
        writeMetronomeSettings(MetronomeType.TIMED, 
                               readMetronomeSettingsFromFile(MetronomeType.TIMED,
                                                             timedSettingsFile)
        );
        writeMetronomeSettings(MetronomeType.SPEED, 
                               readMetronomeSettingsFromFile(MetronomeType.SPEED,
                                                             speedSettingsFile)
        );
        getSession().setSoundSettings(readSoundSettingsFromFile(accentFile,
                                                                beatFile,
                                                                clickFile,
                                                                tmpChFile));
        getSession().setRegularMetronomePresets(
                (LinkedHashMap<String, RegularMetronomeSettings>) 
                        readMetronomePresetsFromFile(MetronomeType.REG,
                                                     regPresetsFile));
        getSession().setGapMetronomePresets(
                (LinkedHashMap<String, GapMetronomeSettings>) 
                        readMetronomePresetsFromFile(MetronomeType.GAP,
                                                     gapPresetsFile));
        getSession().setTimedMetronomePresets(
                (LinkedHashMap<String, TimedMetronomeSettings>) 
                        readMetronomePresetsFromFile(MetronomeType.TIMED,
                                                     timedPresetsFile));
        getSession().setSpeedMetronomePresets(
                (LinkedHashMap<String, SpeedMetronomeSettings>) 
                        readMetronomePresetsFromFile(MetronomeType.SPEED,
                                                     speedPresetsFile));
      }
    }
  }
}
