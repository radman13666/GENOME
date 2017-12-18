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

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class GapMetronomeSettings extends ConstantTempoMetronomeSettings {
  
  private int loudMeasures;
  private int silentMeasures;
  private int gapLengthIncrement;
  private int gapRepetitions;

  public GapMetronomeSettings() {
  }

  public GapMetronomeSettings(int loudMeasures, int silentMeasures,
                              int gapLengthIncrement, int gapRepetitions,
                              float tempo, int measure, int subDivision) {
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
    this.loudMeasures = loudMeasures;
  }

  public int getSilentMeasures() {
    return silentMeasures;
  }

  public void setSilentMeasures(int silentMeasures) {
    this.silentMeasures = silentMeasures;
  }

  public int getGapLengthIncrement() {
    return gapLengthIncrement;
  }

  public void setGapLengthIncrement(int gapLengthIncrement) {
    this.gapLengthIncrement = gapLengthIncrement;
  }

  public int getGapRepetitions() {
    return gapRepetitions;
  }

  public void setGapRepetitions(int gapRepetitions) {
    this.gapRepetitions = gapRepetitions;
  }
  
}
