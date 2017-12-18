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
public class MetronomeSettings {
  
  protected int measure;
  protected int subDivision;
  
  protected MetronomeSettings() {
  }

  protected MetronomeSettings(int measure, int subDivision) {
    setMeasure(measure);
    setSubDivision(subDivision);
  }

  public final int getMeasure() {
    return measure;
  }

  public final void setMeasure(int measure) {
    this.measure = measure;
  }

  public final int getSubDivision() {
    return subDivision;
  }

  public final void setSubDivision(int subDivision) {
    this.subDivision = subDivision;
  }
  
}
