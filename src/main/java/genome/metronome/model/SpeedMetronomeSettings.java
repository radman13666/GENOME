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
public final class SpeedMetronomeSettings 
        extends VariableTempoMetronomeSettings {
  
  private int tempoLength;
  private int tempoIncrement;

  public SpeedMetronomeSettings() {
  }

  public SpeedMetronomeSettings(int tempoLength, int tempoIncrement,
                                float startTempo, float endTempo, int measure,
                                int subDivision) {
    super(startTempo, endTempo, measure, subDivision);
    setTempoLength(tempoLength);
    setTempoIncrement(tempoIncrement);
  }

  public int getTempoLength() {
    return tempoLength;
  }

  public void setTempoLength(int tempoLength) {
    this.tempoLength = tempoLength;
  }

  public int getTempoIncrement() {
    return tempoIncrement;
  }

  public void setTempoIncrement(int tempoIncrement) {
    this.tempoIncrement = tempoIncrement;
  }
  
}
