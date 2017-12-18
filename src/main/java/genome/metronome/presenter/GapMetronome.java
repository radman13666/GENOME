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
import java.util.HashMap;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class GapMetronome extends ConstantTempoMetronome {
  
  private int loudMeasures;
  private int silentMeasures;
  private int gapLengthIncrement;
  private int gapRepetitions;

  public GapMetronome() {
  }

  public GapMetronome(int loudMeasures, int silentMeasures,
                      int gapLengthIncrement, int gapRepetitions, float tempo,
                      int measure, int subDivision) {
    super(tempo, measure, subDivision);
    setLoudMeasures(loudMeasures);
    setSilentMeasures(silentMeasures);
    setGapLengthIncrement(gapLengthIncrement);
    setGapRepetitions(gapRepetitions);
  }

  public int getLoudMeasures() {
    return loudMeasures;
  }

  public void setLoudMeasures(int loudMeasures) {
    if (loudMeasures >= MetronomeConstants.GapMetronome.MIN_LOUD_MEASURES 
        & loudMeasures <= MetronomeConstants.GapMetronome.MAX_LOUD_MEASURES)
      this.loudMeasures = loudMeasures;
    else this.loudMeasures = MetronomeConstants.GapMetronome
            .DEFAULT_LOUD_MEASURES;
  }

  public int getSilentMeasures() {
    return silentMeasures;
  }

  public void setSilentMeasures(int silentMeasures) {
    if (silentMeasures >= MetronomeConstants.GapMetronome.MIN_SILENT_MEASURES 
        & silentMeasures <= MetronomeConstants.GapMetronome.MAX_SILENT_MEASURES)
      this.silentMeasures = silentMeasures;
    else this.silentMeasures = MetronomeConstants.GapMetronome
            .DEFAULT_SILENT_MEASURES;
  }

  public int getGapLengthIncrement() {
    return gapLengthIncrement;
  }

  public void setGapLengthIncrement(int gapLengthIncrement) {
    if (gapLengthIncrement >= MetronomeConstants.GapMetronome
        .MIN_GAP_LENGTH_INCREMENT 
        & gapLengthIncrement <= MetronomeConstants.GapMetronome
          .MAX_GAP_LENGTH_INCREMENT)
      this.gapLengthIncrement = gapLengthIncrement;
    else this.gapLengthIncrement = MetronomeConstants.GapMetronome
            .DEFAULT_GAP_LENGTH_INCREMENT;
  }

  public int getGapRepetitions() {
    return gapRepetitions;
  }

  public void setGapRepetitions(int gapRepetitions) {
    if (gapRepetitions == MetronomeConstants.GapMetronome
        .INFINITE_GAP_REPETITIONS 
        | (gapRepetitions >= MetronomeConstants.GapMetronome.MIN_GAP_REPETITIONS 
        & gapRepetitions <= MetronomeConstants.GapMetronome
          .MAX_GAP_REPETITIONS))
      this.gapRepetitions = gapRepetitions;
    else this.gapRepetitions = MetronomeConstants.GapMetronome
            .DEFAULT_GAP_REPETITIONS;
  }

  @Override
  protected byte[] createSilence(float tempo) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void play() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void stop() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void bulkSet(HashMap<String, Number> settings) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public HashMap<String, Number> getSettings() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
